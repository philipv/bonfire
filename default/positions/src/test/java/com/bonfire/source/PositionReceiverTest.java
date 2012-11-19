package com.bonfire.source;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bonfire.BaseTest;
import com.bonfire.data.Position;
import com.bonfire.factory.FactoryUtility;
import com.bonfire.observer.PositionListener;
import com.bonfire.task.EvaluationTask;
import com.bonfire.task.PositionProcessTask;

public class PositionReceiverTest extends BaseTest{

	private FactoryUtility factoryUtility = mock(FactoryUtility.class);
	private BufferedReader bufferedReader = mock(BufferedReader.class);
	private FileReader fileReader = mock(FileReader.class);
	
	@Before
	public void before(){
		when(factoryUtility.createBufferedReader(any(InputStreamReader.class))).thenReturn(bufferedReader);
	}
	@Test
	public void testWithoutFile() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(bufferedReader.readLine()).thenReturn("usd 1234.43").thenReturn("quit");
		positionReceiver.initialize(null);
		assertAggregatedResult("USD", 1234.43);
	}
	
	@Test
	public void testWithFile() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(factoryUtility.createFileReader(any(File.class))).thenReturn(fileReader);
		when(bufferedReader.readLine()).thenReturn("usd 100", "quit", "usd 100", "quit");
		positionReceiver.initialize(new String[]{"sample"});
		assertAggregatedResult("USD", 200);
	}
	
	@Test
	public void testWithWrongInputValues() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(bufferedReader.readLine()).thenReturn("usd 100", "usd :86", "", "dgr", "abcds 2342", "usd 100" , "quit");
		positionReceiver.initialize(null);
		assertAggregatedResult("USD", 200);
	}
	
	@Test
	public void testWithInputStreamFailure() throws IOException{
		PositionReceiver positionReceiver = createReceiver();
		when(bufferedReader.readLine()).thenThrow(new IOException());
		positionReceiver.initialize(null);
		Assert.assertEquals(0, positions.size());
	}
	
	/*
	 * This test starts one thread for Evaluation task 10ms apart and process  1000000 updates
	 * in parallel. At last we check the final expected results to ensure all the updates were
	 * processed successfully be checking the final results.
	 */
	@Test
	public void testConcurrentAccesOnPositions(){
		ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		threadPoolExecutor.scheduleAtFixedRate(new EvaluationTask(positions), 10, 10, TimeUnit.MILLISECONDS);
		PositionListener positionListener = new PositionListener(positions);
		for(int i=0;i<1000000;i++){
			positionListener.update(null, new Position(i%2==0?"USD":"INR", 10.0));
		}
		assertAggregatedResult("USD", 5000000);
		assertAggregatedResult("INR", 5000000);
	}
	
	private PositionReceiver createReceiver() {
		PositionReceiver positionReceiver = new PositionReceiver();
		positionReceiver.setFactoryUtility(factoryUtility);
		positionReceiver.setPositions(positions);
		return positionReceiver;
	}
}
