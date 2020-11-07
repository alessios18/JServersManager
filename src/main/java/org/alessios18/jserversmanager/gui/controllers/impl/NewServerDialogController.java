package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.servermanagers.factory.ServerManagerFactory;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.util.ImagesLoader;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.gui.view.JSMFileChooser;

import java.io.IOException;


public class NewServerDialogController extends ControllerBase {
	 @FXML
	 private TextField serverName;
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
	 private TextField httpPort;
	 @FXML
	 private TextArea arguments;
	 @FXML
	 private CheckBox enableCustomArgs;
	 @FXML
	 private ComboBox<ServerType> serverType;
	 @FXML
	 private Button bSrvDir;
	 @FXML
	 private Button bStandalone;
	 @FXML
	 private Button bConfigDir;
	 @FXML
	 private Label customPropertiesCount;
	 @FXML
	 private Label deployCount;

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
		  bConfigDir.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
		  enableCustomArgs.selectedProperty().addListener((ov, oldValue, newValue) -> {
				clone.setCustomArgs(newValue);
				isCustomArgsEnable(newValue);
		  });
		  setPageListener();
	 }

	 private void isCustomArgsEnable(boolean isCustom) {
		  configDir.setDisable(isCustom);
		  adminPort.setDisable(isCustom);
		  debugPort.setDisable(isCustom);
		  portOffset.setDisable(isCustom);
		  httpPort.setDisable(isCustom);
		  arguments.setEditable(isCustom);
	 }

	 private void setPageListener() {
		  debugPort.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setDebugPort(newValue);
				updateCustomArgs();
		  });
		  httpPort.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setHttpPort(newValue);
				updateCustomArgs();
		  });
		  portOffset.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setPortOffset(newValue);
				updateCustomArgs();
		  });
		  adminPort.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setAdminPort(newValue);
				updateCustomArgs();
		  });
		  srvDir.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setServerPath(newValue);
				updateCustomArgs();
		  });
		  configDir.textProperty().addListener((observable, oldValue, newValue) -> {
				clone.setConfigDir(newValue);
				updateCustomArgs();
		  });
	 }

	 private void updateCustomArgs() {
		  ServerManagerBase manager = ServerManagerFactory.getServerManager(clone);
		  if (manager != null) {
				arguments.setText(manager.getServerParameters());
		  }
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
				clone.setConfigDir(configDir.getText());
				if (clone.isCustomArgs()) {
					 clone.setCustomArgsValue(arguments.getText());
				} else {
					 clone.setCustomArgsValue(null);
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
		  deployCount.setText(clone.getFilePathToDeploy().size()+"");
		  customPropertiesCount.setText(clone.getCustomProperties().size()+"");
		  if (clone.getServerType() != null) {
				arguments.setText(ServerManagerFactory.getServerManager(clone).getServerParameters());
		  }
		  enableCustomArgs.setSelected(server.isCustomArgs());
		  if (server.getCustomArgsValue() != null && !server.getCustomArgsValue().isEmpty()) {
				arguments.setText(server.getCustomArgsValue());
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
	 private void handleSetConfigDir() {
		  JSMFileChooser dc = new JSMFileChooser(configDir);
		  dc.show("Choose the server config directory", this.dialogStage, JSMFileChooser.SelectionType.DIRECTORY);
	 }

	 @FXML
	 private void handleDeployDialog() {
		  showAddModifyDeployFiles();
		  deployCount.setText(clone.getFilePathToDeploy().size()+"");
	 }

	 @FXML
	 private void handleModCustomProp() {
		  showAddModifyCustomProperties();
		  customPropertiesCount.setText(clone.getCustomProperties().size()+"");
		  updateCustomArgs();
	 }

	 public boolean showAddModifyDeployFiles() {
		  try {
				// Load the fxml file and create a new stage for the popup dialog.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(
						  GuiManager.class.getResource(DeploymentListController.getFXMLFileFullPath()));
				AnchorPane page = loader.load();

				// Create the dialog Stage.
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Add Modify Custom Properties");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(guiManager.getPrimaryStage());
				Scene scene = new Scene(page);
				dialogStage.setScene(scene);
				getGuiManager().chooseDarkSide(scene);

				// Set the person into the controller.
				DeploymentListController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				controller.setGuiManager(guiManager);
				controller.setDeploymentFileItems(clone.getFilePathToDeploy());
				// Show the dialog and wait until the user closes it
				dialogStage.showAndWait();

				return true;
		  } catch (IOException e) {
				ExceptionDialog.showException(e);
				return false;
		  }
	 }

	 public boolean showAddModifyCustomProperties() {
		  try {
				// Load the fxml file and create a new stage for the popup dialog.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(
						  GuiManager.class.getResource(AddModifyCustomPropertiesController.getFXMLFileFullPath()));
				AnchorPane page = loader.load();

				// Create the dialog Stage.
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Add Modify Custom Properties");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(guiManager.getPrimaryStage());
				Scene scene = new Scene(page);
				dialogStage.setScene(scene);
				getGuiManager().chooseDarkSide(scene);

				// Set the person into the controller.
				AddModifyCustomPropertiesController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				controller.setGuiManager(guiManager);
				controller.setServerProperties(clone.getCustomProperties());
				// Show the dialog and wait until the user closes it
				dialogStage.showAndWait();

				return controller.isOkClicked();
		  } catch (IOException e) {
				ExceptionDialog.showException(e);
				return false;
		  }
	 }

	 public ImageView getImageView(Image image) {
		  ImageView view = new ImageView(image);
		  view.setFitHeight(26);
		  view.setFitWidth(26);
		  return view;
	 }
}
