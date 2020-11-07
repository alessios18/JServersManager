
package org.alessios18.jserversmanager.baseobjects;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.serverdata.wrapper.ServersDataWrapper;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/** @author alessio */
public class DataStorage {
	 private static final Logger logger = JServersManagerApp.getLogger();
	 public static final String JSERVERSMANAGER_FOLDER = ".JServersManager";
	 public static final String LIB_FOLDER = "lib";
	 public static final String CONFIG_FOLDER = "config";
	 public static final String DATA_FOLDER = "data";
	 public static final String LOGS_FOLDER = "logs";
	 private static final String SERVERS = "servers.xml";
	 private static final String CONFIGURATION = "jsm-config.xml";

	 private static DataStorage dataStorage = null;

	 String pattern = "yyyyMMdd_HHmmss";
	 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	 private DataStorage() throws UnsupportedOperatingSystemException{
		  checkFiles();
	 }

	 public static DataStorage getInstance() throws UnsupportedOperatingSystemException {
		  if (dataStorage == null) {
				dataStorage = new DataStorage();
		  }
		  return dataStorage;
	 }

	 public void checkFiles() throws UnsupportedOperatingSystemException{
		  String rootPath = getRootPath();
		  if (rootPath != null) {
		  	 	boolean results;
				results = createMissingPath(rootPath);
				results = (results && createMissingPath(getLibPath()));
				results = (results && createMissingPath(getLogPath()));
				results = (results && createMissingPath(getDataPath()));
				results = (results && createMissingPath(getConfigPath()));
				File config = getConfigFile();
				if (!config.exists()) {
					 saveConfig();
				}
				if(results) {
					 return;
				}
		  }
		  throw new UnsupportedOperatingSystemException();
	 }

	 protected boolean createMissingPath(String path) {
		  File lib = new File(path);
		  if (!lib.exists()) {
				return lib.mkdirs();
		  }
		  return true;
	 }

	 public BufferedWriter getServerLogBufferedWriter(Server server) throws IOException {
		  File serverLog = new File( getLogPath() + server.getServerName().replace(" ", "_")+"-"+simpleDateFormat.format(new Date()) + ".log");
		  return new BufferedWriter(new FileWriter(serverLog));
	 }

	 protected String getRootPath() {
		  return OsUtils.getUserHome()
					 + OsUtils.getSeparator()
					 + JSERVERSMANAGER_FOLDER
					 + OsUtils.getSeparator();
	 }

	 public String getLibPath() {
		  return getRootPath() + LIB_FOLDER+ OsUtils.getSeparator();
	 }
	 public String getConfigPath() {
		  return getRootPath() + CONFIG_FOLDER+ OsUtils.getSeparator();
	 }

	 public String getLogPath() {
		  return getRootPath() + LOGS_FOLDER+ OsUtils.getSeparator();
	 }
	 public String getDataPath() {
		  return getRootPath() + DATA_FOLDER+ OsUtils.getSeparator();
	 }

	 public File getServersFile() {
		  return new File(getDataPath() + SERVERS);
	 }

	 public File getConfigFile() {
		  return new File(getConfigPath() + CONFIGURATION);
	 }

	 public void saveConfig() {
		  JSMConfig c = new JSMConfig();
		  JAXBContext context;
		  try {
				context = JAXBContext.newInstance(JSMConfig.class);

				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Marshalling and saving XML to the file.
				m.marshal(c, getConfigFile());
		  } catch (JAXBException e) {
				e.printStackTrace();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				String exceptionText = sw.toString();
				logger.error(exceptionText);
		  }
	 }

	 public JSMConfig getConfig() throws JAXBException {
		  JAXBContext context = JAXBContext.newInstance(JSMConfig.class);
		  Unmarshaller um = context.createUnmarshaller();

		  if (getConfigFile().exists()) {
				// Reading XML from the file and unmarshalling.
				return (JSMConfig) um.unmarshal(getConfigFile());
		  }
		  return null;
	 }

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
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				String exceptionText = sw.toString();
				logger.error(exceptionText);
		  }
	 }
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
				return (ServersDataWrapper) um.unmarshal(getServersFile());
		  }
		  return null;
	 }
}
