/** */
package org.alessios18.jserversmanager.util;

import java.io.File;
import java.util.Locale;

/** @author alessio */
public class OsCheck {
    // cached result of OS detection
  protected static OSType detectedOS;
	 protected static String separator;
  protected static String userHome;

  /**
   * detect the operating system from the os.name System property and cache the result
   *
   * @returns - the operating system detected
   */
  public static OSType getOperatingSystemType() {
    if (detectedOS == null) {
      String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
      if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
        detectedOS = OSType.MacOS;
      } else if (OS.indexOf("win") >= 0) {
        detectedOS = OSType.Windows;
      } else if (OS.indexOf("nux") >= 0) {
        detectedOS = OSType.Linux;
      } else {
        detectedOS = OSType.Other;
      }
    }
    return detectedOS;
  }

  public static String getSeparator() {
    if (separator == null) {
      separator = File.separator;
    }
    return separator;
  }

  public static String getUserHome() {
    if (userHome == null) {
      userHome = System.getProperty("user.home");
    }
    return userHome;
  }

/** types of Operating Systems */
  public enum OSType {
    Windows,
    MacOS,
    Linux,
    Other
  }
}
