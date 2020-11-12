package org.alessios18.jserversmanager.gui.controllers.listener;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import org.alessios18.jserversmanager.gui.GuiManager;

public class OpenWebPageListener implements EventHandler {
  Hyperlink link;
  GuiManager guiManager;

  public OpenWebPageListener(GuiManager guiManager, Hyperlink link) {
    this.link = link;
    this.guiManager = guiManager;
  }

  @Override
  public void handle(Event event) {
    guiManager.openWebPage(link);
  }
}
