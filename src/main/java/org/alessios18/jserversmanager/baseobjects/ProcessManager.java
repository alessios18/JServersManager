package org.alessios18.jserversmanager.baseobjects;

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
	 private final Runtime rt;
	 private final HashMap<String, Process> processesContainer = new HashMap<>();
	 private final ArrayList<Future<Void>> futureList = new ArrayList<>();
	 ExecutorService executor = Executors.newCachedThreadPool();
	 private BufferedReader stdInput;
	 private BufferedReader stdError;

	 public ProcessManager() {
		  rt = Runtime.getRuntime();
	 }

	 public void forceQuit() throws InterruptedException {

		  for (Process p : getProcesses().values()) {
				ProcessHandle processHandle = p.toHandle();
				KillProcessAndchildren(processHandle);
				/*if (p != null) {
					 p.destroyForcibly();
					 p.waitFor();
				}*/
		  }
		  for (Future f : futureList) {
				f.cancel(true);
		  }
	 }

	 private void KillProcessAndchildren(ProcessHandle processHandle) {
		  Stream<ProcessHandle> children = processHandle.children();
		  children.forEach(new Consumer<ProcessHandle>() {
				@Override
				public void accept(ProcessHandle processHandle) {
					 KillProcessAndchildren(processHandle);
					 processHandle.destroyForcibly();
				}
		  });
		  processHandle.destroyForcibly();
	 }

	 public void executeParallelProcess(String[] commands, String dir, BufferedWriter writer, boolean waitEnd) throws IOException, InterruptedException, ExecutionException {
		  String processId = UUID.randomUUID().toString();
		  Process currentProc;
		  ProcessBuilder pb = new ProcessBuilder(commands);
		  pb.directory(new File(dir));
		  ParallelizedProcess parallelizer = new ParallelizedProcess(processId, pb, this, writer, waitEnd);
		  Future<Void> future = executor.submit(parallelizer);
		  futureList.add(future);

		  if (waitEnd) {
				synchronized (pb) {
					 pb.wait();
					 System.out.println("waiting...");
					 if (processesContainer.get(processId).isAlive()) {
						  processesContainer.get(processId).waitFor();
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
}
