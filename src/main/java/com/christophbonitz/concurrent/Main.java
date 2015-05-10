/* Copyright 2015 Christoph Bonitz
 * License: Simplified BSD License
 */
package com.christophbonitz.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.christophbonitz.concurrent.impl.AtomicIntegerCounter;
import com.christophbonitz.concurrent.impl.IntrinsicLockCounter;
import com.christophbonitz.concurrent.impl.LockFreeCounter;
import com.christophbonitz.concurrent.impl.ReadWriteLockCounter;
import com.christophbonitz.concurrent.interfaces.Counter;
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

	/**
	 * Run with baseActorCount incrementors and decremntors, as well as 10*baseActorCount readers.
	 * Each will perform actionsPerActor actions.
	 * @param baseActorCount
	 * @param actionsPerActor
	 * @param pool
	 * @param counter
	 * @return time used in milliseconds.
	 */
	private static long time(int baseActorCount, int actionsPerActor,
			ExecutorService pool, Counter counter) {
		Stopwatch sw = Stopwatch.createStarted();
		CounterExecutor counterExecutor = new CounterExecutor(
				pool, 
				counter, 
				10*baseActorCount, 
				baseActorCount, 
				baseActorCount, actionsPerActor);
		counterExecutor.run();
		sw.stop();
		return sw.elapsed(TimeUnit.MILLISECONDS);
	}
}
