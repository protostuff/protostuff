#!/bin/sh

mvn -DforkMode=never -Dtest_id_strategy=explicit test
