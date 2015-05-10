/* Copyright 2015 Christoph Bonitz
 * License: Simplified BSD License
 */
package com.christophbonitz.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeCounter implements Counter {
	// consciously not using AtomicInteger to be comparable to the map case
	private final AtomicReference<Integer> value = new AtomicReference<>(0);

	@Override
	public void increment() {
		boolean done = false;
		while (!done) {
			Integer count = value.get();
			int newValue = count + 1;
			done = value.compareAndSet(count, newValue);
		}
	}

	@Override
	public void decrement() {
		boolean done = false;
		while (!done) {
			Integer count = value.get();
			int newValue = count + 1;
			done = value.compareAndSet(count, newValue);
		}
	}

	@Override
	public int get() {
		return value.get();
	} 
}
