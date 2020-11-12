package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;

public class OutputAreaController extends ControllerBase {
  protected static final String FXML_FILE_NAME = "outputPane.fxml";
  @FXML private AnchorPane root;
  @FXML private TextArea outputArea;
  @FXML private Label serverName;

  public static String getFXMLFileName() {
    return FXML_FILE_NAME;
  }

  public static String getFXMLFileFullPath() {
    return GuiManager.FXML_FILE_PATH + getFXMLFileName();
  }

  public Node getView() {
    return root;
  }



  public void setServerName(String serverName) {
    this.serverName.setText(serverName);
  }

  public TextArea getOutputArea() {
    return outputArea;
  }

  @FXML
  private void initialize() {
    AnchorPane.setBottomAnchor(root, 5.0);
    AnchorPane.setLeftAnchor(root, 5.0);
    AnchorPane.setRightAnchor(root, 5.0);
    AnchorPane.setTopAnchor(root, 5.0);
    outputArea
            .textProperty()
            .addListener(
                    new ChangeListener<String>() {
                      @Override
                      public void changed(
                              ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        outputArea.setScrollTop(Double.MAX_VALUE);
                      }
                    });
  }
}
