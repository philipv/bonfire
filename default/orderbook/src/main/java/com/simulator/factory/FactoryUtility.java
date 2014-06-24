package com.simulator.factory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * Utility class for creating different types of objects
 */
public class FactoryUtility {
	
	public BufferedReader createBufferedReader(InputStreamReader inputStreamReader) {
		return new BufferedReader(inputStreamReader);
	}
	
}
