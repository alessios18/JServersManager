package org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.factory;

import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.GruntServerConfig;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.JBossServerConfig;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.ServerConfigBase;

public class FactoryServerConfig {

  public static ServerConfigBase getNewServerConfig(String configName, ServerType type) {
    ServerConfigBase sc = null;
    if (type.equals(ServerType.JBOSS)) {
      sc = new JBossServerConfig(configName, type);
    } else if (type.equals(ServerType.GRUNT)) {
      sc = new GruntServerConfig(configName, type);
    }
    return sc;
  }
}
