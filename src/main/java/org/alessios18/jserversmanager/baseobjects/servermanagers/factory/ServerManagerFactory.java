package org.alessios18.jserversmanager.baseobjects.servermanagers.factory;

import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerGrunt;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerJBOSS;

public class ServerManagerFactory {

	 private ServerManagerFactory() {
	 }

	 public static ServerManagerBase getServerManager(Server server) {
		  ServerManagerBase result = null;
		  if (server.getServerType().equals(ServerType.JBOSS)) {
				result = new ServerManagerJBOSS(server);
		  } else if (server.getServerType().equals(ServerType.GRUNT)) {
				result = new ServerManagerGrunt(server);
		  }
		  return result;
	 }
}
