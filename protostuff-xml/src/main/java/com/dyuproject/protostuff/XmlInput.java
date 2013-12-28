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

package com.dyuproject.protostuff;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * An input used for reading data with xml format.
 * 
 * @author David Yu
 * @created May 24, 2010
 */
public final class XmlInput implements Input
{

    private final XMLStreamReader parser;
    private boolean emptyMessage = false;
    
    public XmlInput(XMLStreamReader parser)
    {
        this.parser = parser;
    }
    
    private int next() throws IOException
    {
        try
        {
            return parser.next();
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }
    
    private int nextTag() throws IOException
    {
        try
        {
            return parser.nextTag();
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }
    
    private String getText() throws IOException
    {
        try
        {
            final String text = parser.getElementText();
            // move to next element
            parser.nextTag();
            return text;
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }
    
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        final String name = parser.getLocalName();
        while(true)
        {
            switch(next())
            {
                case END_ELEMENT:
                    if(name.equals(parser.getLocalName()))
                    {
                        // we can skip this unknown scalar field.
                        nextTag();
                        return;
                    }
                    throw new XmlInputException("Unknown field: " + name + " on message " + 
                            schema.messageFullName());
                case END_DOCUMENT:
                    // malformed xml. 
                case START_ELEMENT:
                    // message field
                    // we do not know how deep this message is
                    throw new XmlInputException("Unknown field: " + name + " on message " + 
                            schema.messageFullName());
            }
        }
    }
    
    public <T> int readFieldNumber(final Schema<T> schema) throws IOException
    {
        if(emptyMessage)
        {
            emptyMessage = false;
            return 0;
        }
        
        if(parser.getEventType() == END_ELEMENT)
            return 0;
        
        final String name = parser.getLocalName();
        final int num = schema.getFieldNumber(name);

        if(num == 0)
        {            
            while(true)
            {
                switch(next())
                {
                    case END_ELEMENT:
                        if(name.equals(parser.getLocalName()))
                        {
                            // we can skip this unknown scalar field.
                            nextTag();
                            return readFieldNumber(schema);
                        }
                        throw new XmlInputException("Unknown field: " + name + " on message " + 
                                schema.messageFullName());
                    case END_DOCUMENT:
                        // malformed xml. 
                    case START_ELEMENT:
                        // message field
                        // we do not know how deep this message is
                        throw new XmlInputException("Unknown field: " + name + " on message " + 
                                schema.messageFullName());
                }
            }
        }

        return num;
    }
    
    public int readInt32() throws IOException
    {
        return Integer.parseInt(getText());
    }
    
    public int readUInt32() throws IOException
    {
        return Integer.parseInt(getText());
    }
    
    public int readSInt32() throws IOException
    {
        return Integer.parseInt(getText());
    }
    
    public int readFixed32() throws IOException
    {
        return Integer.parseInt(getText());
    }
    
    public int readSFixed32() throws IOException
    {
        return Integer.parseInt(getText());
    }

    public long readInt64() throws IOException
    {
        return Long.parseLong(getText());
    }

    public long readUInt64() throws IOException
    {
        return Long.parseLong(getText());
    }

    public long readSInt64() throws IOException
    {
        return Long.parseLong(getText());
    }

    public long readFixed64() throws IOException
    {
        return Long.parseLong(getText());
    }
    
    public long readSFixed64() throws IOException
    {
        return Long.parseLong(getText());
    }
    
    public float readFloat() throws IOException
    {
        return Float.parseFloat(getText());
    }
    
    public double readDouble() throws IOException
    {
        return Double.parseDouble(getText());
    }
    
    public boolean readBool() throws IOException
    {
        return Boolean.parseBoolean(getText());
    }
    
    public int readEnum() throws IOException
    {
        return Integer.parseInt(getText());
    }
    
    public String readString() throws IOException
    {
        return getText();
    }
    
    public ByteString readBytes() throws IOException
    {
        return ByteString.wrap(readByteArray());
    }

    public byte[] readByteArray() throws IOException
    {
        return STRING.ser(getText());
    }
    
    public <T> T mergeObject(T value, final Schema<T> schema) throws IOException
    {
        emptyMessage = nextTag() == END_ELEMENT;
        
        if(value == null)
            value = schema.newMessage();
        
        schema.mergeFrom(this, value);
        
        if(!schema.isInitialized(value))
            throw new UninitializedMessageException(value, schema);
        
        // onto the next
        nextTag();
        
        return value;
    }

    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber,
            boolean repeated) throws IOException
    {
        if(utf8String)
            output.writeString(fieldNumber, readString(), repeated);
        else
            output.writeByteArray(fieldNumber, readByteArray(), repeated);
    }
}
