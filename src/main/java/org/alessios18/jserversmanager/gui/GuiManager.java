package org.alessios18.jserversmanager.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.baseobjects.servermanagers.container.ServerManagersContainer;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.controllers.impl.MainWindowController;
import org.alessios18.jserversmanager.gui.controllers.impl.NewServerDialogController;
import org.alessios18.jserversmanager.gui.controllers.impl.OutputAreaController;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuiManager extends Application {
  public static final String FXML_FILE_PATH = "/fxmlfiles/";
  private static Stage primaryStage;
  private final ObservableList<Server> servers = FXCollections.observableArrayList();
  private MainWindowController controller;
  private Map<String, OutputAreaController> outputAreas = new HashMap<>();
  private ServerManagersContainer serverManagersContainer = new ServerManagersContainer();

  public static Stage getPrimaryStage() {
    return primaryStage;
  }

  public void startGUI() {
    launch("");
  }

  @Override
  public void start(Stage primaryStage) {
    GuiManager.primaryStage = primaryStage;
    GuiManager.primaryStage.setTitle("JServersManager");
    initRootLayout();
  }

  public void initRootLayout() {
    try {
      DataStorage.getInstance().loadServersDataFromFile(getServers());
      // Load root layout from fxml file.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(GuiManager.class.getResource(MainWindowController.getFXMLFileFullPath()));
      VBox rootLayout = loader.load();

      // Show the scene containing the root layout.
      Scene scene = new Scene(rootLayout);
      scene
          .getStylesheets()
          .add(GuiManager.class.getResource("/css/darkTheme.css").toExternalForm());
      primaryStage.setScene(scene);

      // Give the controller access to the main app.
      controller = loader.getController();
      controller.setGuiManager(this);
      controller.setServerListItems(servers);

      primaryStage.show();

      primaryStage.setOnCloseRequest(
          (WindowEvent event1) -> {
            try {
              doThingsOnExit();
            } catch (InterruptedException e) {
              ExceptionDialog.showException(e);
            }
          });
    } catch (IOException | UnsupportedOperatingSystemException | JAXBException e) {
      ExceptionDialog.showException(e);
    }
  }

  public void appendOnTextArea(Server server, String text) {
    controller.appendOnTextArea(server, text);
  }

  public ObservableList<Server> getServers() {
    return servers;
  }

  public boolean showNewServerDialog(Server server) {
    try {
      // Load the fxml file and create a new stage for the popup dialog.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          GuiManager.class.getResource(NewServerDialogController.getFXMLFileFullPath()));
      AnchorPane page = loader.load();

      // Create the dialog Stage.
      Stage dialogStage = new Stage();
      dialogStage.setTitle("New Server");
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(getPrimaryStage());
      Scene scene = new Scene(page);
      chooseDarkSide(scene);
      dialogStage.setScene(scene);

      NewServerDialogController newServerDialogController = loader.getController();
      newServerDialogController.setDialogStage(dialogStage);
      newServerDialogController.setGuiManager(this);
      newServerDialogController.setServer(server);
      // Show the dialog and wait until the user closes it
      dialogStage.showAndWait();

      return newServerDialogController.isOkClicked();
    } catch (IOException e) {
      ExceptionDialog.showException(e);
      return false;
    }
  }

  public void chooseDarkSide(Scene scene) {
    scene.getStylesheets().add(GuiManager.class.getResource("/css/darkTheme.css").toExternalForm());
  }

  public void chooseDarkSide(Parent parent) {
    parent
        .getStylesheets()
        .add(GuiManager.class.getResource("/css/darkTheme.css").toExternalForm());
  }

  public void setStyle(Parent parent) {
    chooseDarkSide(parent);
  }

  public void setStyle(Scene scene) {
    chooseDarkSide(scene);
  }

  public void chooseWhiteSide() {
    setUserAgentStylesheet(null);
  }

  public Map<String, OutputAreaController> getOutputAreas() {
    return outputAreas;
  }

  public void setOutputAreas(Map<String, OutputAreaController> outputAreas) {
    this.outputAreas = outputAreas;
  }

  public void startNewOutput(Server server) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource(OutputAreaController.getFXMLFileFullPath()));
    AnchorPane ta = loader.load();
    OutputAreaController outputAreaController = loader.getController();
    outputAreaController.setServerName(server.getServerName());
    outputAreas.put(server.getServerID(), outputAreaController);
    this.controller.changeOutputPanel(ta);
  }

  public ServerManagersContainer getServerManagersContainer() {
    return serverManagersContainer;
  }

  public void setServerManagersContainer(ServerManagersContainer serverManagersContainer) {
    this.serverManagersContainer = serverManagersContainer;
  }

  public void updateServerList() {
    controller.updateServerList();
  }

  public void doThingsOnExit() throws InterruptedException {
    serverManagersContainer.forceQuit();
    Platform.exit();
    System.exit(0);
  }

  public void openWebPage(Hyperlink link) {
    getHostServices().showDocument(link.getText());
  }
}
