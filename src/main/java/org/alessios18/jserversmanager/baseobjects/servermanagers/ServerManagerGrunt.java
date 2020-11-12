package org.alessios18.jserversmanager.baseobjects.servermanagers;

import org.alessios18.jserversmanager.baseobjects.serverdata.Server;

import java.io.IOException;

public class ServerManagerGrunt extends ServerManagerBase {
  public ServerManagerGrunt(Server server, String serverConfigId) {
    super(server, serverConfigId);
  }

  @Override
  public String[] getServerStartCommand() {
    return new String[0];
  }

  @Override
  public String[] getServerStopCommand() {
    return new String[0];
  }

  @Override
  public String getServerDeployDir() {
    return null;
  }

  @Override
  public String getServerConfigDir() {
    return null;
  }

  @Override
  public void doDeploy() throws IOException {}

  @Override
  public String getServerBinPath() {
    return null;
  }

  @Override
  void copyStandaloneFile() throws IOException {}

  @Override
  void copyConfigFiles() throws IOException {}
}
