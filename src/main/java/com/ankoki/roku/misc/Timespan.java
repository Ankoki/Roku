package com.ankoki.roku.misc;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Timespan {

    private final long milliseconds;

    /**
     * Creates a new Timespan with the given amount of milliseconds.
     * @param milliseconds the milliseconds.
     */
    public Timespan(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    /**
     * Gets a timespan from an unparsed string.
     * @param unparsed the string with the format "%number% %span%"
     * @return the new Timespan. null if the unparsed format is invalid.
     */
    @Nullable
    public static Timespan of(String unparsed) {
        String[] split = unparsed.split(" ");
        if (split.length < 2) return null;
        String span = String.join(" ", Arrays.copyOfRange(split, 1, split.length)).toUpperCase();
        long amount;
        try {
            amount = Long.parseLong(split[0]);
        } catch (NumberFormatException ex) {
            return null;
        }
        return switch (span) {
            case "TICK", "TICKS" -> new Timespan((amount * 1000) * 20);
            case "SECONDS", "SECOND" -> new Timespan(amount * 1000);
            case "MINUTE", "MINUTES" -> new Timespan(amount * 60000);
            case "HOUR", "HOURS" -> new Timespan((long) (amount * 3.6e+6));
            case "DAY", "DAYS" -> new Timespan((long) (amount * 8.64e+7));
            default -> null;
        };
    }

    /**
     * Gets the amount of ticks in the current timespan.
     * @return the ticks.
     */
    public double getTicks() {
        return (milliseconds / 20F) / 1000F;
    }

    /**
     * Gets the amount of the unit this timespan is.
     * @param unit the unit.
     * @return the amount of time in given unit.
     */
    public double getAs(Unit unit) {
        return switch (unit) {
            case TICKS -> this.getTicks();
            case SECONDS -> milliseconds / 1000F;
            case MINUTES -> milliseconds / 60000F;
            case HOURS -> milliseconds / 3600000F;
            case DAYS -> milliseconds / 85400000F;
        };
    }

    public enum Unit {
        TICKS, SECONDS, MINUTES, HOURS, DAYS;

        @Override
        public String toString() {
            return StringUtils.toTitleCase(this.name());
        }
    }
}
