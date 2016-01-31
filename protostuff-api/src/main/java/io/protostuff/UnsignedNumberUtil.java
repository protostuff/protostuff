//========================================================================
//Copyright 2016 The Protostuff Authors
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

/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.math.BigInteger;

public final class UnsignedNumberUtil
{
    public static final long MAX_VALUE = -1L; // Equivalent to 2^64 - 1
    static final long INT_MASK = 0xffffffffL;
    // calculated as 0xffffffffffffffff / radix
    private static final long[] maxValueDivs = new long[Character.MAX_RADIX + 1];
    private static final int[] maxValueMods = new int[Character.MAX_RADIX + 1];
    private static final int[] maxSafeDigits = new int[Character.MAX_RADIX + 1];

    static
    {
        BigInteger overflow = new BigInteger("10000000000000000", 16);
        for (int i = Character.MIN_RADIX; i <= Character.MAX_RADIX; i++)
        {
            maxValueDivs[i] = divide(MAX_VALUE, i);
            maxValueMods[i] = (int) remainder(MAX_VALUE, i);
            maxSafeDigits[i] = overflow.toString(i).length() - 1;
        }
    }

    private UnsignedNumberUtil()
    {
    }

    private static int flip(int value)
    {
        return value ^ Integer.MIN_VALUE;
    }

    /**
     * A (self-inverse) bijection which converts the ordering on unsigned longs to the ordering on longs, that is,
     * {@code a <= b} as unsigned longs if and only if {@code flip(a) <= flip(b)} as signed longs.
     */
    private static long flip(long a)
    {
        return a ^ Long.MIN_VALUE;
    }

    /**
     * Returns the value of the given {@code int} as a {@code long}, when treated as unsigned.
     */
    private static long toLong(int value)
    {
        return value & INT_MASK;
    }

    /**
     * Returns the unsigned {@code int} value represented by the given decimal string.
     *
     * @throws NumberFormatException
     *             if the string does not contain a valid unsigned {@code int} value
     * @throws NullPointerException
     *             if {@code s} is null (in contrast to {@link Integer#parseInt(String)})
     */
    public static int parseUnsignedInt(String s)
    {
        return parseUnsignedInt(s, 10);
    }

    /**
     * Returns the unsigned {@code int} value represented by a string with the given radix.
     *
     * @param string
     *            the string containing the unsigned integer representation to be parsed.
     * @param radix
     *            the radix to use while parsing {@code s}; must be between {@link Character#MIN_RADIX} and
     *            {@link Character#MAX_RADIX}.
     * @throws NumberFormatException
     *             if the string does not contain a valid unsigned {@code int}, or if supplied radix is invalid.
     * @throws NullPointerException
     *             if {@code s} is null (in contrast to {@link Integer#parseInt(String)})
     */
    private static int parseUnsignedInt(String string, int radix)
    {
        long result = Long.parseLong(string, radix);
        if ((result & INT_MASK) != result)
        {
            throw new NumberFormatException("Input " + string + " in base " + radix
                    + " is not in the range of an unsigned integer");
        }
        return (int) result;
    }

    /**
     * Returns a string representation of x, where x is treated as unsigned.
     */
    public static String unsignedIntToString(int x)
    {
        return unsignedIntToString(x, 10);
    }

    /**
     * Compares the two specified {@code long} values, treating them as unsigned values between {@code 0} and
     * {@code 2^64 - 1} inclusive.
     *
     * @param a
     *            the first unsigned {@code long} to compare
     * @param b
     *            the second unsigned {@code long} to compare
     * @return a negative value if {@code a} is less than {@code b}; a positive value if {@code a} is greater than
     *         {@code b}; or zero if they are equal
     */
    private static int compareUnsigned(long a, long b)
    {
        return compareSigned(flip(a), flip(b));
    }

    /**
     * Compares the two specified {@code long} values. The sign of the value returned is the same as that of
     * {@code ((Long) a).compareTo(b)}.
     *
     * <p>
     * <b>Note for Java 7 and later:</b> this method should be treated as deprecated; use the equivalent
     * {@link Long#compare} method instead.
     *
     * @param a
     *            the first {@code long} to compare
     * @param b
     *            the second {@code long} to compare
     * @return a negative value if {@code a} is less than {@code b}; a positive value if {@code a} is greater than
     *         {@code b}; or zero if they are equal
     */
    private static int compareSigned(long a, long b)
    {
        return (a < b) ? -1 : ((a > b) ? 1 : 0);
    }

    /**
     * Returns a string representation of {@code x} for the given radix, where {@code x} is treated as unsigned.
     *
     * @param x
     *            the value to convert to a string.
     * @param radix
     *            the radix to use while working with {@code x}
     * @throws IllegalArgumentException
     *             if {@code radix} is not between {@link Character#MIN_RADIX} and {@link Character#MAX_RADIX}.
     */
    private static String unsignedIntToString(int x, int radix)
    {
        long asLong = x & INT_MASK;
        return Long.toString(asLong, radix);
    }

    /**
     * Returns dividend / divisor, where the dividend and divisor are treated as unsigned 64-bit quantities.
     *
     * @param dividend
     *            the dividend (numerator)
     * @param divisor
     *            the divisor (denominator)
     * @throws ArithmeticException
     *             if divisor is 0
     */
    private static long divide(long dividend, long divisor)
    {
        if (divisor < 0)
        { // i.e., divisor >= 2^63:
            if (compareUnsigned(dividend, divisor) < 0)
            {
                return 0; // dividend < divisor
            }
            else
            {
                return 1; // dividend >= divisor
            }
        }

        // Optimization - use signed division if dividend < 2^63
        if (dividend >= 0)
        {
            return dividend / divisor;
        }

        /*
         * Otherwise, approximate the quotient, check, and correct if necessary. Our approximation is guaranteed to be
         * either exact or one less than the correct value. This follows from fact that floor(floor(x)/i) == floor(x/i)
         * for any real x and integer i != 0. The proof is not quite trivial.
         */
        long quotient = ((dividend >>> 1) / divisor) << 1;
        long rem = dividend - quotient * divisor;
        return quotient + (compareUnsigned(rem, divisor) >= 0 ? 1 : 0);
    }

