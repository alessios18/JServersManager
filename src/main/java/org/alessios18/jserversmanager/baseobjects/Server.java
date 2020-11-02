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
	 private final StringProperty httpPort;
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
		  httpPort = new SimpleStringProperty("");
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

	 public String getHttpPort() {
		  return httpPort.getValue();
	 }

	 public void setHttpPort(String httpPort) {
		  this.httpPort.setValue(httpPort);
	 }

	 public String[] getFilePathToDeploy() {
		  return filePathToDeploy.getValue();
	 }

	 public void setFilePathToDeploy(String[] filePathToDeploy) {
		  this.filePathToDeploy.setValue(filePathToDeploy);
	 }

	 public Server getCloneObject() {
		  Server clone = new Server();
		  clone.serverID.setValue("CLONE-" + this.serverID.getValue());
		  clone.serverName.setValue(this.serverName.getValue());
		  clone.serverType.setValue(this.serverType.getValue());
		  clone.standalonePath.setValue(this.standalonePath.getValue());
		  clone.serverPath.setValue(this.serverPath.getValue());
		  clone.adminPort.setValue(this.adminPort.getValue());
		  clone.debugPort.setValue(this.debugPort.getValue());
		  clone.portOffset.setValue(this.portOffset.getValue());
		  clone.httpPort.setValue(this.httpPort.getValue());
		  clone.filePathToDeploy.setValue(this.filePathToDeploy.getValue());
		  return clone;
	 }

	 public void setFromClone(Server clone) {
		  this.serverName.setValue(clone.serverName.getValue());
		  this.serverType.setValue(clone.serverType.getValue());
		  this.standalonePath.setValue(clone.standalonePath.getValue());
		  this.serverPath.setValue(clone.serverPath.getValue());
		  this.adminPort.setValue(clone.adminPort.getValue());
		  this.debugPort.setValue(clone.debugPort.getValue());
		  this.portOffset.setValue(clone.portOffset.getValue());
		  this.httpPort.setValue(clone.httpPort.getValue());
		  this.filePathToDeploy.setValue(clone.filePathToDeploy.getValue());
	 }
}
