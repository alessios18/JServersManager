package org.alessios18.jserversmanager.gui.controllers.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.alessios18.jserversmanager.baseobjects.serverdata.CustomProperty;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.controllers.ControllerBase;
import org.alessios18.jserversmanager.gui.view.EditingCell;

import java.util.List;

public class AddModifyCustomPropertiesController extends ControllerBase {
  private final ObservableList<CustomProperty> properties = FXCollections.observableArrayList();
  @FXML private TableView<CustomProperty> propertiesTable;
  @FXML private TableColumn<CustomProperty, String> columnName;
  @FXML private TableColumn<CustomProperty, String> columnValue;
  private Stage dialogStage;
  private List<CustomProperty> serverProperties = FXCollections.observableArrayList();
  private boolean okClicked = false;

  public static String getFXMLFileName() {
    return "addModifyCustomProperties.fxml";
  }

  public static String getFXMLFileFullPath() {
    return GuiManager.FXML_FILE_PATH + getFXMLFileName();
  }



  /**
   * Sets the stage of this dialog.
   *
   * @param dialogStage
   */
  public void setDialogStage(Stage dialogStage) {
    this.dialogStage = dialogStage;
  }

  public void setServerProperties(List<CustomProperty> serverProperties) {
    properties.clear();
    properties.addAll(serverProperties);
    this.serverProperties = serverProperties;
  }



  protected void removeBlankProp() {
    for (CustomProperty p : properties) {
      if (p.getPropertyName().isEmpty() && p.getPropertyValue().isEmpty()) {
        properties.remove(p);
      }
    }
  }

  public boolean isOkClicked() {
    return okClicked;
  }

  @FXML
  private void initialize() {
    propertiesTable.setEditable(true);
    Callback<TableColumn<CustomProperty, String>, TableCell<CustomProperty, String>> cellFactory =
            (TableColumn<CustomProperty, String> param) -> new EditingCell();
    columnName.setCellValueFactory(cellData -> cellData.getValue().propertyNameProperty());
    columnName.setCellFactory(cellFactory);
    columnName.setOnEditCommit(
            (TableColumn.CellEditEvent<CustomProperty, String> t) ->
                    t.getTableView()
                            .getItems()
                            .get(t.getTablePosition().getRow())
                            .setPropertyName(t.getNewValue()));
    columnValue.setCellValueFactory(cellData -> cellData.getValue().propertyValueProperty());
    columnValue.setCellFactory(cellFactory);
    columnValue.setOnEditCommit(
            (TableColumn.CellEditEvent<CustomProperty, String> t) ->
                    t.getTableView()
                            .getItems()
                            .get(t.getTablePosition().getRow())
                            .setPropertyValue(t.getNewValue()));
    propertiesTable.setItems(properties);
  }

  @FXML
  private void handleSave() {
    removeBlankProp();
    this.serverProperties.clear();
    this.serverProperties.addAll(this.properties);
    okClicked = true;
    dialogStage.close();
  }

  @FXML
  private void handleCancel() {
    dialogStage.close();
  }

  @FXML
  private void handleAdd() {
    properties.add(new CustomProperty());
    propertiesTable.getSelectionModel().clearAndSelect(propertiesTable.getItems().size() - 1);
    propertiesTable.getFocusModel().focus(propertiesTable.getItems().size() - 1, columnName);
    propertiesTable.requestFocus();
  }

  @FXML
  private void handleRemove() {
    int selectedIndex = propertiesTable.getSelectionModel().getSelectedIndex();
    if (selectedIndex >= 0) {
      propertiesTable.getItems().remove(selectedIndex);
    } else {
      // Nothing selected.
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.initOwner(GuiManager.getPrimaryStage());
      alert.setTitle("No Selection");
      alert.setHeaderText("No Row Selected");
      alert.setContentText("Please select a row in the table.");
      alert.showAndWait();
    }
  }
}
