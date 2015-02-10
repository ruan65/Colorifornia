package com.engstuff.coloriphornia.activities;

public class HexColorFrom4parts {

	public static int composeHex(int alpha, int r, int g, int b) {

		String hexString = toHex(alpha) + toHex(r) + toHex(g) + toHex(b);

		return (int) Long.parseLong(hexString, 16);
	}

    private static String toHex(int n) {

        return n > 15 ? Integer.toHexString(n) : "0" + Integer.toHexString(n);
    }
}
