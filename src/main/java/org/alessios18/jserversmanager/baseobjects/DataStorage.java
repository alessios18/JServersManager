/**
 *
 */
package org.alessios18.jserversmanager.baseobjects;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.alessios18.jserversmanager.datamodel.wrapper.ServersDataWrapper;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.util.OsUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/** @author alessio */
public class DataStorage {
	 public static final String JSERVERSMANAGER_FOLDER = ".JServersManager";
	 public static final String LIB_FOLDER = "lib";
	 private static final String SERVERS = "servers.xml";
	 private static final String CONFIGURATION = "jsm-config.xml";
	 private static File servers;

	 private static DataStorage dataStorage = null;
	 private static String rootPath = null;

	 private DataStorage() throws UnsupportedOperatingSystemException, IOException {
		  checkFiles();
	 }

	 public static DataStorage getInstance() throws UnsupportedOperatingSystemException, IOException {
		  if (dataStorage == null) {
				dataStorage = new DataStorage();
		  }
		  return dataStorage;
	 }

	 public void checkFiles() throws UnsupportedOperatingSystemException, IOException {
		  rootPath = getRootPath();
		  if (rootPath != null) {
				File root = new File(rootPath);
				if (!root.exists()) {
					 root.mkdirs();
				}
				File lib = new File(getLibPath());
				if (!lib.exists()) {
					 lib.mkdirs();
				}
				File config = getConfigFile();
				if (!config.exists()) {
					 saveConfig();
				}
		  } else {
				throw new UnsupportedOperatingSystemException();
		  }
	 }

	 public BufferedWriter getServerLogBufferedWriter(Server server) throws IOException {
		  File serverLog = new File(getRootPath() + server.getServerName().replaceAll(" ", "_") + ".log");
		  return new BufferedWriter(new FileWriter(serverLog));
	 }

	 protected String getRootPath() {
		  String path = null;
		  if (OsUtils.OSType.Linux.equals(OsUtils.getOperatingSystemType())) {
				path =
						  OsUtils.getUserHome()
									 + OsUtils.getSeparator()
									 + JSERVERSMANAGER_FOLDER
									 + OsUtils.getSeparator();
		  } else if (OsUtils.OSType.Windows.equals(OsUtils.getOperatingSystemType())) {
				path =
						  OsUtils.getUserHome()
									 + OsUtils.getSeparator()
									 + JSERVERSMANAGER_FOLDER
									 + OsUtils.getSeparator();
		  } else if (OsUtils.OSType.MacOS.equals(OsUtils.getOperatingSystemType())
					 || OsUtils.OSType.Other.equals(OsUtils.getOperatingSystemType())) {
				path =
						  OsUtils.getUserHome()
									 + OsUtils.getSeparator()
									 + JSERVERSMANAGER_FOLDER
									 + OsUtils.getSeparator();
		  }
		  return path;
	 }

	 public String getLibPath() {
		  return getRootPath() + LIB_FOLDER;
	 }

	 public File getServersFile() {
		  return new File(getRootPath() + SERVERS);
	 }

	 public File getConfigFile() {
		  return new File(getRootPath() + CONFIGURATION);
	 }

	 public void saveConfig() {
		  JSMConfig c = new JSMConfig();
		  JAXBContext context = null;
		  try {
				context = JAXBContext.newInstance(JSMConfig.class);

				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Marshalling and saving XML to the file.
				m.marshal(c, getConfigFile());
		  } catch (JAXBException e) {
				e.printStackTrace();
		  }
	 }

	 public JSMConfig getConfig() throws JAXBException {
		  JAXBContext context = JAXBContext.newInstance(JSMConfig.class);
		  Unmarshaller um = context.createUnmarshaller();

		  if (getConfigFile().exists()) {
				// Reading XML from the file and unmarshalling.
				JSMConfig config = (JSMConfig) um.unmarshal(getConfigFile());
				return config;
		  }
		  return null;
	 }

	 /**
	  * Saves the current servers data to the specified file.
	  *
	  * @param servers
	  */
	 public void saveServerToFile(ObservableList<Server> servers) {
		  try {
				JAXBContext context = JAXBContext.newInstance(ServersDataWrapper.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Wrapping our person data.
				ServersDataWrapper wrapper = new ServersDataWrapper();
				wrapper.setServers(servers);

				// Marshalling and saving XML to the file.
				m.marshal(wrapper, getServersFile());

		  } catch (Exception e) { // catches ANY exception
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Could not save data");
				alert.setContentText("Could not save data to file:\n" + getServersFile().getPath());
				alert.showAndWait();
				e.printStackTrace();
		  }
	 }

	 /**
	  * Loads servers data from the specified file. The current person data will be replaced.
	  *
	  * @param servers
	  */
	 public void loadServersDataFromFile(ObservableList<Server> servers) {
		  try {
				ServersDataWrapper wrapper = loadServers();

				servers.clear();
				if (wrapper != null && wrapper.getServers() != null) {
					 servers.addAll(wrapper.getServers());
				}

		  } catch (Exception e) { // catches ANY exception
				ExceptionDialog.showException("Could not load data", e);
		  }
	 }

	 protected ServersDataWrapper loadServers() throws JAXBException {
		  JAXBContext context = JAXBContext.newInstance(ServersDataWrapper.class);
		  Unmarshaller um = context.createUnmarshaller();

		  if (getServersFile().exists()) {
				// Reading XML from the file and unmarshalling.
				ServersDataWrapper wrapper = (ServersDataWrapper) um.unmarshal(getServersFile());
				return wrapper;
		  }
		  return null;
	 }
}
