package com.christophbonitz.concurrent;

public class IntrinsicLockCounter implements Counter {
	private int count = 0;

	@Override
	public synchronized void increment() {
		count++;
	}

	@Override
	public synchronized void decrement() {
		count--;
	}

	@Override
	public synchronized int get() {
		return count;
	}
}
