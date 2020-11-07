package org.alessios18.jserversmanager.baseobjects.serverdata;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeployFile {
	 private StringProperty path;

	 public DeployFile() {
		  this.path = new SimpleStringProperty("");
	 }

	 public String getPath() {
		  return path.get();
	 }

	 public StringProperty pathProperty() {
		  return path;
	 }

	 public void setPath(String path) {
		  this.path.set(path);
	 }
}
