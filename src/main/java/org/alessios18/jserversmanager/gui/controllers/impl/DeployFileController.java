package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.serverdata.DeployFile;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.view.JSMFileChooser;

public class DeployFileController extends ControllerBase {
	 protected static final String FXML_FILE_NAME = "deployFileView.fxml";

	 @FXML
	 private TextField path;
	 @FXML
	 private AnchorPane mainPane;

	 private DeployFile deployFile;

	 private Stage dialogStage;

	 public static String getFXMLFileName() {
		  return FXML_FILE_NAME;
	 }

	 public static String getFXMLFileFullPath() {
		  return GuiManager.FXML_FILE_PATH + getFXMLFileName();
	 }

	 public void setDialogStage(Stage dialogStage) {
		  this.dialogStage = dialogStage;
	 }

	 public Node getView() {
		  return mainPane;
	 }

	 @FXML
	 private void initialize() {
		  path.textProperty().addListener((observable, oldValue, newValue) -> {
				deployFile.setPath(newValue);
		  });
	 }


	 public void setGuiManagerAndDeployFile(GuiManager guiManager, DeployFile deployFile) {
		  this.setGuiManager(guiManager);
		  this.deployFile = deployFile;
		  updateShowedData();
	 }

	 @FXML
	 private void handleChoosePath(){
		  JSMFileChooser dc = new JSMFileChooser(path);
		  dc.show("Choose the server config directory", this.dialogStage, JSMFileChooser.SelectionType.FILE_AND_DIRECTORY);
	 }

	 public void updateShowedData() {
		  path.setText(deployFile.getPath());
	 }
}
