Running the benchmark:
$ mvn -DforkMode=never -Dbenchmark.skip=false -Dtest=BenchmarkTest test

The generated html (filename starts with "benchmark-") will be on the "target" dir.
