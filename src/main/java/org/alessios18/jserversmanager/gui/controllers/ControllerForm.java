package org.alessios18.jserversmanager.gui.controllers;

import javafx.scene.Node;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;

public abstract class ControllerForm extends ControllerBase{
	 public abstract Node getView();
	 public abstract void getServerData();
	 public abstract void setServer(Server server);
}
