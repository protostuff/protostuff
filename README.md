
![Protostuff](https://protostuff.github.io/images/protostuff_300x100.png)

A java serialization library with built-in support for forward-backward compatibility (schema evolution) and validation.

- **efficient**, both in speed and memory
- **flexible**, supporting pluggable formats

### Usecase
- messaging layer in RPC
- storage format in the datastore or cache

For more information, go to https://protostuff.github.io/docs/

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

## Maven

1. When you generate schemas for your classes
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-core</artifactId>
    <version>1.5.9</version>
  </dependency>
  ```

2. Runtime schemas
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-runtime</artifactId>
    <version>1.5.9</version>
  </dependency>
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
