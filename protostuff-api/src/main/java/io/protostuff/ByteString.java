//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// http://code.google.com/p/protobuf/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package io.protostuff;

import static io.protostuff.StringSerializer.STRING;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Immutable array of bytes.
 * 
 * @author crazybob@google.com Bob Lee
 * @author kenton@google.com Kenton Varda
 * @author David Yu
 */
public final class ByteString
{
    // START EXTRA
    // internal package access to avoid double memory allocation
    static ByteString wrap(byte[] bytes)
    {
        return new ByteString(bytes);
    }

    // internal package access to avoid double memory allocation
    byte[] getBytes()
    {
        return bytes;
    }

    /**
     * Writes the bytes to the {@link OutputStream}.
     */
    public static void writeTo(OutputStream out, ByteString bs) throws IOException
    {
        out.write(bs.bytes);
    }

    /**
     * Writes the bytes to the {@link DataOutput}.
     */
    public static void writeTo(DataOutput out, ByteString bs) throws IOException
    {
        out.write(bs.bytes);
    }

    /**
     * Writes the bytes to the {@link Output}.
     */
    public static void writeTo(Output output, ByteString bs, int fieldNumber,
            boolean repeated) throws IOException
    {
        output.writeByteArray(fieldNumber, bs.bytes, repeated);
    }

    public String toString()
    {
        return toStringUtf8();
    }

    // END EXTRA
    private final byte[] bytes;

    private ByteString(final byte[] bytes)
    {
        this.bytes = bytes;
    }

    /**
     * Gets the byte at the given index.
     * 
     * @throws ArrayIndexOutOfBoundsException
     *             {@code index} is &lt; 0 or &gt;= size
     */
    public byte byteAt(final int index)
    {
        return bytes[index];
    }

    /**
     * Gets the number of bytes.
     */
    public int size()
    {
        return bytes.length;
    }

    /**
     * Returns {@code true} if the size is {@code 0}, {@code false} otherwise.
     */
    public boolean isEmpty()
    {
        return bytes.length == 0;
    }

    // =================================================================
    // byte[] -> ByteString

    /**
     * Empty String.
     */
    public static final String EMPTY_STRING = "";

    /**
     * Empty byte array.
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * Empty ByteString.
     */
    public static final ByteString EMPTY = new ByteString(EMPTY_BYTE_ARRAY);

    /**
     * Copies the given bytes into a {@code ByteString}.
     */
    public static ByteString copyFrom(final byte[] bytes, final int offset,
            final int size)
    {
        final byte[] copy = new byte[size];
        System.arraycopy(bytes, offset, copy, 0, size);
        return new ByteString(copy);
    }

    /**
     * Copies the given bytes into a {@code ByteString}.
     */
    public static ByteString copyFrom(final byte[] bytes)
    {
        return copyFrom(bytes, 0, bytes.length);
    }

    /**
     * Encodes {@code text} into a sequence of bytes using the named charset and returns the result as a
     * {@code ByteString}.
     */
    public static ByteString copyFrom(final String text, final String charsetName)
    {
        try
        {
            return new ByteString(text.getBytes(charsetName));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(charsetName + " not supported?", e);
        }
    }

    /**
     * Encodes {@code text} into a sequence of UTF-8 bytes and returns the result as a {@code ByteString}.
     */
    public static ByteString copyFromUtf8(final String text)
    {
        return new ByteString(STRING.ser(text));
        /*
         * @try { return new ByteString(text.getBytes("UTF-8")); } catch (UnsupportedEncodingException e) { throw new
         * RuntimeException("UTF-8 not supported?", e); }
         */
    }

    // =================================================================
    // ByteString -> byte[]

    /**
     * Copies bytes into a buffer at the given offset.
     * 
     * @param target
     *            buffer to copy into
     * @param offset
     *            in the target buffer
     */
    public void copyTo(final byte[] target, final int offset)
    {
        System.arraycopy(bytes, 0, target, offset, bytes.length);
    }

    /**
     * Copies bytes into a buffer.
     * 
     * @param target
     *            buffer to copy into
     * @param sourceOffset
     *            offset within these bytes
     * @param targetOffset
     *            offset within the target buffer
     * @param size
     *            number of bytes to copy
     */
    public void copyTo(final byte[] target, final int sourceOffset,
            final int targetOffset,
            final int size)
    {
        System.arraycopy(bytes, sourceOffset, target, targetOffset, size);
    }

