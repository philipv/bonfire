package com.simulator.processor;

import com.simulator.exception.ProcessingFailedException;


/**
 * @author vinith
 * This interface represents a group of classes which could be used to process any data 
 * asynchronously.
 * @param <T> - type of the input
 * @param <R> - type of the result returned inside a Future
 */
public interface IProcessor<T, R> {
	
	/**
	 * This method accepts an object and returns a future handle to the result
	 * @param marketIdentity
	 * @return future
	 */
	R process(T marketIdentity) throws ProcessingFailedException;

	/**
	 * This method is to close down the async processor
	 */
	void shutdown();

}
