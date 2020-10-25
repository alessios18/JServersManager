package org.alessios18.jserversmanager.gui.controllers.impl;

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

import java.io.BufferedWriter;
import java.io.IOException;

public class ServerViewController extends ControllerBase {
    protected static String FXML_FILE_NEME = "serverView.fxml";
    private static Image playIcon = null;
    private static Image pauseIcon = null;
    private static Image restartIcon = null;
    private static Image stopIcon = null;
    @FXML
    Label serverName;
    @FXML
    Label serverType;
    @FXML
    ImageView playPause;
    @FXML
    ImageView stop;
    @FXML
    ImageView restart;
    @FXML
    ImageView output;
    @FXML
    private AnchorPane mainPane;
    private Server server;

    public ServerViewController() {
        if (playIcon == null || pauseIcon == null) {
            playIcon = new Image(getClass().getResourceAsStream("/images/play.png"));
            pauseIcon = new Image(getClass().getResourceAsStream("/images/pausa.png"));
            restartIcon = new Image(getClass().getResourceAsStream("/images/restart.png"));
            stopIcon = new Image(getClass().getResourceAsStream("/images/stop.png"));
        }

    }

    public static String getFXMLFileName() {
        return FXML_FILE_NEME;
    }

    public static String getFXMLFileFullPath() {
        return GuiManager.FXML_FILE_PATH + getFXMLFileName();
    }

    @FXML
    private void initialize() {
        stop.setDisable(true);
        restart.setDisable(true);
        playPause.setImage(playIcon);
        stop.setImage(stopIcon);
        restart.setImage(restartIcon);
    }


    @SuppressWarnings("restriction")
    public void setGuiManagerAndServer(GuiManager guiManager, Server server) {
        this.setGuiManager(guiManager);
        this.server = server;
        //ImageView playIconView = new ImageView(playIcon);
        //toggle.setGraphic(playIconView);
        //toggle.setText("");
        serverName.setText(server.getServerName());
        if (server.getServerType() != null) {
            serverType.setText(server.getServerType().toString());
        } else {
            serverType.setText("");
        }

    }

    public Node getView() {
        return mainPane;
    }

    @FXML
    private void startStopServer() {
        try {
            ServerManagerOutputWriter writer =  new ServerManagerOutputWriter(DataStorage.getInstance().getServerLogBufferedWriter(server),guiManager);
            ServerManagerBase manager = ServerManagerFactory.getServerManager(server,writer);
            manager.startServer();
        } catch (UnsupportedOperatingSystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
