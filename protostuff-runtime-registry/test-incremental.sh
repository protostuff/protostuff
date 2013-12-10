#!/bin/sh

mvn -DforkMode=never -Dtest_id_strategy=incremental test
