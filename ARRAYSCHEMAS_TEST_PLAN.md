# `ArraySchemas` refactor: test-readiness findings and planned test suite

This follows up on **Opportunity #3** in [REFACTORING_OPPORTUNITIES.md](REFACTORING_OPPORTUNITIES.md): replacing the 16 copy-paste array schema classes in `protostuff-runtime/src/main/java/io/protostuff/runtime/ArraySchemas.java` with a generic boxed-array schema plus per-primitive strategies.

---

## 1. Verdict

**Yes. This refactor needs a dedicated test suite, and part of it has to be written and passing *before* any production code changes.**

The existing tests are a reasonable start, with about 85% line coverage on `ArraySchemas`. They are not enough to prove a refactor safe, for four reasons:

1. **The current tests cannot detect a change to the wire format.** Every array test is a round trip (write, then read back with the same code). If the refactor changes the writer and the reader the same way, for example the derived `-(len + 1)` length encoding or the order of null-count markers, every existing test still passes. Meanwhile, data persisted by older versions becomes unreadable. Only byte-for-byte "golden" tests catch this.
2. **Whole behaviors have zero coverage** (measured, see §3). These include graph back-references inside arrays, all 20+ `"Corrupt input."` error paths, `DelegateArray` pipe transfer and null handling, the derived schemas for six of the reference element types, the `MapWrapper` branch of the handlers, and the text-format field names `a`/`b`/`c`.
3. **There is a surprising existing behavior the refactor must consciously keep or fix.** With the default `IdStrategy` flags, a boxed array containing `null` serializes without error but fails to deserialize with `ProtostuffException: Corrupt input.` (verified, see §3.3). A generic rewrite could easily change this by accident in either direction.
4. **The baseline is not green on the JDK installed here.** 15 runtime tests error on JDK 17/21 because of module-access restrictions. One of them, `ObjectSchemaTest.testGraph`, is the only existing test aimed at graph serialization. A refactor can't be validated against a red baseline.

---

## 2. What the refactor touches (scope for testing)

`ArraySchemas` is reached from **four distinct entry paths**, and each one uses the array classes differently:

| Path | Trigger (example field) | Code path | Schema instance used |
|---|---|---|---|
| **A. Array-typed field** | `Integer[] ints;`, `int[] prim;`, `Size[] sizes;` | `RuntimeFieldFactory.OBJECT` → `PolymorphicSchemaFactories.getFactoryFromField` → `ARRAY.newSchema` → `ArraySchemas.newSchema(...)` | New per-field instance with a field-setter `Handler` |
| **B. Array inside an `Object` field** | `Object o = new int[]{…}` | `ObjectSchema.writeObjectTo` / `readObjectFrom` / `transferObject` (`ObjectSchema.java:659, 964, 1262`) → `ArraySchemas.getSchema(id, primitive, strategy)` using `toArrayId` / `toInlineId` / `isPrimitive` | Shared `strategy.ARRAY_*_PRIMITIVE_SCHEMA` / `ARRAY_*_BOXED_SCHEMA` |
| **C. Array as a repeated-field element** | `List<Integer[]> l;` (default flags) | `RuntimeRepeatedFieldFactory` → `getFactoryFromRepeatedValueGenericType` → `ARRAY.newSchema` | Handler-bound instance |
| **D. Array as a collection or map element through the "derived" schema** | `Map<String, int[]> m;`, or `List<int[]>` with `COLLECTION_SCHEMA_ON_REPEATED_FIELDS` | `RuntimeCollectionFieldFactory` / `RuntimeMapFieldFactory` → `getSchemaFromCollectionOrMapGenericType` → `ArraySchemas.getGenericElementSchema` | Shared `strategy.ARRAY_*_DERIVED_SCHEMA`. These are **anonymous subclasses in `IdStrategy.java:1204-1615`** that override `readFrom`, `writeLengthTo`, `writeTo` and `setValue`, and encode a boxed array's length as **`-(len + 1)`** |

Two more details make up the extension contract the refactor has to preserve:

* **The `IdStrategy` derived subclasses call `protected` hooks:** `readPrimitiveFrom(Input, Object, int)`, `readBoxedFrom(Input, Object, int)`, `writeLengthTo(Output, int, boolean)` and `writeTo(Output, Object, boolean)`. A generic redesign has to keep these hooks or rewrite all seven derived subclasses (Bool, Char, Short, Int32, Int64, Float, Double).
* **The Delegate, Enum and Pojo arrays are built from other classes:** `HasDelegate.java:39,56`, `EnumIO.java:212,253` and `HasSchema.java:62,71` construct `DelegateArray`, `EnumArray` and `PojoArray`. These three classes, plus the nested schema classes, are `public static`, so they are part of the public API.

