package io.protostuff.runtime;

import io.protostuff.GraphIOUtil;
import io.protostuff.Pipe;
import io.protostuff.Schema;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Graph impl for {@link NullArrayElementInObjectArrayTest}.
 * <p>
 * {@link GraphIOUtil} has no pipe api, so {@link #roundTrip} verifies that the graph encoding is stable instead
 * (stream vs byte array, and re-serialization of the deserialized message).
 */
public class GraphNullArrayElementInObjectArrayTest extends NullArrayElementInObjectArrayTest
{

    @Override
    protected <T> void mergeFrom(byte[] data, int offset, int length,
            T message, Schema<T> schema) throws IOException
    {
        GraphIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    @Override
    protected <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        GraphIOUtil.mergeFrom(in, message, schema);
    }

    @Override
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return GraphIOUtil.toByteArray(message, schema, buf());
    }

    @Override
    protected <T> void writeTo(OutputStream out, T message, Schema<T> schema)
            throws IOException
    {
        GraphIOUtil.writeTo(out, message, schema, buf());
    }

    @Override
    protected <T> void roundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws IOException
    {
        byte[] graph = GraphIOUtil.toByteArray(message, schema, buf());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GraphIOUtil.writeTo(out, message, schema, buf());
        assertTrue(Arrays.equals(graph, out.toByteArray()));

        T parsedMessage = schema.newMessage();
        GraphIOUtil.mergeFrom(graph, parsedMessage, schema);

        byte[] graphRoundTrip = GraphIOUtil.toByteArray(parsedMessage, schema, buf());
        assertTrue(Arrays.equals(graph, graphRoundTrip));
    }

}
