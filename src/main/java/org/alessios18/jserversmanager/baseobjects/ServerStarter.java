package org.alessios18.jserversmanager.baseobjects;

import java.io.IOException;

public class ServerStarter extends Thread {
    String[] commands;
    ServerManagerBase serverManager;

    public ServerStarter(ServerManagerBase serverManager, String[] commands) {
        super(serverManager.getServer().getServerName());
        this.commands = commands;
        this.serverManager = serverManager;
    }

    @Override
    public void run() {
        super.run();
        Executor executor = new Executor(commands);
        try {
            executor.execute(serverManager.getServerBinPath(), serverManager.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
