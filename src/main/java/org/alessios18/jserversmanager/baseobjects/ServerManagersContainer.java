package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.baseobjects.factory.ServerManagerFactory;

import java.io.IOException;
import java.util.HashMap;

public class ServerManagersContainer {
	 private HashMap<String, ServerManagerBase> managersContainer = new HashMap<>();

	 public HashMap<String, ServerManagerBase> getManagersContainer() {
		  return managersContainer;
	 }

	 public void setManagersContainer(HashMap<String, ServerManagerBase> managersContainer) {
		  this.managersContainer = managersContainer;
	 }

	 public ServerManagerBase getServerManager(Server server) {
		  if (!managersContainer.containsKey(server.getServerID())) {
				managersContainer.put(server.getServerID(), ServerManagerFactory.getServerManager(server));
		  }
		  return managersContainer.get(server.getServerID());
	 }

	 public void stopAllServers() throws IOException, InterruptedException {
		  for (ServerManagerBase manager :
					 managersContainer.values()) {
				if (manager.isServerRunning()) {
					 manager.stopServer();
				}
		  }
	 }

	 public void forceQuit() {
		  for (ServerManagerBase manager :
					 managersContainer.values()) {
				manager.forceShutdown();
		  }
	 }
}
