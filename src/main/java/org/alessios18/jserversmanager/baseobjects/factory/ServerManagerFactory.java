package org.alessios18.jserversmanager.baseobjects.factory;

import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.ServerManagerJBOSS;

import java.io.BufferedWriter;

public class ServerManagerFactory {

	 public static ServerManagerBase getServerManager(Server server) {
		  switch (server.getServerType()) {
				case JBOSS:
					 return new ServerManagerJBOSS(server);
				default:
					 return null;
		  }
	 }
}
