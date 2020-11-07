package org.alessios18.jserversmanager.updater.baseobjects;

public class Version implements Comparable<Version> {
	 public static final String VERSION_SPECIFIER_ALPHA = "-alpha";
	 public static final String VERSION_SPECIFIER_BETA = "-beta";
	 public static final String VERSION_SPECIFIER_SNAPSHOT = "-SNAPSHOT";

	 private String fullName;
	 private String version;
	 private boolean isPreRelease;

	 public Version(String version) {
		  if (version == null)
				throw new IllegalArgumentException("Version can not be null");

		  this.version = cleanVersionName(version);
		  if (!this.version.matches("[0-9]+(\\.[0-9]+)*"))
				throw new IllegalArgumentException("Invalid version format");
		  this.fullName = version;
		  setIsPreRelease(this.fullName);
	 }

	 public Version(String version, boolean isPreRelease) {
		  if (version == null)
				throw new IllegalArgumentException("Version can not be null");
		  this.version = cleanVersionName(version);
		  if (!this.version.matches("[0-9]+(\\.[0-9]+)*"))
				throw new IllegalArgumentException("Invalid version format");
		  this.isPreRelease = isPreRelease;
		  this.fullName = version;
	 }

	 public final String get() {
		  return this.version;
	 }

	 public boolean setIsPreRelease(String version) {
		  return this.isPreRelease = (version.contains(VERSION_SPECIFIER_ALPHA) || version.contains(VERSION_SPECIFIER_BETA) || version.contains(VERSION_SPECIFIER_SNAPSHOT));
	 }

	 public boolean setIsPreRelease(boolean isPreRelease) {
		  return this.isPreRelease = isPreRelease;
	 }

	 public String getFullName() {
		  return fullName;
	 }

	 public void setFullName(String fullName) {
		  this.fullName = fullName;
	 }

	 public String getVersion() {
		  return version;
	 }

	 public void setVersion(String version) {
		  this.version = version;
	 }

	 public boolean isPreRelease() {
		  return isPreRelease;
	 }

	 public void setPreRelease(boolean preRelease) {
		  isPreRelease = preRelease;
	 }

	 public String cleanVersionName(String version) {
		  return version.replace("v", "").replace(VERSION_SPECIFIER_ALPHA, "").replace(VERSION_SPECIFIER_BETA, "").replace(VERSION_SPECIFIER_SNAPSHOT, "");
	 }

	 @Override
	 public int compareTo(Version that) {
		  if (that == null)
				return 1;
		  String[] thisParts = this.getVersion().split("\\.");
		  String[] thatParts = that.getVersion().split("\\.");
		  int length = Math.max(thisParts.length, thatParts.length);
		  for (int i = 0; i < length; i++) {
				int thisPart = i < thisParts.length ?
						  Integer.parseInt(thisParts[i]) : 0;
				int thatPart = i < thatParts.length ?
						  Integer.parseInt(thatParts[i]) : 0;
				if (thisPart < thatPart)
					 return -1;
				if (thisPart > thatPart)
					 return 1;
		  }
		  if (this.isPreRelease && !that.isPreRelease())
				return -1;
		  if (!this.isPreRelease && that.isPreRelease())
				return 1;
		  return 0;
	 }

	 @Override
	 public boolean equals(Object that) {
		  if (this == that)
				return true;
		  if (that == null)
				return false;
		  if (this.getClass() != that.getClass())
				return false;
		  return this.compareTo((Version) that) == 0;
	 }

	 public boolean isGreaterThen(Version that) {
		  return this.compareTo(that) > 0;
	 }

	 public boolean isMinorThen(Version that) {
		  return this.compareTo(that) < 0;
	 }
}
