package org.alessios18.jserversmanager.baseobjects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JSMConfig {
	 private boolean onlyStable = true;

	 public JSMConfig() {
	 }

	 public boolean isOnlyStable() {
		  return onlyStable;
	 }

	 public void setOnlyStable(boolean onlyStable) {
		  this.onlyStable = onlyStable;
	 }
}
