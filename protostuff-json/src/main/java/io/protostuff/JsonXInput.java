package io.protostuff;

import java.io.IOException;
import java.io.InputStream;
import java.net.NetPermission;
import java.nio.ByteBuffer;

public class JsonXInput implements Input
{
    private static final byte[] NULL = { 'n', 'u', 'l', 'l' };
    private static final byte BEL = '\b';
    private static final byte TAB = '\t';
    private static final byte FF = '\f';
    private static final byte LF = '\n';
    private static final byte CR = '\r';

    private final boolean numeric;
    private InputStream in;

    private final Utf8Decoder utf8;
    private final byte[] buf;
    private int o;
    private int l;

    private boolean lastRepeated;
    private Schema<?> lastSchema;
    private String lastName;
    private int lastNumber;

    public JsonXInput(InputStream in)
    {
        this(in, false);
    }

    public JsonXInput(InputStream in, boolean numeric)
    {
        this.in = in;
        this.numeric = numeric;
        this.utf8 = new Utf8Decoder();
        this.buf = new byte[4096];
    }

    /**
     * Returns whether the incoming messages' field names are numeric.
     */
    public boolean isNumeric()
    {
        return numeric;
    }

    /**
     * Gets the last field number read.
     */
    public int getLastNumber()
    {
        return lastNumber;
    }

    public void reset()
    {
        lastRepeated = false;
        lastSchema = null;
        lastName = null;
        lastNumber = 0;
    }

