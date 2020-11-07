package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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

import java.io.IOException;

@SuppressWarnings("unchecked")
public class MainWindowController extends ControllerBase {
	 protected static final String FXML_FILE_NAME = "MainWindow.fxml";

	 @FXML
	 private ListView<Server> serverList;
	 @FXML
	 private AnchorPane outputPane;

	 public static String getFXMLFileName() {
		  return FXML_FILE_NAME;
	 }

	 public static String getFXMLFileFullPath() {
		  return GuiManager.FXML_FILE_PATH + getFXMLFileName();
	 }

	 @FXML
	 private void initialize() {
		  serverList.setCellFactory(param -> new ServerCell(guiManager));
		  serverList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Server>() {

				@Override
				public void changed(ObservableValue<? extends Server> observable, Server oldValue, Server newValue) {
					 // Your action here
					 try {
						  if (newValue != null) {
								changeOutputPanel(newValue.getServerID());
						  }
					 } catch (Exception e) {
						  ExceptionDialog.showException(e);
					 }
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

		  TextFlow textFlow = new TextFlow(
					 new Text("JServersManager version:" + JServersManagerApp.getCurrentVersion() + "\nAuthor: Alessio Segantin\nProject repository: "),
					 repo,
					 new Text("\nWebsite: "),
					 gitHubLink,
					 new Text("\nIcons made by "),
					 iconsLink,
					 new Text(" from "),
					 iconSiteLink
		  );
		  alert.getDialogPane().setContent(textFlow);
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
					 getGuiManager().getServers().add(server);

					 DataStorage.getInstance().saveServerToFile(getGuiManager().getServers());

				} catch (UnsupportedOperatingSystemException e) {
					 ExceptionDialog.showException(e);
				}
		  }
	 }

	 @Override
	 public void setGuiManager(GuiManager guiManager) {
		  super.setGuiManager(guiManager);
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

	 public void appendOnTextArea(Server server, String text) {
		  OutputAreaController contr = guiManager.getOutputAreas().get(server.getServerID());
		  ((TextArea) contr.getView()).appendText(text);
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

	 public void changeOutputPanel(TextArea ta) throws IOException {
		  this.outputPane.getChildren().clear();
		  this.outputPane.getChildren().add(ta);
	 }

	 public void changeOutputPanel(String serverID) throws IOException {
		  if (guiManager.getOutputAreas().containsKey(serverID)) {
				changeOutputPanel((TextArea) guiManager.getOutputAreas().get(serverID).getView());
		  } else {
				guiManager.startNewOutput(serverID);
		  }
	 }

	 public void updateServerList() {
		  //Can I do it better?
		  for (int i = 0; i < guiManager.getServers().size(); i++) {
				serverList.fireEvent(new ListView.EditEvent<Server>(serverList, ListView.editCommitEvent(), guiManager.getServers().get(i), i));
		  }
	 }
}
