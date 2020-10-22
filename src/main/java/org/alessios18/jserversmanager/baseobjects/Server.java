package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.baseobjects.enums.ServerType;

import java.util.Arrays;

public class Server {

  private String serverName;
  private ServerType serverType;
  private String standalonePath;
  private String serverPath;
  private String adminPort;
  private String debugPort;
  private String portOffset;
  private String[] filePathToDeploy;
  public Server() {}

  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public ServerType getServerType() {
    return serverType;
  }

  public void setServerType(ServerType serverType) {
    this.serverType = serverType;
  }

  public String getStandalonePath() {
    return standalonePath;
  }

  public void setStandalonePath(String standalonePath) {
    this.standalonePath = standalonePath;
  }

  public String getServerPath() {
    return serverPath;
  }

  public void setServerPath(String serverPath) {
    this.serverPath = serverPath;
  }

  public String getAdminPort() {
    return adminPort;
  }

  public void setAdminPort(String adminPort) {
    this.adminPort = adminPort;
  }

  public String getDebugPort() {
    return debugPort;
  }

  public void setDebugPort(String debugPort) {
    this.debugPort = debugPort;
  }

  public String getPortOffset() {
    return portOffset;
  }

  public void setPortOffset(String portOffset) {
    this.portOffset = portOffset;
  }

  public String[] getFilePathToDeploy() {
    return filePathToDeploy;
  }

  public void setFilePathToDeploy(String[] filePathToDeploy) {
    this.filePathToDeploy = filePathToDeploy;
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
        + Arrays.toString(filePathToDeploy)
        + '}';
  }

}
