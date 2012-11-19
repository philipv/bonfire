package com.bonfire.source;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

import com.bonfire.BaseTest;
import com.bonfire.factory.FactoryUtility;

public class PositionReceiverTest extends BaseTest{

	private FactoryUtility factoryUtility = mock(FactoryUtility.class);
	private BufferedReader bufferedReader = mock(BufferedReader.class);
	private FileReader fileReader = mock(FileReader.class);
	@Test
	public void testWithoutFile() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(factoryUtility.createBufferedReader(any(InputStreamReader.class))).thenReturn(bufferedReader);
		when(bufferedReader.readLine()).thenReturn("usd 1234.43").thenReturn("quit");
		positionReceiver.initialize(null);
		assertAggregatedResult("USD", 1234.43);
	}
	
	@Test
	public void testWithFile() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(factoryUtility.createFileReader(any(File.class))).thenReturn(fileReader);
		when(factoryUtility.createBufferedReader(any(InputStreamReader.class))).thenReturn(bufferedReader);
		when(bufferedReader.readLine()).thenReturn("usd 100", "quit", "usd 100", "quit");
		positionReceiver.initialize(new String[]{"sample"});
		assertAggregatedResult("USD", 200);
	}
	
	@Test
	public void testWithWrongInputValues() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(factoryUtility.createBufferedReader(any(InputStreamReader.class))).thenReturn(bufferedReader);
		when(bufferedReader.readLine()).thenReturn("usd 100", "usd :86", "", "dgr", "usd 100", "quit");
		positionReceiver.initialize(null);
		assertAggregatedResult("USD", 200);
	}
	
	@Test
	public void testWithInputStreamFailure() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(factoryUtility.createBufferedReader(any(InputStreamReader.class))).thenReturn(bufferedReader);
		when(bufferedReader.readLine()).thenThrow(new IOException());
		positionReceiver.initialize(null);
		Assert.assertEquals(0, positions.size());
	}
	
	private PositionReceiver createReceiver() {
		PositionReceiver positionReceiver = new PositionReceiver();
		positionReceiver.setFactoryUtility(factoryUtility);
		positionReceiver.setPositions(positions);
		return positionReceiver;
	}
}
