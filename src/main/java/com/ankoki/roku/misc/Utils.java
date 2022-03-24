package com.ankoki.roku.misc;

public class Utils {

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
}
