package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.ServerManagerOutputWriter;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.util.OsCheck;

import java.io.IOException;

public class ServerViewController extends ControllerBase {
	 protected static final String FXML_FILE_NAME = "serverView.fxml";
	 protected static final String IMAGES_FOLDER = "/images/";

	 protected static final String IMAGE_PLAY = IMAGES_FOLDER + "013-play.png";
	 protected static final String IMAGE_RESTART = IMAGES_FOLDER + "015-refresh.png";
	 protected static final String IMAGE_STOP = IMAGES_FOLDER + "003-stop.png";
	 protected static final String IMAGE_FOLDER = IMAGES_FOLDER + "028-folder.png";
	 protected static final String IMAGE_SETTINGS = IMAGES_FOLDER + "020-menu.png";

	 protected static final String IMAGE_BLINK_ON = IMAGES_FOLDER + "blink-on.png";
	 protected static final String IMAGE_BLINK_OFF = IMAGES_FOLDER + "blink-off.png";

	 private static final Image playIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_PLAY));
	 private static final Image folderIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_FOLDER));
	 private static final Image restartIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_RESTART));
	 private static final Image stopIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_STOP));
	 private static final Image settingsIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_SETTINGS));
	 private static final Image outputOn = new Image(ServerViewController.class.getResourceAsStream(IMAGE_BLINK_ON));
	 private static final Image outputOff = new Image(ServerViewController.class.getResourceAsStream(IMAGE_BLINK_OFF));

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
		  playStop.setImage(playIcon);
		  folder.setImage(folderIcon);
		  restart.setImage(restartIcon);
		  settings.setImage(settingsIcon);
		  output.setImage(outputOff);
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
					 playStop.setImage(stopIcon);
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
				playStop.setImage(playIcon);
		  }
	 }

	 @FXML
	 private void handleOpenServerFolder() throws IOException {
		  switch (OsCheck.getOperatingSystemType()) {
				case Windows: {
					 ProcessBuilder pb = new ProcessBuilder("explorer.exe", "/select," + server.getServerPath());
					 pb.redirectError();
					 Process proc = pb.start();
				}
				case Linux: {
					 ProcessBuilder pb = new ProcessBuilder("xdg-open", server.getServerPath());
					 pb.redirectError();
					 Process proc = pb.start();
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

				} catch (UnsupportedOperatingSystemException | IOException e) {
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
		  output.setImage(outputOn);
		  new Thread(() -> {
				try {
					 Thread.sleep(500);
				} catch (InterruptedException e) {
					 e.printStackTrace();
				}
				output.setImage(outputOff);
		  }).start();
	 }
}
