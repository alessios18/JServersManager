package org.alessios18.jserversmanager.baseobjects;

import org.alessios18.jserversmanager.JServersManagerApp;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ProcessManager {
	 private static final Logger logger = JServersManagerApp.getLogger();

	 private final HashMap<String, Process> processesContainer = new HashMap<>();
	 private final ArrayList<Future<Void>> futureList = new ArrayList<>();
	 private final ExecutorService executor = Executors.newCachedThreadPool();
	 private BufferedReader stdInput;
	 private BufferedReader stdError;

	 public ProcessManager() {
		  Runtime rt = Runtime.getRuntime();
	 }

	 public void forceQuit() throws InterruptedException {
		  for (String id : getProcesses().keySet()) {
				Process p = getProcesses().get(id);
				logger.debug("[" + id + "] force kill:STARTED");
				ProcessHandle processHandle = p.toHandle();
				KillProcessAndChildren(id, processHandle, 0);
				logger.debug("[" + id + "] force kill:DONE");
		  }
		  for (Future f : futureList) {
				f.cancel(true);
		  }
	 }

	 private void KillProcessAndChildren(String id, ProcessHandle processHandle, int level) {
		  Stream<ProcessHandle> children = processHandle.children();
		  long childrenCount = children.count();
		  logger.debug("[" + id + "] have " + childrenCount + " children of level " + level);
		  children = processHandle.children();
		  children.forEach(new Consumer<ProcessHandle>() {
				@Override
				public void accept(ProcessHandle processHandle) {
					 KillProcessAndChildren(id, processHandle, level + 1);
					 processHandle.destroyForcibly();
				}
		  });
		  logger.debug("[" + id + "] all " + childrenCount + " of level " + level + " has been killed");
		  processHandle.destroyForcibly();
	 }

	 public void executeParallelProcess(String[] commands, String dir, BufferedWriter writer, boolean waitEnd) throws IOException, InterruptedException, ExecutionException {
		  String processId = UUID.randomUUID().toString();
		  Process currentProc;
		  ProcessBuilder pb = new ProcessBuilder(commands);
		  pb.directory(new File(dir));
		  logger.debug("[" + processId + "] " + getSingleRowCommand(commands));
		  ParallelizedProcess parallelizer = new ParallelizedProcess(processId, pb, this, writer, waitEnd);
		  Future<Void> future = executor.submit(parallelizer);
		  futureList.add(future);

		  if (waitEnd) {
				synchronized (pb) {
					 pb.wait();
					 System.out.println("waiting...");
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
		  //future.get();
	 }

	 public synchronized HashMap<String, Process> getProcesses() {
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
