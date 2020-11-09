package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.controllers.ControllerForm;
import org.alessios18.jserversmanager.gui.util.ServerFormLoader;

import java.io.IOException;


public class NewServerDialogController extends ControllerBase {
	 @FXML
	 private TextField serverName;

	 @FXML
	 private ComboBox<ServerType> serverType;
	 @FXML
	 private AnchorPane formPaneContainer;

	 private Server server;
	 private Server clone;

	 private Stage dialogStage;
	 private boolean okClicked = false;

	 private ControllerForm controllerForm;

	 public static String getFXMLFileName() {
		  return "newServerDialog.fxml";
	 }

	 public static String getFXMLFileFullPath() {
		  return GuiManager.FXML_FILE_PATH + getFXMLFileName();
	 }

	 /**
	  * Initializes the controller class. This method is automatically called after the fxml file has
	  * been loaded.
	  */
	 @FXML
	 private void initialize() {
		  serverType.getItems().setAll(ServerType.values());
	 }

	 /**
	  * Sets the stage of this dialog.
	  *
	  * @param dialogStage
	  */
	 public void setDialogStage(Stage dialogStage) {
		  this.dialogStage = dialogStage;
	 }

	 /**
	  * Returns true if the user clicked OK, false otherwise.
	  *
	  * @return
	  */
	 public boolean isOkClicked() {
		  return okClicked;
	 }

	 /**
	  * Called when the user clicks save.
	  */
	 @FXML
	 private void save() {
		  if (isInputValid()) {
				clone.setServerName(serverName.getText());
				clone.setServerType(serverType.getValue());
				if (controllerForm != null) {
					 controllerForm.getServerData();
				}
				okClicked = true;
				dialogStage.close();
				server.setFromClone(clone);
		  }
	 }

	 /**
	  * Called when the user clicks cancel.
	  */
	 @FXML
	 private void cancel() {
		  dialogStage.close();
	 }

	 /**
	  * Validates the user input in the text fields.
	  *
	  * @return true if the input is valid
	  */
	 private boolean isInputValid() {
		  String errorMessage = "";

		  if (serverName.getText() == null || serverName.getText().length() == 0) {
				errorMessage += "No valid task name!\n";
		  }
		  if (errorMessage.length() == 0) {
				return true;
		  } else {
				// Show the error message.
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.initOwner(dialogStage);
				alert.setTitle("Invalid Data");
				alert.setHeaderText("Please correct invalid field");
				alert.setContentText(errorMessage);
				alert.showAndWait();
				return false;
		  }
	 }

	 public void setServer(Server server) throws IOException {
		  this.server = server;
		  this.clone = server.getCloneObject();
		  serverName.setText(clone.getServerName());
		  serverType.setValue(clone.getServerType());
		  if (clone.getServerType() != null) {
				changeFormPanel();
		  }
	 }

	 protected void changeFormPanel() throws IOException {
		  controllerForm = ServerFormLoader.getFormController(clone);
		  controllerForm.setGuiManager(this.getGuiManager());
		  controllerForm.setDialogStage(this.dialogStage);
		  Node root = controllerForm.getView();
		  formPaneContainer.getChildren().clear();
		  formPaneContainer.getChildren().add(root);
		  dialogStage.sizeToScene();
	 }

	 @FXML
	 private void changedType() throws IOException {
		  clone.setServerType(serverType.getValue());
		  if (clone.getServerType() != null) {
				changeFormPanel();
		  }
	 }

	 public ImageView getImageView(Image image) {
		  ImageView view = new ImageView(image);
		  view.setFitHeight(26);
		  view.setFitWidth(26);
		  return view;
	 }
}
