package org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.CustomProperty;
import org.alessios18.jserversmanager.baseobjects.serverdata.DeployFile;

import java.util.ArrayList;
import java.util.List;

public class JBossServerConfig extends ServerConfigBase {
  private final StringProperty standalonePath;
  private final StringProperty adminPort;
  private final StringProperty debugPort;
  private final StringProperty portOffset;
  private final StringProperty httpPort;
  private final StringProperty configDir;
  private List<DeployFile> filePathToDeploy;
  private List<CustomProperty> customProperties;

  public JBossServerConfig() {
    super();
    standalonePath = new SimpleStringProperty("");
    adminPort = new SimpleStringProperty("");
    debugPort = new SimpleStringProperty("");
    portOffset = new SimpleStringProperty("");
    configDir = new SimpleStringProperty("");
    httpPort = new SimpleStringProperty("");
    filePathToDeploy = new ArrayList<>();
    customProperties = new ArrayList<>();
  }

  public JBossServerConfig(String configName, ServerType type) {
    super(configName, type);
    standalonePath = new SimpleStringProperty("");
    adminPort = new SimpleStringProperty("");
    debugPort = new SimpleStringProperty("");
    portOffset = new SimpleStringProperty("");
    configDir = new SimpleStringProperty("");
    httpPort = new SimpleStringProperty("");
    filePathToDeploy = new ArrayList<>();
    customProperties = new ArrayList<>();
  }

  public String getStandalonePath() {
    return standalonePath.getValue();
  }

  public void setStandalonePath(String standalonePath) {
    this.standalonePath.setValue(standalonePath);
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

  public List<DeployFile> getFilePathToDeploy() {
    return filePathToDeploy;
  }

  public void setFilePathToDeploy(List<DeployFile> filePathToDeploy) {
    this.filePathToDeploy = filePathToDeploy;
  }

  public String getConfigDir() {
    return configDir.get();
  }

  public void setConfigDir(String configDir) {
    this.configDir.set(configDir);
  }

  public StringProperty configDirProperty() {
    return configDir;
  }

  public List<CustomProperty> getCustomProperties() {
    return customProperties;
  }

  public void setCustomProperties(List<CustomProperty> customProperties) {
    this.customProperties = customProperties;
  }

  public JBossServerConfig getCloneObject() {
    JBossServerConfig clone = new JBossServerConfig(this.getConfigName(), this.getConfigType());
    clone.setFromSameObject(this);
    return clone;
  }

  @Override
  public void setFromSameObject(ServerConfigBase obj) {
    super.setFromSameObject(obj);
    JBossServerConfig jboss = (JBossServerConfig) obj;
    this.standalonePath.setValue(jboss.standalonePath.getValue());
    this.adminPort.setValue(jboss.adminPort.getValue());
    this.debugPort.setValue(jboss.debugPort.getValue());
    this.portOffset.setValue(jboss.portOffset.getValue());
    this.httpPort.setValue(jboss.httpPort.getValue());
    this.configDir.setValue(jboss.configDir.getValue());
    this.customProperties.clear();
    this.customProperties.addAll(jboss.customProperties);
    this.filePathToDeploy.clear();
    this.filePathToDeploy.addAll(jboss.filePathToDeploy);
  }
}
