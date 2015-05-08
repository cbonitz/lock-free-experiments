package com.christophbonitz.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockCounter implements Counter {
	private int value = 0;
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	
	@Override
	public void increment() {
		rwl.writeLock().lock();
		try {
			value++;
		} finally {
			rwl.writeLock().unlock();
		}
	}

	@Override
	public void decrement() {
		rwl.writeLock().lock();
		try {
			value--;
		} finally {
			rwl.writeLock().unlock();
		}
	}

	@Override
	public int get() {
		rwl.readLock().lock();
		try {
			return value;
		} finally {
			rwl.readLock().unlock();
		}
	}
	
}
