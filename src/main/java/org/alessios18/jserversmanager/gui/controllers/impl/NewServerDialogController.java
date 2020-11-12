package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.ServerConfigBase;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.factory.FactoryServerConfig;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.controllers.ControllerForm;
import org.alessios18.jserversmanager.gui.util.ServerFormLoader;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewServerDialogController extends ControllerBase {
  private final ObservableList<ServerConfigBase> comboConfigList =
      FXCollections.observableArrayList(new ArrayList<>());
  private final Map<String, ControllerForm> controllerForms = new HashMap<>();
  @FXML private TextField serverName;
  @FXML private ComboBox<ServerType> serverType;
  @FXML private ComboBox<ServerConfigBase> comboConfig;
  @FXML private AnchorPane formPaneContainer;
  @FXML private Button addConfig;
  @FXML private Button deleteConfig;
  private Server server;
  private Server clone;
  private Stage dialogStage;
  private boolean okClicked = false;
  private ChangeListener comboConfigListener;

  public static String getFXMLFileName() {
    return "newServerDialog.fxml";
  }

  public static String getFXMLFileFullPath() {
    return GuiManager.FXML_FILE_PATH + getFXMLFileName();
  }



  public void setDialogStage(Stage dialogStage) {
    this.dialogStage = dialogStage;
  }

  public boolean isOkClicked() {
    return okClicked;
  }



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
      alert.initOwner(GuiManager.getPrimaryStage());
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
      serverType.setDisable(true);
      addConfig.setDisable(false);
      deleteConfig.setDisable(false);
    }
    if (clone.getServerType() != null) {
      fillComboConfig(clone.getServerType());
      changeFormPanel();
    }
  }

  protected void fillComboConfig(ServerType type) {
    if (clone.getServerConfigByType(type).isEmpty()) {
      ServerConfigBase newConf = FactoryServerConfig.getNewServerConfig("", type);
      clone.getServerConfigs().add(newConf);
    }
    comboConfigList.clear();
    comboConfigList.setAll(clone.getServerConfigByType(type));
    comboConfig.getSelectionModel().selectFirst();
  }

  protected void changeFormPanel() throws IOException {
    if (comboConfig.getItems().size() > 0) {
      ServerConfigBase conf;
      if (comboConfig.getValue() == null) {
        conf = comboConfig.getItems().get(0);
      } else {
        conf = comboConfig.getValue();
      }
      ControllerForm controllerForm = getControllerForm(conf);
      Node root = controllerForm.getView();
      formPaneContainer.getChildren().clear();
      formPaneContainer.getChildren().add(root);
      dialogStage.sizeToScene();
    }
  }

  private ControllerForm getControllerForm(ServerConfigBase conf) throws IOException {
    ControllerForm controllerForm;
    if (!controllerForms.containsKey(conf.getConfigID())) {
      controllerForm = ServerFormLoader.getFormController(clone, conf.getConfigID());
      controllerForm.setGuiManager(this.getGuiManager());
      controllerForm.setDialogStage(this.dialogStage);
      controllerForm.setOwner(this);
      controllerForms.put(conf.getConfigID(), controllerForm);
    }
    controllerForm = controllerForms.get(conf.getConfigID());
    return controllerForm;
  }

  public void reloadComboConfig() {
    comboConfig.getSelectionModel().selectedItemProperty().removeListener(getComboConfigListener());
    int index = comboConfig.getSelectionModel().getSelectedIndex();
    comboConfigList.setAll(clone.getServerConfigByType(serverType.getValue()));
    comboConfig.getSelectionModel().select(index);
    comboConfig.getSelectionModel().selectedItemProperty().addListener(getComboConfigListener());
  }

  public ChangeListener getComboConfigListener() {
    if (comboConfigListener == null) {
      comboConfigListener =
          (options, oldValue, newValue) -> {
            try {
              changeFormPanel();
            } catch (IOException e) {
              ExceptionDialog.showException(e);
            }
          };
    }
    return comboConfigListener;
  }



  private boolean areYouSureDeleteConfig(String configName) {
    boolean decision = false;
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete '" + configName + "' configuration ?",
            ButtonType.YES,
            ButtonType.NO,
            ButtonType.CANCEL);
    alert.initOwner(this.dialogStage);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      decision = true;
    }
    return decision;
  }

	 @FXML
	 private void changedType() {
		  clone.setServerType(serverType.getValue());
		  if (clone.getServerType() != null) {
				fillComboConfig(clone.getServerType());
		  }
		  addConfig.setDisable(false);
		  deleteConfig.setDisable(false);
	 }

	 @FXML
	 private void handleAddConfig() throws IOException {
		  ServerConfigBase newConf = FactoryServerConfig.getNewServerConfig("", serverType.getValue());
		  clone.getServerConfigs().add(newConf);
		  comboConfigList.clear();
		  comboConfigList.setAll(clone.getServerConfigByType(serverType.getValue()));
		  comboConfig.getSelectionModel().select(newConf);
	 }

	 @FXML
	 private void handleRemoveConfig() throws IOException {
		  ServerConfigBase conf = comboConfig.getValue();
		  if (areYouSureDeleteConfig(conf.getConfigName())) {
				comboConfigList.remove(conf);
				clone.getServerConfigs().remove(conf);
		  }
	 }

	 @FXML
	 private void initialize() {
		  serverType.getItems().setAll(ServerType.values());
		  comboConfig.setItems(comboConfigList);
		  comboConfig.getSelectionModel().selectedItemProperty().addListener(getComboConfigListener());
		  addConfig.setDisable(true);
		  deleteConfig.setDisable(true);
	 }

	 @FXML
	 private void save() {
		  if (isInputValid()) {
				clone.setServerName(serverName.getText());
				clone.setServerType(serverType.getValue());
				for (ControllerForm c : controllerForms.values()) {
					 c.updateServerDataFromInputs();
				}
				okClicked = true;
				dialogStage.close();
				server.setFromServer(clone);
		  }
	 }

	 @FXML
	 private void cancel() {
		  dialogStage.close();
	 }
}
