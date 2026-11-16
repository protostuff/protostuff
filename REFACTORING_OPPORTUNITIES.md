# Protostuff: Refactoring Opportunities

This report comes from a static review of every `src/main/java` source file in the protostuff multi-module build (180 files, about 60k lines). Each opportunity covers:

* **What**: the smell, with evidence.
* **Refactoring**: the suggested change.
* **Affected / connected parts**: the classes, methods, fields, build files and tests the change touches.
* **Knock-on refactoring**: whether those connected parts also have to be refactored, or only recompiled or retested.

File paths are relative to the `protostuff/` repository root.

> **Global caveat.** Protostuff is a performance-critical serialization library, and much of the duplication below is *intentional*: hand-inlined code that avoids virtual dispatch, boxing or bounds checks on hot paths. Benchmark every item that touches the encode/decode hot path with `protostuff-benchmarks` (JMH) before and after the change. Many classes are also **public API** that downstream users call directly, so prefer deprecation and delegation over removal.

---

## Summary

| # | Opportunity | Module(s) | Size / risk | Hot path? | Public API impact |
|---|---|---|---|---|---|
| 1 | Forked `RuntimeEnv` / `RuntimeFieldFactory` in `runtime-md` | runtime, runtime-md | M / Med | No (init) | Low |
| 2 | Mirrored `RuntimeUnsafeFieldFactory` and `RuntimeReflectionFieldFactory` | runtime | L / High | **Yes** | Low (mostly package-private) |
| 3 | 16 copy-paste array schemas in `ArraySchemas` | runtime | L / Med | Yes | Low |
| 4 | Duplicated protobuf decoding in `ByteArrayInput` / `ByteBufferInput` / `CodedInput` | core | M / High | **Yes** | Medium |
| 5 | `ProtobufIOUtil` and `ProtostuffIOUtil` differ only by a boolean | core | S / Low | No | Medium |
| 6 | Overloaded static IOUtil facades in every format module | json, xml, yaml, msgpack, core | L / Med | No | **High** |
| 7 | Duplicated `StatefulOutput` state and per-scalar templates | json, msgpack, xml | M / Med | Yes | Low |
| 8 | Parallel `StringSerializer` and `StreamedStringSerializer` | api | M / High | **Yes** | Medium |
| 9 | Copy-paste `Registry` in `ExplicitIdStrategy` and `IncrementalIdStrategy` | runtime-registry | M / Low | No | Low |
| 10 | `IdStrategy` god class with ~30 abstract triplet methods | runtime, runtime-registry | L / High | Partly | **High** |
| 11 | Reflection-heavy JDK wrapper handling in `PolymorphicCollectionSchema` / `PolymorphicMapSchema` | runtime | M / Med | No | Low |
| 12 | 79 paired `catch` blocks (multi-catch candidates) | runtime | S / Low | No | None |
| 13 | Java 1.6 baseline and legacy idioms | all (root `pom.xml`) | L / Med | No | Medium |
| 14 | Scattered system-property configuration flags | runtime, runtime-md, compiler, parser, json, xml, yaml, api | S / Low | No | Low |
| 15 | Duplicated option handling across parser AST classes | parser | M / Low | No | Medium (templates) |
| 16 | Copy-paste template-writing loops and unsafe `writer.close()` in compilers | compiler | S / Low | No | Low |
| 17 | `CompilerMain` monolith with static mutable registry | compiler, maven-plugin | M / Med | No | Medium |

The recommended order is listed at the end of this report.

---

## 1. Forked `RuntimeEnv` and `RuntimeFieldFactory` in `protostuff-runtime-md`

**What.** `protostuff-runtime-md` (the "no `sun.misc.Unsafe`" build, for Android and similar platforms) does not depend on `protostuff-runtime`. Instead, an `maven-antrun-plugin` `copy-sources` task in `protostuff-runtime-md/pom.xml` copies the runtime sources into the module and **excludes five files**, two of which are hand-maintained forks:

* `protostuff-runtime-md/src/main/java/io/protostuff/runtime/RuntimeEnv.java` (510 lines vs. 417 in runtime)
* `protostuff-runtime-md/src/main/java/io/protostuff/runtime/RuntimeFieldFactory.java` (425 lines vs. 452)

The forks have **already drifted**:

* `ALWAYS_USE_SUN_REFLECTION_FACTORY` and `NEVER_USE_SUN_REFLECTION_FACTORY` (`runtime/RuntimeEnv.java:140`, `:152`) do not exist in the md fork.
* The md fork has Android-specific `newInstanceFromObjectStreamClass`, `objectConstructorId` and `android43` logic that the main runtime lacks.
* The Javadoc of `ALLOW_NULL_ARRAY_ELEMENT` differs between the two copies.
* The md `RuntimeFieldFactory` hard-codes `RuntimeReflectionFieldFactory.*`, where the main one switches on `USE_SUN_MISC_UNSAFE` (`runtime/RuntimeFieldFactory.java:178-223`).

