package org.alessios18.jserversmanager.gui.util;

import javafx.scene.image.Image;
import org.alessios18.jserversmanager.gui.controllers.impl.ServerViewController;

public class ImagesLoader {
	 protected static final String IMAGES_FOLDER = "/images/";
	 protected static final String IMAGE_PLAY = IMAGES_FOLDER + "013-play.png";
	 protected static final String IMAGE_RESTART = IMAGES_FOLDER + "015-refresh.png";
	 protected static final String IMAGE_STOP = IMAGES_FOLDER + "003-stop.png";
	 protected static final String IMAGE_FOLDER = IMAGES_FOLDER + "028-folder.png";
	 protected static final String IMAGE_SETTINGS = IMAGES_FOLDER + "020-menu.png";
	 protected static final String IMAGE_BLINK_ON = IMAGES_FOLDER + "blink-on.png";
	 protected static final String IMAGE_BLINK_OFF = IMAGES_FOLDER + "blink-off.png";
	 private static final Image playIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_PLAY));
	 private static final Image folderIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_FOLDER));
	 private static final Image restartIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_RESTART));
	 private static final Image stopIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_STOP));
	 private static final Image settingsIcon = new Image(ServerViewController.class.getResourceAsStream(IMAGE_SETTINGS));
	 private static final Image outputOn = new Image(ServerViewController.class.getResourceAsStream(IMAGE_BLINK_ON));
	 private static final Image outputOff = new Image(ServerViewController.class.getResourceAsStream(IMAGE_BLINK_OFF));
	 private ImagesLoader() {
	 }

	 public static Image getPlayIcon() {
		  return playIcon;
	 }

	 public static Image getFolderIcon() {
		  return folderIcon;
	 }

	 public static Image getRestartIcon() {
		  return restartIcon;
	 }

	 public static Image getStopIcon() {
		  return stopIcon;
	 }

	 public static Image getSettingsIcon() {
		  return settingsIcon;
	 }

	 public static Image getOutputOn() {
		  return outputOn;
	 }

	 public static Image getOutputOff() {
		  return outputOff;
	 }
}
