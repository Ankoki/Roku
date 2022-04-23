package com.ankoki.roku.misc;

/**
 * Class to get numbers from a string without all the try {} catch () {} hassle.
 */
public class NumberUtils {

    /**
     * Gets an int from a string.
     * @param unparsed the unparsed string.
     * @return the int from the string. -1 if not a valid int.
     */
    public static int getInt(String unparsed) {
        try {
            return Integer.parseInt(unparsed);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Returns if a string can be parsed as an int.
     * @param unparsed the unparsed string.
     * @return if it can be parsed.
     */
    public static boolean isInt(String unparsed) {
        try {
            Integer.parseInt(unparsed);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Gets a long from a string.
     * @param unparsed the unparsed string.
     * @return the long from the string. -1 if not a valid long.
     */
    public static double getLong(String unparsed) {
        try {
            return Long.parseLong(unparsed);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Returns if a string can be parsed as a long.
     * @param unparsed the unparsed string.
     * @return if it can be parsed.
     */
    public static boolean isLong(String unparsed) {
        try {
            Long.parseLong(unparsed);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Gets a double from a string.
     * @param unparsed the unparsed string.
     * @return the double from the string. -1 if not a valid double.
     */
    public static double getDouble(String unparsed) {
        try {
            return Double.parseDouble(unparsed);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Returns if a string can be parsed as a double.
     * @param unparsed the unparsed string.
     * @return if it can be parsed.
     */
    public static boolean isDouble(String unparsed) {
        try {
            Double.parseDouble(unparsed);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Gets a float from a string.
     * @param unparsed the unparsed string.
     * @return the double from the string. -1 if not a valid float.
     */
    public static double getFloat(String unparsed) {
        try {
            return Float.parseFloat(unparsed);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Returns if a string can be parsed as a float.
     * @param unparsed the unparsed string.
     * @return if it can be parsed.
     */
    public static boolean isFloat(String unparsed) {
        try {
            Float.parseFloat(unparsed);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
