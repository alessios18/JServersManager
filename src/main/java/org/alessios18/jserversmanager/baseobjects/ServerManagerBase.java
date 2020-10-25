package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class ServerManagerBase {
	 private Server server;
	 private BufferedWriter writer;
	 private ServerStarter ss;
	 private boolean isServerRunning = false;

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

	 public void startServer(BufferedWriter writer) {
		  setWriter(writer);
		  startServer();
	 }
	 public void startServer() {
		  System.out.println("doing deploy...");
		  doDeploy();
		  ss = new ServerStarter(this, getServerStartCommand());
		  System.out.println("Starting server...");
		  ss.start();
		  isServerRunning = true;
	 }

	 abstract public String[] getServerStartCommand();

	 abstract public String[] getServerStopCommand();

	 abstract public void doDeploy();

	 abstract public String getServerBinPath();

	 public void stopServer(){
		  isServerRunning = false;
		  Executor executor = new Executor(getServerStopCommand());
		  try {
				executor.execute(this.getServerBinPath(), null);
		  } catch (IOException e) {
				ExceptionDialog.showException(e);
		  }
	 }
}
