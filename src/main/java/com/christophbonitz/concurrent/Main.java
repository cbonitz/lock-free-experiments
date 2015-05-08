package com.christophbonitz.concurrent;

import com.google.caliper.Param;
import com.google.caliper.api.BeforeRep;
import com.google.caliper.api.Macrobenchmark;
import com.google.caliper.runner.CaliperMain;

public class Main {
	@Param({ "1" }) private int baseActorCount;
	@Param({ "1000"}) private int actionsPerActor;
	
	public static void main(String[] args) {
		CaliperMain.main(Main.class, args);
	}
	
	@BeforeRep
	public void BeforeRep() {
		System.out.println("Here we go again");
	}
	
	@Macrobenchmark
	public void timeLockFreeBenchmark() {
		System.out.println("Starting");
		Counter counter = new LockFreeCounter();
		CounterExecutor counterExecutor = new CounterExecutor(counter, 2*baseActorCount, baseActorCount, baseActorCount, actionsPerActor);
		counterExecutor.run();
		System.out.println("Finished");
	}
	
	@Macrobenchmark
	public void timeIntrinsicLockBenchmark() {
		System.out.println("Starting");
		Counter counter = new IntrinsicLockCounter();
		new CounterExecutor(counter, 2*baseActorCount, baseActorCount, baseActorCount, actionsPerActor).run();
		System.out.println("Finished");
	}
	
	@Macrobenchmark
	public void timeRreadWriteLockBenchmark() {
		System.out.println("Starting");
		Counter counter = new ReadWriteLockCounter();
		new CounterExecutor(counter, 2*baseActorCount, baseActorCount, baseActorCount, actionsPerActor).run();
		System.out.println("Finished");
	}
}
