package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;

public class NewServerDialogController extends ControllerBase {
	 @FXML
	 private TextField serverName;
	 @FXML
	 private ComboBox<ServerType> serverType;
	 @FXML
	 private TextField srvDir;
	 @FXML
	 private TextField standalonePath;
	 @FXML
	 private TextField adminPort;
	 @FXML
	 private TextField debugPort;
	 @FXML
	 private TextField portOffset;
	 @FXML
	 private TextField deploFile1;
	 @FXML
	 private TextField deploFile2;

	 private Server server;

	 private Stage dialogStage;
	 private boolean okClicked = false;

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
				server.setServerName(serverName.getText());
				server.setServerType(serverType.getValue());
				server.setServerPath(srvDir.getText());
				server.setStandalonePath(standalonePath.getText());
				server.setAdminPort(adminPort.getText());
				server.setDebugPort(debugPort.getText());
				server.setPortOffset(portOffset.getText());
				// TODO make this dynamic
				String[] files = new String[2];
				files[0] = deploFile1.getText();
				files[1] = deploFile2.getText();
				server.setFilePathToDeploy(files);
				okClicked = true;
				dialogStage.close();
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

	 public void setServer(Server server) {
		  this.server = server;
		  serverName.setText(server.getServerName());
		  serverType.setValue(server.getServerType());
		  srvDir.setText(server.getServerPath());
		  standalonePath.setText(server.getStandalonePath());
		  adminPort.setText(server.getAdminPort());
		  debugPort.setText(server.getDebugPort());
		  portOffset.setText(server.getPortOffset());
		  // TODO make this dynamic
		  deploFile1.setText(server.getFilePathToDeploy()[0]);
		  deploFile2.setText(server.getFilePathToDeploy()[1]);
	 }
}
