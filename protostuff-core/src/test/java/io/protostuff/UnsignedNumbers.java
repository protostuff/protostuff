package io.protostuff;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public final class UnsignedNumbers implements Externalizable, Message<UnsignedNumbers>, Schema<UnsignedNumbers>
{

	static final UnsignedNumbers DEFAULT_INSTANCE = new UnsignedNumbers();
	private static final java.util.HashMap<String, Integer> __fieldMap = new java.util.HashMap<String, Integer>();

	static
	{
		__fieldMap.put("uint32", 1);
		__fieldMap.put("uint64", 2);
		__fieldMap.put("fixed32", 3);
		__fieldMap.put("fixed64", 4);
	}

	private Integer uint32;
	private Long uint64;
	private Integer fixed32;
	private Long fixed64;

	public UnsignedNumbers()
	{

	}

	// getters and setters

	// uint32

	public static Schema<UnsignedNumbers> getSchema()
	{
		return DEFAULT_INSTANCE;
	}

	public static UnsignedNumbers getDefaultInstance()
	{
		return DEFAULT_INSTANCE;
	}

	// uint64

	public Integer getUint32()
	{
		return uint32;
	}

	public void setUint32(Integer uint32)
	{
		this.uint32 = uint32;
	}

	// fixed32

	public Long getUint64()
	{
		return uint64;
	}

	public void setUint64(Long uint64)
	{
		this.uint64 = uint64;
	}

	// fixed64

	public Integer getFixed32()
	{
		return fixed32;
	}

	public void setFixed32(Integer fixed32)
	{
		this.fixed32 = fixed32;
	}

	public Long getFixed64()
	{
		return fixed64;
	}

	public void setFixed64(Long fixed64)
	{
		this.fixed64 = fixed64;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass())
		{
			return false;
		}
		final UnsignedNumbers that = (UnsignedNumbers) obj;
		return equals(this.uint32, that.uint32) &&
				equals(this.uint64, that.uint64) &&
				equals(this.fixed32, that.fixed32) &&
				equals(this.fixed64, that.fixed64);
	}

	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}
	// java serialization

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new Object[] {uint32, uint64, fixed32, fixed64});
	}

	@Override
	public String toString()
	{
		return "UnsignedNumbers{" +
				"uint32=" + uint32 +
				", uint64=" + uint64 +
				", fixed32=" + fixed32 +
				", fixed64=" + fixed64 +
				'}';
	}

	// message method

	@Override
	public void readExternal(ObjectInput in) throws IOException
	{
		GraphIOUtil.mergeDelimitedFrom(in, this, this);
	}

	// schema methods

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		GraphIOUtil.writeDelimitedTo(out, this, this);
	}

	@Override
	public Schema<UnsignedNumbers> cachedSchema()
	{
		return DEFAULT_INSTANCE;
	}

	@Override
	public UnsignedNumbers newMessage()
	{
		return new UnsignedNumbers();
	}

	@Override
	public Class<UnsignedNumbers> typeClass()
	{
		return UnsignedNumbers.class;
	}

	@Override
	public String messageName()
	{
		return UnsignedNumbers.class.getSimpleName();
	}

	@Override
	public String messageFullName()
	{
		return UnsignedNumbers.class.getName();
	}

	@Override
	public boolean isInitialized(UnsignedNumbers message)
	{
		return true;
	}

	@Override
	public void mergeFrom(Input input, UnsignedNumbers message) throws IOException
	{
		for (int number = input.readFieldNumber(this); ; number = input.readFieldNumber(this))
		{
			switch (number)
			{
				case 0:
					return;
				case 1:
					message.uint32 = input.readUInt32();
					break;
				case 2:
					message.uint64 = input.readUInt64();
					break;
				case 3:
					message.fixed32 = input.readFixed32();
					break;
				case 4:
					message.fixed64 = input.readFixed64();
					break;
				default:
					input.handleUnknownField(number, this);
			}
		}
	}

	@Override
	public void writeTo(Output output, UnsignedNumbers message) throws IOException
	{
		if (message.uint32 != null)
			output.writeUInt32(1, message.uint32, false);

		if (message.uint64 != null)
			output.writeUInt64(2, message.uint64, false);

		if (message.fixed32 != null)
			output.writeFixed32(3, message.fixed32, false);

		if (message.fixed64 != null)
			output.writeFixed64(4, message.fixed64, false);
	}

	@Override
	public String getFieldName(int number)
	{
		switch (number)
		{
			case 1:
				return "uint32";
			case 2:
				return "uint64";
			case 3:
				return "fixed32";
			case 4:
				return "fixed64";
			default:
				return null;
		}
	}

	@Override
	public int getFieldNumber(String name)
	{
		final Integer number = __fieldMap.get(name);
		return number == null ? 0 : number;
	}

}
