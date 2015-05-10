/* Copyright 2015 Christoph Bonitz
 * License: Simplified BSD License
 */
package com.christophbonitz.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * Runnable implementation that waits for startLatch, runs a delegate runnable, and counts down stopLatch when done.
 */
public class LatchOperatedRunnableWrapper implements Runnable {
	private final Runnable delegate;
	private final CountDownLatch startLatch;
	private final CountDownLatch stopLatch;

	public LatchOperatedRunnableWrapper(Runnable delegate,
			CountDownLatch startLatch, CountDownLatch stopLatch) {
		this.delegate = delegate;
		this.startLatch = startLatch;
		this.stopLatch = stopLatch;
	}

	@Override
	public void run() {
		try {
			startLatch.await();
			delegate.run();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			stopLatch.countDown();
		}
	}
}
