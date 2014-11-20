//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package io.protostuff;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test the buffering output capability of {@link ProtobufOutput}.
 * 
 * @author David Yu
 * @created Oct 7, 2010
 */
public class ProtobufBufferedOutputTest extends SerDeserTest
{

    @Override
    protected <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        ProtobufIOUtil.mergeDelimitedFrom(in, message, schema);
    }

    @Override
    protected <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
            throws IOException
    {
        ProtobufIOUtil.writeDelimitedTo(out, message, schema, buf());
    }

    @Override
    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema)
            throws IOException
    {
        ProtobufIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    @Override
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return ProtobufIOUtil.toByteArray(message, schema, buf());
    }
    
    public void testNestedOverflow()
    {
        final int bufSize = 256;
        final Bar bar = new Bar();
        
        // reserve 3 bytes: 
        // 1st: tag
        // 2nd and 3rd: delimited length (greater than 127 takes to more than one byte)
        int repeat = bufSize - 3;
        bar.setSomeString(repeatChar('a', repeat));
        byte[] data = ProtobufIOUtil.toByteArray(bar, bar.cachedSchema(), buf(bufSize));
        assertEquals(bufSize, data.length);
        
        // additional size will be:
        // 1 (tag)
        // 1 (delimiter)
        // 1 + 1 = tag + value (10)
        // 1 + 1 + 3 = tag + delim + value ("baz")
        // 1 + 1 = tag + value (15)
        // =====
        // 11
        bar.setSomeBaz(new Baz(10, "baz", 15l));
        data = ProtobufIOUtil.toByteArray(bar, bar.cachedSchema(), buf(bufSize));
        assertEquals(bufSize + 11, data.length);
        
        final Bar parsedBar = new Bar();
        ProtobufIOUtil.mergeFrom(data, parsedBar, parsedBar.cachedSchema());
        assertEquals(bar, parsedBar);
    }

    public void testNestedLarge()
    {
        final int bufSize = 256;
        final Bar bar = new Bar();
        
        // nested message size will be:
        // 1 + 1 = tag + value (10)
        // 1 + 1 + 125 = tag + delim + value ("baz")
        // 1 + 1 = tag + value (15)
        // =====
        // 131
        bar.setSomeBaz(new Baz(10, repeatChar('b', 125), 15l));
        
        // size will be:
        // 1 (tag)
        // 2 (delimited length) (nested message size is greater than 127)
        // 131 (nested message size)
        // =====
        // 134
        byte[] data = ProtobufIOUtil.toByteArray(bar, bar.cachedSchema(), buf(bufSize));
        assertEquals(134, data.length);
        
        final Bar parsedBar = new Bar();
        ProtobufIOUtil.mergeFrom(data, parsedBar, parsedBar.cachedSchema());
        assertEquals(bar, parsedBar);
    }

}
