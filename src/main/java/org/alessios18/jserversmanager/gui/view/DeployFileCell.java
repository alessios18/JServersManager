package org.alessios18.jserversmanager.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.baseobjects.serverdata.DeployFile;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.impl.DeployFileController;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class DeployFileCell extends ListCell<DeployFile> {
	 private static final Logger logger = JServersManagerApp.getLogger();
	 private final GuiManager guiManager;
	 private DeployFileController deployFileController = null;
	 private final Stage dialogStage;

	 public DeployFileCell(GuiManager guiManager, Stage dialogStage) {
		  this.guiManager = guiManager;
		  this.dialogStage = dialogStage;
		  FXMLLoader mLLoader = new FXMLLoader(GuiManager.class.getResource(DeployFileController.getFXMLFileFullPath()));
		  try {
				mLLoader.load();
				deployFileController = mLLoader.getController();

		  } catch (IOException e) {
				e.printStackTrace();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				String exceptionText = sw.toString();
				logger.error(exceptionText);
		  }
	 }

	 @Override
	 protected void updateItem(DeployFile item, boolean empty) {
		  super.updateItem(item, empty);
		  if (empty) {
				setGraphic(null);
		  } else {
				deployFileController.setGuiManagerAndDeployFile(guiManager, item);
				deployFileController.setDialogStage(dialogStage);
				setGraphic(deployFileController.getView());
		  }
	 }

	 public DeployFileController getDeployFileController() {
		  return deployFileController;
	 }
}