There are also three pipe-transfer implementations that must stay byte-compatible:

* the shared `ArraySchemas.transferObject`, which handles negative (derived) lengths
* `EnumArray`'s inline `transfer`, which does **not** handle negative lengths
* `PojoArray`'s inline `transfer`, which also does **not** handle negative lengths

---

## 3. Evidence gathered

### 3.1 How it was measured

* I ran `mvn -o -pl protostuff-runtime jacoco:prepare-agent test jacoco:report` (JaCoCo 0.8.5, offline, Maven 3.10 / JDK 21). The report is in `protostuff-runtime/target/site/jacoco/`.
* I compiled a throwaway probe program against `target/classes` to check runtime behavior directly. It lives in the session scratchpad, not in the repo.
* Only the **runtime module's** tests feed this coverage. I grep-checked the JSON, XML, YAML and Msgpack runtime tests (`JsonRuntimeSerDeserTest` etc.). They serialize `Foo`/`Bar`/`Baz`, which contain lists but **no arrays**, so they add nothing for `ArraySchemas`.

### 3.2 Coverage results (runtime module, 189 tests, 15 errors unrelated to arrays)

| Class | Lines covered | Branches covered |
|---|---|---|
| `BoolArray` … `DoubleArray` (7 primitive-capable classes) | 51/58 each | 26–27/35 each |
| `StringArray`, `ByteStringArray`, `ByteArrayArray`, `BigDecimalArray`, `BigIntegerArray`, `DateArray` | 31/35 each | 16/21 each |
| `EnumArray` / `PojoArray` | 33/37 | 16/21 |
| `DelegateArray` | **26/37** | **11/21** |
| `DelegateArray` pipe schema | **1/3** (the transfer is never run) | – |
| `ArraySchemas` (static helpers) | 70/93 | 61/87 |
| `Base` | 8/12 (`getFieldName`, `getFieldNumber`, `messageName`, `messageFullName` never run) | – |
| `IdStrategy` derived subclasses for the 7 primitives | 15/16 each | 7/8 |
| `IdStrategy` derived subclasses for String, ByteString, byte[], BigDecimal, BigInteger, Date | **1/5 each** (`setValue` never run) | **0/2** |

### 3.3 Specific gaps (uncovered lines mapped to behavior)

| # | Untested behavior | Evidence (`ArraySchemas.java` lines unless noted) | Risk if the refactor breaks it |
|---|---|---|---|
| G1 | **Graph back-reference registration**, `((GraphInput) input).updateLast(array, owner)` | 384, 408, 1428, 1988 and the same line in **every** class: never executed | Cyclic or shared references to arrays break silently under `GraphIOUtil`. The only graph test (`ObjectSchemaTest.testGraph`) errors on JDK 17+. My probe showed that a shared `int[]` currently keeps its identity through a `GraphIOUtil` round trip, and no test asserts this. |
| G2 | **All `"Corrupt input."` error paths** (bad length tag, unexpected field number, missing end tag) | 262, 287, 292, 390, 396, 422, 427, 436, 1420, 1442, 1447, 1980, 2002, 2007, 2057, 2077, 2181, …; `IdStrategy.java:1217` | A generic loop could start silently accepting malformed input or reading past the end of the array. This is a robustness and security regression. |
| G3 | **Null elements with `PRESERVE_NULL_ELEMENTS` off** (the `allowNullArrayElement == false` branch) | Partial branch at 480 and its equivalent in every class | **Verified by probe:** with `new DefaultIdStrategy()`, writing `Integer[]{1, null, 3}` succeeds, but reading it throws `ProtostuffException: Corrupt input.` The writer drops the null while still writing `len = 3`. The refactor must either preserve this exactly or knowingly fix it, which changes behavior and needs a CHANGELOG entry. |
| G4 | **`DelegateArray` pipe transfer and null handling** | 1958 (transfer), 1999–2000 (reading a null count), 2028–2042 (writing null runs) | Delegate-typed arrays (for example a registered `Calendar` delegate) are the most likely to break when they're folded into a generic boxed path. |
| G5 | **Derived schemas for reference-type elements**: `List<String[]>`, `Map<K, BigDecimal[]>`, `Map<K, Date[]>`, etc. | `IdStrategy.java:1540-1614` (`setValue` for String, ByteString, byte[], BigDecimal, BigInteger, Date); `getGenericElementSchema` cases for `ID_BYTES` / `ID_BYTE_ARRAY` (174, 176) | Path D for non-primitive scalars is entirely unverified. |
| G6 | **The `MapWrapper` branch of the handlers** (array as a **map value** through the generic handler) | 80 (`GENERIC_HANDLER`), `IdStrategy.java:1541` | Map values that are arrays may be added to the wrong owner. |
| G7 | **Text-format field naming**: `name()` / `number()` (`"a"`/`"b"`/`"c"`) and `Base.getFieldName` / `getFieldNumber` / `messageName` | 226–253, 312–330 | JSON, XML and YAML output of arrays would change or break. No test in any module serializes a runtime array to a text format. |
| G8 | **Fallback branches**: `"Should not happen."` defaults in `getSchema`, `getGenericElementSchema` and `newSchema`, and the 6 missed branches in the `getGenericElementSchema` switch | 149, 184, 220; partial at 155 | Replacing the `switch` with a lookup table could change which schema is chosen for an ID. |
| G9 | **Negative-length (derived) data arriving at `EnumArray` / `PojoArray` inline transfers** | These transfers don't apply `len = -len - 1` | Probably unreachable, since enum and pojo arrays never use derived encoding, but the refactor should keep that invariant explicit and tested. |

