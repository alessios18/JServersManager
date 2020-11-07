package org.alessios18.jserversmanager.baseobjects.serverdata;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.alessios18.jserversmanager.baseobjects.enums.ServerType;

import java.util.UUID;

public class CustomProperty {
	 private final StringProperty propertyName;
	 private final StringProperty propertyValue;

	 public CustomProperty() {
		  propertyName = new SimpleStringProperty("");
		  propertyValue = new SimpleStringProperty("");
	 }

	 public String getPropertyName() {
		  return propertyName.get();
	 }

	 public StringProperty propertyNameProperty() {
		  return propertyName;
	 }

	 public void setPropertyName(String propertyName) {
		  this.propertyName.set(propertyName);
	 }

	 public String getPropertyValue() {
		  return propertyValue.get();
	 }

	 public StringProperty propertyValueProperty() {
		  return propertyValue;
	 }

	 public void setPropertyValue(String propertyValue) {
		  this.propertyValue.set(propertyValue);
	 }
}
