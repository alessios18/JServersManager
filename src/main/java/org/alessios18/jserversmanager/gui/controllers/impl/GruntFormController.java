package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerForm;

public class GruntFormController extends ControllerForm {
	 @FXML
	 private AnchorPane root;
	 public static String getFXMLFileName() {
		  return "gruntFormView.fxml";
	 }

	 public static String getFXMLFileFullPath() {
		  return GuiManager.FXML_FILE_PATH + getFXMLFileName();
	 }
	 @Override
	 public Node getView() {
		  return root;
	 }

	 @Override
	 public void getServerData() {

	 }

	 @Override
	 public void setServer(Server server) {

	 }
}
