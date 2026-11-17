# `ArraySchemas` characterization test suite: implementation report

This report covers the test suite from [ARRAYSCHEMAS_TEST_PLAN.md](ARRAYSCHEMAS_TEST_PLAN.md) §4 (T1–T7), written against the **unmodified** production code. Nothing under any `src/main` changed. The ArraySchemas refactor itself was out of scope, and nothing is committed.

Branch: `arrayschemas-detailed-tests`.

---

## 1. Summary

| | Before | After |
|---|---|---|
| `protostuff-runtime` tests | 189 run, **15 errors** on JDK 17/21 (module access) | **236 run, 0 failures, 0 errors** (1 skipped, pre-existing) |
| `protostuff-json` tests | 512 run, errors on JDK 17/21 (module access) | **515 run, 0 failures, 0 errors** |
| `protostuff-runtime-md` tests | did not build (see §6) | **189 run, 0 failures, 0 errors** (verified with a temporary POM, see §6) |
| `ArraySchemas*` + `IdStrategy` array subclasses, line coverage | 900 / 1059 = **85.0%** | 1059 / 1059 = **100%** |
| … branch coverage | 449 / 611 = **73.5%** | 611 / 611 = **100%** |
| Characterization findings (possible bugs) | – | **6**, plus 1 plan correction and 2 side observations (§5) |

The coverage target (≥ 95% lines, ≥ 90% branches) is met.

---

## 2. Files added or changed

### Changed

| File | Change |
|---|---|
| `protostuff-runtime/pom.xml` | Adds an empty `<argLine/>` property plus a surefire `<argLine>@{argLine} --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED</argLine>`. The empty default keeps the literal `@{argLine}` from reaching the JVM when JaCoCo is not used, and JaCoCo's `prepare-agent` fills it in when it is (verified). |
| `protostuff-runtime/src/test/java/io/protostuff/runtime/NullArrayElementTest.java` | **T7.** The `equals()` of `PojoWithArrayInList`, `PojoWithArrayPrimitiveInList`, `PojoWithArrayInMap` and `PojoWithArrayPrimitiveInMap` compared only `get(0)` (the first list element, or the value under map key `0`). All 28 comparisons now call two new overloaded helpers, `arraysEqual(List, List)` and `arraysEqual(Map, Map)`, which compare every element or entry by content (`Arrays.deepEquals`, so primitive arrays work too). Adds `import java.util.Map`. Fixtures, `hashCode()` and `toString()` are unchanged. |

### Added: `protostuff-runtime/src/test/java/io/protostuff/runtime/`

| File | Plan item | Tests | What it covers |
|---|---|---|---|
| `ArrayFixtures.java` | shared | – | The 23 array kinds (the 16 element kinds, with the 7 primitives both primitive and boxed), with edge-value samples (`MIN`/`MAX`, `NaN`, `-0.0`, `±Infinity`, lone surrogate `'\uD83D'`, empty strings and byte arrays, a negative-scale `BigDecimal`, a negative epoch `Date`). Four holder classes, one per entry path (`FieldHolder`, `ObjectHolder`, `ListHolder`, `MapHolder`), with `@Tag`-pinned field numbers. Also: `Format` (protostuff, protobuf, graph), the pipe round trip, deep equality/diff and hex helpers. |
| `GraphNullArrayElementTest.java` | T7 | 10 | Runs the whole `NullArrayElementTest` matrix through `GraphIOUtil` |
| `GraphNullArrayElementInObjectArrayTest.java` | T7 | 6 | Runs the whole `NullArrayElementInObjectArrayTest` matrix through `GraphIOUtil` |
| `ArraySchemasGoldenBytesTest.java` | T1 (GB-1..GB-7) | 7 | **969 golden cases**: 23 kinds × 5 paths × null patterns × flag configs × 3 formats. Each case compares bytes against the fixture **and** decodes the fixture back. Two extra tests spell out GB-4 (the null-run encoding) and GB-1 (the derived `-(len + 1)` length) byte by byte. |
| `ArraySchemasCorruptInputTest.java` | T2 (CI-1..CI-8) | 6 | Splits real protobuf encodings into wire fields and applies one mutation per case (missing length, unknown field, too few or too many elements, null count in a primitive array, null-count overshoot). Covers path A and path D (map value / derived schema), for decode **and** pipe. |
| `ArraySchemasGraphTest.java` | T3 (GR-1..GR-5) | 6 | Identity of shared arrays (two `Object` fields, typed fields in two holders, field + list) for every kind, plain and with nulls. Also pojo-array back-references and an `Object[]` that contains itself. |
| `ArraySchemasNullPolicyTest.java` | T4 (NP-1..NP-3) | 3 | Null handling with `PRESERVE_NULL_ELEMENTS` off (4 paths × 16 kinds × 3 formats) and on (the delegate kind × 6 null patterns × 4 paths × 3 formats, plus the pipe). |
| `ArraySchemasEntryPathTest.java` | T5 (EP-1..EP-9) + TF-4 | 9 | Which schema instance each ID dispatches to, the inline array ID arithmetic, the `"Should not happen."` fallbacks, `Byte[]`, the field naming used by text formats, multi-element lists and maps (all kinds, 4 path variants, 3 formats + pipe), and arrays as map keys. |

