package org.alessios18.jserversmanager.baseobjects.servermanagers;

import org.alessios18.jserversmanager.baseobjects.serverdata.CustomProperty;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.commons.io.FileUtils;

import javax.swing.event.ListDataEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerManagerJBOSS extends ServerManagerBase {

	 public static final String CMD = "CMD";
	 public static final String CMD_C = "/C";
	 protected static final String SERVER_START_IP_MASK = "-b=0.0.0.0";
	 protected static final String SERVER_START_BINDING_PORT_OFFSET = "-Djboss.socket.binding.port-offset=";
	 protected static final String SERVER_START_DEBUG_PORT = "--debug";
	 protected static final String SERVER_START_ADMIN_PORT = "-Djboss.management.http.port=";
	 protected static final String SERVER_START_HTTP_PORT = "-Djboss.http.port=";
	 protected static final String SERVER_START_CONFIG_DIR = "-Djboss.server.config.dir=";
	 protected static final String SERVER_EXEC_COMMAND_LINUX = "./standalone.sh";
	 protected static final String SERVER_EXEC_COMMAND_WIN = "standalone.bat";
	 protected static final String SERVER_EXEC_COMMAND = OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Linux) ? SERVER_EXEC_COMMAND_LINUX : SERVER_EXEC_COMMAND_WIN;
	 protected static final String SERVER_JBOSS_CLI_COMMAND_LINUX = "./jboss-cli.sh";
	 protected static final String SERVER_JBOSS_CLI_COMMAND_WIN = "jboss-cli.bat";
	 protected static final String SERVER_JBOSS_CLI_COMMAND = OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Linux) ? SERVER_JBOSS_CLI_COMMAND_LINUX : SERVER_JBOSS_CLI_COMMAND_WIN;
	 protected static final String SERVER_STOP_ADDRESS_TO_CLI_COMMAND = "--controller=localhost:";
	 protected static final String SERVER_STOP_CONNECT_CLI_COMMAND = "--connect";
	 protected static final String SERVER_STOP_SHUTDOWN_COMMAND = "command=:shutdown";
	 protected static final String SERVER_DEPLOY_DIR = "standalone" + OsUtils.getSeparator() + "deployments";
	 protected static final String SERVER_CONFIG_DIR = "standalone" + OsUtils.getSeparator() + "configuration";
	 protected static final String SERVER_START_COMMAND_CONFIG_FILE = "--server-config=";

	 public ServerManagerJBOSS(Server server) {
		  super(server);
	 }

	 @Override
	 public String[] getServerStartCommand() {

		  ArrayList<String> commandsList = new ArrayList<>();
		  if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
				commandsList.add(CMD);
				commandsList.add(CMD_C);
		  }
		  commandsList.add(SERVER_EXEC_COMMAND);
		  commandsList.add(SERVER_START_IP_MASK);
		  if (getServer().getStandalonePath() != null && !getServer().getStandalonePath().isEmpty()) {
				File tmp = new File(getServer().getStandalonePath());
				commandsList.add(SERVER_START_COMMAND_CONFIG_FILE + tmp.getName());
		  }
		  if (getServer().getPortOffset() != null && !getServer().getPortOffset().isEmpty()) {
				commandsList.add(SERVER_START_BINDING_PORT_OFFSET + getServer().getPortOffset());
		  }
		  if (getServer().getDebugPort() != null && !getServer().getDebugPort().isEmpty()) {
				commandsList.add(SERVER_START_DEBUG_PORT);
				commandsList.add(getServer().getDebugPort());
		  }
		  if (getServer().getAdminPort() != null && !getServer().getAdminPort().isEmpty()) {
				commandsList.add(SERVER_START_ADMIN_PORT + getServer().getAdminPort());
		  }
		  if (getServer().getHttpPort() != null && !getServer().getHttpPort().isEmpty()) {
				commandsList.add(SERVER_START_HTTP_PORT + getServer().getHttpPort());
		  }
		  if(getServer().getConfigDir() != null && !getServer().getConfigDir().isEmpty()){
				commandsList.add(SERVER_START_CONFIG_DIR + getServer().getConfigDir());
		  }
		  if(!getServer().getCustomProperties().isEmpty()){
		  	 commandsList.addAll(getCustomProperties());
		  }
		  return commandsList.toArray(new String[0]);
	 }

	 @Override
	 public String[] getServerStopCommand() {
		  ArrayList<String> commandsList = new ArrayList<>();
		  if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
				commandsList.add(CMD);
				commandsList.add(CMD_C);
		  }
		  commandsList.add(SERVER_JBOSS_CLI_COMMAND);
		  commandsList.add(SERVER_STOP_ADDRESS_TO_CLI_COMMAND + getPortWithOffset(getServer().getAdminPort()));
		  commandsList.add(SERVER_STOP_CONNECT_CLI_COMMAND);
		  commandsList.add(SERVER_STOP_SHUTDOWN_COMMAND);
		  return commandsList.toArray(new String[0]);
	 }

	 @Override
	 public String[] getServerCustomArguments(){
		  ArrayList<String> commandsList = new ArrayList<>();
		  if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
				commandsList.add(CMD);
				commandsList.add(CMD_C);
		  }
		  commandsList.add(SERVER_EXEC_COMMAND);
		  for(String c:super.getServerCustomArguments()){
		  	 commandsList.add(c);
		  }
		  return commandsList.toArray(new String[0]);
	 }
	 @Override
	 public String getServerDeployDir() {
		  return getServer().getServerPath().endsWith(OsUtils.getSeparator()) ? getServer().getServerPath() + SERVER_DEPLOY_DIR : getServer().getServerPath() + OsUtils.getSeparator() + SERVER_DEPLOY_DIR;
	 }

	 @Override
	 public String getServerConfigDir() {
		  return getServer().getServerPath().endsWith(OsUtils.getSeparator()) ? getServer().getServerPath() + SERVER_CONFIG_DIR : getServer().getServerPath() + OsUtils.getSeparator() + SERVER_CONFIG_DIR;
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
		  logger.debug("[" + getServer().getServerName() + "] Deploy:DONE");
	 }

	 @Override
	 public String getServerBinPath() {
		  return getServer().getServerPath().endsWith(OsUtils.getSeparator()) ? getServer().getServerPath() + "bin" : getServer().getServerPath() + OsUtils.getSeparator() + "bin";
	 }

	 @Override
	 void copyStandaloneFile() throws IOException {
		  File config = new File(getServer().getStandalonePath());
		  File srvConfig = new File(getServerConfigDir());
		  if (config.exists() && srvConfig.exists()) {
				if (config.isDirectory()) {
					 FileUtils.copyDirectory(config, srvConfig, true);
				} else {
					 FileUtils.copyFileToDirectory(config, srvConfig, true);
				}
		  }
		  logger.debug("[" + getServer().getServerName() + "] Standalone file copy:DONE");
	 }

	 @Override
	 public String getServerParameters() {
		  StringBuilder sb = new StringBuilder();
		  for (String c : getServerStartCommand()) {
				if (!c.equals(SERVER_EXEC_COMMAND) && !c.equals(CMD) && !c.equals(CMD_C)) {
					 sb.append(c + " ");
				}
		  }
		  return sb.toString().trim();
	 }

	 protected List<String> getCustomProperties(){
		  List<String> out = new ArrayList<>();
	 	 for(CustomProperty p:getServer().getCustomProperties()){
	 	 	 if(p.getPropertyName().startsWith("-D")){
	 	 	 	 out.add(p.getPropertyName()+"="+p.getPropertyValue());
			 }else{
				  out.add("-D"+p.getPropertyName()+"="+p.getPropertyValue());
			 }
		 }
	 	 return out;
	 }
}
