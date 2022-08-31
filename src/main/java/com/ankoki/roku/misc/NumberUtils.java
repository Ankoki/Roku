package com.ankoki.roku.misc;

/**
 * Utility class to make number related handling easier.
 */
public class NumberUtils {

    /**
     * Gets an int from a string.
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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

    /**
     * Returns all integers between two numbers (inclusive).
     *
     * @param start the starting point.
     * @param end   the ending point.
     * @return all integers in the given range.
     */
    public static int[] range(int start, int end) {
        if (start == end) return new int[]{start};
        else if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        int[] array = new int[(end - start) + 1];
        int index = 0;
        for (int i = start; i <= end; i++) {
            array[index] = i;
            index++;
        }
        return array;
    }

    /**
     * Checks if a number is in range of two other numbers (inclusive).
     * @param number the number to check for.
     * @param start the starting point.
     * @param end the ending point.
     * @return if the number is in the range.
     */
    public static boolean inRange(int number, int start, int end) {
        return NumberUtils.arrayContains(NumberUtils.range(start, end), number);
    }

    /**
     * Checks if an array contains a certain object.
     * @param array the array.
     * @param key the key to search for.
     * @return whether the given array contains the key.
     */
    public static boolean arrayContains(int[] array, int key) {
        for (int i : array) {
            if (i == key) return true;
        } return false;
    }
}
