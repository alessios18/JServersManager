package org.alessios18.jserversmanager;

import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.executor.ServerStarter;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;

import java.io.IOException;

/** Hello world! */
public class App {
  public App() {
    try {
      DataStorage.getInstance().checkFiles();
    } catch (UnsupportedOperatingSystemException e) {
      ExceptionDialog.showException(e);
    } catch (IOException e) {
      ExceptionDialog.showException(e);
    }
  }

  public static void main(String[] args) {
    GuiManager guiManager = new GuiManager();
    guiManager.startGUI();
  }

  public static void executor() throws IOException {
    ServerStarter ss = new ServerStarter("test1");
    ss.run();
  }
}
