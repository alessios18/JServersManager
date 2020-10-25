package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.util.OsCheck;

import java.io.BufferedWriter;

public class ServerManagerJBOSS extends ServerManagerBase {
    protected static String SERVER_IP_MASK = "-b=0.0.0.0";
    protected static String SERVER_BINDING_PORT_OFFSET = "-Djboss.socket.binding.port-offset=";
    protected static String SERVER_DEBUG_PORT = "--debug";
    protected static String SERVER_EXEC_COMMAND_LINUX = "./standalone.sh";


    public ServerManagerJBOSS(Server server, BufferedWriter writer) {
        super(server, writer);
    }

    @Override
    public String[] getServerStartCommand() {
        String[] commands = {
                SERVER_EXEC_COMMAND_LINUX,
                SERVER_IP_MASK,
                SERVER_BINDING_PORT_OFFSET + getServer().getPortOffset(),
                SERVER_DEBUG_PORT, getServer().getDebugPort()
        };
        return commands;
    }

    @Override
    public void doDeploy() {

    }

    @Override
    public String getServerBinPath() {
        return getServer().getServerPath().endsWith(OsCheck.getSeparator()) ? getServer().getServerPath() + "bin" : getServer().getServerPath() + OsCheck.getSeparator() + "bin";
    }
}
