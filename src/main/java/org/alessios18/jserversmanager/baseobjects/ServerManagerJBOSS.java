package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.util.OsCheck;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ServerManagerJBOSS extends ServerManagerBase {
	 protected static final String SERVER_START_IP_MASK = "-b=0.0.0.0";
	 protected static final String SERVER_START_BINDING_PORT_OFFSET = "-Djboss.socket.binding.port-offset=";
	 protected static final String SERVER_START_DEBUG_PORT = "--debug";
	 protected static final String SERVER_EXEC_COMMAND_LINUX = "./standalone.sh";
	 protected static final String SERVER_EXEC_COMMAND_WIN = "standalone.bat";
	 protected static final String SERVER_EXEC_COMMAND = OsCheck.getOperatingSystemType().equals(OsCheck.OSType.Linux) ? SERVER_EXEC_COMMAND_LINUX : SERVER_EXEC_COMMAND_WIN;
	 protected static final String SERVER_JBOSS_CLI_COMMAND_LINUX = "./jboss-cli.sh";
	 protected static final String SERVER_JBOSS_CLI_COMMAND_WIN = "jboss-cli.bat";
	 protected static final String SERVER_JBOSS_CLI_COMMAND = OsCheck.getOperatingSystemType().equals(OsCheck.OSType.Linux) ? SERVER_JBOSS_CLI_COMMAND_LINUX : SERVER_JBOSS_CLI_COMMAND_WIN;
	 protected static final String SERVER_STOP_ADDRESS_TO_CLI_COMMAND = "--controller=localhost:";
	 protected static final String SERVER_STOP_CONNECT_CLI_COMMAND = "--connect";
	 protected static final String SERVER_STOP_SHUTDOWN_COMMAND = "command=:shutdown";


	 public ServerManagerJBOSS(Server server) {
		  super(server);
	 }

	 @Override
	 public String[] getServerStartCommand() {
		  String[] commands = {
					 SERVER_EXEC_COMMAND,
					 SERVER_START_IP_MASK,
					 SERVER_START_BINDING_PORT_OFFSET + getServer().getPortOffset(),
					 SERVER_START_DEBUG_PORT, getServer().getDebugPort()
		  };
		  return commands;
	 }

	 @Override
	 public String[] getServerStopCommand() {
		  String[] commands = {
					 SERVER_JBOSS_CLI_COMMAND,
					 SERVER_STOP_ADDRESS_TO_CLI_COMMAND + getServer().getAdminPort(),
					 SERVER_STOP_CONNECT_CLI_COMMAND,
					 SERVER_STOP_SHUTDOWN_COMMAND
		  };
		  return commands;
	 }

	 @Override
	 public String getServerDeployDir() {
		  return getServer().getServerPath().endsWith(OsCheck.getSeparator()) ? getServer().getServerPath() + "standalone" + OsCheck.getSeparator() + "deployments" : getServer().getServerPath() + OsCheck.getSeparator() + "standalone" + OsCheck.getSeparator() + "deployments";
	 }

	 @Override
	 public void doDeploy() throws IOException {
		  File deployDir = new File(getServerDeployDir());
		  for (String fPath : getServer().getFilePathToDeploy()) {
				if (fPath != null && !fPath.isEmpty()) {
					 File toDeploy = new File(fPath);
					 if (toDeploy.isDirectory()) {
						  FileUtils.copyDirectory(toDeploy, deployDir, true);
					 } else {
						  FileUtils.copyFileToDirectory(toDeploy, deployDir, true);
					 }
				}
		  }
	 }

	 @Override
	 public String getServerBinPath() {
		  return getServer().getServerPath().endsWith(OsCheck.getSeparator()) ? getServer().getServerPath() + "bin" : getServer().getServerPath() + OsCheck.getSeparator() + "bin";
	 }

}
