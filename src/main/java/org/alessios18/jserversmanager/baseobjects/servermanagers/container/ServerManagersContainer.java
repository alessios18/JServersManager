package org.alessios18.jserversmanager.baseobjects.servermanagers.container;

import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.ServerConfigBase;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.servermanagers.factory.ServerManagerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ServerManagersContainer {
  private Map<String, ServerManagerBase> managersContainer = new HashMap<>();

  public Map<String, ServerManagerBase> getManagersContainer() {
    return managersContainer;
  }

  public void setManagersContainer(Map<String, ServerManagerBase> managersContainer) {
    this.managersContainer = managersContainer;
  }

  public ServerManagerBase getServerManager(Server server, String serverConfigId) {
    if (!managersContainer.containsKey(server.getServerID()+"&"+serverConfigId)) {
      managersContainer.put(
          server.getServerID()+"&"+serverConfigId, ServerManagerFactory.getServerManager(server, serverConfigId));
    }
    return managersContainer.get(server.getServerID()+"&"+serverConfigId);
  }

  public ServerManagerBase getServerManager(Server server, ServerConfigBase serverConfig) {
    return getServerManager(server, serverConfig.getConfigID());
  }

  public void stopAllServers() throws IOException, InterruptedException, ExecutionException {
    for (ServerManagerBase manager : managersContainer.values()) {
      if (manager.isServerRunning()) {
        manager.stopServer();
      }
    }
  }

  public void forceQuit() throws InterruptedException {
    for (ServerManagerBase manager : managersContainer.values()) {
      manager.forceShutdown();
    }
  }
}
