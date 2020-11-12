package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.processes.ServerManagerOutputWriter;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.ServerConfigBase;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.util.ImagesLoader;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.util.OsUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerViewController extends ControllerBase {
  protected static final String FXML_FILE_NAME = "serverView.fxml";
  private final ObservableList<ServerConfigBase> comboConfigList =
      FXCollections.observableArrayList(new ArrayList<>());
  @FXML private Label serverName;
  @FXML private Label serverType;
  @FXML private ImageView playStop;
  @FXML private ImageView folder;
  @FXML private ImageView settings;
  @FXML private ImageView restart;
  @FXML private ImageView output;
  @FXML private AnchorPane mainPane;
  @FXML private ComboBox<ServerConfigBase> serverConfig;
  private Server server;

  public static String getFXMLFileName() {
    return FXML_FILE_NAME;
  }

  public static String getFXMLFileFullPath() {
    return GuiManager.FXML_FILE_PATH + getFXMLFileName();
  }

  @FXML
  private void initialize() {
    restart.setDisable(true);
    playStop.setImage(ImagesLoader.getPlayIcon());
    folder.setImage(ImagesLoader.getFolderIcon());
    restart.setImage(ImagesLoader.getRestartIcon());
    settings.setImage(ImagesLoader.getSettingsIcon());
    output.setImage(ImagesLoader.getOutputOff());
    Callback<ListView<ServerConfigBase>, ListCell<ServerConfigBase>> cellFactory =
        new Callback<ListView<ServerConfigBase>, ListCell<ServerConfigBase>>() {
          @Override
          public ListCell<ServerConfigBase> call(ListView<ServerConfigBase> l) {
            return new ListCell<ServerConfigBase>() {
              @Override
              protected void updateItem(ServerConfigBase item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                  setGraphic(null);
                } else {
                  setText(item.getConfigName());
                }
              }
            };
          }
        };
    serverConfig.setCellFactory(cellFactory);
    serverConfig.setItems(comboConfigList);
  }

  @SuppressWarnings("restriction")
  public void setGuiManagerAndServer(GuiManager guiManager, Server server) {
    this.setGuiManager(guiManager);
    this.server = server;
    updateShowedData();
    comboConfigList.clear();
    comboConfigList.addAll(server.getServerConfigByType(server.getServerType()));
    serverConfig.getSelectionModel().selectFirst();
    guiManager.getServers().addListener((ListChangeListener<Server>) change -> updateShowedData());
  }

  public Node getView() {
    return mainPane;
  }



  public void updateShowedData() {
    serverName.setText(server.getServerName());
    if (server.getServerType() != null) {
      serverType.setText(server.getServerType().toString());
    } else {
      serverType.setText("");
    }
  }

  public void outputBlink() {
    output.setImage(ImagesLoader.getOutputOn());
    new Thread(
            () -> {
              try {
                Thread.sleep(500);
              } catch (InterruptedException e) {
                ExceptionDialog.showException(e);
              }
              output.setImage(ImagesLoader.getOutputOff());
            })
        .start();
  }

  public ServerConfigBase getSelectedConfig() {
    return serverConfig.getSelectionModel().getSelectedItem();
  }

  @FXML
  private void startStopServer() throws Exception {
    if (!guiManager
            .getServerManagersContainer()
            .getServerManager(server, getSelectedConfig())
            .isServerRunning()) {
      try {
        serverConfig.setDisable(true);
        guiManager.startNewOutput(server);
        playStop.setImage(ImagesLoader.getStopIcon());
        guiManager.getServerManagersContainer().getServerManager(server, getSelectedConfig());
        if (guiManager
                .getServerManagersContainer()
                .getServerManager(server, getSelectedConfig())
                .getWriter()
                == null) {
          ServerManagerOutputWriter writer =
                  new ServerManagerOutputWriter(
                          DataStorage.getInstance().getServerLogBufferedWriter(server), server, guiManager);
          guiManager
                  .getServerManagersContainer()
                  .getServerManager(server, getSelectedConfig())
                  .setWriter(writer);
        }
        guiManager
                .getServerManagersContainer()
                .getServerManager(server, getSelectedConfig())
                .startServer();
      } catch (Exception e) {
        ExceptionDialog.showException(e);
      }
    } else {
      guiManager
              .getServerManagersContainer()
              .getServerManager(server, getSelectedConfig())
              .stopServer();
      playStop.setImage(ImagesLoader.getPlayIcon());
      serverConfig.setDisable(false);
    }
  }

  @FXML
  private void handleRestart() {
    try {
      if (guiManager
              .getServerManagersContainer()
              .getServerManager(server, getSelectedConfig())
              .isServerRunning()) {
        guiManager.startNewOutput(server);
        playStop.setImage(ImagesLoader.getStopIcon());
        guiManager.getServerManagersContainer().getServerManager(server, getSelectedConfig());
        if (guiManager
                .getServerManagersContainer()
                .getServerManager(server, getSelectedConfig())
                .getWriter()
                == null) {
          ServerManagerOutputWriter writer =
                  new ServerManagerOutputWriter(
                          DataStorage.getInstance().getServerLogBufferedWriter(server), server, guiManager);
          guiManager
                  .getServerManagersContainer()
                  .getServerManager(server, getSelectedConfig())
                  .setWriter(writer);
        }
        guiManager
                .getServerManagersContainer()
                .getServerManager(server, getSelectedConfig())
                .restartServer();
        serverConfig.setDisable(true);
      } else {
        guiManager.startNewOutput(server);
        playStop.setImage(ImagesLoader.getStopIcon());
        guiManager.getServerManagersContainer().getServerManager(server, getSelectedConfig());
        if (guiManager
                .getServerManagersContainer()
                .getServerManager(server, getSelectedConfig())
                .getWriter()
                == null) {
          ServerManagerOutputWriter writer =
                  new ServerManagerOutputWriter(
                          DataStorage.getInstance().getServerLogBufferedWriter(server), server, guiManager);
          guiManager
                  .getServerManagersContainer()
                  .getServerManager(server, getSelectedConfig())
                  .setWriter(writer);
        }
        guiManager
                .getServerManagersContainer()
                .getServerManager(server, getSelectedConfig())
                .startServer();
        serverConfig.setDisable(false);
      }
    } catch (Exception e) {
      ExceptionDialog.showException(e);
    }
  }

  @FXML
  private void handleOpenServerFolder() throws IOException {
    List<String> commands = new ArrayList<>();
    if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
      commands.add("explorer.exe");
      commands.add("/select," + getSelectedConfig().getServerPath());
    } else if (OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Linux)) {
      commands.add("xdg-open");
      commands.add(getSelectedConfig().getServerPath());
    }
    ProcessBuilder pb = new ProcessBuilder(commands);
    pb.redirectError();
    pb.start();
  }

  @FXML
  private void handleServerMod() {
    boolean okClicked = guiManager.showNewServerDialog(server);
    if (okClicked) {
      try {
        long startTime = System.currentTimeMillis();
        DataStorage.getInstance().saveServerToFile(getGuiManager().getServers());
        getLogger()
                .debug("Server updated in: " + (System.currentTimeMillis() - startTime) + " ms.");
        startTime = System.currentTimeMillis();
        guiManager.updateServerList();
        getLogger()
                .debug("ServerList updated in: " + (System.currentTimeMillis() - startTime) + " ms.");

      } catch (UnsupportedOperatingSystemException | JAXBException e) {
        ExceptionDialog.showException(e);
      }
    }
  }
}
