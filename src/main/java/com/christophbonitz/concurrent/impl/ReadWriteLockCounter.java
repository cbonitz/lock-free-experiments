/* Copyright 2015 Christoph Bonitz
 * License: Simplified BSD License
 */
package com.christophbonitz.concurrent.impl;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.christophbonitz.concurrent.interfaces.Counter;

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