### 3.4 What *is* already well covered (and should be kept as regression tests)

| Existing test | What it exercises |
|---|---|
| `NullArrayElementTest` (abstract) → `ProtostuffNullArrayElementTest`, `ProtobufNullArrayElementTest` | Path A for **all 15 boxed or reference element kinds** (Bool … Date, an enum `Size`, a pojo `SomePojo`) with null-first/last/mid/all/in-between patterns, plus paths C and D for primitive and boxed arrays in a `List` and a `Map`. It runs protostuff **and** protobuf, both from byte arrays and from streams, plus a **pipe round trip that compares bytes across formats**. |
| `NullArrayElementInObjectArrayTest` → its Protostuff and Protobuf subclasses | Path B (arrays inside `Object` / `Object[]`) with null patterns |
| `AbstractRuntimeObjectSchemaTest` (`List<short[]>`, `Map<String, short[]>`, `Map<short[], short[]>`, multi-dimensional arrays) | Mixed paths |
| `ArrayItemHasDelegateThrowNpeWhenDeserializingTest` | `List<Calendar[]>` with a delegate (path C, delegate) |
| `CollectionTest` (`Map<String, BB[]>`, `Map<BB[], String>`, `List<BB[]>`) | Pojo arrays as collection and map elements |

These tests use JUnit 3 style (`extends AbstractTest` → `TestCase`), with `IdStrategy` injected by overriding `newIdStrategy()`.

**Known weakness:** the `equals()` of the `PojoWithArrayInList`, `PojoWithArrayPrimitiveInList` and `PojoWithArrayInMap` test pojos only compares element `get(0)`. A refactor that drops or duplicates later list elements would pass.

### 3.5 Baseline health (must be fixed or isolated first)

On the installed JDK (Maven runs on JDK 21; `java` on the PATH is 17) the runtime module currently reports **189 tests run, 0 failures, 15 errors**:

* `EnumSetAndMapTest` (6), `ObjectSchemaTest.testGraph`, and 4 × 2 `*RuntimeObjectSchemaTest` collection, map and singleton tests
* Every error is `InaccessibleObjectException`, `ExceptionInInitializerError` or `NoClassDefFoundError` from `setAccessible` on `java.util` / `java.lang` internals. None of them involves `ArraySchemas`.

An earlier report left in `target/surefire-reports` showed 21 errors, including the `Throwable` tests. That suggests the failures depend on the environment or the fork order.

**Action:** before refactoring, either add `--add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED` to the surefire `argLine` in `protostuff-runtime/pom.xml`, or run the baseline on JDK 8. Record a green run and its JaCoCo report as the "before" snapshot. Without this, `ObjectSchemaTest.testGraph` gives no protection for G1.

---

## 4. Planned test suite

### 4.1 Principles