    /**
     * Copies bytes to a {@code byte[]}.
     */
    public byte[] toByteArray()
    {
        final int size = bytes.length;
        final byte[] copy = new byte[size];
        System.arraycopy(bytes, 0, copy, 0, size);
        return copy;
    }

    /**
     * Constructs a new read-only {@code java.nio.ByteBuffer} with the same backing byte array.
     */
    public ByteBuffer asReadOnlyByteBuffer()
    {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.asReadOnlyBuffer();
    }

    /*
     * @ Constructs a new {@code String} by decoding the bytes using the specified charset.
     */
    /*
     * @public String toString(final String charsetName) throws UnsupportedEncodingException { return new String(bytes,
     * charsetName); }
     */

    /**
     * Constructs a new {@code String} by decoding the bytes as UTF-8.
     */
    public String toStringUtf8()
    {
        return STRING.deser(bytes);
        /*
         * @try { return new String(bytes, "UTF-8"); } catch (UnsupportedEncodingException e) { throw new
         * RuntimeException("UTF-8 not supported?", e); }
         */
    }

    // =================================================================
    // equals() and hashCode()

    @Override
    public boolean equals(final Object o)
    {
        return o == this || (o instanceof ByteString && equals(this, (ByteString) o, false));
    }

    /**
     * Returns true if the contents of both match.
     */
    public static boolean equals(ByteString bs, ByteString other, boolean checkHash)
    {
        final int size = bs.bytes.length;
        if (size != other.bytes.length)
        {
            return false;
        }

        if (checkHash)
        {
            // volatile reads
            final int h1 = bs.hash, h2 = other.hash;
            if (h1 != 0 && h2 != 0 && h1 != h2)
            {
                return false;
            }
        }

        final byte[] thisBytes = bs.bytes;
        final byte[] otherBytes = other.bytes;
        for (int i = 0; i < size; i++)
        {
            if (thisBytes[i] != otherBytes[i])
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if the contents of the internal array and the provided array match.
     */
    public boolean equals(final byte[] data)
    {
        return equals(data, 0, data.length);
    }

    /**
     * Returns true if the contents of the internal array and the provided array match.
     */
    public boolean equals(final byte[] data, int offset, final int len)
    {
        final byte[] bytes = this.bytes;
        if (len != bytes.length)
            return false;

        for (int i = 0; i < len;)
        {
            if (bytes[i++] != data[offset++])
            {
                return false;
            }
        }

        return true;
    }

    private volatile int hash = 0;

    @Override
    public int hashCode()
    {
        int h = hash;

        if (h == 0)
        {
            final byte[] thisBytes = bytes;
            final int size = bytes.length;

            h = size;
            for (int i = 0; i < size; i++)
            {
                h = h * 31 + thisBytes[i];
            }
            if (h == 0)
            {
                h = 1;
            }

            hash = h;
        }

        return h;
    }

    // =================================================================
    // Input stream

    /*
     * @ Creates an {@code InputStream} which can be used to read the bytes.
     */
    /*
     * @public InputStream newInput() { return new ByteArrayInputStream(bytes); }
     */

    /*
     * @ Creates a {@link CodedInputStream} which can be used to read the bytes. Using this is more efficient than
     * creating a {@link CodedInputStream} wrapping the result of {@link #newInput()}.
     */
    /*
     * @public CodedInputStream newCodedInput() { // We trust CodedInputStream not to modify the bytes, or to give
     * anyone // else access to them. return CodedInputStream.newInstance(bytes); }
     */

    // =================================================================
    // Output stream

    /*
     * @ Creates a new {@link Output} with the given initial capacity.
     */
    /*
     * @public static Output newOutput(final int initialCapacity) { return new Output(new
     * ByteArrayOutputStream(initialCapacity)); }
     */

    /*
     * @ Creates a new {@link Output}.
     */
    /*
     * @public static Output newOutput() { return newOutput(32); }
     */

    /*
     * @ Outputs to a {@code ByteString} instance. Call {@link #toByteString()} to create the {@code ByteString}
     * instance.
     */
    /*
     * @public static final class Output extends FilterOutputStream { private final ByteArrayOutputStream bout;
     * 
     * /** Constructs a new output with the given initial capacity.
     * 
     * @ private Output(final ByteArrayOutputStream bout) { super(bout); this.bout = bout; }
     * 
     * /** Creates a {@code ByteString} instance from this {@code Output}.
     * 
     * @ public ByteString toByteString() { final byte[] byteArray = bout.toByteArray(); return new
     * ByteString(byteArray); } }
     * 
     * /** Constructs a new ByteString builder, which allows you to efficiently construct a {@code ByteString} by
     * writing to a {@link CodedOutputStream}. Using this is much more efficient than calling {@code newOutput()} and
     * wrapping that in a {@code CodedOutputStream}.
     * 
     * <p>This is package-private because it's a somewhat confusing interface. Users can call {@link
     * Message#toByteString()} instead of calling this directly.
     * 
     * @param size The target byte size of the {@code ByteString}. You must write exactly this many bytes before
     * building the result.
     * 
     * @ static CodedBuilder newCodedBuilder(final int size) { return new CodedBuilder(size); }
     * 
     * /** See {@link ByteString#newCodedBuilder(int)}. *@ static final class CodedBuilder { private final
     * CodedOutputStream output; private final byte[] buffer;
     * 
     * private CodedBuilder(final int size) { buffer = new byte[size]; output = CodedOutputStream.newInstance(buffer); }
     * 
     * public ByteString build() { output.checkNoSpaceLeft();
     * 
     * // We can be confident that the CodedOutputStream will not modify the // underlying bytes anymore because it
     * already wrote all of them. So, // no need to make a copy. return new ByteString(buffer); }
     * 
     * public CodedOutputStream getCodedOutput() { return output; } }
     */

    // moved from Internal.java

    /**
     * Helper called by generated code to construct default values for string fields.
     * <p>
     * The protocol compiler does not actually contain a UTF-8 decoder -- it just pushes UTF-8-encoded text around
     * without touching it. The one place where this presents a problem is when generating Java string literals. Unicode
     * characters in the string literal would normally need to be encoded using a Unicode escape sequence, which would
     * require decoding them. To get around this, protoc instead embeds the UTF-8 bytes into the generated code and
     * leaves it to the runtime library to decode them.
     * <p>
     * It gets worse, though. If protoc just generated a byte array, like: new byte[] {0x12, 0x34, 0x56, 0x78} Java
     * actually generates *code* which allocates an array and then fills in each value. This is much less efficient than
     * just embedding the bytes directly into the bytecode. To get around this, we need another work-around. String
     * literals are embedded directly, so protoc actually generates a string literal corresponding to the bytes. The
     * easiest way to do this is to use the ISO-8859-1 character set, which corresponds to the first 256 characters of
     * the Unicode range. Protoc can then use good old CEscape to generate the string.
     * <p>
     * So we have a string literal which represents a set of bytes which represents another string. This function --
     * stringDefaultValue -- converts from the generated string to the string we actually want. The generated code calls
     * this automatically.
     */
    public static String stringDefaultValue(String bytes)
    {
        try
        {
            return new String(bytes.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // This should never happen since all JVMs are required to implement
            // both of the above character sets.
            throw new IllegalStateException(
                    "Java VM does not support a standard character set.", e);
        }
    }

    /**
     * Helper called by generated code to construct default values for bytes fields.
     * <p>
     * This is a lot like {@link #stringDefaultValue}, but for bytes fields. In this case we only need the second of the
     * two hacks -- allowing us to embed raw bytes as a string literal with ISO-8859-1 encoding.
     */
    public static ByteString bytesDefaultValue(String bytes)
    {
        return new ByteString(byteArrayDefaultValue(bytes));
    }

    /**
     * Helper called by generated code to construct default values for byte array fields.
     * <p>
     * This is a lot like {@link #stringDefaultValue}, but for bytes fields. In this case we only need the second of the
     * two hacks -- allowing us to embed raw bytes as a string literal with ISO-8859-1 encoding.
     */
    public static byte[] byteArrayDefaultValue(String bytes)
    {
        try
        {
            return bytes.getBytes("ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            // This should never happen since all JVMs are required to implement
            // ISO-8859-1.
            throw new IllegalStateException(
                    "Java VM does not support a standard character set.", e);
        }
    }
}
