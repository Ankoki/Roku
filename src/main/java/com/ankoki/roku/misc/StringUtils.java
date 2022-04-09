package com.ankoki.roku.misc;

import java.util.stream.Stream;

public class StringUtils {

    /**
     * Replaces the last instance of a string in a string.
     * @param string the string to search in.
     * @param toReplace what to replace.
     * @param replacement what to replace it with.
     * @return the modified string.
     */
    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length());
        } return string;
    }

    /**
     * Removes quotes from the start and end of a string.
     * @param string the string with quotes at the start and end.
     * @return the modified string.
     */
    public static String removeQuotes(String string) {
        return StringUtils.replaceLast(string.replaceFirst("\"", ""), "\"", "");
    }

    /**
     * Escapes characters in an object.
     * @param object the object to escape.
     * @return the escaped string.
     */
    public static String escape(Object object) {
        StringBuilder builder = new StringBuilder();
        String string = String.valueOf(object);
        for (char c : string.toCharArray()) {
            switch (c) {
                case '\"' -> builder.append("\\\"");
                case '\\' -> builder.append("\\\\");
                case '\b' -> builder.append("\\b");
                case '\f' -> builder.append("\\f");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                case '/' -> builder.append("\\/");
            }
            if (c <= '\u001F' || c >= '\u007F' && c <= '\u009F' || c >= '\u2000' && c <= '\u20FF') {
                String s = Integer.toHexString(c);
                builder.append("\\u");
                builder.append("0".repeat(4 - s.length()));
                builder.append(s.toUpperCase());
            } else builder.append(c);
        }
        return builder.toString();
    }

    /**
     * Checks if a character in a char array is escaped.
     * @param position the position.
     * @param array the array.
     * @return true if it is escaped.
     */
    public static boolean isEscaped(int position, char[] array) {
        if (position >= array.length || position < 0) return false;
        if (array.length == 1) return false;
        char c = array[position];
        switch (c) {
            case '\\':
            case '\b':
            case '\f':
            case '\n':
            case '\r':
            case '\t':
            case '/':
                char prev = array[position - 1];
                if (prev == '\\') {
                    if (array.length <= 2) return true;
                    char fin = array[position - 2];
                    return fin != '\\';
                }
            default:
                return false;
        }
    }

    /**
     * Converts a string to title case
     * <p>
     * For example, "hello world" to "Hello World"
     *
     * @param text text to be in title case.
     * @return string in title case.
     */
    public static String toTitleCase(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) return "";
        if (text.length() == 1) return text.toUpperCase();
        text = text.replace("_", " ");

        StringBuilder builder = new StringBuilder(text.length());

        Stream.of(text.split(" ")).forEach(stringPart -> {
            char[] charArray = stringPart.toLowerCase().toCharArray();
            charArray[0] = Character.toUpperCase(charArray[0]);
            builder.append(new String(charArray)).append(" ");
        });
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
