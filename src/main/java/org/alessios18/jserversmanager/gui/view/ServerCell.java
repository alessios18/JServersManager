package org.alessios18.jserversmanager.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.impl.ServerViewController;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ServerCell extends ListCell<Server> {
  private static final Logger logger = JServersManagerApp.getLogger();
  private final GuiManager guiManager;
  private ServerViewController serverViewController = null;

  public ServerCell(GuiManager guiManager) {
    this.guiManager = guiManager;
    FXMLLoader mLLoader =
        new FXMLLoader(GuiManager.class.getResource(ServerViewController.getFXMLFileFullPath()));
    try {
      mLLoader.load();
      serverViewController = mLLoader.getController();
      this.getStyleClass().add("serverListItem");
    } catch (IOException e) {
      ExceptionDialog.showException(e);
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

  public ServerViewController getServerViewController() {
    return serverViewController;
  }
}