### Added: `protostuff-runtime/src/test/resources/io/protostuff/runtime/array-golden/`

| File | Cases |
|---|---|
| `A_field.hex` | 201 |
| `B_objectField.hex` | 192 |
| `C_repeated.hex` | 192 |
| `D_listDerived.hex` | 192 |
| `D_mapDerived.hex` | 192 |

Each line has the form `<path>.<kind>.<pattern>.<config>.<format>=<hex>`. The files were generated from the unmodified code with `-Dprotostuff.golden.regen=true`. The generator refuses to run from any module other than `protostuff-runtime`.

### Added: `protostuff-json/src/test/java/io/protostuff/`

| File | Plan item | Tests | What it covers |
|---|---|---|---|
| `ArraySchemasTextFormatTest.java` | T6 (TF-1..TF-3) | 3 | Exact JSON strings in named and numeric mode for a typed boxed field, a typed primitive field, a String field, a map value (derived schema) and an `Object` field. Plus a round trip of all 23 kinds, in typed fields and in an `Object` field, in both modes. |

---

## 3. How to run

The project's own settings don't run on the JDK installed here (Maven runs on JDK 21), so the commands need extra flags. See §6.

```sh
# runtime module (all tests)
mvn -o -pl protostuff-runtime test -Djavac.target=1.8

# runtime + registry + json (json needs the registry and the tests jars, so use 'package' in one reactor)
mvn -o -pl protostuff-runtime,protostuff-runtime-registry,protostuff-json package -Djavac.target=1.8 \
    -Dmaven.javadoc.skip=true \
    "-DargLine=--add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED"

# coverage
mvn -o -pl protostuff-runtime org.jacoco:jacoco-maven-plugin:0.8.5:prepare-agent test \
    org.jacoco:jacoco-maven-plugin:0.8.5:report -Djavac.target=1.8
# -> protostuff-runtime/target/site/jacoco/jacoco.csv

# regenerate the golden bytes (ONLY for an intended wire-format change)
mvn -o -pl protostuff-runtime test -Djavac.target=1.8 -Dtest=ArraySchemasGoldenBytesTest -Dprotostuff.golden.regen=true
```

---

## 4. Test counts and coverage

### 4.1 Test counts (final runs)

| Module | Test class | Tests |
|---|---|---|
| runtime | `ArraySchemasGoldenBytesTest` | 7 |
| runtime | `ArraySchemasCorruptInputTest` | 6 |
| runtime | `ArraySchemasGraphTest` | 6 |
| runtime | `ArraySchemasNullPolicyTest` | 3 |
| runtime | `ArraySchemasEntryPathTest` | 9 |
| runtime | `GraphNullArrayElementTest` | 10 |
| runtime | `GraphNullArrayElementInObjectArrayTest` | 6 |
| runtime | **new total** | **47** |
| runtime | **module total** | **236** (189 + 47), 0 failures, 0 errors, 1 skipped (pre-existing) |
| json | `ArraySchemasTextFormatTest` | 3 |
| json | **module total** | **515** (512 + 3), 0 failures, 0 errors |
| runtime-md | **module total** (copied runtime tests) | **189** (142 + 47), 0 failures, 0 errors, 1 skipped |

