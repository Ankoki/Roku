package com.ankoki.roku.misc;

public class StringUtils {

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length());
        } return string;
    }

    public static String removeQuotes(String string) {
        return StringUtils.replaceLast(string.replaceFirst("\"", ""), "\"", "");
    }

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
}
