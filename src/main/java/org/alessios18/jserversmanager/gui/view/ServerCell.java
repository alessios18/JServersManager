package org.alessios18.jserversmanager.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.impl.ServerViewController;

import java.io.IOException;

public class ServerCell extends ListCell<Server> {
    private ServerViewController serverViewController = null;
    private GuiManager guiManager;

    public ServerCell(GuiManager guiManager) {
        this.guiManager = guiManager;
        FXMLLoader mLLoader = new FXMLLoader(GuiManager.class.getResource(ServerViewController.getFXMLFileFullPath()));
        try {
            mLLoader.load();
            serverViewController = mLLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Server item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            serverViewController.setGuiManagerAndServer(guiManager, item);
            setGraphic(serverViewController.getView());
        }
    }
}