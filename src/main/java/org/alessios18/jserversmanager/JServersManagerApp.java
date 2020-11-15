package org.alessios18.jserversmanager;

import org.alessios18.jserversmanager.baseobjects.DataStorage;
import org.alessios18.jserversmanager.exceptions.UnsupportedOperatingSystemException;
import org.alessios18.jserversmanager.gui.GuiManager;
import org.alessios18.jserversmanager.gui.view.ExceptionDialog;
import org.alessios18.jserversmanager.updater.JServersManagerUpdater;
import org.alessios18.jserversmanager.updater.baseobjects.Release;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/** Hello world! */
public class JServersManagerApp {
  private static final Logger logger = LogManager.getLogger(JServersManagerApp.class);

  public JServersManagerApp() {
    try {
      DataStorage.getInstance().checkFiles();
    } catch (UnsupportedOperatingSystemException | JAXBException e) {
      ExceptionDialog.showException(e);
    }
  }

  public static void main(String[] args) {
    Thread.currentThread().setContextClassLoader(JServersManagerApp.class.getClassLoader());
    try {
      JServersManagerUpdater updater = new JServersManagerUpdater();
      if (!updater.doUpgrade(args)) {
        Release r = updater.checkForUpdates();
        if (r != null) {
          if (updater.updateVersionConfirmDialog()) {
            updater.updateVersion();
          }
        }
      }
      startApplication();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected static void startApplication() {
    GuiManager guiManager = new GuiManager();
    guiManager.startGUI();
  }

  public static String getCurrentVersion() throws IOException, XmlPullParserException {
    Properties prop = new Properties();
    prop.load(
        new InputStreamReader(
            JServersManagerApp.class.getResourceAsStream("/config/config.properties")));
    return prop.getProperty("project.version");
  }

  public static Logger getLogger() {
    return logger;
  }
}
