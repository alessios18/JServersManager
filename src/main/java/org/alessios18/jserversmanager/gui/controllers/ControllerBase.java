package org.alessios18.jserversmanager.gui.controllers;

import org.alessios18.jserversmanager.gui.GuiManager;

public abstract class ControllerBase {

  // Reference to the main application
  protected GuiManager guiManager;

  public ControllerBase() {}

  public static String getFXMLFileName() {
    return null;
  }

  public static String getFXMLFileFullPath() {
    return null;
  }

  public GuiManager getGuiManager() {
    return guiManager;
  }

  public void setGuiManager(GuiManager guiManager) {
    this.guiManager = guiManager;
  }
}