**Refactoring.**
1. Move the platform-dependent pieces behind a small SPI, for example an `interface RuntimePlatform { Instantiator<T> newInstantiator(...); RuntimeFieldFactory<?>[] scalarFactories(); Accessor.Factory accessorFactory(); }`. Ship two implementations, `UnsafePlatform` (runtime) and `ReflectionPlatform` (md), and select one with `ServiceLoader` or a class-presence check.
2. Make `RuntimeEnv` and `RuntimeFieldFactory` single-sourced, with no platform logic.
3. Replace the antrun copy with a normal Maven dependency, or with a shared `protostuff-runtime-common` module.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `RuntimeEnv` (both copies): static initializer, `OBJECT_CONSTRUCTOR`, `newInstantiator`, `USE_SUN_MISC_UNSAFE`, `JAVA_9_AND_ABOVE`, all `public static final boolean` flags | Merged into one copy | **Yes** |
| `RuntimeFieldFactory` (both copies): static init block, `ACCESSOR_FACTORY`, the scalar `public static final` fields (`BOOL`, `INT32`, …, `DELEGATE`) | Fields assigned from the platform SPI | **Yes** |
| `RuntimeUnsafeFieldFactory`, `UnsafeAccessor`, `OnDemandSunReflectionFactory` | Excluded from md today; they become the "unsafe platform" | Yes (moved or wrapped) |
| `RuntimeReflectionFieldFactory`, `ReflectAccessor` | Become the "reflection platform" | Light |
| `protostuff-runtime-md/pom.xml`: `copy-sources` antrun task, `build-helper-maven-plugin` `add-source`, `extra-test-sources` copy, excluded tests (`AlwaysUseSunReflectionFactoryOptionTest`, `ImmutableObjectsTest`, `EnumSetAndMapTest`, `*RuntimeObjectSchemaTest`) | Build wiring | **Yes** |
| `protostuff-runtime-view`, `protostuff-runtime-registry`, `protostuff-uberjar`, `protostuff-bom` | Depend on the runtime artifacts | Retest, and update the BOM if a module is added |
| `RuntimeEnv.*` references: 12 main files, 11 test files | Callers of the flags | Recompile only, if the field names are kept |

---

## 2. Mirrored `RuntimeUnsafeFieldFactory` and `RuntimeReflectionFieldFactory`

**What.** Two of the largest files in the project hold parallel definitions of the same field factories:

* `protostuff-runtime/.../RuntimeUnsafeFieldFactory.java` (1,587 lines, 19 factories: `CHAR`, `SHORT`, `BYTE`, `INT32`, `INT64`, `FLOAT`, `DOUBLE`, `BOOL`, `STRING`, `BYTES`, `BYTE_ARRAY`, `ENUM`, `POJO`, `POLYMORPHIC_POJO`, `OBJECT`, `BIGDECIMAL`, `BIGINTEGER`, `DATE`, `DELEGATE`)
* `protostuff-runtime/.../RuntimeReflectionFieldFactory.java` (2,008 lines, the same factories)

Each factory is an anonymous `RuntimeFieldFactory<X>` whose `create()` returns an anonymous `Field<T>`. The `mergeFrom`, `writeTo` and `transfer` bodies are identical except for the access primitive: `us.putBoolean(message, offset, …)` versus `f.setBoolean(message, …)` wrapped in a `try/catch (IllegalAccessException) / catch (IllegalArgumentException)`. The non-field methods (`transfer(Pipe,…)`, `readFrom`, `writeTo(Output,int,X,boolean)`, `getFieldType`, `typeClass`) are duplicated verbatim.

An `Accessor` abstraction **already exists** (`Accessor`, `UnsafeAccessor`, `ReflectAccessor`, `RuntimeFieldFactory.ACCESSOR_FACTORY`), but only `RuntimeCollectionFieldFactory`, `RuntimeMapFieldFactory` and `RuntimeRepeatedFieldFactory` use it.

**Refactoring.**
1. Extend `Accessor` with primitive-specialized accessors (`getInt`/`setInt`, `getLong`/`setLong`, `getBoolean`/`setBoolean`, and so on) so the hot path never boxes.
2. Rewrite each scalar factory **once**, using `accessor.getX` and `accessor.setX`. Keep the shared `readFrom`, `writeTo` and `transfer` pieces in a single place.
3. Delete one of the two files, or reduce it to a thin `Accessor.Factory`.

*Risk:* `Unsafe` calls on `final long offset` constants are JIT-inlined. Going through a virtual `Accessor` must be benchmarked, though a monomorphic call site usually inlines just as well.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `Accessor` (`abstract set/get`), `UnsafeAccessor`, `ReflectAccessor`, `Accessor.Factory` | Gain primitive methods | **Yes** |
| `RuntimeFieldFactory` static init switch (`:178-223`) and every `public static final RuntimeFieldFactory<…>` field | Simplified to one assignment per type | **Yes** |
| `Field<T>` (the abstract `mergeFrom`, `writeTo`, `transfer`) | Unchanged contract | No |
| `RuntimeSchema.createFrom()` / `getFieldFactory(Class, IdStrategy)` | Consumers of the factories | Retest |
| `RuntimeMessageField`, `RuntimeObjectField`, `RuntimeDerivativeField` | Created inside `POJO`, `OBJECT` and `POLYMORPHIC_POJO` | Moved, not rewritten |
| `RuntimeEnv.USE_SUN_MISC_UNSAFE` | Selects the accessor factory | Light |
| Opportunity #1 (`runtime-md` fork) | Shares this abstraction | Done together |
| Opportunity #12 (catch blocks): 40 of the 79 paired catches live in `RuntimeReflectionFieldFactory` | Removed by moving the exception wrapping into `ReflectAccessor` | **Yes** (absorbed) |

---

## 3. Sixteen copy-paste array schemas in `ArraySchemas`

