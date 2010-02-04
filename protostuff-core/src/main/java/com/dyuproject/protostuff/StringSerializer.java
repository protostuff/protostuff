package com.dyuproject.protostuff;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * String serialization - deferred binding. 
 *
 * @author David Yu
 * @created Feb 4, 2010
 */
public abstract class StringSerializer
{
    
    /**
     * Serializes the string to bytes.
     */
    public abstract byte[] ser(String nonNullValue);
    
    /**
     * Deserializes the bytes to a string.
     */
    public abstract String deser(byte[] nonNullValue);
    
    private static final StringSerializer __utf8string = new StringSerializer()
    {
        public String deser(byte[] nonNullValue)
        {
            try
            {
                return new String(nonNullValue, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }
        }

        public byte[] ser(String nonNullValue)
        {
            try
            {
                return nonNullValue.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }
        }
    };
    
    private static final Charset utf8 = Charset.forName("UTF-8");
    
    private static final StringSerializer __utf8stringFromCharset = new StringSerializer()
    {
        public String deser(byte[] nonNullValue)
        {
            return new String(nonNullValue, utf8);
        }

        public byte[] ser(String nonNullValue)
        {
            return nonNullValue.getBytes(utf8);
        }
    };

    private static final StringSerializer __string = new StringSerializer()
    {
        public String deser(byte[] nonNullValue)
        {
            return new String(nonNullValue);
        }

        public byte[] ser(String nonNullValue)
        {
            return nonNullValue.getBytes();
        }
    };
    
    /**
     * UTF-8 string serializer.  Binding deferred.
     */
    public static final StringSerializer STRING = 
        "UTF-8".equals(Charset.defaultCharset().name()) ? __string : 
            (System.getProperty("charset.utf8")!=null ? __utf8stringFromCharset : __utf8string);


}
