package org.alessios18.jserversmanager.baseobjects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class ParallelizedProcess implements Callable<Void> {
	 private final ProcessBuilder processBuilder;
	 private final BufferedWriter writer;
	 private final ProcessManager processManager;
	 private final String processId;
	 private Process process;
	 private boolean isRunning = false;
	 private BufferedReader stdInput;
	 private BufferedReader stdError;
	 private final boolean waitEnd;

	 ParallelizedProcess(String processId, ProcessBuilder processBuilder, ProcessManager processManager, BufferedWriter writer, boolean waitEnd) {
		  this.processId = processId;
		  this.processBuilder = processBuilder;
		  this.writer = writer;
		  this.processManager = processManager;
		  this.waitEnd = waitEnd;
	 }

	 protected void getProcessOutput(BufferedWriter writer) throws IOException {
		  if (writer != null) {
				// Read the standard output from the command
				String s = null;
				while ((s = stdInput.readLine()) != null) {
					 writer.write(s + "\n");
					 writer.flush();
				}
				// Read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					 writer.write(s + "\n");
					 writer.flush();
				}
		  }
	 }

	 @Override
	 public Void call() throws Exception {
		  isRunning = true;
		  process = processBuilder.start();
		  System.out.println("[" + processId + "] is started");
		  processManager.addProcess(this.processId, process);
		  if (waitEnd) {
				synchronized (processBuilder) {
					 processBuilder.notify();
				}
		  }
		  stdInput = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
		  stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		  getProcessOutput(writer);
		  process.waitFor();
		  processManager.removeProcess(processId);
		  System.out.println("[" + processId + "] is ended");
		  return null;
	 }

	 public boolean isRunning() {
		  return isRunning;
	 }
}
