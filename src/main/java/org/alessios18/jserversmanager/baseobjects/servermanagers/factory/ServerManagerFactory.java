package org.alessios18.jserversmanager.baseobjects.servermanagers.factory;

import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerGrunt;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerJBOSS;

public class ServerManagerFactory {

	 public static ServerManagerBase getServerManager(Server server) {
		  ServerManagerBase result = null;
		  switch (server.getServerType()) {
				case JBOSS:
					 result = new ServerManagerJBOSS(server);
					 break;
				case GRUNT:
					 result = new ServerManagerGrunt(server);
					 break;
		  }
		  return result;
	 }
}
