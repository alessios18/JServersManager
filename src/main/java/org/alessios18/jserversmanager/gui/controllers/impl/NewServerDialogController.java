package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.servermanagers.factory.ServerManagerFactory;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.util.ImagesLoader;
import org.alessios18.jserversmanager.gui.view.JSMFileChooser;


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
	 private TextField configDir;
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
	 @FXML
	 private TextArea arguments;
	 @FXML
	 private CheckBox enableCustomArgs;
	 @FXML
	 private TextField httpPort;
	 @FXML
	 private Button bSrvDir;
	 @FXML
	 private Button bStandalone;
	 @FXML
	 private Button bPathDeploy;
	 @FXML
	 private Button bConfigDir;

	 private Server server;
	 private Server clone;

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
		  bSrvDir.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
		  bStandalone.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
		  bPathDeploy.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
		  bConfigDir.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
		  setPageListener();
	 }

	 private void setPageListener() {
		  debugPort.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setDebugPort(newValue);
				ServerManagerBase manager = ServerManagerFactory.getServerManager(clone);
				if(manager != null) {
					 arguments.setText(manager.getServerParameters());
				}
		  });
		  httpPort.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setHttpPort(newValue);
				ServerManagerBase manager = ServerManagerFactory.getServerManager(clone);
				if(manager != null) {
					 arguments.setText(manager.getServerParameters());
				}
		  });
		  portOffset.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setPortOffset(newValue);
				ServerManagerBase manager = ServerManagerFactory.getServerManager(clone);
				if(manager != null) {
					 arguments.setText(manager.getServerParameters());
				}
		  });
		  adminPort.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setAdminPort(newValue);
				ServerManagerBase manager = ServerManagerFactory.getServerManager(clone);
				if(manager != null) {
					 arguments.setText(manager.getServerParameters());
				}
		  });
		  srvDir.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setServerPath(newValue);
				ServerManagerBase manager = ServerManagerFactory.getServerManager(clone);
				if(manager != null) {
					 arguments.setText(manager.getServerParameters());
				}
		  });
		  configDir.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setConfigDir(newValue);
				ServerManagerBase manager = ServerManagerFactory.getServerManager(clone);
				if(manager != null) {
					 arguments.setText(manager.getServerParameters());
				}
		  });
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
				clone.setServerPath(srvDir.getText());
				clone.setStandalonePath(standalonePath.getText());
				clone.setAdminPort(adminPort.getText());
				clone.setDebugPort(debugPort.getText());
				clone.setPortOffset(portOffset.getText());
				clone.setHttpPort(httpPort.getText());
				// TODO make this dynamic
				String[] files = new String[2];
				files[0] = deploFile1.getText();
				files[1] = deploFile2.getText();
				clone.setFilePathToDeploy(files);
				clone.setConfigDir(configDir.getText());
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

	 public void setServer(Server server) {
		  this.server = server;
		  this.clone = server.getCloneObject();
		  serverName.setText(clone.getServerName());
		  serverType.setValue(clone.getServerType());
		  srvDir.setText(clone.getServerPath());
		  standalonePath.setText(clone.getStandalonePath());
		  adminPort.setText(clone.getAdminPort());
		  debugPort.setText(clone.getDebugPort());
		  portOffset.setText(clone.getPortOffset());
		  httpPort.setText(clone.getHttpPort());
		  configDir.setText(clone.getConfigDir());
		  // TODO make this dynamic
		  if (clone.getFilePathToDeploy() != null) {
				deploFile1.setText(clone.getFilePathToDeploy()[0]);
				deploFile2.setText(clone.getFilePathToDeploy()[1]);
		  }
		  if (clone.getServerType() != null) {
				arguments.setText(ServerManagerFactory.getServerManager(clone).getServerParameters());
		  }
	 }

	 @FXML
	 private void changedType() {
		  clone.setServerType(serverType.getValue());
		  arguments.setText(ServerManagerFactory.getServerManager(clone).getServerParameters());
	 }

	 @FXML
	 private void handleSetSrvDir() {
		  JSMFileChooser dc = new JSMFileChooser(srvDir);
		  dc.show("Choose the server Directory", this.dialogStage, JSMFileChooser.SelectionType.DIRECTORY);
	 }

	 @FXML
	 private void handleSetStandalone() {
		  JSMFileChooser dc = new JSMFileChooser(standalonePath);
		  dc.show("Choose the standalone.xml file", this.dialogStage, JSMFileChooser.SelectionType.FILE);
	 }

	 @FXML
	 private void handleSetConfigDir(){
		  JSMFileChooser dc = new JSMFileChooser(configDir);
		  dc.show("Choose the server config directory", this.dialogStage, JSMFileChooser.SelectionType.DIRECTORY);
	 }

	 @FXML
	 private void handleSetDeployPath() {
		  JSMFileChooser dc = new JSMFileChooser(deploFile1);
		  dc.show("Choose the file or directory to be deployed", this.dialogStage, JSMFileChooser.SelectionType.FILE_AND_DIRECTORY);
	 }

	 public ImageView getImageView(Image image) {
		  ImageView view = new ImageView(image);
		  view.setFitHeight(26);
		  view.setFitWidth(26);
		  return view;
	 }
}
