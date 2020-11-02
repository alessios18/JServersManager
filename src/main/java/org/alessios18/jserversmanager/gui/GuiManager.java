package org.alessios18.jserversmanager.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.baseobjects.ServerManagersContainer;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.controllers.impl.MainWindowController;
import org.alessios18.jserversmanager.gui.controllers.impl.NewServerDialogController;
import org.alessios18.jserversmanager.gui.controllers.impl.OutputAreaController;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class GuiManager extends Application {
	 public static final String FXML_FILE_PATH = "/fxmlfiles/";
	 private final ObservableList<Server> servers = FXCollections.observableArrayList();
	 private Stage primaryStage;
	 private VBox rootLayout;
	 private MainWindowController controller;
	 private HashMap<String, OutputAreaController> outputAreas = new HashMap<>();
	 private ServerManagersContainer serverManagersContainer = new ServerManagersContainer();

	 public void startGUI() {
		  launch("");
	 }

	 @Override
	 public void start(Stage primaryStage) {
		  this.primaryStage = primaryStage;
		  this.primaryStage.setTitle("JServersManager");
		  initRootLayout();
	 }

	 public void initRootLayout() {
		  try {
				DataStorage.getInstance().loadServersDataFromFile(getServers());
				// Load root layout from fxml file.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(GuiManager.class.getResource(MainWindowController.getFXMLFileFullPath()));
				rootLayout = loader.load();

				// Show the scene containing the root layout.
				Scene scene = new Scene(rootLayout);
				scene.getStylesheets().add(GuiManager.class.getResource("/css/darkTheme.css").toExternalForm());
				primaryStage.setScene(scene);

				// Give the controller access to the main app.
				controller = loader.getController();
				controller.setGuiManager(this);
				controller.setServerListItems(servers);

				primaryStage.show();

				// loadTaskDataFromFile(DataStorage.getinstance().getTaskFile());
				primaryStage.setOnCloseRequest(
						  (WindowEvent event1) -> {
								try {
									 doThingsOnExit();
								} catch (InterruptedException | IOException | ExecutionException e) {
									 ExceptionDialog.showException(e);
								}
						  });
		  } catch (IOException | UnsupportedOperatingSystemException e) {
				ExceptionDialog.showException(e);
		  }
	 }

	 public void appendOnTextArea(Server server, String text) {
		  controller.appendOnTextArea(server, text);
	 }

	 public Stage getPrimaryStage() {
		  return primaryStage;
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
				dialogStage.initOwner(this.getPrimaryStage());
				Scene scene = new Scene(page);
				scene.getStylesheets().add
						  (GuiManager.class.getResource("/css/darkTheme.css").toExternalForm());
				dialogStage.setScene(scene);

				// Set the person into the controller.
				NewServerDialogController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				controller.setServer(server);
				// Show the dialog and wait until the user closes it
				dialogStage.showAndWait();

				return controller.isOkClicked();
		  } catch (IOException e) {
				ExceptionDialog.showException(e);
				return false;
		  }
	 }

	 public HashMap<String, OutputAreaController> getOutputAreas() {
		  return outputAreas;
	 }

	 public void setOutputAreas(HashMap<String, OutputAreaController> outputAreas) {
		  this.outputAreas = outputAreas;
	 }

	 public void startNewOutput(String serverID) throws IOException {
		  FXMLLoader loader = new FXMLLoader();
		  loader.setLocation(
					 getClass().getResource(OutputAreaController.getFXMLFileFullPath()));
		  TextArea ta = loader.load();
		  OutputAreaController controller = loader.getController();
		  outputAreas.put(serverID, controller);
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

	 public void doThingsOnExit() throws InterruptedException, IOException, ExecutionException {
		  //serverManagersContainer.stopAllServers();
		  serverManagersContainer.forceQuit();
		  Platform.exit();
		  System.exit(0);
	 }

	 public void openWebPage(Hyperlink link) {
		  getHostServices().showDocument(link.getText());
	 }
}
