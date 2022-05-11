package com.ankoki.roku.misc;

/**
 * Class to make comparing and storing versions easy.
 */
public class Version {

    /**
     * Gets a new version from a string.
     * @param version the string to get a version from. Can can only have '-%text%' on the last subversion.
     * @return a new Version.
     * @throws IllegalArgumentException if version does not match the criteria.
     */
    public static Version of(String version) {
        return new Version(version);
    }

    /**
     * Gets a new version from an array.
     * @param version the array to declare the sub-versions.
     * @return a new Version.
     */
    public static Version of(int... version) {
        return new Version(version);
    }

    private final int[] version;

    private Version(String version) {
        String[] indices = version.split("\\.");
        if (indices.length == 0) throw new IllegalArgumentException();
        this.version = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            String index = indices[i];
            if (NumberUtils.isInt(index)) this.version[i] = NumberUtils.getInt(index);
            else if (i != indices.length - 1) throw new IllegalArgumentException("You can only have a '-<text>' on the last sub-version, e.g: 1.0-beta");
            else if (NumberUtils.isInt(index.split("-")[0])) this.version[i] = NumberUtils.getInt(index.split("-")[0]);
            else throw new IllegalArgumentException();
        }
    }

    private Version(int... version) {
        this.version = version;
    }

    protected int[] getVersion() {
        return version;
    }

    /**
     * Returns whether the current version is newer than the given version.
     * @param ver the version to compare to.
     * @return true if the current version is newer.
     */
    public boolean isNewerThan(String ver) {
        Version version = Version.of(ver);
        int[] foreignVersion = version.getVersion();
        Version largest = foreignVersion.length > this.getVersion().length ? version : this;
        for (int i = 0; i < largest.getVersion().length; i++) {
            if (foreignVersion[i] < this.version[i]) return true;
        } return false;
    }

    /**
     * Returns whether the current version is newer than the given version.
     * @param foreignVersion the version to compare to.
     * @return true if the current version is newer.
     */
    public boolean isNewerThan(int... foreignVersion) {
        Version largest = foreignVersion.length > this.getVersion().length ? Version.of(foreignVersion) : this;
        for (int i = 0; i < largest.getVersion().length; i++) {
            if (foreignVersion[i] < this.version[i]) return true;
        } return false;
    }

    /**
     * Returns whether the current version is newer than the given version.
     * @param version the version to compare to.
     * @return true if the current version is newer.
     */
    public boolean isNewerThan(Version version) {
        int[] foreignVersion = version.getVersion();
        Version smallest = foreignVersion.length < this.getVersion().length ? version : this;
        for (int i = 0; i < smallest.getVersion().length; i++) {
            if (foreignVersion[i] < this.version[i]) return true;
        } return false;
    }
}
