package com.dyuproject.protostuff.benchmark.serializers;

/**
 * Copied from http://thrift-protobuf-compare.googlecode.com/svn/trunk/tpc/src/serializers/
 * 
 */

public interface ObjectSerializer<T>
{
    public T create() throws Exception;
    public T deserialize(byte[] array) throws Exception;
    public byte[] serialize(T content) throws Exception;
    public String getName ();
}
