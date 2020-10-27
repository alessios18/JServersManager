package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

import java.io.IOException;

public class ServerStarter extends Thread {
	 String[] commands;
	 ServerManagerBase serverManager;
	 Executor executor;

	 public ServerStarter(ServerManagerBase serverManager, String[] commands) {
		  super(serverManager.getServer().getServerName());
		  this.commands = commands;
		  this.serverManager = serverManager;
	 }

	 @Override
	 public void run() {
		  super.run();
		  executor = new Executor(commands);
		  try {
				executor.execute(serverManager.getServerBinPath(), serverManager.getWriter());
		  } catch (IOException | InterruptedException e) {
				e.printStackTrace();

		  }
	 }

	 public void forceProcessExit() {
		  if (executor != null) {
				executor.forceQuit();
		  }
	 }
}