* **Write the characterization tests first** (golden bytes plus the gap tests) against the **unmodified** code, commit them green, and only then refactor. The suite must pass unchanged afterwards.
* **Use test-only changes.** No production code changes are needed to add these tests. Every class goes in `protostuff-runtime/src/test/java/io/protostuff/runtime/`, in the same package, so the package-private `ArraySchemas` helpers and `IdStrategy` fields are reachable.
* **Follow the existing conventions:** JUnit 3 `AbstractTest`, `newIdStrategy()` overrides, and abstract base classes with Protostuff/Protobuf/Graph subclasses, the same pattern as `NullArrayElementTest`.
* `protostuff-runtime-md` copies the runtime test sources through its antrun task. The new tests then also run against the reflection-only build automatically, as long as they don't depend on `sun.misc.Unsafe`.

### 4.2 Test dimensions (the matrix the suite must cover)

| Dimension | Values |
|---|---|
| **Element kind** (16) | the 7 primitives as `x[]` **and** as boxed `X[]` (bool, char, short, int, long, float, double); String; ByteString; byte[]; BigDecimal; BigInteger; Date; enum; pojo; delegate |
| **Entry path** (4) | A field, B `Object` field, C repeated element (`List<X[]>`), D derived (`Map<String, X[]>`, plus `List<X[]>` with `COLLECTION_SCHEMA_ON_REPEATED_FIELDS`) |
| **Format** | protostuff, protobuf, **graph**, pipe (protobuf ↔ protostuff), **JSON** (named and numeric) |
| **Flags** | `PRESERVE_NULL_ELEMENTS` on and off |
| **Shapes** | empty `{}`; a single element; ≥ 128 elements (multi-byte varint length); a ≥ 128-element null run (multi-byte null count); null first, last, middle, all, alternating |
| **Edge values** | `Integer.MIN_VALUE`/`MAX_VALUE`, `Long.MIN_VALUE`, `NaN`, `-0.0f`, `±Infinity`, `'\uD83D'` (lone surrogate char), empty String, `new byte[0]`, `BigDecimal` with negative scale, `new Date(0)` and negative epoch |

Not every combination needs its own hand-written test. §4.3 uses **parameterized loops inside a test method** (JUnit 3 style) over element kinds, so the full matrix stays small in code.

### 4.3 New test classes

#### T1. `ArraySchemasGoldenBytesTest` (highest priority; catches wire-format drift)

Purpose: freeze today's exact encodings so a symmetric reader+writer change can't slip through.

* **How:** for each (element kind × path A/B/C/D × null pattern × flag), serialize a fixed fixture with `ProtostuffIOUtil` and `ProtobufIOUtil` and compare against a hex string stored in `src/test/resources/io/protostuff/runtime/array-golden/*.hex`, one file per case.
* **Generating the fixtures:** a `main()` or system-property switch (`-Dprotostuff.golden.regen=true`) writes the files from the **pre-refactor** code. The normal mode only compares. Commit the generated files together with the test, before any refactoring.
* **Must include:**
  - **GB-1:** derived boxed arrays, to pin `-(len + 1)` (for example `Map<String, Integer[]>` with value `{1, null}`)
  - **GB-2:** derived primitive arrays, to pin the non-negative length (`Map<String, int[]>`)
  - **GB-3:** path B array IDs, to pin the `toArrayId` mapping (primitive IDs 0–6, boxed 8–14, String→16 … Date→21) for every kind
  - **GB-4:** null-run encoding: `{null, null, 1, null}` produces exactly NULLCOUNT(2), DATA, NULLCOUNT(1)
  - **GB-5:** 200-element array with a 150-null run (multi-byte varints)
  - **GB-6:** enum array, both by ordinal and with `ENUMS_BY_NAME`
  - **GB-7:** delegate array (for example `Singleton[]` with the existing `SampleDelegates.SINGLETON_DELEGATE` registered)
* **Also decode each golden blob** back to the fixture. This proves the new reader still reads old data. That is the persistence-compatibility guarantee.

#### T2. `ArraySchemasCorruptInputTest` (closes G2)

Hand-craft malformed byte sequences with `ProtostuffOutput` / `LinkedBuffer` (or hex literals), deserialize them, and assert that `ProtostuffException` with message `"Corrupt input."` is thrown, never an `ArrayIndexOutOfBoundsException` and never silent success.

