/** */
package org.alessios18.jserversmanager.gui.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/** @author alessio */
public class ExceptionDialog {
  private static final Logger logger = JServersManagerApp.getLogger();

  private ExceptionDialog() {}

  public static void showException(Exception ex) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("An error has occurred");
    alert.setHeaderText(ex.getMessage());
    alert.setContentText(ex.getMessage());
    alert.initOwner(GuiManager.getPrimaryStage());
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
    expContent.add(getTextFlow(ex.getMessage()), 0, 0);
    expContent.add(label, 0, 1);
    expContent.add(textArea, 0, 2);
    // Set expandable Exception into the dialog pane.
    alert.getDialogPane().setExpandableContent(expContent);

    alert.showAndWait();
  }

  public static void showException(String messege, Exception ex) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("An error has occurred");
    alert.setHeaderText(messege);
    alert.setContentText(getTextFlow(messege).getAccessibleText());
    alert.initOwner(GuiManager.getPrimaryStage());
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

  public static TextFlow getTextFlow(String message) {
    TextFlow textFlow =
        new TextFlow(
            getAlertText(new Text("An error has occurred\n")),
            getAlertText(
                new Text("Please consider to open a issue related to this error at the link:\n")),
            getNewIssueLink(),
            getAlertText(new Text("\n")),
            getAlertText(new Text("Error message:\n")),
            getAlertText(new Text(message)));
    return textFlow;
  }

  private static Text getAlertText(Text t) {
    t.getStyleClass().add("text-id");
    return t;
  }

  public static Hyperlink getNewIssueLink() {
    Hyperlink newIssue = new Hyperlink();
    newIssue.setText("https://github.com/alessios18/JServersManager/issues");
    return newIssue;
  }
}
