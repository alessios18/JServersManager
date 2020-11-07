package org.alessios18.jserversmanager.baseobjects.serverdata;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

	 public void setPropertyName(String propertyName) {
		  this.propertyName.set(propertyName);
	 }

	 public StringProperty propertyNameProperty() {
		  return propertyName;
	 }

	 public String getPropertyValue() {
		  return propertyValue.get();
	 }

	 public void setPropertyValue(String propertyValue) {
		  this.propertyValue.set(propertyValue);
	 }

	 public StringProperty propertyValueProperty() {
		  return propertyValue;
	 }
}
