package com.ankoki.roku.misc;

public class Pair<T, K>{

    private T first;
    private K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public K getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(K second) {
        this.second = second;
    }

    public boolean hasFirst() {
        return first != null;
    }

    public boolean hasSecond() {
        return second != null;
    }

    public boolean isIdentical() {
        if (!this.hasFirst() && !this.hasSecond()) return true;
        if (!this.hasFirst() || !this.hasSecond()) return false;
        return first.equals(second);
    }
}
