package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.Server;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.gui.view.ServerCell;

import java.io.IOException;

public class MainWindowController extends ControllerBase {

    @FXML
    private ListView<Server> serverList;
    @FXML
    private TextArea outputArea;

    public static String getFXMLFileName() {
        return "MainWindow.fxml";
    }

    public static String getFXMLFileFullPath() {
        return GuiManager.FXML_FILE_PATH + getFXMLFileName();
    }

    @FXML
    private void initialize() {
        serverList.setCellFactory(new Callback<ListView<Server>, ListCell<Server>>() {
            @Override
            public ListCell<Server> call(ListView<Server> param) {

                return new ServerCell(guiManager);
            }
        });
    }
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JTaskLogger");
        alert.setHeaderText("About");
        alert.setContentText(
                "Author: Alessio Segantin\nRepo: https://github.com/alessios18/JServersManager\nWebsite: https://github.com/alessios18");
        alert.showAndWait();
    }

    @FXML
    private void handleNewServer() {
        Server server = new Server();
        boolean okClicked = showNewServerDialog(server);
        if (okClicked) {
            try {
                getGuiManager().getServers().add(server);

                DataStorage.getInstance().saveServerToFile(getGuiManager().getServers());

            } catch (UnsupportedOperatingSystemException e) {
                ExceptionDialog.showException(e);
            } catch (IOException e) {
                ExceptionDialog.showException(e);
            }
        }
    }

    private boolean showNewServerDialog(Server server) {
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
            dialogStage.initOwner(guiManager.getPrimaryStage());
            Scene scene = new Scene(page);
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

    public void setServerListItems(ObservableList<Server> servers) {
        serverList.setItems(servers);
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    public void appendOnTextArea(String text){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                outputArea.setText(outputArea.getText()+text);
            }
        });

        t.run();
    }
}
