package org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig;

import javafx.beans.property.*;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.interfaces.Cloneable;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.UUID;

@XmlSeeAlso({JBossServerConfig.class, GruntServerConfig.class})
public abstract class ServerConfigBase implements Cloneable<ServerConfigBase> {
  private final StringProperty configID;
  private final StringProperty configName;
  private final StringProperty serverPath;
  private final ObjectProperty<ServerType> configType;
  private final BooleanProperty customArgs;
  private final StringProperty customArgsValue;

  public ServerConfigBase() {
    configID = new SimpleStringProperty("");
    this.configName = new SimpleStringProperty("");
    configType = new SimpleObjectProperty<>(null);
    serverPath = new SimpleStringProperty("");
    customArgs = new SimpleBooleanProperty(false);
    customArgsValue = new SimpleStringProperty();
  }

  public ServerConfigBase(String configName, ServerType type) {
    configID = new SimpleStringProperty(UUID.randomUUID().toString());
    this.configName = new SimpleStringProperty(configName);
    configType = new SimpleObjectProperty<>(type);
    serverPath = new SimpleStringProperty("");
    customArgs = new SimpleBooleanProperty(false);
    customArgsValue = new SimpleStringProperty();
  }

  public String getConfigID() {
    return configID.getValue();
  }

  public void setConfigID(String serverID) {
    this.configID.setValue(serverID);
  }

  public String getConfigName() {
    return configName.get();
  }

  public void setConfigName(String configName) {
    this.configName.set(configName);
  }

  public StringProperty configNameProperty() {
    return configName;
  }

  public ServerType getConfigType() {
    return configType.getValue();
  }

  public void setConfigType(ServerType configType) {
    this.configType.setValue(configType);
  }

  public boolean isCustomArgs() {
    return customArgs.get();
  }

  public void setCustomArgs(boolean isCustomArgs) {
    customArgs.set(isCustomArgs);
  }

  public String getCustomArgsValue() {
    return customArgsValue.getValue();
  }

  public void setCustomArgsValue(String customArgs) {
    this.customArgsValue.setValue(customArgs);
  }

  public String getServerPath() {
    return serverPath.getValue();
  }

  public void setServerPath(String serverPath) {
    this.serverPath.setValue(serverPath);
  }

  @Override
  public void setFromSameObject(ServerConfigBase clone) {
    this.setConfigName(clone.getConfigName());
    this.setConfigType(clone.getConfigType());
    this.customArgs.setValue(clone.customArgs.getValue());
    this.customArgsValue.setValue(clone.customArgsValue.getValue());
    this.serverPath.setValue(clone.serverPath.getValue());
  }

  @Override
  public String toString() {
    return getConfigName();
  }
}
