package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.ServerManagerOutputWriter;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.util.ImagesLoader;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.util.OsUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ServerViewController extends ControllerBase {
	 protected static final String FXML_FILE_NAME = "serverView.fxml";
	 private static final Logger logger = JServersManagerApp.getLogger();
	 @FXML
	 private Label serverName;
	 @FXML
	 private Label serverType;
	 @FXML
	 private ImageView playStop;
	 @FXML
	 private ImageView folder;
	 @FXML
	 private ImageView settings;
	 @FXML
	 private ImageView restart;
	 @FXML
	 private ImageView output;
	 @FXML
	 private AnchorPane mainPane;


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
	 }


	 @SuppressWarnings("restriction")
	 public void setGuiManagerAndServer(GuiManager guiManager, Server server) {
		  this.setGuiManager(guiManager);
		  this.server = server;
		  updateShowedData();
		  guiManager.getServers().addListener((ListChangeListener<Server>) change -> updateShowedData());
	 }

	 public Node getView() {
		  return mainPane;
	 }

	 @FXML
	 private void startStopServer() throws Exception {
		  if (!guiManager.getServerManagersContainer().getServerManager(server).isServerRunning()) {
				try {
					 guiManager.startNewOutput(server.getServerID());
					 playStop.setImage(ImagesLoader.getStopIcon());
					 guiManager.getServerManagersContainer().getServerManager(server);
					 if (guiManager.getServerManagersContainer().getServerManager(server).getWriter() == null) {
						  ServerManagerOutputWriter writer = new ServerManagerOutputWriter(DataStorage.getInstance().getServerLogBufferedWriter(server), server, guiManager);
						  guiManager.getServerManagersContainer().getServerManager(server).setWriter(writer);
					 }
					 guiManager.getServerManagersContainer().getServerManager(server).startServer();
				} catch (Exception e) {
					 ExceptionDialog.showException(e);
				}
		  } else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("JServersManager");
				alert.setHeaderText("Stopping server");
				alert.setContentText("Stopping server");
				alert.show();
				guiManager.getServerManagersContainer().getServerManager(server).stopServer();
				playStop.setImage(ImagesLoader.getPlayIcon());
		  }
	 }

	 @FXML
	 private void handleRestart() {
		  try {
				if (guiManager.getServerManagersContainer().getServerManager(server).isServerRunning()) {

					 guiManager.startNewOutput(server.getServerID());
					 playStop.setImage(ImagesLoader.getStopIcon());
					 guiManager.getServerManagersContainer().getServerManager(server);
					 if (guiManager.getServerManagersContainer().getServerManager(server).getWriter() == null) {
						  ServerManagerOutputWriter writer = new ServerManagerOutputWriter(DataStorage.getInstance().getServerLogBufferedWriter(server), server, guiManager);
						  guiManager.getServerManagersContainer().getServerManager(server).setWriter(writer);
					 }
					 guiManager.getServerManagersContainer().getServerManager(server).restartServer();
				} else {
					 guiManager.startNewOutput(server.getServerID());
					 playStop.setImage(ImagesLoader.getStopIcon());
					 guiManager.getServerManagersContainer().getServerManager(server);
					 if (guiManager.getServerManagersContainer().getServerManager(server).getWriter() == null) {
						  ServerManagerOutputWriter writer = new ServerManagerOutputWriter(DataStorage.getInstance().getServerLogBufferedWriter(server), server, guiManager);
						  guiManager.getServerManagersContainer().getServerManager(server).setWriter(writer);
					 }
					 guiManager.getServerManagersContainer().getServerManager(server).startServer();
				}
		  } catch (Exception e) {
				ExceptionDialog.showException(e);
		  }
	 }

	 @FXML
	 private void handleOpenServerFolder() throws IOException {
		  switch (OsUtils.getOperatingSystemType()) {
				case Windows: {
					 ProcessBuilder pb = new ProcessBuilder("explorer.exe", "/select," + server.getServerPath());
					 pb.redirectError();
					 Process proc = pb.start();
					 break;
				}
				case Linux: {
					 ProcessBuilder pb = new ProcessBuilder("xdg-open", server.getServerPath());
					 pb.redirectError();
					 Process proc = pb.start();
					 break;
				}
		  }
	 }

	 @FXML
	 private void handleServerMod() {
		  boolean okClicked = guiManager.showNewServerDialog(server);
		  if (okClicked) {
				try {
					 DataStorage.getInstance().saveServerToFile(getGuiManager().getServers());
					 guiManager.updateServerList();

				} catch (UnsupportedOperatingSystemException e) {
					 ExceptionDialog.showException(e);
				}
		  }
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
		  new Thread(() -> {
				try {
					 Thread.sleep(500);
				} catch (InterruptedException e) {
					 e.printStackTrace();
				}
				output.setImage(ImagesLoader.getOutputOff());
		  }).start();
	 }
}
