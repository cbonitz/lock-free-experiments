/* Copyright 2015 Christoph Bonitz
 * License: Simplified BSD License
 */
package com.christophbonitz.concurrent.interfaces;

public interface Counter {
	public void increment();
	public void decrement();
	public int get();
}
