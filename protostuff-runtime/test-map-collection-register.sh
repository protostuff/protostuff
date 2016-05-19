#!/bin/sh

mvn32 -o -DforkMode=never \
    -Dprotostuff.runtime.pojo_schema_on_collection_fields=true \
    -Dprotostuff.runtime.pojo_schema_on_map_fields=true \
    -Dtest="*CollectionSchemaTest" test
