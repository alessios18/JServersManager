package org.alessios18.jserversmanager.gui.view;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.alessios18.jserversmanager.util.OsUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class JSMFileChooser {
	 private final TextField textField;

	 public JSMFileChooser(TextField textField) {
		  this.textField = textField;
	 }

	 public void show(String title, Stage primaryStage, SelectionType sel) {
		  File choosed = null;
		  if (sel.equals(SelectionType.DIRECTORY)) {
				choosed = showDirectortyChooser(title, primaryStage);
		  } else if (sel.equals(SelectionType.FILE)) {
				choosed = showFileChooser(title, primaryStage);
		  } else if (sel.equals(SelectionType.FILE_AND_DIRECTORY)) {
				choosed = showFileDirectoryChooser(title);
		  }
		  if (choosed != null) {
				textField.setText(choosed.getAbsolutePath());
		  }
	 }

	 protected File showDirectortyChooser(String title, Stage primaryStage) {
		  DirectoryChooser directoryChooser = new DirectoryChooser();
		  directoryChooser.setTitle(title);
		  directoryChooser.setInitialDirectory(getInitialDirectory());
		  return directoryChooser.showDialog(primaryStage);
	 }

	 protected File showFileChooser(String title, Stage primaryStage) {
		  FileChooser fileChooser = new FileChooser();
		  fileChooser.setTitle(title);
		  fileChooser.setInitialDirectory(getInitialDirectory());
		  fileChooser.getExtensionFilters().addAll(
					 new FileChooser.ExtensionFilter("XML Files ( *.xml )", "*.xml"),
					 new FileChooser.ExtensionFilter("All Files ( *.* )", "*.*"));
		  return fileChooser.showOpenDialog(primaryStage);
	 }

	 protected File showFileDirectoryChooser(String title) {
		  JFileChooser fileChooser = new JFileChooser();
		  fileChooser.setDialogTitle(title);
		  fileChooser.setCurrentDirectory(getInitialDirectory());
		  fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		  FileFilter xmlFilter = new FileNameExtensionFilter(".docx", "XML Files ( *.xml )");
		  FileFilter allFilter = new FileNameExtensionFilter("*.*", "All Files ( *.* )");
		  fileChooser.addChoosableFileFilter(xmlFilter);
		  fileChooser.addChoosableFileFilter(allFilter);
		  int response = fileChooser.showOpenDialog(null);
		  if (response == JFileChooser.APPROVE_OPTION) {
				return fileChooser.getSelectedFile();
		  } else {
				return null;
		  }
	 }

	 protected File getInitialDirectory() {
		  File file = new File(textField.getText());
		  if (!textField.getText().isEmpty() && file.exists()) {
				if (file.isDirectory()) {
					 return file;
				} else {
					 if (file.getParentFile().exists()) {
						  return file.getParentFile();
					 }
				}
		  }
		  return OsUtils.getUserHomeAsFile();
	 }

	 public enum SelectionType {
		  FILE,
		  DIRECTORY,
		  FILE_AND_DIRECTORY
	 }
}
