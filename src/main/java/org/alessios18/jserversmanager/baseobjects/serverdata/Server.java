package org.alessios18.jserversmanager.baseobjects.serverdata;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.ServerConfigBase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server {

  private final StringProperty serverID;
  private final StringProperty serverName;
  private final ObjectProperty<ServerType> serverType;
  private List<ServerConfigBase> serverConfigs;

  public Server() {
    serverID = new SimpleStringProperty(UUID.randomUUID().toString());
    serverName = new SimpleStringProperty("");
    serverType = new SimpleObjectProperty<>(null);
    serverConfigs = new ArrayList<>();
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

  public List<ServerConfigBase> getServerConfigs() {
    return serverConfigs;
  }

  public void setServerConfigs(List<ServerConfigBase> serverConfigs) {
    this.serverConfigs = serverConfigs;
  }

  public Server getCloneObject() {
    Server clone = new Server();
    clone.setFromServer(this);
    clone.serverID.setValue("CLONE-" + this.serverID.getValue());
    clone.setServerConfigs(getClonedConfigs(this));
    return clone;
  }

  public void setFromServer(Server clone) {
    this.serverName.setValue(clone.serverName.getValue());
    this.setServerType(clone.getServerType());
    this.serverConfigs.clear();
    this.serverConfigs.addAll(clone.serverConfigs);
  }

  public ServerConfigBase getServerConfigBaseById(String id) {
    for (ServerConfigBase sc : serverConfigs) {
      if (sc.getConfigID().equals(id)) {
        return sc;
      }
    }
    return null;
  }

  public List<ServerConfigBase> getClonedConfigs(Server server) {
    List<ServerConfigBase> list = new ArrayList<>();
    for (ServerConfigBase sc : server.getServerConfigs()) {
      list.add(sc.getCloneObject());
    }
    return list;
  }

  public List<ServerConfigBase> getServerConfigByType(ServerType type) {
    List<ServerConfigBase> list = new ArrayList<>();
    for (ServerConfigBase sc : this.getServerConfigs()) {
      if (sc.getConfigType().equals(type)) {
        list.add(sc);
      }
    }
    return list;
  }

  public ServerConfigBase getServerConfigById(String id) {
    for (ServerConfigBase sc : this.getServerConfigs()) {
      if (sc.getConfigID().equals(id)) {
        return sc;
      }
    }
    return null;
  }
}
