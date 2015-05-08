package com.christophbonitz.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CounterExecutor implements Runnable {
	private final int readers;
	private final int incrementors;
	private final int decrementors;
	private final int actionsPerActor;
	private final Counter counter;
	
	public CounterExecutor(
			Counter counter,
			int readers, 
			int incrementors, 
			int decrementors,
			int actionsPerActor) {
		
		this.counter = counter;
		this.readers = readers;
		this.incrementors = incrementors;
		this.decrementors = decrementors;
		this.actionsPerActor = actionsPerActor;
	}

	@Override
	public void run() {
		System.out.println("Starting " + this);
		ExecutorService pool = Executors.newCachedThreadPool();
		int totalActors = readers + incrementors + decrementors;
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch stopLatch = new CountDownLatch(totalActors);
		for (int i =  0; i < incrementors; i++) {
			pool.execute(new LatchOperatedRunnableWrapper(
					new Incrementor(counter, actionsPerActor), startLatch, stopLatch));
		}
		for (int i =  0; i < decrementors; i++) {
			pool.execute(new LatchOperatedRunnableWrapper(
					new Decrementor(counter, actionsPerActor), startLatch, stopLatch));
		}
		for (int i =  0; i < readers; i++) {
			pool.execute(new LatchOperatedRunnableWrapper(
					new Reader(counter, actionsPerActor), startLatch, stopLatch));
		}
		startLatch.countDown();
		try {
			stopLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		pool.shutdownNow();
		try {
			pool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Done: " + this);
	}
	
	public class Incrementor implements Runnable {
		private final Counter counter;
		private final int count;
		
		public Incrementor(Counter counter, int count) {
			this.counter = counter;
			this.count = count;
		}


		@Override
		public void run() {
			for (int i = 0; i < count; i++) {
				counter.increment();
			}
		}
	}
	
	public class Decrementor implements Runnable {
		private final Counter counter;
		private final int count;
		
		public Decrementor(Counter counter, int count) {
			this.counter = counter;
			this.count = count;
		}


		@Override
		public void run() {
			for (int i = 0; i < count; i++) {
				counter.decrement();
			}
		}
	}
	
	public class Reader implements Runnable {
		private final Counter counter;
		private final int count;
		
		public Reader(Counter counter, int count) {
			this.counter = counter;
			this.count = count;
		}


		@Override
		public void run() {
			int result = 0;
			for (int i = 0; i < count; i++) {
				result |= counter.get();
			}
			if (("" + result).equals("Do not optimize this")) {
				throw new RuntimeException();
			};
		}
	}

	@Override
	public String toString() {
		return "CounterExecutor [readers=" + readers + ", incrementors="
				+ incrementors + ", decrementors=" + decrementors
				+ ", actionsPerActor=" + actionsPerActor + ", counter="
				+ counter.getClass().getName() + "]";
	}
	
	
	
}
