package org.alessios18.jserversmanager.baseobjects;

import java.io.BufferedWriter;

public abstract class ServerManagerBase {
    private Server server;
    private BufferedWriter writer;
    private ServerStarter ss;

    public ServerManagerBase(Server server, BufferedWriter writer) {
        this.server = server;
        this.writer = writer;
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

    public void startServer(){
        System.out.println("doing deploy...");
        doDeploy();
        ss = new ServerStarter(this,getServerStartCommand());
        System.out.println("Starting server...");
        ss.start();
    }

    abstract public String[] getServerStartCommand();
    abstract public void doDeploy();
    abstract public String getServerBinPath();
}
