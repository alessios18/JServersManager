package org.alessios18.jserversmanager.baseobjects.processes;

import org.alessios18.jserversmanager.JServersManagerApp;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class ParallelizedProcess implements Callable<Void> {
  private static final Logger logger = JServersManagerApp.getLogger();
  private final ProcessBuilder processBuilder;
  private final BufferedWriter writer;
  private final ProcessManager processManager;
  private final String processId;
  private final boolean waitEnd;
  private boolean isRunning = false;
  private BufferedReader stdInput;
  private BufferedReader stdError;

  ParallelizedProcess(
      String processId,
      ProcessBuilder processBuilder,
      ProcessManager processManager,
      BufferedWriter writer,
      boolean waitEnd) {
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
      while ((s = stdError.readLine()) != null) {
        writer.write(s + "\n");
        writer.flush();
      }
    }
  }

  @Override
  public Void call() throws Exception {
    isRunning = true;
    Process process = processBuilder.start();
    logger.debug("[" + processId + "] is started");
    processManager.addProcess(this.processId, process);
    if (waitEnd) {
      synchronized (processBuilder) {
        processBuilder.notify();
      }
    }
    stdInput =
        new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
    stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    getProcessOutput(writer);
    int res = process.waitFor();
    processManager.removeProcess(processId);
    logger.debug("[" + processId + "] is ended with results: " + res);
    return null;
  }

  public boolean isRunning() {
    return isRunning;
  }
}
