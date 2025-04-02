package io.protostuff.benchmarks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import io.protostuff.Input;
import io.protostuff.JsonIOUtil;
import io.protostuff.JsonXIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Schema;

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
public class JsonIOBenchmark
{

    private static final Pojo POJO;
    private static final byte[] data;
    static
    {
        Pojo p = new Pojo();
        for (int i = 0; i < 50; i++)
        {
            p = new Pojo().setLoop(Arrays.asList(p.setTest("test" + i).setV(i)));
        }
        POJO = p;

        data = JsonIOUtil.toByteArray(POJO, POJO.cachedSchema(), false);
    }

    @Benchmark
    public Pojo jsonOutput() throws IOException
    {
        Pojo message = new Pojo();
        JsonIOUtil.mergeFrom(data, message, message.cachedSchema(), false);
        return message;
    }

    @Benchmark
    public byte[] jsonInput()
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        return JsonIOUtil.toByteArray(POJO, POJO.cachedSchema(), false, buffer);
    }

    @Benchmark
    public Pojo xjsonOutput() throws IOException
    {
        Pojo message = new Pojo();
        JsonXIOUtil.mergeFrom(data, message, message.cachedSchema(), false);
        return message;
    }

    @Benchmark
    public byte[] xjsonInput()
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        return JsonXIOUtil.toByteArray(POJO, POJO.cachedSchema(), false, buffer);
    }

    public static class Pojo implements Message<Pojo>
    {
        String test;
        Integer v;
        List<Pojo> loop;

        public String getTest()
        {
            return test;
        }

        public Pojo setTest(String test)
        {
            this.test = test;
            return this;
        }

        public Integer getV()
        {
            return v;
        }

        public Pojo setV(Integer v)
        {
            this.v = v;
            return this;
        }

        public List<Pojo> getLoop()
        {
            return loop;
        }

        public Pojo setLoop(List<Pojo> loop)
        {
            this.loop = loop;
            return this;
        }

        @Override
        public Schema<Pojo> cachedSchema()
        {
            return SCHEMA;
        }

        public static Schema<Pojo> getSchema()
        {
            return SCHEMA;
        }

        static final Schema<Pojo> SCHEMA = new Schema<Pojo>()
        {
            // schema methods

            @Override
            public Pojo newMessage()
            {
                return new Pojo();
            }

            @Override
            public Class<Pojo> typeClass()
            {
                return Pojo.class;
            }

            @Override
            public String messageName()
            {
                return Pojo.class.getSimpleName();
            }

            @Override
            public String messageFullName()
            {
                return Pojo.class.getName();
            }

            @Override
            public boolean isInitialized(Pojo message)
            {
                return true;
            }

            @Override
            public void mergeFrom(Input input, Pojo message) throws IOException
            {
                for (int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
                {
                    switch (number)
                    {
                        case 0:
                            return;
                        case 1:
                            message.test = input.readString();
                            break;
                        case 2:
                            message.v = input.readUInt32();
                            break;
                        case 3:
                            if (message.loop == null)
                                message.loop = new ArrayList<Pojo>();
                            message.loop.add(input.mergeObject(null, Pojo.getSchema()));
                            break;

                        default:
                            input.handleUnknownField(number, this);
                    }
                }
            }

            @Override
            public void writeTo(Output output, Pojo message) throws IOException
            {
                if (message.test != null)
                    output.writeString(1, message.test, false);

                if (message.v != null)
                    output.writeUInt32(2, message.v, false);

                if (message.loop != null)
                {
                    for (Pojo loop : message.loop)
                    {
                        if (loop != null)
                            output.writeObject(3, loop, Pojo.getSchema(), true);
                    }
                }

            }

            @Override
            public String getFieldName(int number)
            {
                switch (number)
                {
                    case 1:
                        return "test";
                    case 2:
                        return "v";
                    case 3:
                        return "loop";
                    default:
                        return null;
                }
            }

            @Override
            public int getFieldNumber(String name)
            {
                final Integer number = fieldMap.get(name);
                return number == null ? 0 : number.intValue();
            }

            final java.util.HashMap<String, Integer> fieldMap = new java.util.HashMap<String, Integer>();
            {
                fieldMap.put("test", 1);
                fieldMap.put("v", 2);
                fieldMap.put("loop", 3);
            }
        };

    }
}
