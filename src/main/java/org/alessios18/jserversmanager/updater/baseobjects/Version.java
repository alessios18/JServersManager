package org.alessios18.jserversmanager.updater.baseobjects;

import java.util.Objects;

public class Version implements Comparable<Version> {
  public static final String VERSION_SPECIFIER_ALPHA = "-alpha";
  public static final String VERSION_SPECIFIER_BETA = "-beta";
  public static final String VERSION_SPECIFIER_SNAPSHOT = "-SNAPSHOT";

  private String fullName;
  private String versionCode;
  private boolean isPreRelease;

  public Version(String versionCode) {
    if (versionCode == null) {
      throw new IllegalArgumentException("Version can not be null");
    }
    this.versionCode = cleanVersionName(versionCode);
    if (!this.versionCode.matches("[0-9]+(\\.[0-9]+)*")) {
      throw new IllegalArgumentException("Invalid version format");
    }
    this.fullName = versionCode;
    setIsPreRelease(this.fullName);
  }

  public Version(String versionCode, boolean isPreRelease) {
    if (versionCode == null) {
      throw new IllegalArgumentException("Version can not be null");
    }
    this.versionCode = cleanVersionName(versionCode);
    if (!this.versionCode.matches("[0-9]+(\\.[0-9]+)*")) {
      throw new IllegalArgumentException("Invalid version format");
    }
    this.isPreRelease = isPreRelease;
    this.fullName = versionCode;
  }

  public final String get() {
    return this.versionCode;
  }

  public void setIsPreRelease(String version) {
    this.isPreRelease =
        (version.contains(VERSION_SPECIFIER_ALPHA)
            || version.contains(VERSION_SPECIFIER_BETA)
            || version.contains(VERSION_SPECIFIER_SNAPSHOT));
  }

  public void setIsPreRelease(boolean isPreRelease) {
    this.isPreRelease = isPreRelease;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(String versionCode) {
    this.versionCode = versionCode;
  }

  public boolean isPreRelease() {
    return isPreRelease;
  }

  public void setPreRelease(boolean preRelease) {
    isPreRelease = preRelease;
  }

  public String cleanVersionName(String version) {
    return version
        .replace("v", "")
        .replace(VERSION_SPECIFIER_ALPHA, "")
        .replace(VERSION_SPECIFIER_BETA, "")
        .replace(VERSION_SPECIFIER_SNAPSHOT, "");
  }

  @Override
  public int compareTo(Version that) {
    if (that == null) {
      return 1;
    }
    String[] thisParts = this.getVersionCode().split("\\.");
    String[] thatParts = that.getVersionCode().split("\\.");
    int length = Math.max(thisParts.length, thatParts.length);
    for (int i = 0; i < length; i++) {
      int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
      int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;
      if (thisPart < thatPart) return -1;
      if (thisPart > thatPart) return 1;
    }
    if (this.isPreRelease && !that.isPreRelease()) {
      return -1;
    }
    if (!this.isPreRelease && that.isPreRelease()) {
      return 1;
    }
    return 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, versionCode, isPreRelease);
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (this.getClass() != that.getClass()) {
      return false;
    }
    return this.compareTo((Version) that) == 0;
  }

  public boolean isGreaterThen(Version that) {
    return this.compareTo(that) > 0;
  }

  public boolean isMinorThen(Version that) {
    return this.compareTo(that) < 0;
  }
}
