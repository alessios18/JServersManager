package org.alessios18.jserversmanager.gui.util;

import javafx.fxml.FXMLLoader;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.gui.controllers.ControllerForm;
import org.alessios18.jserversmanager.gui.controllers.impl.GruntFormController;
import org.alessios18.jserversmanager.gui.controllers.impl.JBOSSFormController;

import java.io.IOException;

public class ServerFormLoader {

	 public static ControllerForm getFormController(Server server) throws IOException {
		  FXMLLoader loader = new FXMLLoader();
		  if(server.getServerType().equals(ServerType.JBOSS)) {
				loader.setLocation(ServerFormLoader.class.getResource(JBOSSFormController.getFXMLFileFullPath()));
		  }else if(server.getServerType().equals(ServerType.GRUNT)){
				loader.setLocation(ServerFormLoader.class.getResource(GruntFormController.getFXMLFileFullPath()));
		  }
		  loader.load();
		  ControllerForm controller = loader.getController();
			controller.setServer(server);
		  return controller;
	 }
}
