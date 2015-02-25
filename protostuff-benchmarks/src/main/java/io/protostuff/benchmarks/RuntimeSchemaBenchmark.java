package io.protostuff.benchmarks;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class RuntimeSchemaBenchmark
{

    private RuntimeSchema<Int1> int1RuntimeSchema;
    private RuntimeSchema<Int10> int10RuntimeSchema;
    private Schema<GeneratedInt1> generatedInt1Schema;
    private Schema<GeneratedInt10> generatedInt10Schema;

    private Int1 int1Instance;
    private Int10 int10Instance;
    private GeneratedInt1 generatedInt1Instance;
    private GeneratedInt10 generatedInt10Instance;

    private LinkedBuffer buffer;

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
                .include(RuntimeSchemaBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void prepare() throws IOException
    {
        int1RuntimeSchema = RuntimeSchema.createFrom(Int1.class);
        int10RuntimeSchema = RuntimeSchema.createFrom(Int10.class);
        generatedInt1Schema = GeneratedInt1.getSchema();
        generatedInt10Schema = GeneratedInt10.getSchema();
        int1Instance = new Int1();
        int1Instance.a0 = 1;
        int10Instance = new Int10();
        int10Instance.a0 = 1;
        int10Instance.a1 = 2;
        int10Instance.a2 = 3;
        int10Instance.a3 = 4;
        int10Instance.a4 = 5;
        int10Instance.a5 = 6;
        int10Instance.a6 = 7;
        int10Instance.a7 = 8;
        int10Instance.a8 = 9;
        int10Instance.a9 = 10;
        generatedInt1Instance = new GeneratedInt1();
        generatedInt1Instance.setA0(1);
        generatedInt10Instance = new GeneratedInt10();
        generatedInt10Instance.setA0(1);
        generatedInt10Instance.setA1(2);
        generatedInt10Instance.setA2(3);
        generatedInt10Instance.setA3(4);
        generatedInt10Instance.setA4(5);
        generatedInt10Instance.setA5(6);
        generatedInt10Instance.setA6(7);
        generatedInt10Instance.setA7(8);
        generatedInt10Instance.setA8(9);
        generatedInt10Instance.setA9(10);
        buffer = LinkedBuffer.allocate();
    }

    @Benchmark
    public void baseline()
    {
        buffer.clear();
    }

    @Benchmark
    public void runtime_serialize_1_int_field() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, int1Instance, int1RuntimeSchema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public void runtime_serialize_10_int_fields() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, int10Instance, int10RuntimeSchema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public void generated_serialize_1_int_field() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, generatedInt1Instance, generatedInt1Schema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public void generated_serialize_10_int_field() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, generatedInt10Instance, generatedInt10Schema);
        }
        finally
        {
            buffer.clear();
        }
    }

    private static class Int1
    {
        public int a0;
    }

    private static class Int10
    {
        public int a0;
        public int a1;
        public int a2;
        public int a3;
        public int a4;
        public int a5;
        public int a6;
        public int a7;
        public int a8;
        public int a9;
    }

}
