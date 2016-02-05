package io.protostuff.benchmarks;

import java.io.ByteArrayOutputStream;
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
import io.protostuff.Tag;
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
    private RuntimeSchema<SparseInt1> sparseInt1RuntimeSchema;
    private RuntimeSchema<SparseInt10> sparseInt10RuntimeSchema;
    private Schema<GeneratedInt1> generatedInt1Schema;
    private Schema<GeneratedInt10> generatedInt10Schema;

    private Int1 int1;
    private Int10 int10;
    private SparseInt1 sparseInt1;
    private SparseInt10 sparseInt10;
    private GeneratedInt1 generatedInt1;
    private GeneratedInt10 generatedInt10;

    private byte[] data_1_int;
    private byte[] data_10_int;

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
        sparseInt1RuntimeSchema = RuntimeSchema.createFrom(SparseInt1.class);
        sparseInt10RuntimeSchema = RuntimeSchema.createFrom(SparseInt10.class);
        generatedInt1Schema = GeneratedInt1.getSchema();
        generatedInt10Schema = GeneratedInt10.getSchema();
        int1 = new Int1();
        int1.a0 = 1;
        int10 = new Int10();
        int10.a0 = 1;
        int10.a1 = 2;
        int10.a2 = 3;
        int10.a3 = 4;
        int10.a4 = 5;
        int10.a5 = 6;
        int10.a6 = 7;
        int10.a7 = 8;
        int10.a8 = 9;
        int10.a9 = 10;
        sparseInt1 = new SparseInt1();
        sparseInt1.a0 = 1;
        sparseInt10 = new SparseInt10();
        sparseInt10.a0 = 1;
        sparseInt10.a1 = 2;
        sparseInt10.a2 = 3;
        sparseInt10.a3 = 4;
        sparseInt10.a4 = 5;
        sparseInt10.a5 = 6;
        sparseInt10.a6 = 7;
        sparseInt10.a7 = 8;
        sparseInt10.a8 = 9;
        sparseInt10.a9 = 10;
        generatedInt1 = new GeneratedInt1();
        generatedInt1.setA0(1);
        generatedInt10 = new GeneratedInt10();
        generatedInt10.setA0(1);
        generatedInt10.setA1(2);
        generatedInt10.setA2(3);
        generatedInt10.setA3(4);
        generatedInt10.setA4(5);
        generatedInt10.setA5(6);
        generatedInt10.setA6(7);
        generatedInt10.setA7(8);
        generatedInt10.setA8(9);
        generatedInt10.setA9(10);
        buffer = LinkedBuffer.allocate();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ProtobufIOUtil.writeTo(outputStream, int1, int1RuntimeSchema, buffer);
        data_1_int = outputStream.toByteArray();
        outputStream.reset();
        buffer.clear();

        ProtobufIOUtil.writeTo(outputStream, int10, int10RuntimeSchema, buffer);
        data_10_int = outputStream.toByteArray();
        outputStream.reset();
        buffer.clear();
    }

    @Benchmark
    public void baseline()
    {
        buffer.clear();
    }

    @Benchmark
    public Int1 runtime_deserialize_1_int_field() throws Exception
    {
        Int1 int1Local = new Int1();
        ProtobufIOUtil.mergeFrom(data_1_int, int1Local, int1RuntimeSchema);
        return int1Local;
    }

    @Benchmark
    public void runtime_serialize_1_int_field() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, int1, int1RuntimeSchema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public Int10 runtime_deserialize_10_int_field() throws Exception
    {
        Int10 int10Local = new Int10();
        ProtobufIOUtil.mergeFrom(data_10_int, int10Local, int10RuntimeSchema);
        return int10Local;
    }

    @Benchmark
    public void runtime_serialize_10_int_fields() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, int10, int10RuntimeSchema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public SparseInt1 runtime_sparse_deserialize_1_int_field() throws Exception
    {
        SparseInt1 int1Local = new SparseInt1();
        ProtobufIOUtil.mergeFrom(data_1_int, int1Local, sparseInt1RuntimeSchema);
        return int1Local;
    }

    @Benchmark
    public void runtime_sparse_serialize_1_int_field() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, sparseInt1, sparseInt1RuntimeSchema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public SparseInt10 runtime_sparse_deserialize_10_int_field() throws Exception
    {
        SparseInt10 int10Local = new SparseInt10();
        ProtobufIOUtil.mergeFrom(data_10_int, int10Local, sparseInt10RuntimeSchema);
        return int10Local;
    }

    @Benchmark
    public void runtime_sparse_serialize_10_int_fields() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, sparseInt10, sparseInt10RuntimeSchema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public GeneratedInt1 generated_deserialize_1_int_field() throws Exception
    {
        GeneratedInt1 int1Local = new GeneratedInt1();
        ProtobufIOUtil.mergeFrom(data_1_int, int1Local, GeneratedInt1.getSchema());
        return int1Local;
    }

    @Benchmark
    public void generated_serialize_1_int_field() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, generatedInt1, generatedInt1Schema);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Benchmark
    public GeneratedInt10 generated_deserialize_10_int_field() throws Exception
    {
        GeneratedInt10 int10Local = new GeneratedInt10();
        ProtobufIOUtil.mergeFrom(data_10_int, int10Local, GeneratedInt10.getSchema());
        return int10Local;
    }

    @Benchmark
    public void generated_serialize_10_int_field() throws Exception
    {
        try
        {
            ProtobufIOUtil.writeTo(buffer, generatedInt10, generatedInt10Schema);
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

    private static class SparseInt1
    {
        @Tag(101)
        public int a0;
    }

    private static class SparseInt10
    {
        @Tag(1)
        public int a0;
        @Tag(2)
        public int a1;
        @Tag(3)
        public int a2;
        @Tag(4)
        public int a3;
        @Tag(5)
        public int a4;
        @Tag(6)
        public int a5;
        @Tag(7)
        public int a6;
        @Tag(8)
        public int a7;
        @Tag(9)
        public int a8;
        @Tag(101)
        public int a9;
    }
}
