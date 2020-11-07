package org.alessios18.jserversmanager.gui.view;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import org.alessios18.jserversmanager.baseobjects.serverdata.CustomProperty;

public class EditingCell extends TableCell<CustomProperty, String> {

	 private TextField textField;

	 @Override
	 public void startEdit() {
		  if (!isEmpty()) {
				super.startEdit();
				createTextField();
				setText(null);
				setGraphic(textField);
				textField.selectAll();
		  }
	 }

	 @Override
	 public void cancelEdit() {
		  super.cancelEdit();

		  setText(getItem());
		  setGraphic(null);
	 }

	 @Override
	 public void updateItem(String item, boolean empty) {
		  super.updateItem(item, empty);

		  if (empty) {
				setText(item);
				setGraphic(null);
		  } else {
				if (isEditing()) {
					 if (textField != null) {
						  textField.setText(getString());
					 }
					 setText(null);
					 setGraphic(textField);
				} else {
					 setText(getString());
					 setGraphic(null);
				}
		  }
	 }

	 private void createTextField() {
		  textField = new TextField(getString());
		  textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		  textField.setOnAction(e -> commitEdit(textField.getText()));
		  textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
				if (!newValue) {
					 commitEdit(textField.getText());
				}
		  });
		  textField.requestFocus();
	 }

	 private String getString() {
		  return getItem() == null ? "" : getItem();
	 }
}