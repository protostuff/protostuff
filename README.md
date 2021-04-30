
![Protostuff](https://protostuff.github.io/images/protostuff_300x100.png)

A java serialization library with built-in support for forward-backward compatibility (schema evolution) and validation.

- **efficient**, both in speed and memory
- **flexible**, supporting pluggable formats

### Usecase
- messaging layer in RPC
- storage format in the datastore or cache

For more information, go to https://protostuff.github.io/docs/

## Maven

1. For the core formats (protostuff, protobuf, graph)
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-core</artifactId>
    <version>1.7.4</version>
  </dependency>
  ```

2. For schemas generated at runtime
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-runtime</artifactId>
    <version>1.7.4</version>
  </dependency>
  ```

## Usage
```java
public final class Foo
{
    String name;
    int id;
    
    public Foo(String name, int id)
    {
        this.name = name;
        this.id = id;
    }
}

static void roundTrip()
{
    Foo foo = new Foo("foo", 1);

    // this is lazily created and cached by RuntimeSchema
    // so its safe to call RuntimeSchema.getSchema(Foo.class) over and over
    // The getSchema method is also thread-safe
    Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);

    // Re-use (manage) this buffer to avoid allocating on every serialization
    LinkedBuffer buffer = LinkedBuffer.allocate(512);

    // ser
    final byte[] protostuff;
    try
    {
        protostuff = ProtostuffIOUtil.toByteArray(foo, schema, buffer);
    }
    finally
    {
        buffer.clear();
    }

    // deser
    Foo fooParsed = schema.newMessage();
    ProtostuffIOUtil.mergeFrom(protostuff, fooParsed, schema);
}
```

## Important (for version 1.8.x)
If you are to purely use this to replace java serialization (no compatibility with protobuf), set the following system properties:
```
-Dprotostuff.runtime.always_use_sun_reflection_factory=true
-Dprotostuff.runtime.preserve_null_elements=true
-Dprotostuff.runtime.morph_collection_interfaces=true
-Dprotostuff.runtime.morph_map_interfaces=true
-Dprotostuff.runtime.morph_non_final_pojos=true
```

You can also customize it programmatically:
```java
static final DefaultIdStrategy STRATEGY = new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS 
        | IdStrategy.PRESERVE_NULL_ELEMENTS
        | IdStrategy.MORPH_COLLECTION_INTERFACES
        | IdStrategy.MORPH_MAP_INTERFACES
        | IdStrategy.MORPH_NON_FINAL_POJOS);
```

Use it:
```java
Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class, STRATEGY);
```

Questions/Concerns/Suggestions
------------------------------

- subscribe to http://groups.google.com/group/protostuff/

Requirements
------------

Java 1.6 or higher

Build Requirements
------------------

Maven 3.2.3 or higher

Developing with eclipse
------------------
```sh
mvn install && mvn eclipse:eclipse
# Open eclipse, import existing project, navigate to the protostuff module you're after, then hit 'Finish'.
```
