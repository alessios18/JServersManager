package org.alessios18.jserversmanager.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.controllers.impl.MainWindowController;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

import java.io.IOException;

public class GuiManager extends Application {
  public static final String FXML_FILE_PATH = "/fxmlfiles/";
  private final ObservableList<Server> servers = FXCollections.observableArrayList();
  private Stage primaryStage;
  private VBox rootLayout;

  public void startGUI() {
    launch("");
  }

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("JServersManager");
    // this.primaryStage.getIcons().add(new
    // Image(getClass().getResourceAsStream("/images/icon_app.png")));

    initRootLayout();
  }

  public void initRootLayout() {
    try {
      // Load root layout from fxml file.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(GuiManager.class.getResource(MainWindowController.getFXMLFileFullPath()));
      rootLayout = loader.load();

      // Show the scene containing the root layout.
      Scene scene = new Scene(rootLayout);
      primaryStage.setScene(scene);

      // Give the controller access to the main app.
      MainWindowController controller = loader.getController();
      controller.setGuiManager(this);

      primaryStage.show();

      DataStorage.getInstance().loadServersDataFromFile(getServers());
      // loadTaskDataFromFile(DataStorage.getinstance().getTaskFile());
      primaryStage.setOnCloseRequest(
          (WindowEvent event1) -> {
            // do something before exit
            System.out.println("Mi sto chiudendo!");
          });
    } catch (IOException | UnsupportedOperatingSystemException e) {
      ExceptionDialog.showException(e);
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public ObservableList<Server> getServers() {
    return servers;
  }
}
