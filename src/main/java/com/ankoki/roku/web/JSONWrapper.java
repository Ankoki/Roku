package com.ankoki.roku.web;

import com.ankoki.roku.misc.Pair;
import com.ankoki.roku.misc.StringUtils;
import com.ankoki.roku.web.exceptions.MalformedJsonException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONWrapper extends LinkedHashMap implements Map {

    private static final Pattern KEY_PATTERN = Pattern.compile("\"(.+)?\":[ ]?");
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("\"(.+)?\":[ ]?(.+)");

    /**
     * Converts a given Map to a JSON String.
     * @param map the map to convert.
     * @return the converted text.
     */
    public static String toString(Map map) {
        StringBuilder builder = new StringBuilder("{");
        for (Object o : map.entrySet()) {
            Entry entry = (Entry) o;
            builder.append("\"")
                    .append(entry.getKey())
                    .append("\"")
                    .append(":")
                    .append(JSONWrapper.writeJson(entry.getValue()))
                    .append(",");
        }
        builder.setLength(builder.length() - 1);
        return builder.append("}")
                .toString();
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Matches a key value line into a pair.
     * @param line the line to match.
     * @return the key value pair.
     * @throws MalformedJsonException if the line does not match the regex.
     */
    private static Pair<String, Object> matchLine(String line) throws MalformedJsonException {
        Matcher matcher = KEY_VALUE_PATTERN.matcher(line);
        if (matcher.matches()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            return new Pair<>(key, JSONWrapper.parseValue(value));
        } throw new MalformedJsonException();
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Parses a val
     * @param value into its linked type.
     * @return the correct object.
     * @throws MalformedJsonException if there's an error in the value.
     */
    private static Object parseValue(String value) throws MalformedJsonException {
        if (value.startsWith("\"")) {
            if (!value.endsWith("\"")) throw new MalformedJsonException();
            value = value.replaceFirst("\"", "");
            StringBuilder builder = new StringBuilder(value);
            builder.setLength(builder.length() - 1);
            value = builder.toString();
            return value;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {}
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {}
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {}
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ignored) {}
        if (value.equalsIgnoreCase("TRUE")) return true;
        if (value.equalsIgnoreCase("FALSE")) return false;
        if (value.equalsIgnoreCase("NULL")) return null;
        throw new MalformedJsonException();
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Writes a JSON string from an object.
     * @param value the object.
     * @return the finished JSON text.
     */
    private static String writeJson(Object value) {
        StringBuilder builder = new StringBuilder();
        if (value instanceof Number number) {
            if (number instanceof Double d && d.isInfinite() && d.isNaN()) builder.append("null");
            else if (number instanceof Float f && f.isInfinite() && f.isNaN()) builder.append("null");
            else builder.append(number);
        } else if (value instanceof Boolean bool) builder.append(bool);
        else if (value instanceof List list) builder.append(JSONWrapper.writeJson(list));
        else if (value instanceof Map map) builder.append(JSONWrapper.writeJson(map));
        else builder.append("\"")
                    .append(StringUtils.escape(value))
                    .append("\"");
        return builder.toString();
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Writes a JSON string from a list.
     * @param list the list.
     * @return the finished JSON text.
     */
    private static String writeJson(List list) {
        StringBuilder builder = new StringBuilder("[");
        for (Object value : list) builder.append(JSONWrapper.writeJson(value)).append(",");
        builder.setLength(builder.length() - 1);
        return builder.append("]").toString();
    }

    /**
     * <strong>INTERNAL USE ONLY</strong>
     * <p>
     * Writes a JSON string from a map.
     * @param map the map.
     * @return the finished JSON text.
     */
    private static String writeJson(Map map) {
        StringBuilder builder = new StringBuilder("{");
        for (Object o : map.entrySet()) {
            Entry entry = (Entry) o;
            builder.append("\"")
                    .append(entry.getKey())
                    .append("\"")
                    .append(":");
            builder.append(JSONWrapper.writeJson(entry.getValue()))
                    .append(",");
        }
        builder.setLength(builder.length() - 1);
        return builder
                .append("}")
                .toString();
    }

    /**
     * Creates a new JSONWrapper object.
     */
    public JSONWrapper() {
        super();
    }

    /**
     * Converts a Map into a new JSONWrapper object.
     * @param map the map to convert.
     */
    public JSONWrapper(Map map) {
        super(map);
    }


    /**
     * Converts a JSON text into a JSONWrapper object.
     * <p>
     * <strong>THIS NEEDS TO BE FIXED, FIND A WAY TO ALLOW MAPS IN MAPS.</strong>
     * @param json the text.
     * @throws MalformedJsonException thrown if there is an issue with the JSON.
     */
    public JSONWrapper(String json) throws MalformedJsonException {
        if (!json.startsWith("{") && !json.endsWith("}")) throw new MalformedJsonException();

        boolean first = true;
        boolean inQuotes = false;
        boolean inArray = false;
        boolean inMap = false;
        boolean ignoreNext = false;

        int index = 0;

        StringBuilder currentLine = new StringBuilder();
        String currentKey = "";
        List<Object> currentList = new ArrayList<>();
        Map<String, Object> mapWeb = new HashMap<>();
        Map<String, Object> currentMap = new HashMap<>();

        char[] array = json.toCharArray();

        for (char ch : array) {
            if (first) {
                first = false;
                continue;
            }
            index++;

            if (ignoreNext) {
                ignoreNext = false;
                continue;
            }

            switch (ch) {

                case '"':
                    if (inQuotes && !StringUtils.isEscaped(index, array)) {
                        currentLine.append(ch);
                        inQuotes = false;
                    } else if (!inQuotes) {
                        inQuotes = true;
                        currentLine
                            .append(ch);
                    } else currentLine.append(ch);
                    break;

                case ':':
                    if (!inQuotes && inArray) throw new MalformedJsonException();
                    currentLine.append(ch);
                    break;

                case ',':
                    if (inQuotes) currentLine.append(ch);
                    // Earliest , can be is at "{\"\"," (index 3)
                    else if (index < 3) throw new MalformedJsonException();

                    else if (inArray) {
                        currentList.add(StringUtils.removeQuotes(currentLine.toString()));
                        currentLine.setLength(0);
                    }

                    else if (inMap) {
                        Pair<String, Object> pair = JSONWrapper.matchLine(currentLine.toString());
                        Object obj = mapWeb.get(currentKey);
                        if (obj instanceof HashMap map) {
                            map.put(pair.getFirst(), pair.getSecond());
                            mapWeb.put(currentKey, map);
                        } else mapWeb.put(currentKey, obj);
                        currentLine.setLength(0);
                    }

                    else if (currentLine.length() > 0) {
                        Pair<String, Object> pair = JSONWrapper.matchLine(currentLine.toString());
                        this.put(pair.getFirst(), pair.getSecond());
                        currentLine.setLength(0);
                    } else throw new MalformedJsonException();
                    break;

                case '[':
                    if (!inQuotes && inArray) throw new MalformedJsonException();
                    inArray = true;
                    Matcher matcher = KEY_PATTERN.matcher(currentLine.toString());
                    if (matcher.matches()) currentKey = matcher.group(1);
                    else throw new MalformedJsonException();
                    currentLine.setLength(0);
                    break;

                case ']':
                    if (!inQuotes && !inArray) throw new MalformedJsonException();
                    if (!inQuotes) {
                        currentList.add(StringUtils.removeQuotes(currentLine.toString()));
                        this.put(currentKey, currentList);
                        inArray = false;
                        currentList = new ArrayList<>();
                        currentKey = "";
                        currentLine.setLength(0);
                        ignoreNext = true;
                    } else currentLine.append(ch);
                    break;

                case '{':

                    if (!inMap) inMap = true;
                    Matcher matcher1 = KEY_PATTERN.matcher(currentLine.toString());
                    if (matcher1.matches()) {
                        currentKey = matcher1.group(1);
                        currentLine.setLength(0);
                    }
                    else throw new MalformedJsonException();
                    break;

                case '}':
                    if (inQuotes) currentLine.append(ch);
                    else if (!inMap) {
                        if (index + 1 != array.length) throw new MalformedJsonException();

                        else if (currentLine.length() > 0) {
                            Pair<String, Object> pair = JSONWrapper.matchLine(currentLine.toString());
                            this.put(pair.getFirst(), pair.getSecond());
                            currentLine.setLength(0);
                        }

                    } else {
                        Pair<String, Object> pair = JSONWrapper.matchLine(currentLine.toString());
                        Object obj = mapWeb.get(currentKey);
                        if (obj instanceof HashMap map) {
                            map.put(pair.getFirst(), pair.getSecond());
                            mapWeb.put(currentKey, map);
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put(pair.getFirst(), pair.getSecond());
                            mapWeb.put(currentKey, map);
                        }
                        currentLine.setLength(0);
                        this.putAll(mapWeb);
                        inMap = false;
                    }
                    break;

                default:
                    currentLine.append(ch);
            }
        }
    }

    /**
     * Converts the current JSONWrapper to a JSON text.
     * @return the JSON text.
     */
    @Override
    public String toString() {
        return JSONWrapper.toString(this);
    }
}