Many test methods loop over cases and report every failing case at once, so the method counts understate the volume. For example, the 5 golden path tests cover 969 cases, and `testMultipleArraysAsElementsAndValues` runs 23 kinds × 4 paths × (3 formats + pipe).

### 4.2 Coverage (JaCoCo 0.8.5, `protostuff-runtime` tests only)

| Class group | Lines before | Lines after | Branches before | Branches after |
|---|---|---|---|---|
| 7 primitive-capable classes (`BoolArray` … `DoubleArray`) | 357 / 406 | 406 / 406 | 185 / 245 | 245 / 245 |
| 6 reference classes (`StringArray` … `DateArray`) | 186 / 210 | 210 / 210 | 96 / 126 | 126 / 126 |
| `DelegateArray`, `EnumArray`, `PojoArray` | 92 / 111 | 111 / 111 | 43 / 63 | 63 / 63 |
| `ArraySchemas` static helpers, `Base`, `GENERIC_HANDLER` | 82 / 110 | 110 / 110 | 64 / 91 | 91 / 91 |
| Pipe schemas (anonymous `Pipe.Schema` per class) | 72 / 80 | 80 / 80 | 12 / 18 | 18 / 18 |
| `IdStrategy` anonymous `ArraySchemas` subclasses (derived and shared) | 111 / 142 | 142 / 142 | 49 / 68 | 68 / 68 |
| **Total** | **900 / 1059 (85.0%)** | **1059 / 1059 (100%)** | **449 / 611 (73.5%)** | **611 / 611 (100%)** |

The "before" numbers come from the coverage run of the plan's §3. The denominators are identical, so the two runs are comparable.

The only JaCoCo instrumentation error in the log is for a JDK-internal class (`sun/util/resources/cldr/provider/CLDRLocaleDataMetaInfo`, class file version 65). JaCoCo 0.8.5 can't read Java 21 class files. No project class is affected.

---

## 5. Characterization findings

Each test below asserts the **current** behavior and is marked `// CHARACTERIZATION: possible bug`. If anyone changes the behavior, deliberately or by accident during the refactor, the test fails and says so.

### F1. Enum arrays as collection or map elements cannot be deserialized (NullPointerException)

* **What:** a `Map<K, SomeEnum[]>` value, a `Map<SomeEnum[], V>` key, or a `List<SomeEnum[]>` element is serialized without error, but deserializing it throws `NullPointerException: Cannot invoke "PolymorphicSchema$Handler.setValue(...)" because "this.handler" is null`. The list case applies when `COLLECTION_SCHEMA_ON_REPEATED_FIELDS` is in effect, which **`PRESERVE_NULL_ELEMENTS` implies**. This happens in every format (protostuff, protobuf, graph). The pipe is not affected.
* **Cause:** `EnumIO.java:212` creates its `genericElementSchema` as `new ArraySchemas.EnumArray(strategy, null, this)`, with a **null handler**. `HasSchema` (`:62`) and `HasDelegate` (`:39`) pass `ArraySchemas.GENERIC_HANDLER`, and the derived scalar schemas in `IdStrategy` override `setValue`.
* **Where it's asserted:** `ArraySchemasGoldenBytesTest.enumArrayDecodeThrowsNpe` (42 golden cases), `ArraySchemasNullPolicyTest`, `ArraySchemasEntryPathTest.testMultipleArraysAsElementsAndValues`, `testMapValuesUseTheGenericHandler` and `testArraysAsMapKeys`.
* **Not caught before:** no existing test puts an enum array in a collection or map.

### F2. `Byte[]` (boxed byte arrays) are not supported at all

* **What:** `RuntimeFieldFactory.ID_BYTE` is missing from `ArraySchemas.getSchema`, `getGenericElementSchema` and `newSchema`. As a result:
  * A pojo with a `Byte[]` field, a `List<Byte[]>` or a `Map<K, Byte[]>` **cannot get a schema at all**, even when the field is null: `RuntimeSchema.getSchema` throws `RuntimeException("Should not happen.")`.
  * A `Byte[]` inside an `Object` field fails on write (`ObjectSchema.writeArrayTo` → `ArraySchemas.getSchema:149`).
