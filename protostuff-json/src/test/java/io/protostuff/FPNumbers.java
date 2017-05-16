package io.protostuff;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public final class FPNumbers implements Externalizable, Message<FPNumbers>, Schema<FPNumbers>
{

	static final FPNumbers DEFAULT_INSTANCE = new FPNumbers();
	private static final java.util.HashMap<String, Integer> __fieldMap = new java.util.HashMap<String, Integer>();

	static
	{
		__fieldMap.put("float", 1);
		__fieldMap.put("double", 2);
	}

	private Float floatValue;
	private Double doubleValue;

	public FPNumbers()
	{

	}

	// getters and setters

	public static Schema<FPNumbers> getSchema()
	{
		return DEFAULT_INSTANCE;
	}

	public static FPNumbers getDefaultInstance()
	{
		return DEFAULT_INSTANCE;
	}

	// float

	public Float getFloat()
	{
		return floatValue;
	}

	public void setFloat(Float floatValue)
	{
		this.floatValue = floatValue;
	}

	// double

	public Double getDouble()
	{
		return doubleValue;
	}

	public void setDouble(Double doubleValue)
	{
		this.doubleValue = doubleValue;
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
		final FPNumbers that = (FPNumbers) obj;
		return equals(this.floatValue, that.floatValue) &&
				equals(this.doubleValue, that.doubleValue);
	}

	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}
	// java serialization

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new Object[] {floatValue, doubleValue});
	}

	@Override
	public String toString()
	{
		return "FPNumbers{" +
				"float=" + floatValue +
				", double=" + doubleValue +
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
	public Schema<FPNumbers> cachedSchema()
	{
		return DEFAULT_INSTANCE;
	}

	@Override
	public FPNumbers newMessage()
	{
		return new FPNumbers();
	}

	@Override
	public Class<FPNumbers> typeClass()
	{
		return FPNumbers.class;
	}

	@Override
	public String messageName()
	{
		return FPNumbers.class.getSimpleName();
	}

	@Override
	public String messageFullName()
	{
		return FPNumbers.class.getName();
	}

	@Override
	public boolean isInitialized(FPNumbers message)
	{
		return true;
	}

	@Override
	public void mergeFrom(Input input, FPNumbers message) throws IOException
	{
		for (int number = input.readFieldNumber(this); ; number = input.readFieldNumber(this))
		{
			switch (number)
			{
				case 0:
					return;
				case 1:
					message.floatValue = input.readFloat();
					break;
				case 2:
					message.doubleValue = input.readDouble();
					break;
				default:
					input.handleUnknownField(number, this);
			}
		}
	}

	@Override
	public void writeTo(Output output, FPNumbers message) throws IOException
	{
		if (message.floatValue != null)
			output.writeFloat(1, message.floatValue, false);

		if (message.doubleValue != null)
			output.writeDouble(2, message.doubleValue, false);
	}

	@Override
	public String getFieldName(int number)
	{
		switch (number)
		{
			case 1:
				return "float";
			case 2:
				return "double";
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