| Case | Malformed input | Applies to |
|---|---|---|
| CI-1 | First field is not `ID_ARRAY_LEN` (for example field 2 first) | all 16 kinds × primitive/boxed |
| CI-2 | A primitive array gets an `ID_ARRAY_NULLCOUNT` field | the 7 primitives |
| CI-3 | Unknown field number (for example 4) inside the element loop | all |
| CI-4 | Fewer elements than declared length (end of message too early) | all |
| CI-5 | More elements than declared (a trailing `ID_ARRAY_DATA` instead of the end marker) | all |
| CI-6 | Null count that overshoots the length (`len = 2`, `NULLCOUNT = 5`) | boxed and reference kinds. **First capture current behavior** (it may currently pass silently); assert exactly that, and flag it as a candidate bug if it does |
| CI-7 | Derived schema: missing length tag | `IdStrategy` derived (`IdStrategy.java:1217`) |
| CI-8 | Pipe transfer of each malformed case | `transferObject`, `EnumArray` and `PojoArray` inline transfers |

#### T3. `ArraySchemasGraphTest` (closes G1)

Uses `GraphIOUtil` (protostuff graph format). **It must run green on the baseline**, which depends on §3.5.

* **GR-1:** one array instance referenced from two fields (`Object a = arr; Object b = arr;`). After the round trip, `a == b` (identity kept) and the contents are equal. This is the behavior my probe confirmed exists today.
* **GR-2:** the same array referenced from a field and from inside a `List<Object>`
* **GR-3:** a pojo array whose element points back to the owner (`Node[] children` where `child.parent == owner`)
* **GR-4:** an `Object[]` containing itself (`arr[0] = arr`)
* **GR-5:** repeat GR-1 for every element kind: primitive, boxed, String, enum, pojo and delegate. Every class has its own `updateLast` call, so each needs its own test.

#### T4. `ArraySchemasNullPolicyTest` (closes G3; pins the surprising behavior)

* **NP-1:** with `PRESERVE_NULL_ELEMENTS` **off** (plain `new DefaultIdStrategy()`), writing a boxed array that contains a null succeeds, but reading it throws `ProtostuffException("Corrupt input.")`. Assert this for every boxed and reference kind on paths A, C and D. This is **current behavior** as verified. If the team decides to fix it, this test changes **in a separate, deliberate commit** with a CHANGELOG entry, never as a side effect of the refactor.
* **NP-2:** flag off, array with **no** nulls: the round trip succeeds and the bytes equal those of the flag-on run (the flag must not affect null-free data).
* **NP-3:** flag on: complete null-pattern coverage for the **delegate** kind, which `NullArrayElementTest` lacks. Closes G4.

#### T5. `ArraySchemasEntryPathTest` (closes G5, G6, G8, G9)

A parameterized loop over all 16 element kinds that asserts a round trip and the **selected schema** for each path.

* **EP-1:** for each kind, `ArraySchemas.getSchema(id, primitive, strategy)` returns the same instance as before (`strategy.ARRAY_INT32_PRIMITIVE_SCHEMA`, …). This pins the mapping that a lookup-table refactor would replace.
* **EP-2:** `getGenericElementSchema(id, strategy)` for every ID, **including `ID_BYTES` and `ID_BYTE_ARRAY`** (currently uncovered)
* **EP-3:** `toArrayId`, `toInlineId` and `isPrimitive` are exhaustive inverses for every ID (a pure unit test)
* **EP-4:** unknown IDs passed to `getSchema`, `getGenericElementSchema` and `newSchema` throw `RuntimeException("Should not happen.")`
* **EP-5:** path D for **reference** kinds: `Map<String, String[]>`, `Map<String, BigDecimal[]>`, `Map<String, Date[]>`, `Map<String, ByteString[]>`, `Map<String, byte[][]>`, and `List<X[]>` with `COLLECTION_SCHEMA_ON_REPEATED_FIELDS` on. Closes G5.
* **EP-6:** arrays as **map values** through `GENERIC_HANDLER` / `MapWrapper` (`Map<String, Calendar[]>` delegate, `Map<String, Size[]>` enum, `Map<String, SomePojo[]>` pojo). Closes G6.
* **EP-7:** arrays as **map keys** (`Map<int[], String>`, `Map<String[], String>`), which exercise the other branch
* **EP-8:** a pipe round trip for every case above (protobuf → protostuff → protobuf, with byte equality), especially `DelegateArray`'s transfer. Closes G4 and G9.
* **EP-9:** multiple elements in each list or map, with an equality check **over all elements**, to fix the `get(0)`-only weakness in the existing pojos

