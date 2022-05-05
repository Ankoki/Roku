package com.ankoki.roku.misc;

public class Pair<T, K>{

    private T first;
    private K second;

    /**
     * Creates a new pair with the given first and second.
     * @param first the first value.
     * @param second the second value.
     */
    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first value.
     * @return the first value.
     */
    public T getFirst() {
        return first;
    }

    /**
     * Gets the second value.
     * @return the second value.
     */
    public K getSecond() {
        return second;
    }

    /**
     * Sets the first value.
     * @param first the new first value.
     */
    public void setFirst(T first) {
        this.first = first;
    }

    /**
     * Sets the second value.
     * @param second the new second value.
     */
    public void setSecond(K second) {
        this.second = second;
    }

    /**
     * Returns whether the first value is null.
     * @return true if not null, else false.
     */
    public boolean hasFirst() {
        return first != null;
    }

    /**
     * Returns whether the second value is null.
     * @return true if not null, else false.
     */
    public boolean hasSecond() {
        return second != null;
    }

    /**
     * Returns whether the second and first value are the same.
     * @return true if identical, else false.
     */
    public boolean isIdentical() {
        if (!this.hasFirst() && !this.hasSecond()) return true;
        if (!this.hasFirst() || !this.hasSecond()) return false;
        return first.equals(second);
    }
}
