package org.alessios18.jserversmanager.executor;

import java.io.IOException;

public class ServerStarter extends Thread {
  public ServerStarter(String name) {
    super(name);
  }

  @Override
  public void run() {
    super.run();
    String[] commands = {"./standalone.sh"};
    Executor executor = new Executor(commands);
    try {
      executor.execute();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