**What.** `protostuff-runtime/.../ArraySchemas.java` (2,294 lines) declares `BoolArray`, `CharArray`, `ShortArray`, `Int32Array`, `Int64Array`, `FloatArray`, `DoubleArray`, `StringArray`, `ByteStringArray`, `ByteArrayArray`, `BigDecimalArray`, `BigIntegerArray`, `DateArray`, `DelegateArray`, `EnumArray` and `PojoArray`, all `extends Base`. Each repeats the same skeleton:

* a `Pipe.Schema` field that calls `transferObject(..., RuntimeFieldFactory.X)`
* `readPrimitiveFrom` (allocate the primitive array, call `GraphInput.updateLast`, loop over `ID_ARRAY_DATA`)
* `readBoxedFrom` (the same plus `ID_ARRAY_NULLCOUNT` handling)
* `readFrom` (validate `ID_ARRAY_LEN`)
* `writeTo(Output, Object, boolean)` (run-length-encode nulls via `allowNullArrayElement`)

The string `"Corrupt input."` appears dozens of times. Two big `switch (id)` blocks (`:113`, `:155`) map type IDs to these classes.

**Refactoring.**
* **The boxed path is fully generic.** `RuntimeFieldFactory<V>` already exposes `readFrom(Input)` and `writeTo(Output, int, V, boolean)`, so one `BoxedArray<V>(RuntimeFieldFactory<V> codec, Class<V> type)` can replace every `readBoxedFrom` and boxed `writeTo` branch.
* **The primitive path** needs per-type loops to avoid boxing. Keep those loops in small per-primitive strategy objects, or generate them with a template.
* Extract `ProtostuffException corrupt()` and `readArrayLength(Input)` helpers.
* Replace the two `switch` blocks with an `EnumMap` or array lookup keyed on the type ID.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `ArraySchemas.Base`, `newSchema(...)`, `getSchema(...)`, `ID_ARRAY_LEN`, `ID_ARRAY_DATA`, `ID_ARRAY_NULLCOUNT` | Core of the change | **Yes** |
| `Base.allowNullArrayElement` (set from `IdStrategy.PRESERVE_NULL_ELEMENTS & strategy.flags`, `:306`) | Shared null-RLE logic | Moved to the base |
| `RuntimeFieldFactory` scalar codecs (`BOOL`, `INT32`, …) | Become the element codecs | No (reused) |
| `GraphInput.updateLast` | Called by every reader | No |
| Callers: `ObjectSchema`, `IdStrategy`, `EnumIO`, `HasSchema`, `HasDelegate`, `PolymorphicSchemaFactories` | Reference `ArraySchemas.*` | Light (constructor and factory signatures) |
| `ArraySchema`, `ArrayFieldMap` | Related array support | Review |
| `protostuff-runtime-md` | Receives this file through the source copy | Automatic |

---

## 4. Duplicated protobuf decoding in `ByteArrayInput`, `ByteBufferInput` and `CodedInput`

**What.** The three binary `Input` implementations in `protostuff-core` repeat the same logic over different byte sources:

| Method | `ByteArrayInput` | `ByteBufferInput` | `CodedInput` |
|---|---|---|---|
| `readFieldNumber(Schema)`: packed-field check, tail-delimiter and end-group handling | `:221` | `:210` | `:1169` |
| `handleUnknownField` | `:215` | `:204` | `:1252` |
| `mergeObject` / `mergeObjectEncodedAsGroup` | `:487` / `:514` | `:490` / `:526` | `:318` / `:348` |
| `readSInt32` / `readSInt64` (ZigZag inlined as `(n >>> 1) ^ -(n & 1)`) | `:412` / `:423` | `:401` / `:412` | `:455` / `:465` (uses `decodeZigZag32/64`) |
| `readRawVarint32` / `readRawVarint64` | `:529` / `:580` | `:541` / `:592` | `:476` / `:614` |
| `readRawLittleEndian32/64` | `:604` / `:625` | `:616` / `:640` | `:634` / `:649` |

The `readFieldNumber` bodies of `ByteArrayInput` and `ByteBufferInput` are identical apart from `offset == limit` vs. `!buffer.hasRemaining()` and `packedLimit < offset` vs. `packedLimit < buffer.position()`.

**Refactoring.**
* Introduce `abstract class AbstractProtobufInput implements Input`. It owns `lastTag`, `packedLimit`, `decodeNestedMessageAsGroup`, `readFieldNumber`, `mergeObject*`, `readSInt*` and `checkLastTagWas`, and defines abstract `isAtEnd()`, `position()`, `pushLimit()`/`popLimit()` and `readRawByte()` methods.
* Move the ZigZag decoding into a single static helper. `CodedInput.decodeZigZag32/64` already exists; make the other two classes call it.
* Keep the varint fast paths per class if benchmarks show the abstraction costs throughput. The control-flow methods are the main win.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `ByteArrayInput`, `ByteBufferInput`, `CodedInput` | Extend the new base | **Yes** |
| `GraphByteArrayInput` and `GraphCodedInput` (graph wrappers around these inputs) | Delegate to them | Retest |
| `KvpByteArrayInput`, `KvpInput` (kvp module) | Use `StringSerializer`; similar shape | Optional follow-up |
| `ProtobufException` factories (`misreportedSize`, `invalidTag`, `negativeSize`, …), `UninitializedMessageException` | Used by the moved code | No |
| `WireFormat` constants (`TAG_TYPE_BITS`, `TAG_TYPE_MASK`, `WIRETYPE_TAIL_DELIMITER`, `WIRETYPE_END_GROUP`) | Used by the moved code | No |
| `IOUtil`, `ProtobufIOUtil`, `ProtostuffIOUtil`, `GraphIOUtil` | Construct these inputs | No (constructors unchanged) |
| Tests: `ByteArrayInput` is referenced in **69** test files, `CodedInput` in 4, `ByteBufferInput` in 3 | Regression net | Retest |