* `toArrayId` / `toInlineId` still reserve IDs 1 and 9 for `ID_BYTE`, which suggests `Byte[]` support was intended.
* **Where it's asserted:** `ArraySchemasEntryPathTest.testBoxedByteArrayIsNotSupported`, `testUnknownIdsShouldNotHappen` and `testInlineArrayIds`.

### F3. Nulls without `PRESERVE_NULL_ELEMENTS` can be written but not read (known; kept per decision 1)

* For **every** non-primitive kind (16) × **every** path (field, `Object` field, repeated list, map) × **every** format (3), the write succeeds and the read throws `ProtostuffException("Corrupt input.")`. The writer drops the nulls but still writes the full length.
* **Where it's asserted:** `ArraySchemasNullPolicyTest.testNullsWithoutPreserveCannotBeRead`.

### F4. The pipe accepts a null count inside a *primitive* array (CI-2)

* Replacing one element of a primitive array with `ID_ARRAY_NULLCOUNT = 1` (count kept consistent) is rejected on **read** ("Corrupt input.") but **piped without error** for all 7 primitive kinds. The shared `ArraySchemas.transferObject` doesn't know the component type, so a pipe can forward data that no reader accepts.
* **Where it's asserted:** `ArraySchemasCorruptInputTest.testMalformedArrayMessage`.

### F5. A null count larger than the array length is accepted silently (CI-6)

* The input `LEN = 2, NULLCOUNT = 5` is accepted by the reader **and** the pipe for all 16 non-primitive kinds. The result is an all-null array of the declared length (2), with no error. The reader adds the null count to its index without checking it against the length.
* **Where it's asserted:** `ArraySchemasCorruptInputTest.testNullCountOvershootsLength`.

### F6. JSON output repeats the `"b"` key for boxed arrays with nulls

* `Integer[] {1, null, 3}` is written as `{"a":3,"b":[1],"c":1,"b":[3]}`, with the `"b"` key twice in the same object. Protostuff reads it back correctly, but strict JSON parsers reject duplicate keys, and many others keep only the last value. That makes the data unreadable or silently wrong outside protostuff.
* **Where it's asserted:** `ArraySchemasTextFormatTest.testNamedOutput` (the exact strings) and `testNumericOutput`.

### Plan correction: the "path C" wording in ARRAYSCHEMAS_TEST_PLAN.md §2 is incomplete

* `PRESERVE_NULL_ELEMENTS` **implicitly enables** `COLLECTION_SCHEMA_ON_REPEATED_FIELDS` (`IdStrategy.java:204-205`). So a `List<X[]>` uses the handler-bound path C schema **only when nulls are not preserved**. With the flag on, it uses the derived (path D) schema.
* Consequences:
  * The existing `NullArrayElementTest` family (always run with `PRESERVE_NULL_ELEMENTS`) **never exercised path C**. The new tests cover it with null preservation off.
  * For a `List<X[]>`, turning the flag on **changes the bytes even when there are no nulls**. `ArraySchemasNullPolicyTest.testNullFreeArraysAreUnaffectedByTheFlag` asserts this. For the other paths the bytes are identical with the flag on or off.

### Side observations (outside `ArraySchemas`; not turned into tests)

* **`GraphIOUtil` doesn't restore back-references to the root message.** A child whose `parent` field points to the root message comes back with a *different* parent instance, with or without arrays. Back-references to any nested object work, including through pojo arrays. For this reason GR-3 wraps the owner in a `NodeBox`.
* **`protostuff-runtime-registry` hides 4 errors.** Its test classes run nested JUnit 3 suites, which print 4 errors that surefire does not count: `UnsupportedOperationException` in `testPojoWithHashMapInnerKeySetAsDelegate` and `testPojoWithImmutableListAsDelegate`, for the Protostuff and Protobuf variants. They exist before and after this change and are unrelated to arrays.

---

## 6. What I could not do as specified, and deviations

### Build and verification

