package org.alessios18.jserversmanager.gui.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;

public abstract class ControllerForm extends ControllerBase {
  private Stage dialogStage;
  private String configId;
  private ControllerBase owner;

  public abstract Node getView();

  public abstract void updateServerDataFromInputs();

  public abstract void setServer(Server server);

  public ControllerBase getOwner() {
    return owner;
  }

  public void setOwner(ControllerBase owner) {
    this.owner = owner;
  }

  public String getConfigId() {
    return configId;
  }

  public void setConfigId(String configId) {
    this.configId = configId;
  }

  public Stage getDialogStage() {
    return dialogStage;
  }

  public void setDialogStage(Stage dialogStage) {
    this.dialogStage = dialogStage;
  }
}
