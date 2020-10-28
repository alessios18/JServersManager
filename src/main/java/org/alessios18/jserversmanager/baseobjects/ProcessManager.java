package org.alessios18.jserversmanager.baseobjects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessManager {
	 private final Runtime rt;
	 ExecutorService executor = Executors.newCachedThreadPool();
	 private BufferedReader stdInput;
	 private BufferedReader stdError;
	 private final HashMap<String, Process> processesContainer = new HashMap<>();
	 private final ArrayList<Future<Void>> futureList = new ArrayList<>();

	 public ProcessManager() {
		  rt = Runtime.getRuntime();
	 }

	 public void forceQuit() {
		  for (Process p : getProcesses().values()) {
				if (p != null) {
					 p.destroyForcibly();
				}
		  }
		  for (Future f : futureList) {
				f.cancel(true);
		  }
	 }

	 public void executeParallelProcess(String[] commands, String dir, BufferedWriter writer, boolean waitEnd) throws IOException, InterruptedException, ExecutionException {
		  Process currentProc;
		  ProcessBuilder pb = new ProcessBuilder(commands);
		  pb.directory(new File(dir));
		  ParallelizedProcess parallelizer = new ParallelizedProcess(pb, this, writer);
		  Future<Void> future = executor.submit(parallelizer);
		  futureList.add(future);
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
}
