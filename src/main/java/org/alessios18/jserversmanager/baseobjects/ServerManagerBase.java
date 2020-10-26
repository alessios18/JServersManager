package org.alessios18.jserversmanager.baseobjects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class ServerManagerBase {
	 private Server server;
	 private BufferedWriter writer;
	 private ServerStarter ss;
	 private boolean isServerRunning = false;
	 private Executor stopExecutor;

	 public ServerManagerBase(Server server) {
		  this.server = server;
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

	 public void startServer(BufferedWriter writer) throws IOException {
		  setWriter(writer);
		  startServer();
	 }

	 public void startServer() throws IOException {
		  doUnDeploy();
		  doDeploy();
		  ss = new ServerStarter(this, getServerStartCommand());
		  ss.start();
		  isServerRunning = true;
	 }

	 abstract public String[] getServerStartCommand();

	 abstract public String[] getServerStopCommand();

	 abstract public String getServerDeployDir();

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

	 public void stopServer() throws IOException, InterruptedException {
		  isServerRunning = false;
		  stopExecutor = new Executor(getServerStopCommand());
		  stopExecutor.execute(this.getServerBinPath(), null);
		  ss.join();
	 }

	 public void forceShutdown() {
		  ss.forceProcessExit();
		  if (stopExecutor != null) {
				stopExecutor.forceQuit();
		  }
	 }
}
