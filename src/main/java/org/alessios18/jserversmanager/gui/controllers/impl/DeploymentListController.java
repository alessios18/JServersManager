package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.serverdata.DeployFile;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.view.DeployFileCell;

import java.util.ArrayList;
import java.util.List;

public class DeploymentListController extends ControllerBase {
	 protected static final String FXML_FILE_NAME = "deploymentList.fxml";
	 private final ObservableList<DeployFile> deployPaths = FXCollections.observableArrayList();
	 @FXML
	 private ListView<DeployFile> fileList;

	 private Stage dialogStage;

	 private List<DeployFile> origin = new ArrayList<>();

	 public static String getFXMLFileName() {
		  return FXML_FILE_NAME;
	 }

	 public static String getFXMLFileFullPath() {
		  return GuiManager.FXML_FILE_PATH + getFXMLFileName();
	 }

	 public void setDeploymentFileItems(List<DeployFile> deployFiles) {
		  deployPaths.addAll(deployFiles);
		  origin=deployFiles;
	 }

	 public void setDialogStage(Stage dialogStage) {
		  this.dialogStage = dialogStage;
	 }

	 @FXML
	 private void initialize() {
		  fileList.setCellFactory(param -> new DeployFileCell(guiManager,dialogStage));
		  fileList.setItems(deployPaths);
	 }
	 @FXML
	 private void handleSave() {
		  origin.clear();
		  origin.addAll(deployPaths);
		  dialogStage.close();
	 }
	 @FXML
	 private void handleCancel() {
		  dialogStage.close();
	 }

	 @FXML
	 private void handleAdd() {
		  deployPaths.add(new DeployFile());
	 }

	 @FXML
	 private void handleRemove() {
		  int selectedIndex = fileList.getSelectionModel().getSelectedIndex();
		  if (selectedIndex >= 0) {
				deployPaths.remove(selectedIndex);
		  } else {
				// Nothing selected.
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(getGuiManager().getPrimaryStage());
				alert.setTitle("No Selection");
				alert.setHeaderText("No Row Selected");
				alert.setContentText("Please select a row in the table.");
				alert.showAndWait();
		  }
	 }

}