---

## 5. `ProtobufIOUtil` and `ProtostuffIOUtil` are near-duplicates

**What.** After normalizing the names, the 460-line `ProtobufIOUtil` and the 435-line `ProtostuffIOUtil` differ in about 155 lines. Most of those are Javadoc. The real difference is the `decodeNestedMessageAsGroup` flag (`new ByteArrayInput(data, offset, len, false)` vs. `true`, and `IOUtil.mergeFrom(..., false)` vs. `true`) and the Output class (`ProtobufOutput` vs. `ProtostuffOutput`). `GraphIOUtil` follows the same pattern, with the graph wrappers added.

**Refactoring.** Move the bodies into a package-private `BinaryFormat` enum (`PROTOBUF`, `PROTOSTUFF`, `GRAPH`) or a helper class parameterized by the group flag and an output factory. Keep the public static facade methods for compatibility, as one-liners that delegate.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `ProtobufIOUtil`, `ProtostuffIOUtil`, `GraphIOUtil`: `newPipe`, `mergeFrom` ×4–5, `mergeDelimitedFrom` ×3, `writeTo` ×2, `writeDelimitedTo` ×2, `optWriteDelimitedTo`, `writeListTo`, `parseListFrom`, `toByteArray` | Bodies move | **Yes** |
| `IOUtil.mergeFrom` / `mergeDelimitedFrom` (already takes the boolean) | Becomes the shared core | Light |
| `ProtobufOutput`, `ProtostuffOutput`, `LowCopyProtobufOutput`, `LowCopyProtostuffOutput`, `GraphProtostuffOutput` | Selected by the format | No |
| `LinkedBuffer`, `CodedInput.readRawVarint32(InputStream, …)` | Shared helpers | No |
| Opportunity #6 | Same facade-reduction theme | Can share a design |

---

## 6. Overloaded static IOUtil facades in every format module

**What.** Every format exposes a large static utility class with nearly the same API:

| Class | `mergeFrom` | `writeTo` | `writeListTo` | `parseListFrom` | `toByteArray` |
|---|---|---|---|---|---|
| `JsonIOUtil` | 6 | 4 | 4 | 4 | 2 |
| `SmileIOUtil` | 4 | 2 | 2 | 2 | 2 |
| `XmlIOUtil` | 8 | 5 | 3 | 3 | 2 |
| `MsgpackIOUtil` | 5 | 3 | 3 | 3 | 1 |
| `YamlIOUtil` | 0 | 2 | 2 | 0 | 1 |
| `ProtobufIOUtil` / `ProtostuffIOUtil` / `GraphIOUtil` | 4–5 | 2 | 1 | 0–1 | 1 |

The overloads differ only in their source or sink (`byte[]`, `byte[]+offset+len`, `InputStream`, `OutputStream`, `Reader`/`Writer`, a parser or generator) and in an optional `numeric` or `LinkedBuffer` argument. `SmileIOUtil` largely delegates to `JsonIOUtil` but re-declares every overload. There are also parallel "X" utilities (`MsgpackXIOUtil`, `JsonXIOUtil`, `XmlXIOUtil`).

**Refactoring.** Introduce a `Format<I extends Input, O extends Output>` abstraction (or per-format codec instances) with a small number of primitive operations: `newInput(source)`, `newOutput(sink)`, and `finish`. Implement `mergeFrom`, `writeTo`, `writeListTo`, `parseListFrom` and `toByteArray` once, generically. Keep the existing static methods as `@Deprecated` delegates for a release cycle.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| All `*IOUtil` / `*XIOUtil` classes listed above | Re-expressed over `Format` | **Yes** |
| `JsonInput`, `JsonOutput`, `JsonXOutput`, `XmlInput`, `XmlOutput`, `XmlXOutput`, `YamlOutput`, `MsgpackInput`, `MsgpackOutput`, `MsgpackXOutput`, `KvpInput`, `KvpOutput` | Wrapped by `Format` | No |
| `XmlIOFactoryUtil` (StAX factory selection), `SmileIOUtil.newSmileParser` / `newSmileGenerator`, `JsonIOUtil.DEFAULT_JSON_FACTORY` | Format-specific construction | Moved into the format |
| `Pipe` / `Pipe.Schema`, `newPipe(...)` methods | Each util has `newPipe` overloads | **Yes** |
| `protostuff-it` integration tests and `protostuff-benchmarks` | Heavy users of these statics | Retest; migrate gradually |
| **Downstream users** | Public static API | Deprecate, don't remove |

---

## 7. Duplicated `StatefulOutput` state and per-scalar write templates

