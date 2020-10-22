package org.alessios18.jserversmanager.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Executor {
  private final Runtime rt;
  private final String[] commands;
  private BufferedReader stdInput;
  private BufferedReader stdError;

  public Executor(String[] commands) {
    rt = Runtime.getRuntime();
    this.commands = commands;
  }

  public void execute() throws IOException {

    Process p = null;
    ProcessBuilder pb = new ProcessBuilder(commands);
    pb.directory(new File("/home/alessio/projects/servers/test_server_1/bin"));
    Process proc = pb.start();

    stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

    stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

    // Read the output from the command
    System.out.println("Here is the standard output of the command:\n");
    String s = null;
    while ((s = stdInput.readLine()) != null) {
      System.out.println(s);
    }

    // Read any errors from the attempted command
    System.out.println("Here is the standard error of the command (if any):\n");
    while ((s = stdError.readLine()) != null) {
      System.out.println(s);
    }
  }
}
