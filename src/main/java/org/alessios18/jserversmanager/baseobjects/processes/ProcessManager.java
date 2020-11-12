package org.alessios18.jserversmanager.baseobjects.processes;

import org.alessios18.jserversmanager.JServersManagerApp;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class ProcessManager {
  private static final Logger logger = JServersManagerApp.getLogger();

  private final Map<String, Process> processesContainer = new HashMap<>();
  private final ArrayList<Future<Void>> futureList = new ArrayList<>();
  private final ExecutorService executor = Executors.newCachedThreadPool();

  public ProcessManager() {}

  public void forceQuit() {
    for (String id : getProcesses().keySet()) {
      Process p = getProcesses().get(id);
      logger.debug("[" + id + "] force kill:STARTED");
      ProcessHandle processHandle = p.toHandle();
      killProcessAndChildren(id, processHandle, 0);
      logger.debug("[" + id + "] force kill:DONE");
    }
    futureList.forEach(f -> f.cancel(true));
  }

  private void killProcessAndChildren(String id, ProcessHandle processHandle, int level) {
    Stream<ProcessHandle> children = processHandle.children();
    long childrenCount = children.count();
    logger.debug("[" + id + "] have " + childrenCount + " children of level " + level);
    children = processHandle.children();
    children.forEach(
        processHandle1 -> {
          killProcessAndChildren(id, processHandle1, level + 1);
          processHandle1.destroyForcibly();
        });
    logger.debug("[" + id + "] all " + childrenCount + " of level " + level + " has been killed");
    processHandle.destroyForcibly();
  }

  public void executeParallelProcess(
      String[] commands, String dir, BufferedWriter writer, boolean waitEnd)
      throws InterruptedException {
    String processId = UUID.randomUUID().toString();
    ProcessBuilder pb = new ProcessBuilder(commands);
    pb.directory(new File(dir));
    logger.debug("[" + processId + "] " + getSingleRowCommand(commands));
    ParallelizedProcess parallelizer =
        new ParallelizedProcess(processId, pb, this, writer, waitEnd);
    Future<Void> future = executor.submit(parallelizer);
    futureList.add(future);

    if (waitEnd) {
      synchronized (pb) {
        boolean keepWait = true;
        while (keepWait) {
          pb.wait(3000);
          keepWait = false;
        }
        if (processesContainer.get(processId).isAlive()) {
          int result = processesContainer.get(processId).waitFor();
          if (result == 0) {
            logger.debug("[" + processId + "] Stop Server:DONE");
          } else {
            logger.error("[" + processId + "] Stop Server:ERROR");
            this.forceQuit();
          }
        }
      }
    }
  }

  public synchronized Map<String, Process> getProcesses() {
    return processesContainer;
  }

  public synchronized void addProcess(String processId, Process p) {
    processesContainer.put(processId, p);
  }

  public synchronized void removeProcess(String processId) {
    processesContainer.remove(processId);
  }

  private String getSingleRowCommand(String[] commands) {
    StringBuilder sb = new StringBuilder();
    for (String s : commands) {
      sb.append(s + " ");
    }
    return sb.toString();
  }
}