**What.**
* `JsonOutput`, `JsonXOutput` and `MsgpackXOutput` each declare the same `private Schema<?> schema; private final boolean numeric; private boolean lastRepeated; private int lastNumber;` fields, plus `use(Schema)`, `reset()`, `isNumeric()`, `getLastNumber()`, `isLastRepeated()` and `updateLast(Schema, Schema)`. `MsgpackOutput` and `XmlOutput` implement `StatefulOutput` with part of that state.
* Inside `JsonOutput`, every scalar writer (`writeBool` `:110`, `writeInt32` `:300`, `writeInt64` `:330`, `writeFloat`, `writeDouble`, `writeUInt32`, `writeUInt64`, `writeString`, …) is the same 25-line template: repeated-field shortcut, `writeEndArray` if `lastRepeated`, field-name resolution (`numeric ? Integer.toString(n) : schema.getFieldName(n)`), array-start or field write, then update `lastNumber`/`lastRepeated`. Only the generator call changes. `writeFixed32`, `writeSInt32` and similar already delegate to `writeInt32`, which shows the template can be shared.

**Refactoring.**
* Create `abstract class AbstractStatefulOutput implements Output, StatefulOutput` that holds the shared state and lifecycle methods. `JsonXOutput` and `MsgpackXOutput` already extend `WriteSession`, so the shared part may need to be a composed `FieldState` helper instead of a superclass.
* In `JsonOutput`, extract `private String beginField(int fieldNumber, boolean repeated)`, which returns `null` for a repeated continuation, so each scalar method shrinks to about 5 lines.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `StatefulOutput` interface (`updateLast`) | Contract stays the same | No |
| `JsonOutput`, `JsonXOutput`, `MsgpackOutput`, `MsgpackXOutput`, `XmlOutput`, `XmlXOutput` | Share state | **Yes** |
| `WriteSession` (superclass of the `X` outputs) | Forces composition over inheritance | Design constraint |
| `JsonOutput.FIX_UNSIGNED_INT` (affects `writeUInt32` / `writeUInt64`) | Special case inside the template | Kept as a parameter |
| Graph/object-schema users of `updateLast` (`ObjectSchema`, the polymorphic schemas through `StatefulOutput`) | Callers | Retest |
| `JsonXOutput` is referenced in 8 test files | Regression net | Retest |

---

## 8. Parallel `StringSerializer` and `StreamedStringSerializer`

**What.** `protostuff-api/.../StringSerializer.java` (1,337 lines) and `StreamedStringSerializer.java` (587 lines) implement the same API in parallel: `writeInt`, `writeLong`, `writeFloat`, `writeDouble`, `writeUTF8`, `writeAscii`, `writeUTF8FixedDelimited` ×2, the private `writeUTF8OneByteDelimited` and `writeUTF8VarDelimited`, and the public `writeUTF8VarDelimited`. The only real difference is that the streamed variant flushes full `LinkedBuffer` nodes to the `OutputStream` (`flushAndReset`). `StringSerializer` also contains two package-private `writeUTF8` overloads (`:340`, `:663`) and a nested `STRING` class (`deser`, `ser`, `readUTF`) that mixes decoding into the encoder.

**Refactoring.**
* Factor the encoding into one core with a pluggable "buffer full" policy (allocate a new node vs. flush). That policy already exists as the `WriteSink.BUFFERED` and `WriteSink.STREAMED` enum constants, so it is a natural home.
* Move `StringSerializer.STRING` (the decoder) into its own `Utf8Decoder` class.
* Share the size helpers (`computeUTF8Size`, `stringSize`, `putBytesFromInt`, `putBytesFromLong`).

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `WriteSink` (`BUFFERED` `:27`, `STREAMED` `:329`) | Dispatches to the two serializers | **Yes** |
| `WriteSession`, `LinkedBuffer` | Buffer model | No |
| Callers of `StringSerializer.*` / `StreamedStringSerializer.*`: `ByteArrayInput`, `ByteBufferInput`, `CodedInput`, `ByteString`, `KvpByteArrayInput`, `KvpInput`, `KvpOutput`, `MsgpackWriteSink`, `NumberParser`, `ProtobufOutput`, `XmlOutput` | Callers | Recompile; review the `STRING.deser` callers if it moves |
| `StringSerializer.STRING.CESU8_COMPAT` (`io.protostuff.cesu8_compat`) | Config flag | See #14 |
| `StringSerializerBenchmark` (benchmarks module) | Must be re-run | Benchmark |

---

## 9. Copy-paste `Registry` in `ExplicitIdStrategy` and `IncrementalIdStrategy`

**What.** In `protostuff-runtime-registry`, the two strategies' `Registry` inner classes are line-for-line duplicates. Compare `registerCollection` (`Explicit:136` / `Incremental:131`), `registerMap` (`:163` / `:160`), `registerEnum` ×2, `registerPojo` ×2 (`:245,:276` / `:248,:280`), `mapPojo` and `registerDelegate` (`:327` / `:331`). Each one does the same ID validation (`id < 1`), `grow(list, id+1)`, `"Duplicate id registration: "` / `"Duplicate registration for: "` checks, and `pojoMapping` updates. Their supporting types mirror each other too: `RegisteredCollectionFactory` ↔ `RuntimeCollectionFactory`, `RegisteredMapFactory` ↔ `RuntimeMapFactory`, `RegisteredEnumIO` ↔ `RuntimeEnumIO`, and `BaseHS`, `Registered` and `Lazy` in both.

