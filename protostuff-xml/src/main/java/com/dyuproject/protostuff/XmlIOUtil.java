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

import static com.dyuproject.protostuff.XmlIOFactoryUtil.DEFAULT_INPUT_FACTORY;
import static com.dyuproject.protostuff.XmlIOFactoryUtil.DEFAULT_OUTPUT_FACTORY;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Utility for the XML serialization/deserialization of messages and objects tied to a schema.
 *
 * @author David Yu
 * @created May 24, 2010
 */
public final class XmlIOUtil
{
    
    private XmlIOUtil() {}
    
    public static final String XML_ENCODING = "UTF-8", XML_VERSION = "1.0";
    
    /**
     * Serializes the {@code message} into a byte array via {@link XmlOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArray(T message)
    {
        return toByteArray(message, message.cachedSchema(), DEFAULT_OUTPUT_FACTORY);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link XmlOutput}.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return toByteArray(message, schema, DEFAULT_OUTPUT_FACTORY);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link XmlOutput}.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, XMLOutputFactory outFactory)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            writeTo(out, message, schema, outFactory);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link XmlOutput}.
     */
    public static <T extends Message<T>> void writeTo(OutputStream out, T message)
    throws IOException
    {
        writeTo(out, message, message.cachedSchema(), DEFAULT_OUTPUT_FACTORY);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link XmlOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema)
    throws IOException
    {
        writeTo(out, message, schema, DEFAULT_OUTPUT_FACTORY);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link XmlOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, 
            XMLOutputFactory outFactory) throws IOException
    {
        try
        {
            XMLStreamWriter writer = outFactory.createXMLStreamWriter(out, XML_ENCODING);
            
            writer.writeStartDocument(XML_ENCODING, XML_VERSION);
            writeTo(writer, message, schema);
            writer.writeEndDocument();
            
            writer.flush();
            writer.close();
        }
        catch(XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
    }
    
    /**
     * Serializes the {@code message} into a {@link Writer} via {@link XmlOutput}.
     */
    public static <T extends Message<T>> void writeTo(Writer w, T message)
    throws IOException
    {
        writeTo(w, message, message.cachedSchema(), DEFAULT_OUTPUT_FACTORY);
    }
    
    /**
     * Serializes the {@code message} into a {@link Writer} via {@link XmlOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(Writer w, T message, Schema<T> schema)
    throws IOException
    {
        writeTo(w, message, schema, DEFAULT_OUTPUT_FACTORY);
    }
    
    /**
     * Serializes the {@code message} into a {@link Writer} via {@link XmlOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(Writer w, T message, Schema<T> schema, 
            XMLOutputFactory outFactory) throws IOException
    {
        try
        {
            XMLStreamWriter writer = outFactory.createXMLStreamWriter(w);
            
            writer.writeStartDocument(XML_ENCODING, XML_VERSION);
            writeTo(writer, message, schema);
            writer.writeEndDocument();
            
            writer.flush();
            writer.close();
        }
        catch(XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
    }
    
    /**
     * Serializes the {@code message} into an {@link XMLStreamWriter} via {@link XmlOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(XMLStreamWriter writer, T message, Schema<T> schema)
    throws IOException, XMLStreamException, XmlOutputException
    {
        writer.writeStartElement(schema.messageName());
        
        schema.writeTo(new XmlOutput(writer).use(schema), message);
        
        writer.writeEndElement();
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    public static <T extends Message<T>> void mergeFrom(byte[] data, T message)
    {
        mergeFrom(data, 0, data.length, message, message.cachedSchema(), DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema)
    {
        mergeFrom(data, 0, data.length, message, schema, DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int len, T message, Schema<T> schema)
    {
        mergeFrom(data, 0, data.length, message, schema, DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int len, T message, 
            Schema<T> schema, XMLInputFactory inFactory)
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data, offset, len);
        try
        {
            mergeFrom(in, message, schema, inFactory);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream}.
     */
    public static <T extends Message<T>> void mergeFrom(InputStream in, T message)
    throws IOException
    {
        mergeFrom(in, message, message.cachedSchema(), DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
    throws IOException
    {
        mergeFrom(in, message, schema, DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, 
            XMLInputFactory inFactory) throws IOException
    {
        try
        {
            XMLStreamReader parser = inFactory.createXMLStreamReader(in, XML_ENCODING);
            mergeFrom(parser, message, schema);
            parser.close();
        }
        catch(XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }
    
    /**
     * Merges the {@code message} from the {@link Reader}.
     */
    public static <T extends Message<T>> void mergeFrom(Reader r, T message)
    throws IOException
    {
        mergeFrom(r, message, message.cachedSchema(), DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Merges the {@code message} from the {@link Reader} using the given {@code schema}.
     */
    public static <T> void mergeFrom(Reader r, T message, Schema<T> schema)
    throws IOException
    {
        mergeFrom(r, message, schema, DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Merges the {@code message} from the {@link Reader} using the given {@code schema}.
     */
    public static <T> void mergeFrom(Reader r, T message, Schema<T> schema, 
            XMLInputFactory inFactory) throws IOException
    {
        try
        {
            XMLStreamReader parser = inFactory.createXMLStreamReader(r);
            mergeFrom(parser, message, schema);
            parser.close();
        }
        catch(XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }
    
    /**
     * Merges the {@code message} from the {@link XMLStreamReader} using the given {@code schema}.
     */
    public static <T> void mergeFrom(XMLStreamReader parser, T message, Schema<T> schema)
    throws IOException, XMLStreamException, XmlInputException
    {
        String simpleName = schema.messageName();
        
        if(parser.nextTag() != START_ELEMENT || 
                !simpleName.equals(parser.getLocalName()))
        {
            throw new XmlInputException("Expected token START_ELEMENT: " + simpleName);
        }
        
        if(parser.nextTag() == END_ELEMENT)
        {
            if(!simpleName.equals(parser.getLocalName()))
                throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);
            
            // empty message;
            return;
        }
        
        schema.mergeFrom(new XmlInput(parser), message);
        
        if(!simpleName.equals(parser.getLocalName()))
            throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);
    }
    
    /**
     * Serializes the {@code messages} into the {@link OutputStream} using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema)
    throws IOException
    {
        writeListTo(out, messages, schema, DEFAULT_OUTPUT_FACTORY);
    }
    
    /**
     * Serializes the {@code messages} into the {@link OutputStream} using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            XMLOutputFactory outFactory) throws IOException
    {
        try
        {
            XMLStreamWriter writer = outFactory.createXMLStreamWriter(out, XML_ENCODING);
            
            writer.writeStartDocument(XML_ENCODING, XML_VERSION);
            writeListTo(writer, messages, schema);
            writer.writeEndDocument();
            
            writer.flush();
            writer.close();
        }
        catch(XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
    }
    
    /**
     * Serializes the {@code messages} into the {@link XMLStreamWriter} using the given schema.
     */
    public static <T> void writeListTo(XMLStreamWriter writer, List<T> messages, Schema<T> schema) 
    throws IOException, XMLStreamException
    {
        String simpleName = schema.messageName();
        writer.writeStartElement("list");
        
        if(messages.isEmpty())
        {
            writer.writeEndElement();
            return;
        }

        final XmlOutput output = new XmlOutput(writer);
        for(T m : messages)
        {
            writer.writeStartElement(simpleName);
            schema.writeTo(output.use(schema), m);
            writer.writeEndElement();
        }
        
        writer.writeEndElement();
    }
    
    /**
     * Parses the {@code messages} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema) 
    throws IOException
    {
        return parseListFrom(in, schema, DEFAULT_INPUT_FACTORY);
    }
    
    /**
     * Parses the {@code messages} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, 
            XMLInputFactory inFactory) throws IOException
    {
        try
        {
            XMLStreamReader parser = inFactory.createXMLStreamReader(in);
            List<T> list = parseListFrom(parser, schema);
            parser.close();
            return list;
        }
        catch(XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }
    
    /**
     * Parses the {@code messages} from the {@link XMLStreamReader} using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(XMLStreamReader parser, Schema<T> schema) 
    throws IOException, XMLStreamException
    {
        if(parser.nextTag() != START_ELEMENT || !"list".equals(parser.getLocalName()))
            throw new XmlInputException("Expected token START_ELEMENT: list");
        
        final String simpleName = schema.messageName();
        final ArrayList<T> list = new ArrayList<T>();
        final XmlInput input = new XmlInput(parser);
        
        for(int tag = parser.nextTag(); tag != END_ELEMENT; tag = parser.nextTag())
        {
            if(tag != START_ELEMENT || !simpleName.equals(parser.getLocalName()))
                throw new XmlInputException("Expected token START_ELEMENT: " + simpleName);
            
            T message = schema.newMessage();
            
            if(parser.nextTag() == END_ELEMENT)
            {
                if(!simpleName.equals(parser.getLocalName()))
                    throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);
                
                // empty message
                list.add(message);
                continue;
            }

            schema.mergeFrom(input, message);
            
            if(!simpleName.equals(parser.getLocalName()))
                throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);
            
            list.add(message);
        }
        
        return list;
    }
    
}
