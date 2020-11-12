package org.alessios18.jserversmanager.updater;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.updater.baseobjects.Asset;
import org.alessios18.jserversmanager.updater.baseobjects.Release;
import org.alessios18.jserversmanager.updater.baseobjects.Version;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JServersManagerUpdater {
  public static final String VERSION_SPECIFIER_ALPHA = "-alpha";
  public static final String VERSION_SPECIFIER_BETA = "-beta";
  public static final String VERSION_SPECIFIER_SNAPSHOT = "-SNAPSHOT";
  public static final String COMMAND_UPDATE = "update";
  protected static final String SERVICE_URL =
      " https://api.github.com/repos/alessios18/JServersManager/releases";
  private static final Logger logger = JServersManagerApp.getLogger();
  private final Gson gson = new Gson();
  private Release available;

  public static void startJar(String jarFileName, String oldJarFilePAth) {
    Thread th =
        new Thread(
            () -> {
              String[] commands =
                  new String[] {"java", "-jar", jarFileName, COMMAND_UPDATE, oldJarFilePAth};
              ProcessBuilder pb = new ProcessBuilder(commands);
              try {
                logger.debug(
                    "Calling new version for update: java -jar"
                        + jarFileName
                        + " "
                        + COMMAND_UPDATE
                        + " "
                        + oldJarFilePAth);
                pb.directory(new File(DataStorage.getInstance().getLibPath()));
                pb.start();
                logger.debug(
                    "Called new version for update: java -jar "
                        + jarFileName
                        + " "
                        + COMMAND_UPDATE
                        + " "
                        + oldJarFilePAth);
              } catch (UnsupportedOperatingSystemException | IOException | JAXBException e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String exceptionText = sw.toString();
                logger.error(exceptionText);
              }
            });
    th.start();
    System.exit(0);
  }

  public boolean doUpgrade(String[] args) throws IOException {
    logger.debug("Started upgrade...");
    File runningJar =
        new java.io.File(
            JServersManagerApp.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    if (args != null && args.length > 0) {
      if (args[0].equals(COMMAND_UPDATE)) {
        File oldJar = new File(args[1]);
        logger.debug("Checking old jar");
        if (oldJar.exists()) {
          File newJar =
              new File(
                  oldJar.getParentFile().getAbsolutePath()
                      + OsUtils.getSeparator()
                      + runningJar.getName());
          FileUtils.copyFile(runningJar, newJar);
          boolean result = oldJar.delete();
          logger.debug(
              "Jar copied to:"
                  + newJar.getAbsolutePath()
                  + " and old jar is deleted with result:"
                  + result);
          logger.debug("Starting the new Version");
          startUpdatedJar(oldJar.getParentFile().getAbsolutePath(), newJar.getName());
        } else {
          logger.debug("Jar " + oldJar.getAbsolutePath() + " not exist");
        }
      }
    }
    return false;
  }

  public Release checkForUpdates()
      throws IOException, XmlPullParserException, UnsupportedOperatingSystemException,
          JAXBException {
    boolean bOnlyStable = DataStorage.getInstance().getConfig().isOnlyStable();
    List<Release> releases = getReleases();
    Version currentVersion = new Version(JServersManagerApp.getCurrentVersion());
    Version maxVersion = currentVersion;
    for (Release r : releases) {
      if (isPresentJarInVersion(r)) {
        Version v = new Version(r.getTagName(), r.getPrerelease());
        if (maxVersion.isMinorThen(v)
            && (!(bOnlyStable && r.getPrerelease()) || !r.getPrerelease())) {
          available = r;
          maxVersion = v;
        }
      }
    }
    logger.debug("Found new version " + maxVersion.getFullName());
    return available;
  }

  public boolean isPresentJarInVersion(Release r) {
    for (Asset a : r.getAssets()) {
      if (a.getName().contains(".jar")) {
        return true;
      }
    }
    return false;
  }

  public Asset getJarAsset(Release r) {
    for (Asset a : r.getAssets()) {
      if (a.getName().contains(".jar")) {
        return a;
      }
    }
    return null;
  }

  public void updateVersion()
      throws IOException, UnsupportedOperatingSystemException, JAXBException {
    Asset jar = downloadNewVersion();
    String jarFilePath =
        new java.io.File(
                JServersManagerApp.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
            .getAbsolutePath();
    startJar(jar.getName(), jarFilePath);
  }

  public Asset downloadNewVersion()
      throws IOException, UnsupportedOperatingSystemException, JAXBException {
    Asset jar = getJarAsset(available);
    if (!new File(DataStorage.getInstance().getLibPath() + jar.getName()).exists()) {
      try (BufferedInputStream in =
          new BufferedInputStream(new URL(jar.getBrowserDownloadUrl()).openStream())) {
        try (FileOutputStream fileOutputStream =
            new FileOutputStream(DataStorage.getInstance().getLibPath() + jar.getName())) {
          byte[] dataBuffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
          }
        }
      }
      logger.debug("downloaded jar: " + jar.getName());
    }
    return jar;
  }

  protected boolean areEqualsButOneIsStableVersion(
      boolean currentIsStable, String maxVersion, Release available, Release r) {
    return maxVersion.compareTo(cleanVersionName(r.getTagName())) == 0
        && ((available == null && !currentIsStable && !r.getPrerelease())
            || (available != null && available.getPrerelease() && !r.getPrerelease()));
  }

  protected boolean versionIsGreater(String maxVersion, Release r) {
    return maxVersion.compareTo(cleanVersionName(r.getTagName())) > 0;
  }

  public String cleanVersionName(String version) {
    return version
        .replace("v", "")
        .replace(VERSION_SPECIFIER_ALPHA, "")
        .replace(VERSION_SPECIFIER_BETA, "")
        .replace(VERSION_SPECIFIER_SNAPSHOT, "");
  }

  public boolean isStable(String version) {
    return !(version.contains(VERSION_SPECIFIER_ALPHA)
        || version.contains(VERSION_SPECIFIER_BETA)
        || version.contains(VERSION_SPECIFIER_SNAPSHOT));
  }

  public boolean updateVersionConfirmDialog() {
    int res =
        JOptionPane.showConfirmDialog(
            null,
            "The new version "
                + available.getTagName()
                + " as been found!\nDo you want execute the upgrade?");
    return res == 0;
  }

  public List<Release> getReleases() throws IOException {
    URL url = new URL(SERVICE_URL);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
    if (conn.getResponseCode() != 200) {
      JServersManagerApp.getLogger().error("Failed : HTTP error code : " + conn.getResponseCode());
      return new ArrayList<>();
    }
    String json = getServiceResponse(conn);
    Type listType = new TypeToken<ArrayList<Release>>() {}.getType();
    conn.disconnect();
    return gson.fromJson(json, listType);
  }

  private String getServiceResponse(HttpURLConnection conn) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
    StringBuilder sb = new StringBuilder();
    String output;
    while ((output = br.readLine()) != null) {
      sb.append(output);
    }
    return sb.toString();
  }

  public void startUpdatedJar(String jarDir, String jarName) {
    Thread th =
        new Thread(
            () -> {
              String[] commands = new String[] {"java", "-jar", jarName};
              ProcessBuilder pb = new ProcessBuilder(commands);
              try {
                pb.directory(new File(jarDir));
                pb.start();
              } catch (IOException e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String exceptionText = sw.toString();
                logger.error(exceptionText);
              }
            });
    th.start();
    System.exit(0);
  }
}
