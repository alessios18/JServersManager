package org.alessios18.jserversmanager.baseobjects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;

import java.util.UUID;


public class Server {

	 private final StringProperty serverID;
	 private final StringProperty serverName;
	 private final ObjectProperty<ServerType> serverType;
	 private final StringProperty standalonePath;
	 private final StringProperty serverPath;
	 private final StringProperty adminPort;
	 private final StringProperty debugPort;
	 private final StringProperty portOffset;
	 private final ObjectProperty<String[]> filePathToDeploy;

	 public Server() {
		  serverID = new SimpleStringProperty(UUID.randomUUID().toString());
		  serverName = new SimpleStringProperty("");
		  serverType = new SimpleObjectProperty<ServerType>(null);
		  standalonePath = new SimpleStringProperty("");
		  serverPath = new SimpleStringProperty("");
		  adminPort = new SimpleStringProperty("");
		  debugPort = new SimpleStringProperty("");
		  portOffset = new SimpleStringProperty("");
		  filePathToDeploy = new SimpleObjectProperty<String[]>(null);
	 }

	 public String getServerID() {
		  return serverID.getValue();
	 }

	 public void setServerID(String serverID) {
		  this.serverID.setValue(serverID);
	 }

	 public String getServerName() {
		  return serverName.getValue();
	 }

	 public void setServerName(String serverName) {
		  this.serverName.setValue(serverName);
	 }

	 public ServerType getServerType() {
		  return serverType.getValue();
	 }

	 public void setServerType(ServerType serverType) {
		  this.serverType.setValue(serverType);
	 }

	 public String getStandalonePath() {
		  return standalonePath.getValue();
	 }

	 public void setStandalonePath(String standalonePath) {
		  this.standalonePath.setValue(standalonePath);
	 }

	 public String getServerPath() {
		  return serverPath.getValue();
	 }

	 public void setServerPath(String serverPath) {
		  this.serverPath.setValue(serverPath);
	 }

	 public String getAdminPort() {
		  return adminPort.getValue();
	 }

	 public void setAdminPort(String adminPort) {
		  this.adminPort.setValue(adminPort);
	 }

	 public String getDebugPort() {
		  return debugPort.getValue();
	 }

	 public void setDebugPort(String debugPort) {
		  this.debugPort.setValue(debugPort);
	 }

	 public String getPortOffset() {
		  return portOffset.getValue();
	 }

	 public void setPortOffset(String portOffset) {
		  this.portOffset.setValue(portOffset);
	 }

	 public String[] getFilePathToDeploy() {
		  return filePathToDeploy.getValue();
	 }

	 public void setFilePathToDeploy(String[] filePathToDeploy) {
		  this.filePathToDeploy.setValue(filePathToDeploy);
	 }

	 @Override
	 public String toString() {
		  return "Server{"
					 + "serverName='"
					 + serverName
					 + '\''
					 + ", serverType="
					 + serverType
					 + ", standalonePath='"
					 + standalonePath
					 + '\''
					 + ", serverPath='"
					 + serverPath
					 + '\''
					 + ", adminPort='"
					 + adminPort
					 + '\''
					 + ", debugPort='"
					 + debugPort
					 + '\''
					 + ", portOffset='"
					 + portOffset
					 + '\''
					 + ", filePathToDeploy="
					 + filePathToDeploy.getValue()
					 + '}';
	 }

}
