/**
 *
 */
package org.alessios18.jserversmanager.gui.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/** @author alessio */
public class ExceptionDialog {
	 private static final Logger logger = JServersManagerApp.getLogger();

	 private ExceptionDialog() {
	 }

	 public static void showException(Exception ex) {
		  Alert alert = new Alert(AlertType.ERROR);
		  alert.setTitle("Exception Dialog");
		  alert.setHeaderText(ex.getMessage());
		  alert.setContentText(ex.getMessage());

		  // Create expandable Exception.
		  StringWriter sw = new StringWriter();
		  PrintWriter pw = new PrintWriter(sw);
		  ex.printStackTrace(pw);
		  String exceptionText = sw.toString();
		  logger.error(exceptionText);
		  Label label = new Label("The exception stacktrace was:");

		  TextArea textArea = new TextArea(exceptionText);
		  textArea.setEditable(false);
		  textArea.setWrapText(true);

		  textArea.setMaxWidth(Double.MAX_VALUE);
		  textArea.setMaxHeight(Double.MAX_VALUE);
		  GridPane.setVgrow(textArea, Priority.ALWAYS);
		  GridPane.setHgrow(textArea, Priority.ALWAYS);

		  GridPane expContent = new GridPane();
		  expContent.setMaxWidth(Double.MAX_VALUE);
		  expContent.add(label, 0, 0);
		  expContent.add(textArea, 0, 1);

		  // Set expandable Exception into the dialog pane.
		  alert.getDialogPane().setExpandableContent(expContent);

		  alert.showAndWait();
	 }

	 public static void showException(String messege, Exception ex) {
		  Alert alert = new Alert(AlertType.ERROR);
		  alert.setTitle(messege);
		  alert.setHeaderText(messege);
		  alert.setContentText(ex.getMessage());

		  // Create expandable Exception.
		  StringWriter sw = new StringWriter();
		  PrintWriter pw = new PrintWriter(sw);
		  ex.printStackTrace(pw);
		  String exceptionText = sw.toString();
		  logger.error(exceptionText);
		  Label label = new Label("The exception stacktrace was:");

		  TextArea textArea = new TextArea(exceptionText);
		  textArea.setEditable(false);
		  textArea.setWrapText(true);

		  textArea.setMaxWidth(Double.MAX_VALUE);
		  textArea.setMaxHeight(Double.MAX_VALUE);
		  GridPane.setVgrow(textArea, Priority.ALWAYS);
		  GridPane.setHgrow(textArea, Priority.ALWAYS);

		  GridPane expContent = new GridPane();
		  expContent.setMaxWidth(Double.MAX_VALUE);
		  expContent.add(label, 0, 0);
		  expContent.add(textArea, 0, 1);

		  // Set expandable Exception into the dialog pane.
		  alert.getDialogPane().setExpandableContent(expContent);

		  alert.showAndWait();
	 }
}