#### T6. `ArraySchemasTextFormatTest` (closes G7)

Location: `protostuff-json/src/test/java/io/protostuff/` (the JSON module already depends on the runtime, as `JsonRuntimeSerDeserTest` shows).

* **TF-1:** `JsonIOUtil` named mode: `{"ints":{"a":3,"b":[1,3],"c":1}}`-style output (the exact current string is captured as a golden value) for path A with and without nulls
* **TF-2:** numeric mode (`numeric = true`): field numbers instead of `a`/`b`/`c`
* **TF-3:** read back with `JsonIOUtil.mergeFrom` and assert equality, for all 16 kinds
* **TF-4:** `Base.getFieldName(1..3)`, `getFieldNumber("a".."c")`, an unknown name (`"ab"`, `"z"`) → 0, and `messageName()` / `messageFullName()` equal `Array` / `java.lang.reflect.Array`
* *(Optional)* the same for XML through `XmlIOUtil` in `protostuff-xml`

#### T7. Strengthen existing tests (small edits)

* Fix `equals()` in `NullArrayElementTest.PojoWithArrayInList`, `PojoWithArrayPrimitiveInList`, `PojoWithArrayInMap` and `PojoWithArrayPrimitiveInMap` so they compare **all** elements (with `Arrays.deepEquals` per element), not just `get(0)`.
* Add a `GraphNullArrayElementTest extends NullArrayElementTest` (and an `…InObjectArrayTest` variant) that implements the abstract I/O methods with `GraphIOUtil`. This instantly runs the entire existing null-pattern matrix through the graph format.

### 4.4 Non-functional checks

* **Performance:** `protostuff-benchmarks` (JMH) has no array-specific benchmark. Add `ArraySerBenchmark` covering `int[]`, `Integer[]`, `String[]` and `SomePojo[]` at sizes 10, 1k and 100k, for serialize and deserialize. Record the pre-refactor numbers. Acceptance: no regression above about 5% on the primitive paths (the per-primitive loops must still avoid boxing).
* **API compatibility:** `BoolArray` … `PojoArray` and `Base` are `public static`. Run a binary-compatibility check (for example `japicmp-maven-plugin` against the last released `protostuff-runtime` jar), or at least keep the class names and the `protected` hook signatures listed in §2.
* **Coverage gate:** after the refactor, the JaCoCo report for `ArraySchemas` (and its replacement classes) should show **≥ 95% line and ≥ 90% branch** coverage. The only exclusions are the unreachable `"Should not happen."` defaults, if EP-4 can't reach them.

### 4.5 Execution order

| Step | Action | Exit criterion |
|---|---|---|
| 0 | Fix the baseline (§3.5: `--add-opens` or JDK 8) | `mvn -pl protostuff-runtime test`: 0 errors. Save the JaCoCo "before" report |
| 1 | Add T7 (strengthen existing tests) | Green on the unmodified code |
| 2 | Add T1 (golden bytes); generate and commit the fixtures from the unmodified code | Green |
| 3 | Add T2–T6 against the unmodified code; record any surprising current behavior *as-is* and list it for team review | Green; JaCoCo meets the §4.4 gate on the **old** code |
| 4 | Add the JMH baseline (§4.4) | Numbers recorded |
| 5 | Refactor `ArraySchemas` (and the `IdStrategy` derived subclasses) | **No test file changes.** Everything green, including `protostuff-runtime-md` |
| 6 | Re-run JMH and the API check | Within thresholds |
| 7 | *(Optional, separate PR)* Decide on the NP-1 behavior or bug | Deliberate test change plus CHANGELOG |

---

## 5. Decisions for you to review

1. **NP-1 (null with `PRESERVE_NULL_ELEMENTS` off fails on read):** keep it as-is during the refactor (recommended), or fix it in the same change?
2. **Baseline JDK:** add `--add-opens` to surefire (keeps JDK 17/21), or standardize the baseline on JDK 8 to match the project's `javac.target` of 1.6?
3. **T6 scope:** JSON only (recommended), or also XML and YAML?
4. **Golden fixtures:** stored as hex files in `src/test/resources` (recommended, easy to diff), or inlined as string constants in the test class?
