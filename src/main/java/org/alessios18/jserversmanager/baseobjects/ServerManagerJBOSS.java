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
	 protected static final String SERVER_DEPLOY_DIR = "standalone" + OsCheck.getSeparator() + "deployments";
	 protected static final String SERVER_CONFIG_DIR = "standalone" + OsCheck.getSeparator() + "configuration";


	 public ServerManagerJBOSS(Server server) {
		  super(server);
	 }

	 @Override
	 public String[] getServerStartCommand() {
		  String[] commands = new String[0];
		  if (OsCheck.getOperatingSystemType().equals(OsCheck.OSType.Linux)) {
				commands = new String[]{
						  SERVER_EXEC_COMMAND,
						  SERVER_START_IP_MASK,
						  SERVER_START_BINDING_PORT_OFFSET + getServer().getPortOffset(),
						  SERVER_START_DEBUG_PORT, getServer().getDebugPort()
				};
		  } else if (OsCheck.getOperatingSystemType().equals(OsCheck.OSType.Windows)) {
				commands = new String[]{
						  "CMD",
						  "/C",
						  SERVER_EXEC_COMMAND,
						  SERVER_START_IP_MASK,
						  SERVER_START_BINDING_PORT_OFFSET + getServer().getPortOffset(),
						  SERVER_START_DEBUG_PORT, getServer().getDebugPort()
				};
		  }
		  return commands;
	 }

	 @Override
	 public String[] getServerStopCommand() {
		  String[] commands = new String[0];
		  if (OsCheck.getOperatingSystemType().equals(OsCheck.OSType.Linux)) {
				commands = new String[]{
						  SERVER_JBOSS_CLI_COMMAND,
						  SERVER_STOP_ADDRESS_TO_CLI_COMMAND + getPortWithOffset(getServer().getAdminPort()),
						  SERVER_STOP_CONNECT_CLI_COMMAND,
						  SERVER_STOP_SHUTDOWN_COMMAND
				};
		  } else if (OsCheck.getOperatingSystemType().equals(OsCheck.OSType.Windows)) {
				commands = new String[]{
						  "CMD",
						  "/C",
						  SERVER_JBOSS_CLI_COMMAND,
						  SERVER_STOP_ADDRESS_TO_CLI_COMMAND + getPortWithOffset(getServer().getAdminPort()),
						  SERVER_STOP_CONNECT_CLI_COMMAND,
						  SERVER_STOP_SHUTDOWN_COMMAND
				};
		  }
		  return commands;
	 }

	 @Override
	 public String getServerDeployDir() {
		  return getServer().getServerPath().endsWith(OsCheck.getSeparator()) ? getServer().getServerPath() + SERVER_DEPLOY_DIR : getServer().getServerPath() + OsCheck.getSeparator() + SERVER_DEPLOY_DIR;
	 }

	 @Override
	 public String getServerConfigDir() {
		  return getServer().getServerPath().endsWith(OsCheck.getSeparator()) ? getServer().getServerPath() + SERVER_CONFIG_DIR : getServer().getServerPath() + OsCheck.getSeparator() + SERVER_CONFIG_DIR;
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

	 @Override
	 void copyConfig() throws IOException {
		  File config = new File(getServer().getStandalonePath());
		  File srvConfig = new File(getServerConfigDir());
		  if (config.exists() && srvConfig.exists()) {
				if (config.isDirectory()) {
					 FileUtils.copyDirectory(config, srvConfig, true);
				} else {
					 FileUtils.copyFileToDirectory(config, srvConfig, true);
				}
		  }
	 }

}
