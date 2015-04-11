package com.engstuff.coloriphornia.helpers;

public class ColorParams {

	public static int composeHex(int alpha, int r, int g, int b) {

		String hexString = toHex(alpha) + toHex(r) + toHex(g) + toHex(b);

		return (int) Long.parseLong(hexString, 16);
	}

    private static String toHex(int n) {

        return n > 15 ? Integer.toHexString(n) : "0" + Integer.toHexString(n);
    }

    /**
     * Hex color string to int[] array converter
     *
     * @param hexARGB should be color hex string: #AARRGGBB or #RRGGBB
     * @return int[] array: [alpha, r, g, b]
     * @throws IllegalArgumentException
     */

    public static int[] hexStringToARGB(String hexARGB) throws IllegalArgumentException {

        if (!hexARGB.startsWith("#") || !(hexARGB.length() == 7 || hexARGB.length() == 9)) {

            hexARGB = "#00000000";
        }

        int[] intARGB = new int[4];

        if (hexARGB.length() == 9) {
            intARGB[0] = Integer.valueOf(hexARGB.substring(1, 3), 16); // alpha
            intARGB[1] = Integer.valueOf(hexARGB.substring(3, 5), 16); // red
            intARGB[2] = Integer.valueOf(hexARGB.substring(5, 7), 16); // green
            intARGB[3] = Integer.valueOf(hexARGB.substring(7), 16); // blue
        } else hexStringToARGB("#FF" + hexARGB.substring(1));

        return intARGB;
    }

    public static boolean blackOrWhiteText(int alpha, int r, int g, int b) {

        return alpha < 100 || (r + g + b > 450 || g > 200) ? false : true;
    }

    public static boolean blackOrWhiteText(String hex) {

        int[] argb = hexStringToARGB(hex);
        return blackOrWhiteText(argb[0], argb[1], argb[2], argb[3]);
    }

    public static String makeArgbInfo(int a, int r, int g, int b) {
        return "\u03b1: " + a + " r:" + r + " g:" + g + " b:" + b;
    }

    public static String makeArgbInfo(String hex) {
        int[] argb = hexStringToARGB(hex);
        return makeArgbInfo(argb[0], argb[1], argb[2], argb[3]);
    }

    public static String makeHexInfo(int color) {
        return "#" + Integer.toHexString(color);
    }
}
