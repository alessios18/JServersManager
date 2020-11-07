package org.alessios18.jserversmanager.baseobjects.servermanagers;

import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.ProcessManager;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public abstract class ServerManagerBase {
	 public static final Logger logger = JServersManagerApp.getLogger();

	 private final ProcessManager processManager;
	 private Server server;
	 private BufferedWriter writer;
	 private boolean isServerRunning = false;

	 public ServerManagerBase(Server server) {
		  this.server = server;
		  processManager = new ProcessManager();
	 }

	 public Server getServer() {
		  return server;
	 }

	 public void setServer(Server server) {
		  this.server = server;
	 }

	 public BufferedWriter getWriter() {
		  return writer;
	 }

	 public void setWriter(BufferedWriter writer) {
		  this.writer = writer;
	 }

	 public boolean isServerRunning() {
		  return isServerRunning;
	 }

	 public void startServer(BufferedWriter writer) throws Exception {
		  setWriter(writer);
		  startServer();
	 }

	 public void startServer() throws Exception {
		  doUnDeploy();
		  copyStandaloneFile();
		  doDeploy();
		  String commands[] = server.isCustomArgs()?getServerCustomArguments():getServerStartCommand();
		  processManager.executeParallelProcess(commands, this.getServerBinPath(), writer, false);
		  isServerRunning = true;
	 }

	 abstract public String[] getServerStartCommand();

	 abstract public String[] getServerStopCommand();

	 abstract public String getServerDeployDir();

	 abstract public String getServerConfigDir();

	 abstract public void doDeploy() throws IOException;

	 public String[] getServerCustomArguments(){
	 	 String custom = server.getCustomArgsValue();
	 	 return custom.trim().split(" +");
	 }

	 public void doUnDeploy() throws IOException {
		  File deployDir = new File(getServerDeployDir());
		  if (deployDir.isDirectory() && deployDir.listFiles().length > 0) {
				for (File f : deployDir.listFiles()) {
					 Files.delete(Paths.get(f.toURI()));
				}
		  }
		  logger.debug("[" + getServer().getServerName() + "] Undeploy:DONE");
	 }

	 abstract public String getServerBinPath();

	 public void stopServer() throws IOException, InterruptedException, ExecutionException {
		  isServerRunning = false;
		  if(OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows) ){
				processManager.forceQuit();
		  }else{
				processManager.executeParallelProcess(getServerStopCommand(), this.getServerBinPath(), null, true);
		  }
	 }

	 public void restartServer() throws Exception {
	 	 stopServer();
	 	 startServer();
	 }

	 abstract void copyStandaloneFile() throws IOException;
	 abstract void copyConfigFiles() throws IOException;

	 public String getPortWithOffset(String port) {
	 	 if(port != null) {
			  return "" + (Integer.parseInt(port) + Integer.parseInt((server.getPortOffset() != null && !server.getPortOffset().isEmpty() )? server.getPortOffset() : "0"));
		 }
	 	 return "0";
	 }

	 public void forceShutdown() throws InterruptedException {
		  if (processManager != null) {
				processManager.forceQuit();
		  }
	 }

	 public String getServerParameters() {
		  StringBuilder sb = new StringBuilder();
		  for (String c : getServerStartCommand()) {
				sb.append(c + " ");
		  }
		  return sb.toString().trim();
	 }
}
