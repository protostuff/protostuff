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

import static io.protostuff.XmlIOFactoryUtil.DEFAULT_INPUT_FACTORY;
import static io.protostuff.XmlIOFactoryUtil.DEFAULT_OUTPUT_FACTORY;
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

    private XmlIOUtil()
    {
    }

    public static final String XML_ENCODING = "UTF-8", XML_VERSION = "1.0";

    /**
     * Creates an xml pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data) throws IOException
    {
        return newPipe(data, 0, data.length);
    }

    /**
     * Creates an xml pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, int offset, int length) throws IOException
    {
        return newPipe(new ByteArrayInputStream(data, offset, length));
    }

    /**
     * Creates an xml pipe from an {@link InputStream}.
     */
    public static Pipe newPipe(InputStream in) throws IOException
    {
        try
        {
            return newPipe(DEFAULT_INPUT_FACTORY.createXMLStreamReader(in, XML_ENCODING));
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }

    /**
     * Creates an xml pipe from a {@link Reader}.
     */
    public static Pipe newPipe(Reader reader) throws IOException
    {
        try
        {
            return newPipe(DEFAULT_INPUT_FACTORY.createXMLStreamReader(reader));
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
    }

    /**
     * Creates an xml pipe from an {@link XMLStreamReader}.
     */
    public static Pipe newPipe(final XMLStreamReader parser)
    {
        final XmlInput xmlInput = new XmlInput(parser);
        return new Pipe()
        {
            @Override
            protected Input begin(Pipe.Schema<?> pipeSchema) throws IOException
            {
                // final String simpleName = pipeSchema.wrappedSchema.messageName();

                try
                {
                    if (parser.nextTag() != START_ELEMENT ||
                            !pipeSchema.wrappedSchema.messageName().equals(parser.getLocalName()))
                    {
                        throw new XmlInputException("Expected token START_ELEMENT: " +
                                pipeSchema.wrappedSchema.messageName());
                    }

                    if (parser.nextTag() == END_ELEMENT)
                    {
                        // if(!simpleName.equals(parser.getLocalName()))
                        // throw new XmlInputException("Expecting token END_ELEMENT: " +
                        // simpleName);

                        // empty message;
                        return null;
                    }
                }
                catch (XMLStreamException e)
                {
                    throw new XmlInputException(e);
                }

                return xmlInput;
            }

            @Override
            protected void end(Pipe.Schema<?> pipeSchema, Input input,
                    boolean cleanupOnly) throws IOException
            {
                if (cleanupOnly)
                {
                    try
                    {
                        parser.close();
                    }
                    catch (XMLStreamException e)
                    {
                        // ignore
                    }
                    return;
                }

                assert input == xmlInput;

                // final String simpleName = pipeSchema.wrappedSchema.messageName();
                // final String localName = parser.getLocalName();

                try
                {
                    parser.close();
                }
                catch (XMLStreamException e)
                {
                    // end of pipe transfer ... ignore
                }

                /*
                 * if(!simpleName.equals(localName)) { throw new XmlInputException("Expecting token END_ELEMENT: " +
                 * simpleName); }
                 */
            }
        };
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
    public static <T> void mergeFrom(byte[] data, int offset, int len, T message,
            Schema<T> schema)
    {
        mergeFrom(data, 0, data.length, message, schema, DEFAULT_INPUT_FACTORY);
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int len, T message,
            Schema<T> schema, XMLInputFactory inFactory)
    {
        final ByteArrayInputStream in = new ByteArrayInputStream(data, offset, len);
        try
        {
            mergeFrom(in, message, schema, inFactory);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
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
        XMLStreamReader parser = null;
        try
        {
            parser = inFactory.createXMLStreamReader(in, XML_ENCODING);
            mergeFrom(parser, message, schema);
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
        finally
        {
            if (parser != null)
            {
                try
                {
                    parser.close();
                }
                catch (XMLStreamException e)
                {
                    // ignore
                }
            }
        }
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
        XMLStreamReader parser = null;
        try
        {
            parser = inFactory.createXMLStreamReader(r);
            mergeFrom(parser, message, schema);
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
        finally
        {
            if (parser != null)
            {
                try
                {
                    parser.close();
                }
                catch (XMLStreamException e)
                {
                    // ignore
                }
            }
        }
    }

    /**
     * Merges the {@code message} from the {@link XMLStreamReader} using the given {@code schema}.
     */
    public static <T> void mergeFrom(XMLStreamReader parser, T message, Schema<T> schema)
            throws IOException, XMLStreamException, XmlInputException
    {
        // final String simpleName = schema.messageName();

        if (parser.nextTag() != START_ELEMENT ||
                !schema.messageName().equals(parser.getLocalName()))
        {
            throw new XmlInputException("Expected token START_ELEMENT: " + schema.messageName());
        }

        if (parser.nextTag() == END_ELEMENT)
        {
            // if(!simpleName.equals(parser.getLocalName()))
            // throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);

            // empty message;
            return;
        }

        schema.mergeFrom(new XmlInput(parser), message);

        // if(!simpleName.equals(parser.getLocalName()))
        // throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);
    }

    /**
     * Serializes the {@code message} into a byte array.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return toByteArray(message, schema, DEFAULT_OUTPUT_FACTORY);
    }

    /**
     * Serializes the {@code message} into a byte array.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema,
            XMLOutputFactory outFactory)
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            writeTo(out, message, schema, outFactory);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " +
                    "(should never happen).", e);
        }
        return out.toByteArray();
    }

    /**
     * Serializes the {@code message} into an {@link OutputStream} using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema)
            throws IOException
    {
        writeTo(out, message, schema, DEFAULT_OUTPUT_FACTORY);
    }

    /**
     * Serializes the {@code message} into an {@link OutputStream} using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema,
            XMLOutputFactory outFactory) throws IOException
    {
        XMLStreamWriter writer = null;
        try
        {
            writer = outFactory.createXMLStreamWriter(out, XML_ENCODING);

            writer.writeStartDocument(XML_ENCODING, XML_VERSION);
            writeTo(writer, message, schema);
            writer.writeEndDocument();

            writer.flush();
        }
        catch (XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (XMLStreamException e)
                {
                    // ignore
                }
            }
        }
    }

    /**
     * Serializes the {@code message} into a {@link Writer} using the given {@code schema}.
     */
    public static <T> void writeTo(Writer w, T message, Schema<T> schema)
            throws IOException
    {
        writeTo(w, message, schema, DEFAULT_OUTPUT_FACTORY);
    }

    /**
     * Serializes the {@code message} into a {@link Writer} using the given {@code schema}.
     */
    public static <T> void writeTo(Writer w, T message, Schema<T> schema,
            XMLOutputFactory outFactory) throws IOException
    {
        XMLStreamWriter writer = null;
        try
        {
            writer = outFactory.createXMLStreamWriter(w);

            writer.writeStartDocument(XML_ENCODING, XML_VERSION);
            writeTo(writer, message, schema);
            writer.writeEndDocument();

            writer.flush();
        }
        catch (XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (XMLStreamException e)
                {
                    // ignore
                }
            }
        }
    }

    /**
     * Serializes the {@code message} into an {@link XMLStreamWriter} using the given {@code schema}.
     */
    public static <T> void writeTo(XMLStreamWriter writer, T message, Schema<T> schema)
            throws IOException, XMLStreamException, XmlOutputException
    {
        writer.writeStartElement(schema.messageName());

        schema.writeTo(new XmlOutput(writer, schema), message);

        writer.writeEndElement();
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
        XMLStreamWriter writer = null;
        try
        {
            writer = outFactory.createXMLStreamWriter(out, XML_ENCODING);

            writer.writeStartDocument(XML_ENCODING, XML_VERSION);
            writeListTo(writer, messages, schema);
            writer.writeEndDocument();

            writer.flush();
        }
        catch (XMLStreamException e)
        {
            throw new XmlOutputException(e);
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (XMLStreamException e)
                {
                    // ignore
                }
            }
        }
    }

    /**
     * Serializes the {@code messages} into the {@link XMLStreamWriter} using the given schema.
     */
    public static <T> void writeListTo(XMLStreamWriter writer, List<T> messages, Schema<T> schema)
            throws IOException, XMLStreamException
    {
        writer.writeStartElement("list");

        if (messages.isEmpty())
        {
            writer.writeEndElement();
            return;
        }

        final String simpleName = schema.messageName();
        final XmlOutput output = new XmlOutput(writer, schema);
        for (T m : messages)
        {
            writer.writeStartElement(simpleName);
            schema.writeTo(output, m);
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
        XMLStreamReader parser = null;
        try
        {
            parser = inFactory.createXMLStreamReader(in);
            return parseListFrom(parser, schema);
        }
        catch (XMLStreamException e)
        {
            throw new XmlInputException(e);
        }
        finally
        {
            if (parser != null)
            {
                try
                {
                    parser.close();
                }
                catch (XMLStreamException e)
                {
                    // ignore
                }
            }
        }
    }

    /**
     * Parses the {@code messages} from the {@link XMLStreamReader} using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(XMLStreamReader parser, Schema<T> schema)
            throws IOException, XMLStreamException
    {
        if (parser.nextTag() != START_ELEMENT || !"list".equals(parser.getLocalName()))
            throw new XmlInputException("Expected token START_ELEMENT: list");

        // final String simpleName = schema.messageName();
        final ArrayList<T> list = new ArrayList<>();
        final XmlInput input = new XmlInput(parser);

        for (int tag = parser.nextTag(); tag != END_ELEMENT; tag = parser.nextTag())
        {
            if (tag != START_ELEMENT || !schema.messageName().equals(parser.getLocalName()))
                throw new XmlInputException("Expected token START_ELEMENT: " + schema.messageName());

            final T message = schema.newMessage();

            if (parser.nextTag() == END_ELEMENT)
            {
                // if(!simpleName.equals(parser.getLocalName()))
                // throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);

                // empty message
                list.add(message);
                continue;
            }

            schema.mergeFrom(input, message);

            // if(!simpleName.equals(parser.getLocalName()))
            // throw new XmlInputException("Expecting token END_ELEMENT: " + simpleName);

            list.add(message);
        }

        return list;
    }

}
