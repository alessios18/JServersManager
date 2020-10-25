package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.util.OsCheck;

import java.io.BufferedWriter;

public class ServerManagerJBOSS extends ServerManagerBase {
	 protected static final String SERVER_START_IP_MASK = "-b=0.0.0.0";
	 protected static final String SERVER_START_BINDING_PORT_OFFSET = "-Djboss.socket.binding.port-offset=";
	 protected static final String SERVER_START_DEBUG_PORT = "--debug";
	 protected static final String SERVER_EXEC_COMMAND_LINUX = "./standalone.sh";
	 protected static final String SERVER_JBOSS_CLI_COMMAND_LINUX = "./jboss-cli.sh";
	 protected static final String SERVER_STOP_ADDRESS_TO_CLI_COMMAND_LINUX = "--controller=localhost:";//add admin port;
	 protected static final String SERVER_STOP_CONNECT_CLI_COMMAND_LINUX = "--connect";
	 protected static final String SERVER_STOP_SHUTDOWN_COMMAND_LINUX = "command=:shutdown";


	 public ServerManagerJBOSS(Server server) {
		  super(server);
	 }

	 @Override
	 public String[] getServerStartCommand() {
		  String[] commands = {
					 SERVER_EXEC_COMMAND_LINUX,
					 SERVER_START_IP_MASK,
					 SERVER_START_BINDING_PORT_OFFSET + getServer().getPortOffset(),
					 SERVER_START_DEBUG_PORT, getServer().getDebugPort()
		  };
		  return commands;
	 }

	 @Override
	 public String[] getServerStopCommand() {
		  String[] commands = {
					 SERVER_JBOSS_CLI_COMMAND_LINUX,
					 SERVER_STOP_ADDRESS_TO_CLI_COMMAND_LINUX+getServer().getAdminPort(),
					 SERVER_STOP_CONNECT_CLI_COMMAND_LINUX,
					 SERVER_STOP_SHUTDOWN_COMMAND_LINUX
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
