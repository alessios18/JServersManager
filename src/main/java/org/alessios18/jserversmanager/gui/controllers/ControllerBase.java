package org.alessios18.jserversmanager.gui.controllers;

import org.alessios18.jserversmanager.gui.GuiManager;

public abstract class ControllerBase {

    protected static String FXML_FILE_NEME = "";
    // Reference to the main application
    protected GuiManager guiManager;

    public ControllerBase() {
    }

    public static String getFXMLFileName() {
        return FXML_FILE_NEME;
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
}