**Refactoring.** Pull the registration logic up into `NumericIdStrategy`, as a `protected` helper or an `AbstractRegistry` that implements `NumericIdStrategy.Registry`. Move the shared list/map fields (`pojos`, `pojoMapping`, `collections`, `maps`, `enums`, `delegates`) and the `grow` helper up as well. Merge the duplicated `BaseHS` / `Registered` hierarchies, keeping `IncrementalIdStrategy.LazyRegister` as its own subclass.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `NumericIdStrategy` and its `Registry` interface | Receives the shared code | **Yes** |
| `ExplicitIdStrategy.Registry`, `IncrementalIdStrategy.Registry`, `IncrementalIdStrategy.Factory` | Shrink | **Yes** |
| Inner factory, HasSchema and EnumIO types listed above | Merged | **Yes** |
| `IdStrategy` abstract methods (see #10) implemented by these classes | Unchanged here | No |
| `HasSchema`, `HasDelegate`, `EnumIO`, `CollectionSchema.MessageFactory`, `MapSchema.MessageFactory` | Base types | No |
| One test file each for `ExplicitIdStrategy` and `IncrementalIdStrategy` | Regression net | Retest (worth adding duplicate-ID tests) |

---

## 10. `IdStrategy` god class with ~30 abstract "triplet" methods

**What.** `protostuff-runtime/.../IdStrategy.java` (1,926 lines) mixes three concerns:

* configuration flags (`ENUMS_BY_NAME`, `PRESERVE_NULL_ELEMENTS`, `MORPH_*`, …) declared at `:38`
* schema and delegate lookup
* a long list of abstract wire methods (`:1717-1844`) that repeat the same `write…IdTo` / `transfer…Id` / `resolve…From` triplet for every kind: **Collection, Map, Enum, Pojo, Delegate, Array, Class** (plus `writeMessageIdTo` and `tryWrite…`).

Every concrete strategy (`DefaultIdStrategy`, `NumericIdStrategy` → `ExplicitIdStrategy`, `IncrementalIdStrategy`) must implement all of them.

**Refactoring.**
* Introduce a per-kind `TypeIdCodec<K>` interface with `write(Output, int fieldNumber, K key)`, `transfer(Input, Output, int fieldNumber)` and `resolve(Input)`. `IdStrategy` then exposes a few codec getters instead of ~30 methods.
* Extract the flags into an immutable `IdStrategyConfig` value object.
* Move the `UnknownTypeException`, `Factory` and `Wrapper` nested types out, or leave them where they are.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `DefaultIdStrategy` (893 lines), `NumericIdStrategy`, `ExplicitIdStrategy`, `IncrementalIdStrategy` | Implementors | **Yes** |
| Callers of the triplets: `ObjectSchema`, `PolymorphicSchemaFactories`, `PolymorphicCollectionSchema`, `PolymorphicMapSchema`, `PolymorphicPojoSchema`, `PolymorphicEnumSchema`, `RuntimeCollectionFieldFactory`, `RuntimeMapFieldFactory`, `RuntimeRepeatedFieldFactory`, `ArraySchemas` | Call the codecs instead | **Yes** (mechanical) |
| `IdStrategy.flags` bitmask, read by `ArraySchemas.Base` and others | Moves to config | Light |
| `RuntimeEnv` flag defaults | Feed the config | Light |
| Users who wrote **custom `IdStrategy` subclasses** | `protected abstract` API | **Breaking.** Needs an adapter base class during migration |

---

## 11. Reflection-heavy JDK wrapper handling in the polymorphic schemas

**What.** `PolymorphicCollectionSchema` (1,411 lines) and `PolymorphicMapSchema` (1,013 lines) hold static `java.lang.reflect.Field` handles on JDK internals:

* collection fields `fSingletonSet_element`, `fUnmodifiableCollection_c`, `fUnmodifiableSortedSet_ss`, `fUnmodifiableList_list`, `fSynchronizedCollection_c`, `fSynchronizedCollection_mutex`, `fSynchronizedSortedSet_ss`, `fSynchronizedList_list`, and the checked-collection fields
* map fields `fSingletonMap_k`, `fSingletonMap_v`, …

They call `setAccessible(true)` in a static initializer (`:134-250`) and follow a parallel structure in both files: `map(String,int)`, `name(int)`, `number(String)`, `number(char)`, `idFrom(Class)`, `instanceFrom(int)`, `writeObjectTo`, `writeNonPublic…To`, `writeUnmodifiable…To`, `writeSynchronized…To`, `writeChecked…To`, and the matching `read…From` and `transferObject` methods. Each pairs `ID_*` int constants with `STR_*` single-letter constants by hand. On JDK 16+, `setAccessible` on `java.util` internals fails without `--add-opens`.

**Refactoring.**
* Replace the parallel `ID_*` / `STR_*` constants and the `name()`/`number()` switches with an enum descriptor (`WrapperKind { EMPTY_SET(1,'a',…), … }`).
* Extract a shared `JdkWrapperCodec` for the unmodifiable, synchronized and checked patterns, used by both the collection and map schemas.
* Isolate the reflective access in one place so a JDK 9+ fallback (for example, rebuilding via `Collections.unmodifiableX(...)` instead of setting private fields) can be added cleanly.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `PolymorphicCollectionSchema`, `PolymorphicMapSchema` (static init, `__nonPublicCollections`, `__nonPublicMaps`, the `RuntimeEnv.Instantiator` fields `iSingletonSet` etc.) | Core | **Yes** |
| `RuntimeEnv.newInstantiator`, `RuntimeEnv.Instantiator`, `RuntimeEnv.JAVA_9_AND_ABOVE` | Used for construction | No |
| `ObjectSchema` (dispatches to these for `Collection`/`Map` values), `PolymorphicSchemaFactories` | Callers | Retest |
| `IdStrategy` collection and map ID methods (#10) | Called from here | Coordinate |
| `PolymorphicThrowableSchema` (also reflects on JDK internals; 3 paired catches) | Same smell | Optional |
| `EnumSetAndMapTest`, `ImmutableObjectsTest` (excluded in runtime-md) | Coverage | Retest |

---

## 12. Paired `catch (IllegalAccessException)` / `catch (IllegalArgumentException)` blocks

**What.** There are 79 `catch (IllegalAccessException e)` blocks in main code, each immediately followed by an identical `catch (IllegalArgumentException e) { throw new RuntimeException(e); }`. They are concentrated in `RuntimeReflectionFieldFactory` (40), `PolymorphicCollectionSchema` (14), `PolymorphicMapSchema` (10), `PolymorphicThrowableSchema` (3), `EnumIO` (2), `ReflectAccessor` (2), `RuntimeEnv` (2) and `IdStrategy` (1).

**Refactoring.** In the short term, use Java 7 multi-catch (`catch (IllegalAccessException | IllegalArgumentException e)`); this requires #13. Better, move the wrapping into `ReflectAccessor` (see #2) so call sites don't catch at all.

**Affected parts:** the files listed above. The change is purely mechanical with no API impact, but it depends on the language level in #13.

---

## 13. Java 1.6 baseline and legacy idioms

**What.** The root `pom.xml` sets `javac.target` to `1.6` and `javadoc.source` to `6`. That blocks diamond `<>`, multi-catch, try-with-resources, lambdas, `java.util.Objects`, `StandardCharsets` and `VarHandle` (the supported replacement for `sun.misc.Unsafe`). Symptoms in the code:

* 29 `finally { … close(); }` blocks that are try-with-resources candidates
* `StringBuffer` in `ProtoToProtoCompiler.java:181`
* verbose generic instantiation everywhere, such as `new LinkedHashMap<String, Object>()`
* `sun.misc.Unsafe` / `sun.reflect` usage in `RuntimeEnv` (both copies), `RuntimeUnsafeFieldFactory`, `UnsafeAccessor` and `OnDemandSunReflectionFactory`
* hand-rolled `JAVA_9_AND_ABOVE` detection (see #14)
* old build plugins (`maven-surefire-plugin` 2.18.1, `build-helper-maven-plugin` 1.9.1)

The workspace already contains `.github/modernize/java-upgrade/` tooling, which suggests an upgrade is planned.

**Refactoring.** Raise the baseline (at least 8; 11 or 17 if Android support can move to a separate artifact), then apply the mechanical modernizations. Consider `VarHandle` / `MethodHandles.privateLookupIn` as the long-term replacement for the Unsafe path in #2.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| Root `pom.xml` (`javac.target`, `javadoc.source`, plugin versions), every module `pom.xml` that overrides them | Build | **Yes** |
| `protostuff-runtime-md` (Android consumers) | Sets the minimum level | Decision needed |
| The `sun.misc` users listed above | Long-term migration | Yes, eventually |
| Enables #12, #16 (try-with-resources) and simplifies #2 | Downstream items | Prerequisite |
| `protostuff-maven-plugin` (Maven API levels) | Build-time | Verify |

---

## 14. Scattered system-property configuration flags

**What.** Global behavior is configured through `System.getProperty` / `Boolean.getBoolean` reads scattered across modules, with inconsistent naming (`protostuff.runtime.*`, `io.protostuff.*`, `ppc.*`, `parser.*`, bare `proto_path`, `cache_protos`, `yamloutput.linebreak`):

* `JAVA_9_AND_ABOVE` is defined **three times**: `runtime/RuntimeEnv.java:33`, `runtime-md/RuntimeEnv.java:36` and `compiler/ProtoModule.java:36`.
* `JsonOutput.FIX_UNSIGNED_INT`, `StringSerializer.STRING.CESU8_COMPAT`, `XmlIOFactoryUtil.CHECK_PARENT`, `YamlOutput` line break, `CompilerMain.SILENT_MODE`, `PluginProtoCompiler` `ppc.check_filename_placeholder`, `AbstractParser.SUPPRESS_WARNINGS`, `ConfiguredReference.RESOLVE_ENUM_VALUE_REF`, `DefaultProtoLoader` `proto_path`
* the ~13 `RuntimeEnv` flags

**Refactoring.** Add a small `io.protostuff.Env` / `ProtostuffConfig` utility in `protostuff-api` (every runtime module depends on it) with `isJava9OrAbove()` and typed property getters. Document every property in one place. The compiler and parser don't depend on `protostuff-api` today, so either add that dependency or give them their own tiny helper.

**Affected parts:** every class named above, the module `pom.xml` files if a dependency is added, and the README/docs. The risk is low. Keep the public `static final` field names so existing references still compile.

---

## 15. Duplicated option handling across parser AST classes

**What.** In `protostuff-parser`, `Message`, `EnumGroup`, `Service`, `Service.RpcMethod`, `Proto`, `Field`, `Extension` and `EnumField` each declare `final LinkedHashMap<String,Object> standardOptions` / `extraOptions` and re-implement `putStandardOption` (which calls `putExtraOption` and then `standardOptions.put`), `putExtraOption` (with a `"Duplicate … option"` error), `getStandardOption`, `getStandardOptions`, `getExtraOptions`, `getExtraOption` and `getOptions`. Compare `Message.java:299-310` with `Service.java:133-145` and `Service.java:287-298`. The `HasOptions` interface defines the contract, but there is no shared implementation.

**Refactoring.** Create an `OptionsHolder` class that the AST nodes delegate to, or fold the options into `AnnotationContainer`, which most of these classes already extend. Keep the getters on each node, because the StringTemplate files call them reflectively.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `HasOptions`, `HasFields`, `AnnotationContainer` | Contract / base | **Yes** |
| `Message`, `EnumGroup`, `EnumField`, `Service` (+ `RpcMethod`), `Proto`, `Field` (+ the `Number` subclasses), `Extension` | Implementors | **Yes** |
| `ProtoParser` / `ProtoLexer` (ANTLR-generated from the grammar) | Call `putStandardOption` / `putExtraOption` | Unchanged if the signatures are kept |
| `.stg` templates that read options: `base.stg`, `java_bean.stg`, `java_bean_model_base.stg`, `java_bean_model_schema.stg`, `java_v2protoc_schema.stg` | Reflective getter access | **Must keep getter names** |
| `ConfiguredReference`, `TextFormat` | Resolve option values | Retest |
| Error message text (`err("Duplicate message option: " …)`) | Kind-specific wording | Pass the kind into the holder |

---

## 16. Copy-paste template-writing loops and unsafe `writer.close()` in compilers

**What.** `ProtoToJavaBeanCompiler.writeEnums` / `writeMessages`, `ProtoToJavaBeanMeCompiler.compile` (an inline copy of both loops), `ProtoToGwtOverlayCompiler`, `ProtoToJavaBeanModelCompiler`, `ProtoToJavaV2ProtocSchemaCompiler`, `PluginProtoCompiler` and `ProtoToProtoCompiler` all repeat the same steps: `CompilerUtil.newWriter(module, pkg, name + ".java")`, wrap it in `AutoIndentWriter`, `group.getInstanceOf("…_block")`, set the attributes `module` and `options` plus the item, `write(out)`, and `writer.close()`. There are **17** `writer.close()` calls, and none of them is in a `finally` block, so any template error leaks a file handle and can leave a partially written file.

**Refactoring.** Add a single `protected void emit(ProtoModule module, String pkg, String fileName, StringTemplateGroup group, String templateName, String attrName, Object value)` method to `STCodeGenerator`, using try-with-resources (or `try/finally` until #13 lands). Have `ProtoToJavaBeanMeCompiler` reuse `ProtoToJavaBeanCompiler.writeEnums` / `writeMessages`, or extend that class.

**Affected / connected parts:** `STCodeGenerator` (`getSTG`), `CompilerUtil.newWriter`, every compiler class listed above, and the `.stg` templates (unchanged). Retest the `protostuff-compiler` tests and the `protostuff-maven-plugin` integration.

---

## 17. `CompilerMain` monolith with static mutable registry

**What.** `CompilerMain.java` (654 lines) combines:

* a global mutable registry (`static final HashMap<String,ProtoCompiler> __compilers`, `private static CompilerResolver __compilerResolver`, `setCompilerResolver`, `addCompiler`)
* properties and module loading (`propsFrom` ×2, `loadModules` ×4, `loadModule`, `newProtoModule`, `putIncludes`, `newGlobalOptions`, `newOptions`, `addOptionsTo` ×2)
* CLI parsing and usage text (`main`, `compileWithArgs`, `compileWithNoArgs`, `compileProfile`, `usage`, `propsErr`)
* compilation orchestration (`compile`, which temporarily mutates `module.setOutput` / `setGenerator` and restores them in `finally`)
* generator-name sanitizing (`createGeneratorName`, `isAllowedCharacter`, `isNumber`, `isAlpha`)

The static registry makes concurrent or embedded use, such as parallel Maven builds, unsafe.

**Refactoring.** Split it into `CompilerRegistry` (an instance, not static), `ProtoModuleLoader`, `CompilerCli` and `GeneratorNames`. Make `compile` create a per-output copy of the module instead of mutating and restoring the shared one.

**Affected / connected parts.**

| Part | Relationship | Needs refactoring? |
|---|---|---|
| `protostuff-maven-plugin/.../ProtoCompilerMojo` | Calls `CompilerMain` statics | **Yes** (inject a registry) |
| `ProtoModule` (`setOutput`, `setGenerator`, `getOptions`, `JAVA_9_AND_ABOVE`) | Mutated by `compile` | Light (add a copy method) |
| `PluginProtoCompiler`, `CachingProtoLoader` (`cache_protos` property), `ProtoCompiler` implementations | Registered compilers | Light |
| `FilenameUtil.getFileName` | Used by `createGeneratorName` | No |
| Public statics (`compile`, `loadModules`, `addCompiler`) | External callers | Keep as delegating facades |

---

## Suggested sequencing

1. **#13 Java baseline.** This unlocks #12, the try-with-resources fixes in #16 and a cleaner #2.
2. **Low-risk wins:** #12, #14, #16, #9 and #5. They are mechanical, have little hot-path exposure and are well covered by tests.
3. **Structural runtime work:** #2 together with #1 (they share the platform/Accessor abstraction), then #3, then #11.
4. **Hot-path core:** #4, #8 and #7. Run JMH benchmarks before and after each change.
5. **API-shaping items:** #6, #10, #15 and #17. These need a deprecation plan because they touch public or `protected` API used by downstream code and the StringTemplate files.
