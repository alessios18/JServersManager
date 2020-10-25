package org.alessios18.jserversmanager;

import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.factory.ServerManagerFactory;

import java.util.HashMap;

public class ServerManagersContainer {
	 private HashMap<String, ServerManagerBase> managersContainer = new HashMap<>();

	 public HashMap<String, ServerManagerBase> getManagersContainer() {
		  return managersContainer;
	 }

	 public void setManagersContainer(HashMap<String, ServerManagerBase> managersContainer) {
		  this.managersContainer = managersContainer;
	 }

	 public ServerManagerBase getServerManager(Server server){
	 	 if(!managersContainer.containsKey(server.getServerID())){
			  managersContainer.put(server.getServerID(), ServerManagerFactory.getServerManager(server));
		 }
		  return managersContainer.get(server.getServerID());
	 }
}
