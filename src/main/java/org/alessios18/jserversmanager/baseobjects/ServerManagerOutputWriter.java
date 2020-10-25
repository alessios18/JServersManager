package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.gui.GuiManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class ServerManagerOutputWriter extends BufferedWriter {
    GuiManager guiManager;

    public ServerManagerOutputWriter(Writer out) {
        super(out);
    }

    public ServerManagerOutputWriter(Writer out, GuiManager guiManager) {
        super(out);
        this.guiManager = guiManager;
    }

    @Override
    public void write(String str) throws IOException {
        super.write(str);
        if (guiManager != null) {
            guiManager.appendOnTextArea(str);
        }
    }
}
