/**
 *
 */
package org.alessios18.jserversmanager.baseobjects;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.alessios18.jserversmanager.datamodel.wrapper.ServersDataWrapper;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.util.OsCheck;

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
	 private static final String SERVERS = "servers.xml";
	 private static final String JSERVERSMANAGER_FOLDER = ".JServersManager";

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
		  if (OsCheck.OSType.Linux.equals(OsCheck.getOperatingSystemType())) {
				path =
						  OsCheck.getUserHome()
									 + OsCheck.getSeparator()
									 + JSERVERSMANAGER_FOLDER
									 + OsCheck.getSeparator();
		  } else if (OsCheck.OSType.Windows.equals(OsCheck.getOperatingSystemType())) {
				path =
						  OsCheck.getUserHome()
									 + OsCheck.getSeparator()
									 + JSERVERSMANAGER_FOLDER
									 + OsCheck.getSeparator();
		  } else if (OsCheck.OSType.MacOS.equals(OsCheck.getOperatingSystemType())
					 || OsCheck.OSType.Other.equals(OsCheck.getOperatingSystemType())) {
				path =
						  OsCheck.getUserHome()
									 + OsCheck.getSeparator()
									 + JSERVERSMANAGER_FOLDER
									 + OsCheck.getSeparator();
		  }
		  return path;
	 }

	 public File getServersFile() {
		  return new File(getRootPath() + SERVERS);
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
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Could not load data");
				alert.setContentText("Could not load data from file:\n" + getServersFile().getPath());

				alert.showAndWait();
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