1. **`-Djavac.target=1.8` is passed on every Maven command.** The root POM's `javac.target` of 1.6 is rejected by JDK 21's `javac` ("Target option 6 is no longer supported"). I did not change the root POM. To make sure the new code still compiles at 1.6, I scanned it for Java 7+ constructs (diamond, try-with-resources, multi-catch, lambdas, switch on strings, `StandardCharsets`, `Objects`, `java.nio.file`) and found none. The code was **not actually compiled with `-source 1.6`**, because no JDK that supports it is installed.
2. **The verification command `mvn -o -pl protostuff-runtime,protostuff-json test` cannot work as written.** `protostuff-json` depends on `protostuff-runtime-registry`, which isn't in the local Maven repository, and on the `tests` jars, which only exist after `package`. I ran the equivalent reactor build from §3 instead. It also needs `-Dmaven.javadoc.skip=true`, because javadoc fails with source 6 on JDK 21.
3. **`protostuff-json` baseline errors.** At baseline it errors on JDK 17/21 (`InaccessibleObjectException`) for the same reason the runtime module did. Since the decision only covered `protostuff-runtime/pom.xml`, I passed `--add-opens` on the command line (`-DargLine=...`) and left `protostuff-json/pom.xml` unchanged. **Recommendation:** apply the same argLine to the json POM.
4. **`protostuff-runtime-md` doesn't build with the current tooling,** independent of this work:
   * Its `maven-antrun-plugin` configuration uses `<tasks>`, which antrun 3.x removed, and the POM pins no version, so Maven 3.10 resolves 3.2.0.
   * Its tests also need `--add-opens` for `java.util`, `java.lang` **and `java.util.concurrent.atomic`**. The reflection-only path reflects into `AtomicBoolean`.

   I verified it with a **temporary** copy of its POM (`pom-verify-tmp.xml`, with antrun pinned to 1.8, which is available offline, and the three `--add-opens`). Results: baseline 142/0/0, after 189/0/0. **I deleted the temporary POM and the module's `target/` afterwards.** The real POM is unchanged. **Recommendation:** pin `maven-antrun-plugin` to 1.8 (or migrate `<tasks>` to `<target>`) and add the argLine there too.
5. **The golden test reads fixtures through a fallback path in `runtime-md`.** That module copies test *sources* but not test *resources*, so `ArraySchemasGoldenBytesTest` falls back to `../protostuff-runtime/src/test/resources/...`. It passes there. That proves the reflection-only build produces **byte-identical** output to the Unsafe build, which is also useful for refactoring opportunity #2.

### Deviations from the plan

6. **Golden fixtures are grouped into one file per path** (5 files, 969 cases) instead of one file per case, to keep them reviewable. Plan §4.3 allowed `*.hex` files. Decision 4 (hex files under `src/test/resources`) is followed.
7. **TF-4 is in the runtime module.** The `Base` field-naming test is in `ArraySchemasEntryPathTest` (protostuff-runtime), not in the JSON test, because the array schema instances (`IdStrategy.ARRAY_*`) are package-private. TF-1..TF-3 are in `protostuff-json` as planned.
8. **The graph variants of the null-element tests can't do a pipe round trip.** `GraphIOUtil` has no pipe API, so their `roundTrip` checks that the streamed output equals the byte-array output and that re-serializing the parsed message reproduces identical bytes. Message equality is still asserted by the inherited tests.
9. **GR-3 uses a nested owner.** See the side observation about root back-references in §5.
10. **The CI-2 mutation replaces an element instead of inserting a null count.** My first version *inserted* the null count, which also broke the element count, so the pipe failed for the wrong reason. The final version *replaces* one element with `NULLCOUNT = 1`, which isolates the null-count check. That's how F4 surfaced.
11. **The CI tests use the protobuf wire format only.** Protostuff and graph nest messages as groups, which would need a second wire parser. Those formats are covered for valid data by T1, T3 and T4, and for the null-policy failure (NP-1).
12. **Copyright headers.** I did not copy the "Copyright David Yu" header into the new files, to avoid misattributing them. Five existing test files also have no header.

---

## 7. Reverting

Everything is uncommitted on branch `arrayschemas-detailed-tests`:

```sh
git restore protostuff-runtime/pom.xml protostuff-runtime/src/test/java/io/protostuff/runtime/NullArrayElementTest.java
git clean -nd   # preview the new files, then:
git clean -fd protostuff-runtime/src/test protostuff-json/src/test ARRAYSCHEMAS_TEST_REPORT.md
```
