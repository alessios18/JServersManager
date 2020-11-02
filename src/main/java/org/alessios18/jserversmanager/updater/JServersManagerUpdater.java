package org.alessios18.jserversmanager.updater;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.alessios18.jserversmanager.App;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.updater.baseobjects.Asset;
import org.alessios18.jserversmanager.updater.baseobjects.Release;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JServersManagerUpdater {

	 public static final String VERSION_SPECIFIER_ALPHA = "-alpha";
	 public static final String VERSION_SPECIFIER_BETA = "-beta";
	 public static final String VERSION_SPECIFIER_SNAPSHOT = "-SNAPSHOT";

	 public static final String COMMAND_UPDATE = "update";
	 protected static final String SERVICE_URL = " https://api.github.com/repos/alessios18/JServersManager/releases";
	 protected static boolean bOnlyStable = false;
	 private String currentVersion;
	 private Release available;
	 private final Gson gson = new Gson();

	 public JServersManagerUpdater() throws Exception {
		  bOnlyStable = DataStorage.getInstance().getConfig().isOnlyStable();
	 }

	 public static void startJar(String jarFileName, String oldJarFilePAth) {
		  Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					 String[] commands = new String[]{
								"java", "-jar", jarFileName, COMMAND_UPDATE, oldJarFilePAth};
					 ProcessBuilder pb = new ProcessBuilder(commands);
					 try {
						  pb.directory(new File(DataStorage.getInstance().getLibPath()));
						  Process p = pb.start();
						  //p.waitFor();
					 } catch (UnsupportedOperatingSystemException e) {
						  e.printStackTrace();
					 } catch (IOException e) {
						  e.printStackTrace();
					 }
				}
		  });
		  th.start();
		  System.exit(0);
	 }

	 public boolean doUpgrade(String[] args) throws IOException {
		  File runningJar = new java.io.File(App.class.getProtectionDomain()
					 .getCodeSource()
					 .getLocation()
					 .getPath());
		  if (args != null && args.length > 0) {
				if (args[0].equals(COMMAND_UPDATE)) {
					 File oldJar = new File(args[1]);
					 if (oldJar.exists()) {
						  FileUtils.copyFile(runningJar, oldJar);
						  File newName = new File(oldJar.getParentFile().getAbsolutePath() + OsUtils.getSeparator() + runningJar.getName());
						  oldJar.renameTo(newName);
						  startUpdatedJar(oldJar.getParentFile().getAbsolutePath(), newName.getName());
					 }
				}
		  }
		  return false;
	 }

	 public Release checkForUpdates() throws IOException, XmlPullParserException {
		  ArrayList<Release> releases = getReleases();
		  currentVersion = App.getCurrentVersion();
		  boolean currentIsStable = isStable(currentVersion);
		  String maxVersion = cleanVersionName(currentVersion);
		  for (Release r : releases) {
				if (bOnlyStable && r.getPrerelease() && !isPresentJarInVersion(r)) {
					 continue;
				} else {
					 if (versionIsGreater(maxVersion, r) || areEqualsButOneIsStableVersion(currentIsStable, maxVersion, available, r)) {
						  available = r;
						  maxVersion = cleanVersionName(r.getTagName());
					 }
				}
		  }
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

	 public void updateVersion() throws Exception {
		  Asset jar = downloadNewVersion();
		  String jarFilePath = new java.io.File(App.class.getProtectionDomain()
					 .getCodeSource()
					 .getLocation()
					 .getPath())
					 .getAbsolutePath();
		  startJar(jar.getName(), jarFilePath);
	 }

	 public Asset downloadNewVersion() throws IOException, UnsupportedOperatingSystemException {
		  Asset jar = getJarAsset(available);
		  if (!new File(DataStorage.getInstance().getLibPath()
					 + OsUtils.getSeparator() + jar.getName()).exists()) {
				BufferedInputStream in = new BufferedInputStream(new URL(jar.getBrowserDownloadUrl()).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(DataStorage.getInstance().getLibPath()
						  + OsUtils.getSeparator() + jar.getName());
				byte[] dataBuffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					 fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
				in.close();
				fileOutputStream.close();
				App.getLogger().debug("downloaded jar: " + jar.getName());
		  }
		  return jar;
	 }

	 protected boolean areEqualsButOneIsStableVersion(boolean currentIsStable, String maxVersion, Release available, Release r) {
		  return maxVersion.compareTo(cleanVersionName(r.getTagName())) == 0
					 && ((available == null && !currentIsStable && !r.getPrerelease())
					 || (available != null && available.getPrerelease() && !r.getPrerelease()));
	 }

	 protected boolean versionIsGreater(String maxVersion, Release r) {
		  return maxVersion.compareTo(cleanVersionName(r.getTagName())) < 0;
	 }

	 public String cleanVersionName(String version) {
		  return version.replace("v", "").replace(VERSION_SPECIFIER_ALPHA, "").replace(VERSION_SPECIFIER_BETA, "").replace(VERSION_SPECIFIER_SNAPSHOT, "");
	 }

	 public boolean isStable(String version) {
		  return !(version.contains(VERSION_SPECIFIER_ALPHA) || version.contains(VERSION_SPECIFIER_BETA) || version.contains(VERSION_SPECIFIER_SNAPSHOT));
	 }

	 public boolean updateVersionConfirmDialog() {
		  int res = JOptionPane.showConfirmDialog(null, "The new version " + available.getTagName() + " as been found!\nDo you want execute the upgrade?");
		  return res == 0;
	 }

	 public ArrayList<Release> getReleases() throws IOException {
		  URL url = new URL(SERVICE_URL);
		  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		  conn.setRequestMethod("GET");
		  conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
		  if (conn.getResponseCode() != 200) {
				App.getLogger().error("Failed : HTTP error code : " + conn.getResponseCode());
				return null;
		  }
		  String json = getServiceResponse(conn);
		  Type listType = new TypeToken<ArrayList<Release>>() {
		  }.getType();
		  conn.disconnect();
		  return gson.fromJson(json, listType);
	 }

	 private String getServiceResponse(HttpURLConnection conn) throws IOException {
		  BufferedReader br = new BufferedReader(new InputStreamReader(
					 (conn.getInputStream())));
		  StringBuilder sb = new StringBuilder();
		  String output;
		  while ((output = br.readLine()) != null) {
				sb.append(output);
		  }
		  return sb.toString();
	 }

	 public void startUpdatedJar(String jarDir, String jarName) {
		  Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					 String[] commands = new String[]{
								"java", "-jar", jarName};
					 ProcessBuilder pb = new ProcessBuilder(commands);
					 try {
						  pb.directory(new File(jarDir));
						  Process p = pb.start();
						  //p.waitFor();
					 } catch (IOException e) {
						  e.printStackTrace();
					 }
				}
		  });
		  th.start();
		  System.exit(0);
	 }
}
