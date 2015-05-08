package com.christophbonitz.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerCounter implements Counter {
	// consciously not using AtomicInteger to be comparable to the map case
	private final AtomicInteger value = new AtomicInteger();

	@Override
	public void increment() {
		value.incrementAndGet();
	}

	@Override
	public void decrement() {
		value.decrementAndGet();
	}

	@Override
	public int get() {
		return value.get();
	} 
}
