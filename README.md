
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

Support for null elements in concrete arrays.
```java

public final class Wrap
{
    Object obj, any;
    String[] strArray;
    Foo[] fooArray;
    Object[] objArray;
}

// To enable by default, set the system property:
// -Dprotostuff.runtime.allow_null_array_element=true
static final DefaultIdStrategy STRATEGY = new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS | 
        IdStrategy.ALLOW_NULL_ARRAY_ELEMENT);
        
static void roundTrip()
{
    Wrap wrap = new Wrap();
    wrap.obj = new Foo[]{ null, new Foo("0", 0), null, new Foo("1", 1), null };
    wrap.any = new String[]{ null, "hello", "world", null };
    wrap.strArray = new String[]{ "hello", null, "world" };
    wrap.fooArray = new Foo[]{ null, new Foo("0", 0), null };
    wrap.objArray = new Object[]{
        new Foo[] { null, new Foo("hello", 0), new Foo("world", 1), null }
    };
    
    // this is lazily created and cached by RuntimeSchema
    // so its safe to call RuntimeSchema.getSchema(Foo.class) over and over
    // The getSchema method is also thread-safe
    Schema<Wrap> schema = RuntimeSchema.getSchema(Wrap.class, STRATEGY);

    // Re-use (manage) this buffer to avoid allocating on every serialization
    LinkedBuffer buffer = LinkedBuffer.allocate(512);

    // ser
    final byte[] protostuff;
    try
    {
        protostuff = ProtostuffIOUtil.toByteArray(wrap, schema, buffer);
    }
    finally
    {
        buffer.clear();
    }

    // deser
    Wrap parsed = schema.newMessage();
    ProtostuffIOUtil.mergeFrom(protostuff, parsed, schema);
    
    assertArrayEquals((Foo[])wrap.obj, (Foo[])parsed.obj);
    assertArrayEquals((String[])wrap.any, (String[])parsed.any);
    assertArrayEquals(wrap.strArray, parsed.strArray);
    assertArrayEquals(wrap.fooArray, parsed.fooArray);
    assertNotNull(parsed.objArray);
    assertEquals(wrap.objArray.length, parsed.objArray.length);
    assertArrayEquals((Foo[])wrap.objArray[0], (Foo[])parsed.objArray[0]);
}

static <T> void assertArrayEquals(T[] a, T[] b)
{
    if (a == b)
        return;
    
    assertNotNull(a);
    assertEquals(a.length, b.length);
    for (int i = 0, l = a.length; i < l; i++)
        assertEquals(a[i], b[i]);
}

```

## Maven

1. When you generate schemas for your classes
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-core</artifactId>
    <version>1.6.2</version>
  </dependency>
  ```

2. Runtime schemas
   
  ```xml
  <dependency>
    <groupId>io.protostuff</groupId>
    <artifactId>protostuff-runtime</artifactId>
    <version>1.6.2</version>
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
