package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.controllers.listener.OpenWebPageListener;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.gui.view.ServerCell;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class MainWindowController extends ControllerBase {
  protected static final String FXML_FILE_NAME = "MainWindow.fxml";

  @FXML private ListView<Server> serverList;
  @FXML private AnchorPane outputPane;

  public static String getFXMLFileName() {
    return FXML_FILE_NAME;
  }

  public static String getFXMLFileFullPath() {
    return GuiManager.FXML_FILE_PATH + getFXMLFileName();
  }



  private Text getAlertText(Text t) {
    t.getStyleClass().add("text-id");
    return t;
  }



  @Override
  public void setGuiManager(GuiManager guiManager) {
    super.setGuiManager(guiManager);
  }

  public void setServerListItems(ObservableList<Server> servers) {
    serverList.setItems(servers);
  }

  /** Closes the application. */
  @FXML
  private void handleExit() {
    System.exit(0);
  }

  public void appendOnTextArea(Server server, String text) {
    OutputAreaController contr = guiManager.getOutputAreas().get(server.getServerID());
    contr.getOutputArea().appendText(text);
    for (int i = 0; i < serverList.getItems().size(); i++) {
      if (serverList.getItems().get(i).equals(server)) {
        ServerCell sc = getListCell(serverList, i);
        sc.getServerViewController().outputBlink();
        break;
      }
    }
  }

  public ServerCell getListCell(ListView list, int index) {
    Object[] cells = list.lookupAll(".cell").toArray();
    return (ServerCell) cells[index];
  }

  public void changeOutputPanel(Node node) throws IOException {
    this.outputPane.getChildren().clear();
    this.outputPane.getChildren().add(node);
  }

  public void changeOutputPanel(Server server) throws IOException {
    if (guiManager.getOutputAreas().containsKey(server.getServerID())) {
      changeOutputPanel(guiManager.getOutputAreas().get(server.getServerID()).getView());
    } else {
      guiManager.startNewOutput(server);
    }
  }

  public void updateServerList() {
    // Can I do it better?
    for (int i = 0; i < guiManager.getServers().size(); i++) {
      serverList.fireEvent(
          new ListView.EditEvent<Server>(
              serverList, ListView.editCommitEvent(), guiManager.getServers().get(i), i));
    }
  }


  @FXML
  private void handleAbout() throws IOException, XmlPullParserException {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("JTaskLogger");
    alert.setHeaderText("About");

    Hyperlink gitHubLink = new Hyperlink();
    gitHubLink.setText("https://github.com/alessios18");
    //noinspection unchecked
    gitHubLink.setOnAction(new OpenWebPageListener(guiManager, gitHubLink));
    Hyperlink iconsLink = new Hyperlink();
    iconsLink.setText("https://www.flaticon.com/authors/freepik");
    iconsLink.setOnAction(new OpenWebPageListener(guiManager, iconsLink));
    Hyperlink repo = new Hyperlink();
    repo.setText("https://github.com/alessios18/JServersManager");
    repo.setOnAction(new OpenWebPageListener(guiManager, repo));
    Hyperlink iconSiteLink = new Hyperlink();
    iconSiteLink.setText("https://www.flaticon.com/");
    iconSiteLink.setOnAction(new OpenWebPageListener(guiManager, iconSiteLink));

    TextFlow textFlow =
            new TextFlow(
                    getAlertText(
                            new Text(
                                    "JServersManager version:"
                                            + JServersManagerApp.getCurrentVersion()
                                            + "\nAuthor: Alessio Segantin\nProject repository: ")),
                    repo,
                    getAlertText(new Text("\nWebsite: ")),
                    gitHubLink,
                    getAlertText(new Text("\nIcons made by ")),
                    iconsLink,
                    getAlertText(new Text(" from ")),
                    iconSiteLink);
    alert.getDialogPane().setContent(textFlow);
    alert.getDialogPane().getStyleClass().add("aboutAlert");
    alert.initOwner(GuiManager.getPrimaryStage());
    alert.setWidth(250);
    alert.setHeight(150);
    alert.showAndWait();
  }
  @FXML
  private void handleNewServer() {
    Server server = new Server();
    boolean okClicked = guiManager.showNewServerDialog(server);
    if (okClicked) {
      try {
        long startTime = System.currentTimeMillis();
        getGuiManager().getServers().add(server);
        getLogger()
                .debug("ServerList updated in: " + (System.currentTimeMillis() - startTime) + " ms.");
        startTime = System.currentTimeMillis();
        DataStorage.getInstance().saveServerToFile(getGuiManager().getServers());
        getLogger()
                .debug("Server updated in: " + (System.currentTimeMillis() - startTime) + " ms.");

      } catch (UnsupportedOperatingSystemException | JAXBException e) {
        ExceptionDialog.showException(e);
      }
    }
  }
  @FXML
  private void initialize() {
    serverList.setCellFactory(param -> new ServerCell(guiManager));
    serverList
            .getSelectionModel()
            .selectedItemProperty()
            .addListener(
                    new ChangeListener<Server>() {

                      @Override
                      public void changed(
                              ObservableValue<? extends Server> observable, Server oldValue, Server newValue) {
                        try {
                          if (newValue != null) {
                            changeOutputPanel(newValue);
                          }
                        } catch (Exception e) {
                          ExceptionDialog.showException(e);
                        }
                      }
                    });
  }
}
