package org.alessios18.jserversmanager.baseobjects;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Executor {
	 private final Runtime rt;
	 private final String[] commands;
	 private BufferedReader stdInput;
	 private BufferedReader stdError;

	 private Process currentProc;

	 public Executor(String[] commands) {
		  rt = Runtime.getRuntime();
		  this.commands = commands;
	 }

	 public void execute(String dir, BufferedWriter writer) throws IOException, InterruptedException {

		  ProcessBuilder pb = new ProcessBuilder(commands);
		  pb.directory(new File(dir));
		  currentProc = pb.start();
		  stdInput = new BufferedReader(new InputStreamReader(currentProc.getInputStream(), StandardCharsets.UTF_8));

		  stdError = new BufferedReader(new InputStreamReader(currentProc.getErrorStream()));

		  if (writer != null) {
				// Read the standard output from the command
				String s = null;
				while ((s = stdInput.readLine()) != null) {
					 writer.write(s + "\n");
					 writer.flush();
				}
				boolean firstError = true;
				// Read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					 if (firstError) {
						  System.out.print("Command: ");
						  for (int i = 0; i < commands.length; i++) {
								System.out.print(commands[i] + " ");
						  }
						  System.out.println();
						  firstError = false;
					 }
					 System.out.println(s);
				}
		  }
		  currentProc.waitFor();
	 }

	 public void forceQuit() {
		  if (currentProc.isAlive()) {
				currentProc.destroy();
		  }
	 }
}
