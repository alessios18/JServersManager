package org.alessios18.jserversmanager.baseobjects.servermanagers;

import org.alessios18.jserversmanager.baseobjects.serverdata.CustomProperty;
import org.alessios18.jserversmanager.baseobjects.serverdata.DeployFile;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.JBossServerConfig;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerManagerJBOSS extends ServerManagerBase {

  public static final String CMD = "CMD";
  public static final String CMD_C = "/C";
  protected static final String SERVER_START_IP_MASK = "-b=0.0.0.0";
  protected static final String SERVER_START_BINDING_PORT_OFFSET =
      "-Djboss.socket.binding.port-offset=";
  protected static final String SERVER_START_DEBUG_PORT = "--debug";
  protected static final String SERVER_START_ADMIN_PORT = "-Djboss.management.http.port=";
  protected static final String SERVER_START_HTTP_PORT = "-Djboss.http.port=";
  protected static final String SERVER_START_CONFIG_DIR = "-Djboss.server.config.dir=";
  protected static final String SERVER_EXEC_COMMAND_LINUX = "./standalone.sh";
  protected static final String SERVER_EXEC_COMMAND_WIN = "standalone.bat";
  protected static final String SERVER_EXEC_COMMAND =
      OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Linux)
          ? SERVER_EXEC_COMMAND_LINUX
          : SERVER_EXEC_COMMAND_WIN;
  protected static final String SERVER_JBOSS_CLI_COMMAND_LINUX = "./jboss-cli.sh";
  protected static final String SERVER_JBOSS_CLI_COMMAND_WIN = "jboss-cli.bat";
  protected static final String SERVER_JBOSS_CLI_COMMAND =
      OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Linux)
          ? SERVER_JBOSS_CLI_COMMAND_LINUX
          : SERVER_JBOSS_CLI_COMMAND_WIN;
  protected static final String SERVER_STOP_ADDRESS_TO_CLI_COMMAND = "--controller=localhost:";
  protected static final String SERVER_STOP_CONNECT_CLI_COMMAND = "--connect";
  protected static final String SERVER_STOP_SHUTDOWN_COMMAND = "command=:shutdown";
  protected static final String SERVER_DEPLOY_DIR =
      "standalone" + OsUtils.getSeparator() + "deployments";
  protected static final String SERVER_CONFIG_DIR =
      "standalone" + OsUtils.getSeparator() + "configuration";
  protected static final String SERVER_START_COMMAND_CONFIG_FILE = "--server-config=";

  public ServerManagerJBOSS(Server server, String serverConfigId) {
    super(server, serverConfigId);
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
    if (getServerConfig(JBossServerConfig.class).getStandalonePath() != null
        && !getServerConfig(JBossServerConfig.class).getStandalonePath().isEmpty()) {
      File tmp = new File(getServerConfig(JBossServerConfig.class).getStandalonePath());
      commandsList.add(SERVER_START_COMMAND_CONFIG_FILE + tmp.getName());
    }
    if (getServerConfig(JBossServerConfig.class).getPortOffset() != null
        && !getServerConfig(JBossServerConfig.class).getPortOffset().isEmpty()) {
      commandsList.add(
          SERVER_START_BINDING_PORT_OFFSET
              + getServerConfig(JBossServerConfig.class).getPortOffset());
    }
    if (getServerConfig(JBossServerConfig.class).getDebugPort() != null
        && !getServerConfig(JBossServerConfig.class).getDebugPort().isEmpty()) {
      commandsList.add(SERVER_START_DEBUG_PORT);
      commandsList.add(getServerConfig(JBossServerConfig.class).getDebugPort());
    }
    if (getServerConfig(JBossServerConfig.class).getAdminPort() != null
        && !getServerConfig(JBossServerConfig.class).getAdminPort().isEmpty()) {
      commandsList.add(
          SERVER_START_ADMIN_PORT + getServerConfig(JBossServerConfig.class).getAdminPort());
    }
    if (getServerConfig(JBossServerConfig.class).getHttpPort() != null
        && !getServerConfig(JBossServerConfig.class).getHttpPort().isEmpty()) {
      commandsList.add(
          SERVER_START_HTTP_PORT + getServerConfig(JBossServerConfig.class).getHttpPort());
    }
    if (!getServerConfig(JBossServerConfig.class).getCustomProperties().isEmpty()) {
      commandsList.addAll(getCustomProperties());
    }
    return commandsList.toArray(new String[0]);
  }

  public String getPortWithOffset(String port) {
    if (port != null) {
      return ""
          + (Integer.parseInt(port)
              + Integer.parseInt(
                  (getServerConfig(JBossServerConfig.class).getPortOffset() != null
                          && !getServerConfig(JBossServerConfig.class).getPortOffset().isEmpty())
                      ? getServerConfig(JBossServerConfig.class).getPortOffset()
                      : "0"));
    }
    return "0";
  }

  @Override
  public String[] getServerStopCommand() {
    ArrayList<String> commandsList = new ArrayList<>();
    if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
      commandsList.add(CMD);
      commandsList.add(CMD_C);
    }
    commandsList.add(SERVER_JBOSS_CLI_COMMAND);
    commandsList.add(
        SERVER_STOP_ADDRESS_TO_CLI_COMMAND
            + getPortWithOffset(getServerConfig(JBossServerConfig.class).getAdminPort()));
    commandsList.add(SERVER_STOP_CONNECT_CLI_COMMAND);
    commandsList.add(SERVER_STOP_SHUTDOWN_COMMAND);
    return commandsList.toArray(new String[0]);
  }

  @Override
  public String[] getServerCustomArguments() {
    ArrayList<String> commandsList = new ArrayList<>();
    if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
      commandsList.add(CMD);
      commandsList.add(CMD_C);
    }
    commandsList.add(SERVER_EXEC_COMMAND);
    commandsList.addAll(Arrays.asList(super.getServerCustomArguments()));
    return commandsList.toArray(new String[0]);
  }

  @Override
  public String getServerDeployDir() {
    return getServerConfig(JBossServerConfig.class).getServerPath().endsWith(OsUtils.getSeparator())
        ? getServerConfig(JBossServerConfig.class).getServerPath() + SERVER_DEPLOY_DIR
        : getServerConfig(JBossServerConfig.class).getServerPath()
            + OsUtils.getSeparator()
            + SERVER_DEPLOY_DIR;
  }

  @Override
  public String getServerConfigDir() {
    return getServerConfig(JBossServerConfig.class).getServerPath().endsWith(OsUtils.getSeparator())
        ? getServerConfig(JBossServerConfig.class).getServerPath() + SERVER_CONFIG_DIR
        : getServerConfig(JBossServerConfig.class).getServerPath()
            + OsUtils.getSeparator()
            + SERVER_CONFIG_DIR;
  }

  @Override
  public void doDeploy() throws IOException {
    copyConfigFiles();
    File deployDir = new File(getServerDeployDir());
    for (DeployFile fPath : getServerConfig(JBossServerConfig.class).getFilePathToDeploy()) {
      if (fPath.getPath() != null && !fPath.getPath().isEmpty()) {
        File toDeploy = new File(fPath.getPath());
        if (toDeploy.getAbsolutePath().contains("*") || toDeploy.getAbsolutePath().contains("?")) {
          copyWithWildCard(deployDir, toDeploy);
        } else {
          standardCopy(deployDir, toDeploy);
        }
      }
    }
    logger.debug("[" + getServer().getServerName() + "] Deploy:DONE");
  }

  @Override
  public String getServerBinPath() {
    return getServerConfig(JBossServerConfig.class).getServerPath().endsWith(OsUtils.getSeparator())
        ? getServerConfig(JBossServerConfig.class).getServerPath() + "bin"
        : getServerConfig(JBossServerConfig.class).getServerPath() + OsUtils.getSeparator() + "bin";
  }

  @Override
  void copyStandaloneFile() throws IOException {
    File config = new File(getServerConfig(JBossServerConfig.class).getStandalonePath());
    File srvConfig = new File(getServerConfigDir());
    if (config.exists() && srvConfig.exists()) {
      standardCopy(srvConfig, config);
    }
    logger.debug("[" + getServer().getServerName() + "] Standalone file copy:DONE");
  }

  @Override
  void copyConfigFiles() throws IOException {
    File srvConfig = new File(getServerConfigDir());
    File conf = new File(getServerConfig(JBossServerConfig.class).getConfigDir());
    if (srvConfig.exists() && conf.exists() && conf.isDirectory()) {
      for (File f : conf.listFiles()) {
        FileUtils.copyFileToDirectory(f, srvConfig, false);
      }
    }
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

  protected List<String> getCustomProperties() {
    List<String> out = new ArrayList<>();
    for (CustomProperty p : getServerConfig(JBossServerConfig.class).getCustomProperties()) {
      if (p.getPropertyName().startsWith("-D")) {
        out.add(p.getPropertyName() + "=" + p.getPropertyValue());
      } else {
        out.add("-D" + p.getPropertyName() + "=" + p.getPropertyValue());
      }
    }
    return out;
  }
}
