package org.alessios18.jserversmanager.baseobjects.factory;

import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.ServerManagerJBOSS;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;

import java.io.BufferedWriter;

import static org.alessios18.jserversmanager.baseobjects.enums.ServerType.JBOSS;

public class ServerManagerFactory {

    public static ServerManagerBase getServerManager(Server server, BufferedWriter writer){
        switch(server.getServerType()){
            case JBOSS: return new ServerManagerJBOSS(server,writer);
            default : return null;
        }
    }
}
