package com.ankoki.roku;

import com.ankoki.roku.misc.Version;

public class Roku {
    private static final Version VERSION = Version.of("@VERSION@");

    /**
     * Gets the version of Roku.
     * @return Roku's current version.
     */
    public static Version getVersion() {
        return VERSION;
    }

    /**
     * Checks whether Roku has the '-dev' prefix.
     * @return if Roku is on a development version.
     */
    public static boolean isDevelopmentVersion() {
        return VERSION.hasSuffix() && VERSION.getSuffix().equalsIgnoreCase("DEV");
    }
}
