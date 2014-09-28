#!/bin/sh

mvn -o -DforkMode=never \
    -Dprotostuff.runtime.allow_null_array_element=true \
    -Dtest="*NullArrayElementTest" test
