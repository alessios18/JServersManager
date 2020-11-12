package org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig;

import org.alessios18.jserversmanager.baseobjects.enums.ServerType;

public class GruntServerConfig extends ServerConfigBase {

  public GruntServerConfig() {
    super();
  }

  public GruntServerConfig(String configName, ServerType type) {
    super(configName, type);
  }

  @Override
  public GruntServerConfig getCloneObject() {
    GruntServerConfig clone = new GruntServerConfig(this.getConfigName(), this.getConfigType());
    clone.setFromSameObject(this);
    return clone;
  }

  public String getConfigName() {
    return super.getConfigName();
  }

  public void setConfigName(String configName) {
    super.setConfigName(configName);
  }

  public ServerType getConfigType() {
    return super.getConfigType();
  }

  public void setConfigType(ServerType configType) {
    super.setConfigType(configType);
  }

  public boolean isCustomArgs() {
    return super.isCustomArgs();
  }

  public void setCustomArgs(boolean isCustomArgs) {
    super.setCustomArgs(isCustomArgs);
  }

  public String getCustomArgsValue() {
    return super.getCustomArgsValue();
  }

  public void setCustomArgsValue(String customArgs) {
    super.setCustomArgsValue(customArgs);
  }

  public String getServerPath() {
    return super.getServerPath();
  }

  public void setServerPath(String serverPath) {
    super.setServerPath(serverPath);
  }
}
