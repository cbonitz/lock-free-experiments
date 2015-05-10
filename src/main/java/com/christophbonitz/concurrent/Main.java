package com.christophbonitz.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;


public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		int[] actionsPerActor = { 10000, 100000, 1000000 };
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < actionsPerActor.length; i++) {
		for (int baseActorCount = 1; baseActorCount <= 10; baseActorCount++) {
				long ai = time(baseActorCount, actionsPerActor[i], pool, new AtomicIntegerCounter());
				long lockFree = time(baseActorCount, actionsPerActor[i], pool, new LockFreeCounter());
				long intrinsic = time(baseActorCount, actionsPerActor[i], pool, new IntrinsicLockCounter());
				long readwrite = time(baseActorCount, actionsPerActor[i], pool, new ReadWriteLockCounter());
				System.out.printf("%d %d %d %d %d %d\n", 
						baseActorCount, actionsPerActor[i], ai, lockFree, intrinsic, readwrite);
			}
		}
		pool.shutdownNow();
		pool.awaitTermination(10, TimeUnit.SECONDS);
	}

	private static long time(int baseActorCount, int actionsPerActor,
			ExecutorService pool, Counter counter) {
		Stopwatch sw = Stopwatch.createStarted();
		CounterExecutor counterExecutor = new CounterExecutor(pool, counter, 10*baseActorCount, baseActorCount, baseActorCount, actionsPerActor);
		counterExecutor.run();
		sw.stop();
		return sw.elapsed(TimeUnit.MILLISECONDS);
	}
}
