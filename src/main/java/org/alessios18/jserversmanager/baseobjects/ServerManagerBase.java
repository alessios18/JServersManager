package org.alessios18.jserversmanager.baseobjects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public abstract class ServerManagerBase {
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
		  copyStandaloneFile();
		  doUnDeploy();
		  doDeploy();
		  processManager.executeParallelProcess(getServerStartCommand(), this.getServerBinPath(), writer, false);
		  isServerRunning = true;
	 }

	 abstract public String[] getServerStartCommand();

	 abstract public String[] getServerStopCommand();

	 abstract public String getServerDeployDir();

	 abstract public String getServerConfigDir();

	 abstract public void doDeploy() throws IOException;

	 public void doUnDeploy() throws IOException {
		  File deployDir = new File(getServerDeployDir());
		  if (deployDir.isDirectory() && deployDir.listFiles().length > 0) {
				for (File f : deployDir.listFiles()) {
					 Files.delete(Paths.get(f.toURI()));
				}
		  }
	 }

	 abstract public String getServerBinPath();

	 public void stopServer() throws IOException, InterruptedException, ExecutionException {
		  isServerRunning = false;
		  processManager.executeParallelProcess(getServerStopCommand(), this.getServerBinPath(), null, true);
	 }

	 abstract void copyStandaloneFile() throws IOException;

	 public int getPortWithOffset(String port) {
		  return Integer.parseInt(port) + Integer.parseInt(server.getPortOffset());
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