    @Override
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        int nestedObjects = 0;
        boolean quote = false;
        while (true)
        {
            checkBuffer(1);
            byte b = buf[o++];
            if (b == '\\')
            {
                checkBuffer(1);
                b = buf[o++];
                if (b == 'u')
                {
                    checkBuffer(4);
                    o += 4;
                }
                continue;
            }
            if (b == '"')
                quote = !quote;
            if (quote)
                continue;

            if (b == '{' || b == ']')
                nestedObjects++;
            else if (b == '}' || b == ']')
            {
                if (nestedObjects == 0)
                {
                    o--;
                    break;
                }
                if (--nestedObjects == 0)
                    break;
            }

            if (b == ',' && nestedObjects == 0)
                break;
        }
        readRepeated();
    }

    @Override
    public <T> int readFieldNumber(final Schema<T> schema) throws IOException
    {
        lastSchema = schema;
        if (readNext() != ',')
            --o;

        while ((lastNumber = readFieldNumber()) == -1)
            ;
        return lastNumber;
    }

    private int readFieldNumber() throws IOException
    {
        if (lastRepeated)
        {
            checkBuffer(5);
            while (readNull() && buf[o] == ',')
                o++;
            if (buf[o] == ']')
            {
                o++;
                lastRepeated = false;
                checkBuffer(5);
                return -1;
            }
            return lastNumber;
        }

        int next = readNext();
        if (next == '}')
            return lastNumber = 0;
        if (next != '"')
            throwUnexpectedContent('"', next);
        lastName = readRawString();
        readNext(':');
        if (readNull())
            return -1;

        checkBuffer(1);
        if (readNext() == '[')
        {
            checkBuffer(5);
            while (readNull() && buf[o] == ',')
                o++;
            if (buf[o] == ']')
            {
                o++;
                lastRepeated = false;
                return -1;
            }
            lastRepeated = true;
        }
        else
            --o;
        return lastNumber = numeric ? Integer.parseInt(lastName) : lastSchema.getFieldNumber(lastName);
    }

    @Override
    public boolean readBool() throws IOException
    {
        boolean b = Boolean.parseBoolean(readValue());
        readRepeated();
        return b;
    }

    @Override
    public byte[] readByteArray() throws IOException
    {
        return B64Code.decode(readString());
    }

    @Override
    public ByteString readBytes() throws IOException
    {
        return ByteString.wrap(readByteArray());
    }

    @Override
    public void readBytes(final ByteBuffer bb) throws IOException
    {
        bb.put(readByteArray());
    }

    @Override
    public double readDouble() throws IOException
    {
        double d = Double.parseDouble(readValue());
        readRepeated();
        return d;
    }

    @Override
    public int readEnum() throws IOException
    {
        return readInt32();
    }

    @Override
    public int readFixed32() throws IOException
    {
        String rawValue = readValue();
        readRepeated();
        if (rawValue.startsWith("-"))
            return Integer.parseInt(rawValue);
        return UnsignedNumberUtil.parseUnsignedInt(rawValue);
    }

    @Override
    public long readFixed64() throws IOException
    {
        String rawValue = readValue();
        readRepeated();
        if (rawValue.startsWith("-"))
        {
            return Long.parseLong(rawValue);
        }
        return UnsignedNumberUtil.parseUnsignedLong(rawValue);
    }

    @Override
    public float readFloat() throws IOException
    {
        float value = Float.parseFloat(readValue());
        readRepeated();
        return value;
    }

    @Override
    public int readInt32() throws IOException
    {
        int value = Integer.parseInt(readValue());
        readRepeated();
        return value;
    }

    @Override
    public long readInt64() throws IOException
    {
        long value = Long.parseLong(readValue());
        readRepeated();
        return value;
    }

    @Override
    public int readSFixed32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readSFixed64() throws IOException
    {
        return readInt64();
    }

    @Override
    public int readSInt32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readSInt64() throws IOException
    {
        return readInt64();
    }

    @Override
    public String readString() throws IOException
    {
        readNext('"');
        String value = readRawString();
        readRepeated();
        return value;
    }

    @Override
    public int readUInt32() throws IOException
    {
        return readFixed32();
    }

    @Override
    public long readUInt64() throws IOException
    {
        return readFixed64();
    }

    @Override
    public <T> T mergeObject(T value, final Schema<T> schema) throws IOException
    {
        readNext('{');

        final int previousNumber = this.lastNumber;
        final boolean previousRepeated = this.lastRepeated;
        final String previousName = this.lastName;

        // reset
        this.lastRepeated = false;

        if (value == null)
            value = schema.newMessage();

        schema.mergeFrom(this, value);

        if (!schema.isInitialized(value))
            throw new UninitializedMessageException(value, schema);

        // restore state
        this.lastNumber = previousNumber;
        this.lastRepeated = previousRepeated;
        this.lastName = previousName;

        readRepeated();

        return value;
    }

    @Override
    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber, boolean repeated)
            throws IOException
    {
        if (utf8String)
            output.writeString(fieldNumber, readString(), repeated);
        else
            output.writeByteArray(fieldNumber, readByteArray(), repeated);
    }

    /**
     * Reads a byte array/ByteBuffer value.
     */
    @Override
    public ByteBuffer readByteBuffer() throws IOException
    {
        return ByteBuffer.wrap(readByteArray());
    }

    private void checkBuffer(int n) throws IOException
    {
        if (n <= l - o)
            return;

        if (in == null)
        {
            if (l - o == 0)
                throwEOF();
            return;
        }

        if (o != l && o > 2048)
        {
            l -= o;
            System.arraycopy(buf, o, buf, o, l);
            o = 0;
        }
        do
        {
            int i = in.read(buf, o, buf.length - l - o);
            if (i == -1)
            {
                in = null;
                if (l - o == 0)
                    throwEOF();
                return;
            }
            l += i;
        } while (l < n);
    }

    /**
     * read next meaningful char
     * 
     * @return char or -1 on eof
     * @throws IOException
     *             on read error
     */
    private byte readNext() throws IOException
    {
        checkBuffer(1);
        byte b = buf[o++];
        while (b == ' ' || b == '\t' || b == '\n' || b == '\r')
        {
            checkBuffer(1);
            b = buf[o++];
        }
        return b;
    }

    private boolean readNull() throws IOException
    {
        byte next = readNext();
        if (next != 'n')
        {
            --o;
            return false;
        }

        checkBuffer(3);
        if (l - o > 3 && equals(buf, o, NULL, 1, 3))
        {
            o += 3;
            return true;
        }
        return false;
    }

    private void throwUnexpectedContent(int expected, int actual) throws JsonInputException
    {
        throwUnexpectedContent(toString(expected),
                actual == -1 ? "EOF" : toString(actual));
    }

    private void throwUnexpectedContent(String expected, String actual) throws JsonInputException
    {
        StringBuilder sb = new StringBuilder("Expected ").append(expected).append(" but was ").append(actual)
                .append(" on ").append(lastName);
        if (lastSchema != null)
            sb.append(" of message ").append(lastSchema.messageFullName());
        throw new JsonInputException(sb.toString());
    }

    private void throwEOF() throws JsonInputException
    {
        throwUnexpectedContent("data", "EOF");
    }

    public void readNext(char expected) throws IOException
    {
        byte b = readNext();
        if (expected != b)
            throwUnexpectedContent(expected, b);
    }

    public void readNext(byte[] expected) throws IOException
    {
        readNext();
        --o;
        checkBuffer(expected.length);
        int i = 0;
        while (i < expected.length && o < l)
        {
            if (buf[o++] != expected[i++])
                throwUnexpectedContent(expected[i - 1], buf[o - 1]);
        }
        if (o == l && i < expected.length)
            throwEOF();
    }

    public boolean tryNext(byte[] c) throws IOException
    {
        if (c.length == 0)
            return false;
        readNext();
        --o;
        checkBuffer(c.length);
        if (l - o < c.length)
            return false;
        if (!equals(c, 0, buf, o, c.length))
            return false;
        o += c.length;
        return true;
    }

    public boolean isNext(char c)
    {
        return buf[o] == c;
    }

    private String readRawString() throws IOException
    {
        byte i;
        while ((i = buf[o++]) != '"')
        {
            checkBuffer(1);
            if (i == '\\')
            {
                i = buf[o++];
                if (i == '"' || i == '\\' || i == '/')
                    utf8.append(i);
                else if (i == 'b')
                    utf8.append(BEL);
                else if (i == 'f')
                    utf8.append(FF);
                else if (i == 'n')
                    utf8.append(LF);
                else if (i == 'r')
                    utf8.append(CR);
                else if (i == 't')
                    utf8.append(TAB);
                else if (i == 'u')
                {
                    checkBuffer(4);
                    utf8.appendUnicode(readHex() << 12 | readHex() << 8 | readHex() << 4 | readHex());
                }
                break;
            }
            utf8.append(i);
        }
        return utf8.done();
    }

    private int readHex() throws IOException
    {
        byte b = buf[o++];
        if (b >= '0' && b <= '9')
            return b - '0';
        if (b >= 'a' && b <= 'f')
            return 10 + b - 'a';
        if (b >= 'A' && b <= 'F')
            return 10 + b - 'A';
        throwUnexpectedContent("hex digit", toString(b));
        return 0; // will not happen
    }

    private String readValue() throws IOException
    {
        byte i = buf[o++];
        if (i == '"')
            return readRawString();
        while (i != ',' && i != '}' && i != ']')
        {
            utf8.append(i);
            checkBuffer(1);
            i = buf[o++];
        }
        o--;
        return utf8.done();
    }

    private void readRepeated() throws IOException
    {
        if (!lastRepeated)
            return;
        checkBuffer(1);
        if (buf[o] == ']')
        {
            o++;
            lastRepeated = false;
        }
    }

    /**
     * check array range equality
     * 
     * @param a
     *            first array
     * @param aIndex
     *            starting index in a
     * @param b
     *            second array
     * @param bIndex
     *            starting index in b
     * @param len
     *            max length to check
     * @return true if range are equals
     */
    public static boolean equals(byte[] a, int aIndex, byte[] b, int bIndex, int len)
    {
        if (a == null || b == null)
            throw new IllegalArgumentException("a or b null");
        if (len < 0)
            throw new ArrayIndexOutOfBoundsException("len < 0");
        if (aIndex < 0)
            throw new ArrayIndexOutOfBoundsException("aIndex < 0");
        if (aIndex + len > a.length)
            throw new ArrayIndexOutOfBoundsException("aIndex +len > a.length");

        if (bIndex < 0)
            throw new ArrayIndexOutOfBoundsException("bIndex < 0");
        if (bIndex + len > b.length)
            throw new ArrayIndexOutOfBoundsException("bIndex +len > b.length");

        while (len-- > 0)
        {
            if (a[aIndex++] != b[bIndex++])
                return false;
        }
        return true;
    }

    /**
     * convert unicode code point to String
     * 
     * @param c
     *            unicode code point
     * @return the string
     */
    private static String toString(int c)
    {
        return new String(Character.toChars(c));
    }
}
