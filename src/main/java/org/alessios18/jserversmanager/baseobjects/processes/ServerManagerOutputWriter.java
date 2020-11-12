package org.alessios18.jserversmanager.baseobjects.processes;

import javafx.application.Platform;
import org.alessios18.jserversmanager.baseobjects.serverdata.Server;
import org.alessios18.jserversmanager.gui.GuiManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class ServerManagerOutputWriter extends BufferedWriter {
  GuiManager guiManager;
  Server server;

  public ServerManagerOutputWriter(Writer out, Server server) {
    super(out);
    this.server = server;
  }

  public ServerManagerOutputWriter(Writer out, Server server, GuiManager guiManager) {
    super(out);
    this.server = server;
    this.guiManager = guiManager;
  }

  @Override
  public void write(String str) throws IOException {
    super.write(str);
    if (guiManager != null) {
      Platform.runLater(() -> guiManager.appendOnTextArea(server, str));
    }
  }
}
