package org.alessios18.jserversmanager.gui.controllers;

import org.alessios18.jserversmanager.JServersManagerApp;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.apache.logging.log4j.Logger;

public abstract class ControllerBase {

  // Reference to the main application
  protected GuiManager guiManager;

  public ControllerBase() {}

  public static String getFXMLFileName() {
    return null;
  }

  public static String getFXMLFileFullPath() {
    return GuiManager.FXML_FILE_PATH + getFXMLFileName();
  }

  public GuiManager getGuiManager() {
    return guiManager;
  }

  public void setGuiManager(GuiManager guiManager) {
    this.guiManager = guiManager;
  }

  public Logger getLogger() {
    return JServersManagerApp.getLogger();
  }
}
