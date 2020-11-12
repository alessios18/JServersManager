package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.serverdata.serverconfig.JBossServerConfig;
import org.alessios18.jserversmanager.baseobjects.servermanagers.ServerManagerBase;
import org.alessios18.jserversmanager.baseobjects.servermanagers.factory.ServerManagerFactory;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerForm;
import org.alessios18.jserversmanager.gui.util.ImagesLoader;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.gui.view.JSMFileChooser;

import java.io.IOException;

public class JBOSSFormController extends ControllerForm {

  @FXML private AnchorPane root;
  @FXML private TextField srvDir;
  @FXML private TextField standalonePath;
  @FXML private TextField configDir;
  @FXML private TextField adminPort;
  @FXML private TextField debugPort;
  @FXML private TextField portOffset;
  @FXML private TextField httpPort;
  @FXML private TextArea arguments;
  @FXML private CheckBox enableCustomArgs;
  @FXML private Button bSrvDir;
  @FXML private Button bStandalone;
  @FXML private Button bConfigDir;
  @FXML private Label customPropertiesCount;
  @FXML private Label deployCount;
  @FXML private TextField configName;

  private Server clone;

  private JBossServerConfig serverConfig;

  public static String getFXMLFileName() {
    return "jbossFormView.fxml";
  }

  public static String getFXMLFileFullPath() {
    return GuiManager.FXML_FILE_PATH + getFXMLFileName();
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
    debugPort
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              serverConfig.setDebugPort(newValue);
              updateCustomArgs();
            });
    httpPort
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              serverConfig.setHttpPort(newValue);
              updateCustomArgs();
            });
    portOffset
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              serverConfig.setPortOffset(newValue);
              updateCustomArgs();
            });
    adminPort
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              serverConfig.setAdminPort(newValue);
              updateCustomArgs();
            });
    srvDir
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              serverConfig.setServerPath(newValue);
              updateCustomArgs();
            });
    configDir
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              serverConfig.setConfigDir(newValue);
              updateCustomArgs();
            });
  }

  private void updateCustomArgs() {
    ServerManagerBase manager =
        ServerManagerFactory.getServerManager(clone, serverConfig.getConfigID());
    if (manager != null) {
      arguments.setText(manager.getServerParameters());
    }
  }

  public ImageView getImageView(Image image) {
    ImageView view = new ImageView(image);
    view.setFitHeight(26);
    view.setFitWidth(26);
    return view;
  }

  @Override
  public Node getView() {
    return root;
  }

  public void updateServerDataFromInputs() {
    serverConfig.setServerPath(srvDir.getText());
    serverConfig.setStandalonePath(standalonePath.getText());
    serverConfig.setAdminPort(adminPort.getText());
    serverConfig.setDebugPort(debugPort.getText());
    serverConfig.setPortOffset(portOffset.getText());
    serverConfig.setHttpPort(httpPort.getText());
    serverConfig.setConfigDir(configDir.getText());
    serverConfig.setConfigName(configName.getText());
    if (serverConfig.isCustomArgs()) {
      serverConfig.setCustomArgsValue(arguments.getText());
    } else {
      serverConfig.setCustomArgsValue(null);
    }
  }

  public void setServer(Server server) {
    this.clone = server;
    serverConfig = (JBossServerConfig) clone.getServerConfigBaseById(this.getConfigId());
    srvDir.setText(serverConfig.getServerPath());
    standalonePath.setText(serverConfig.getStandalonePath());
    adminPort.setText(serverConfig.getAdminPort());
    debugPort.setText(serverConfig.getDebugPort());
    portOffset.setText(serverConfig.getPortOffset());
    httpPort.setText(serverConfig.getHttpPort());
    configDir.setText(serverConfig.getConfigDir());
    deployCount.setText(serverConfig.getFilePathToDeploy().size() + "");
    customPropertiesCount.setText(serverConfig.getCustomProperties().size() + "");
    configName.setText(serverConfig.getConfigName());
    if (server.getServerType() != null) {
      arguments.setText(
          ServerManagerFactory.getServerManager(clone, serverConfig.getConfigID())
              .getServerParameters());
    }
    enableCustomArgs.setSelected(serverConfig.isCustomArgs());
    if (serverConfig.getCustomArgsValue() != null && !serverConfig.getCustomArgsValue().isEmpty()) {
      arguments.setText(serverConfig.getCustomArgsValue());
    }
  }


  public void showAddModifyDeployFiles() {
    try {
      // Load the fxml file and create a new stage for the popup dialog.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          GuiManager.class.getResource(DeploymentListController.getFXMLFileFullPath()));
      AnchorPane page = loader.load();

      // Create the dialog Stage.
      Stage deplotFileDialogStage = new Stage();
      deplotFileDialogStage.setTitle("Add Modify Custom Properties");
      deplotFileDialogStage.initModality(Modality.WINDOW_MODAL);
      deplotFileDialogStage.initOwner(this.getDialogStage());
      Scene scene = new Scene(page);
      deplotFileDialogStage.setScene(scene);
      getGuiManager().chooseDarkSide(scene);

      // Set the person into the controller.
      DeploymentListController controller = loader.getController();
      controller.setDialogStage(deplotFileDialogStage);
      controller.setGuiManager(guiManager);
      controller.setDeploymentFileItems(serverConfig.getFilePathToDeploy());
      // Show the dialog and wait until the user closes it
      deplotFileDialogStage.showAndWait();

    } catch (IOException e) {
      ExceptionDialog.showException(e);
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
      Stage propertyDialogStage = new Stage();
      propertyDialogStage.setTitle("Add Modify Custom Properties");
      propertyDialogStage.initModality(Modality.WINDOW_MODAL);
      propertyDialogStage.initOwner(this.getDialogStage());
      Scene scene = new Scene(page);
      propertyDialogStage.setScene(scene);
      getGuiManager().chooseDarkSide(scene);

      // Set the person into the controller.
      AddModifyCustomPropertiesController controller = loader.getController();
      controller.setDialogStage(propertyDialogStage);
      controller.setGuiManager(guiManager);
      controller.setServerProperties(serverConfig.getCustomProperties());
      // Show the dialog and wait until the user closes it
      propertyDialogStage.showAndWait();

      return controller.isOkClicked();
    } catch (IOException e) {
      ExceptionDialog.showException(e);
      return false;
    }
  }
  @FXML
  private void initialize() {
    AnchorPane.setBottomAnchor(root, 0.0);
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    AnchorPane.setTopAnchor(root, 0.0);
    bSrvDir.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
    bStandalone.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
    bConfigDir.setGraphic(getImageView(ImagesLoader.getFolderIcon()));
    enableCustomArgs
            .selectedProperty()
            .addListener(
                    (ov, oldValue, newValue) -> {
                      serverConfig.setCustomArgs(newValue);
                      isCustomArgsEnable(newValue);
                    });
    configName
            .textProperty()
            .addListener(
                    (observable, oldValue, newValue) -> {
                      serverConfig.setConfigName(newValue);
                      if (getOwner() != null) {
                        ((NewServerDialogController) getOwner()).reloadComboConfig();
                      }
                      Platform.runLater(() -> configName.requestFocus());
                    });
    setPageListener();
  }

  @FXML
  private void handleSetSrvDir() {
    JSMFileChooser dc = new JSMFileChooser(srvDir);
    dc.show(
            "Choose the server Directory",
            this.getDialogStage(),
            JSMFileChooser.SelectionType.DIRECTORY);
  }

  @FXML
  private void handleSetStandalone() {
    JSMFileChooser dc = new JSMFileChooser(standalonePath);
    dc.show(
            "Choose the standalone.xml file", this.getDialogStage(), JSMFileChooser.SelectionType.FILE);
  }

  @FXML
  private void handleSetConfigDir() {
    JSMFileChooser dc = new JSMFileChooser(configDir);
    dc.show(
            "Choose the server config directory",
            this.getDialogStage(),
            JSMFileChooser.SelectionType.DIRECTORY);
  }

  @FXML
  private void handleDeployDialog() {
    showAddModifyDeployFiles();
    deployCount.setText(serverConfig.getFilePathToDeploy().size() + "");
  }

  @FXML
  private void handleModCustomProp() {
    showAddModifyCustomProperties();
    customPropertiesCount.setText(serverConfig.getCustomProperties().size() + "");
    updateCustomArgs();
  }
}