    /**
     * Returns dividend % divisor, where the dividend and divisor are treated as unsigned 64-bit quantities.
     *
     * @param dividend
     *            the dividend (numerator)
     * @param divisor
     *            the divisor (denominator)
     * @throws ArithmeticException
     *             if divisor is 0
     * @since 11.0
     */
    private static long remainder(long dividend, long divisor)
    {
        if (divisor < 0)
        { // i.e., divisor >= 2^63:
            if (compareUnsigned(dividend, divisor) < 0)
            {
                return dividend; // dividend < divisor
            }
            else
            {
                return dividend - divisor; // dividend >= divisor
            }
        }

        // Optimization - use signed modulus if dividend < 2^63
        if (dividend >= 0)
        {
            return dividend % divisor;
        }

        /*
         * Otherwise, approximate the quotient, check, and correct if necessary. Our approximation is guaranteed to be
         * either exact or one less than the correct value. This follows from fact that floor(floor(x)/i) == floor(x/i)
         * for any real x and integer i != 0. The proof is not quite trivial.
         */
        long quotient = ((dividend >>> 1) / divisor) << 1;
        long rem = dividend - quotient * divisor;
        return rem - (compareUnsigned(rem, divisor) >= 0 ? divisor : 0);
    }

	/**
	 * Returns the unsigned {@code long} value represented by the given decimal string.
	 *
	 * @throws NumberFormatException if the string does not contain a valid unsigned {@code long}
	 *         value
	 * @throws NullPointerException if {@code s} is null
	 *         (in contrast to {@link Long#parseLong(String)})
	 */
	public static long parseUnsignedLong(String s) {
		return parseUnsignedLong(s, 10);
	}

    /**
     * Returns the unsigned {@code long} value represented by a string with the given radix.
     *
     * @param s
     *            the string containing the unsigned {@code long} representation to be parsed.
     * @param radix
     *            the radix to use while parsing {@code s}
     * @throws NumberFormatException
     *             if the string does not contain a valid unsigned {@code long} with the given radix, or if
     *             {@code radix} is not between {@link Character#MIN_RADIX} and {@link Character#MAX_RADIX}.
     * @throws NullPointerException
     *             if {@code s} is null (in contrast to {@link Long#parseLong(String)})
     */
    private static long parseUnsignedLong(String s, int radix)
    {
        if (s.length() == 0)
        {
            throw new NumberFormatException("empty string");
        }
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
        {
            throw new NumberFormatException("illegal radix: " + radix);
        }

        int max_safe_pos = maxSafeDigits[radix] - 1;
        long value = 0;
        for (int pos = 0; pos < s.length(); pos++)
        {
            int digit = Character.digit(s.charAt(pos), radix);
            if (digit == -1)
            {
                throw new NumberFormatException(s);
            }
            if (pos > max_safe_pos && overflowInParse(value, digit, radix))
            {
                throw new NumberFormatException("Too large for unsigned long: " + s);
            }
            value = (value * radix) + digit;
        }

        return value;
    }

    /**
     * Returns true if (current * radix) + digit is a number too large to be represented by an unsigned long. This is
     * useful for detecting overflow while parsing a string representation of a number. Does not verify whether supplied
     * radix is valid, passing an invalid radix will give undefined results or an ArrayIndexOutOfBoundsException.
     */
    private static boolean overflowInParse(long current, int digit, int radix)
    {
        if (current >= 0)
        {
            if (current < maxValueDivs[radix])
            {
                return false;
            }
            if (current > maxValueDivs[radix])
            {
                return true;
            }
            // current == maxValueDivs[radix]
            return (digit > maxValueMods[radix]);
        }

        // current < 0: high bit is set
        return true;
    }

    /**
     * Returns a string representation of x, where x is treated as unsigned.
     */
    public static String unsignedLongToString(long x)
    {
        return unsignedLongToString(x, 10);
    }

    /**
     * Returns a string representation of {@code x} for the given radix, where {@code x} is treated as unsigned.
     *
     * @param x
     *            the value to convert to a string.
     * @param radix
     *            the radix to use while working with {@code x}
     * @throws IllegalArgumentException
     *             if {@code radix} is not between {@link Character#MIN_RADIX} and {@link Character#MAX_RADIX}.
     */
    private static String unsignedLongToString(long x, int radix)
    {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
        {
            throw new IllegalArgumentException("Invalid radix: " + radix);
        }
        if (x == 0)
        {
            // Simply return "0"
            return "0";
        }
        else
        {
            char[] buf = new char[64];
            int i = buf.length;
            if (x < 0)
            {
                // Separate off the last digit using unsigned division. That will leave
                // a number that is nonnegative as a signed integer.
                long quotient = divide(x, radix);
                long rem = x - quotient * radix;
                buf[--i] = Character.forDigit((int) rem, radix);
                x = quotient;
            }
            // Simple modulo/division approach
            while (x > 0)
            {
                buf[--i] = Character.forDigit((int) (x % radix), radix);
                x /= radix;
            }
            // Generate string
            return new String(buf, i, buf.length - i);
        }
    }
}
