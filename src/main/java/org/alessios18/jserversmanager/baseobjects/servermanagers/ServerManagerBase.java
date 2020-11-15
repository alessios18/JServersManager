package org.alessios18.jserversmanager.baseobjects.servermanagers;

import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.processes.ProcessManager;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.ServerConfigBase;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.DirectoryScanner;

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
  private String serverConfigId;
  private ServerConfigBase serverConfig;

  public ServerManagerBase(Server server, String serverConfigId) {
    this.server = server;
    processManager = new ProcessManager();
    this.serverConfigId = serverConfigId;
    serverConfig = server.getServerConfigBaseById(this.serverConfigId);
  }

  public abstract String[] getServerStartCommand();

  public abstract String[] getServerStopCommand();

  public abstract String getServerDeployDir();

  public abstract String getServerConfigDir();

  public abstract void doDeploy() throws IOException;

  public abstract String getServerBinPath();

  abstract void copyStandaloneFile() throws IOException;

  abstract void copyConfigFiles() throws IOException;

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

  public void startServer(BufferedWriter writer)
      throws IOException, ExecutionException, InterruptedException {
    setWriter(writer);
    startServer();
  }

  public void startServer() throws IOException, ExecutionException, InterruptedException {
    JServersManagerApp.getLogger().debug("Starting server with configuration: "+getServerConfig(ServerConfigBase.class).getConfigName());
    doUnDeploy();
    copyStandaloneFile();
    doDeploy();
    String[] commands =
        getServerConfig(ServerConfigBase.class).isCustomArgs()
            ? getServerCustomArguments()
            : getServerStartCommand();
    processManager.executeParallelProcess(commands, this.getServerBinPath(), writer, false);
    isServerRunning = true;
  }

  public String[] getServerCustomArguments() {

    String custom = getServerConfig(ServerConfigBase.class).getCustomArgsValue();
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

  public void stopServer() throws IOException, InterruptedException, ExecutionException {
    isServerRunning = false;
    if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
      processManager.forceQuit();
    } else {
      processManager.executeParallelProcess(
          getServerStopCommand(), this.getServerBinPath(), null, true);
    }
  }

  public void restartServer() throws IOException, ExecutionException, InterruptedException {
    stopServer();
    startServer();
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

  public void setServerConfig(String serverConfigId) {
    this.serverConfigId = serverConfigId;
    serverConfig = server.getServerConfigBaseById(this.serverConfigId);
  }

  public void setServerConfig(ServerConfigBase serverConfig) {
    this.serverConfigId = serverConfig.getConfigID();
    serverConfig = serverConfig;
  }

  protected void standardCopy(File deployDir, File toDeploy) throws IOException {
    if (toDeploy.isDirectory()) {
      FileUtils.copyDirectory(toDeploy, deployDir, false);
    } else {
      FileUtils.copyFileToDirectory(toDeploy, deployDir, false);
    }
  }

  protected void copyWithWildCard(File deployDir, File toDeploy) throws IOException {
    for (String fName : getFileWithWildcard(toDeploy)) {
      File f = new File(toDeploy.getParent() + OsUtils.getSeparator() + fName);
      standardCopy(deployDir, f);
    }
  }

  protected String[] getFileWithWildcard(File toDeploy) {
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setIncludes(new String[] {toDeploy.getName()});
    scanner.setBasedir(toDeploy.getParentFile());
    scanner.setCaseSensitive(false);
    scanner.scan();
    return scanner.getIncludedFiles();
  }

  public String getServerConfigId() {
    return serverConfigId;
  }

  public <T extends ServerConfigBase> T getServerConfig(Class<T> type) {
    return type.cast(serverConfig);
  }
}
