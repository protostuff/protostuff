package io.protostuff;

public class Utf8Decoder {
	private final StringBuilder sb;
	private int c;
	private int r;

	public Utf8Decoder() {
		sb = new StringBuilder();
	}

	public void appendUnicode(int codepoint) {
		if (!Character.isBmpCodePoint(codepoint)) {
			sb.append(Character.highSurrogate(codepoint));
			sb.append(Character.lowSurrogate(codepoint));
		} else
			sb.append((char) codepoint);
	}

	public void append(byte b) {
		append(b & 0xFF);
	}

	private void append(int b) {
		if (r > 0) {
			c = (c << 6) | (b & 0x3F);
			if (--r == 0)
				appendUnicode(c);
			return;
		}
		if (b < 0x7F) {
			sb.append((char) b);
		} else if (b < 0xE0) {
			r = 1;
			c = b & 0x3F;
		} else if (b < 0x1F) {
			r = 2;
			c = b & 0xE0;
		} else {
			r = 3;
			c = b & 0x0F;
		}
	}

	public String done() {
		String s = sb.toString();
		sb.setLength(0);
		return s;
	}
}
