package org.alessios18.jserversmanager.gui.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;

public abstract class ControllerForm extends ControllerBase{
	 private Stage dialogStage;

	 public abstract Node getView();
	 public abstract void getServerData();
	 public abstract void setServer(Server server);

	 public void setDialogStage(Stage dialogStage){
	 	 this.dialogStage=dialogStage;
	 }

	 public Stage getDialogStage() {
		  return dialogStage;
	 }
}
