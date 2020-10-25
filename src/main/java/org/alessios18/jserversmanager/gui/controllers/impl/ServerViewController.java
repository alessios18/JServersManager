package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.ServerManagerOutputWriter;
import org.alessios18.jserversmanager.baseobjects.factory.ServerManagerFactory;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

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
		  guiManager.getServers().addListener(new ListChangeListener<Server>() {
				@Override
				public void onChanged(Change<? extends Server> change) {
					 updateShowedData();
				}
		  });

		/*
		  serverName.setText(server.getServerName());
		  if (server.getServerType() != null) {
				serverType.setText(server.getServerType().toString());
		  } else {
				serverType.setText("");
		  }

		   */

	 }

	 public Node getView() {
		  return mainPane;
	 }

	 @FXML
	 private void startStopServer() {
		  if (!guiManager.getServerManagersContainer().getServerManager(server).isServerRunning()) {
				try {
					 guiManager.startNewOutput(server.getServerID());
					 playStop.setImage(stopIcon);
					 guiManager.getServerManagersContainer().getServerManager(server);
					 if(guiManager.getServerManagersContainer().getServerManager(server).getWriter() == null) {
						  ServerManagerOutputWriter writer = new ServerManagerOutputWriter(DataStorage.getInstance().getServerLogBufferedWriter(server), server, guiManager);
						  guiManager.getServerManagersContainer().getServerManager(server).setWriter(writer);
					 }
					 guiManager.getServerManagersContainer().getServerManager(server).startServer();
				} catch (UnsupportedOperatingSystemException | IOException e) {
					 ExceptionDialog.showException(e);
				}
		  } else {
				guiManager.getServerManagersContainer().getServerManager(server).stopServer();
				playStop.setImage(playIcon);
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
		  this.server = server;
		  serverName.setText(server.getServerName());
		  if (server.getServerType() != null) {
				serverType.setText(server.getServerType().toString());
		  } else {
				serverType.setText("");
		  }
	 }

	 public void outputBlink() {
		  output.setImage(outputOn);
		  new Thread(new Runnable() {
				@Override
				public void run() {
					 try {
						  Thread.sleep(500);
					 } catch (InterruptedException e) {
						  e.printStackTrace();
					 }
					 output.setImage(outputOff);
				}
		  }).start();
	 }
}
