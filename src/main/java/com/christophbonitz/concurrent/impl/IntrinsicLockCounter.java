/* Copyright 2015 Christoph Bonitz
 * License: Simplified BSD License
 */
package com.christophbonitz.concurrent.impl;

import com.christophbonitz.concurrent.interfaces.Counter;

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
