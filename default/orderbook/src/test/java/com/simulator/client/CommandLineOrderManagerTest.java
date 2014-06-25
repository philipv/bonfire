package com.simulator.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import com.simulator.BaseUnitTest;
import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.processor.AsyncParallelProcessor;

public class CommandLineOrderManagerTest extends BaseUnitTest{
	private CommandLineOrderManager commandLineOrderManager;
	
	@Mock
	private AsyncParallelProcessor asyncMarketProcessor;
	
	@Mock
	private ExecutorService asyncResultProcessor;
	
	@Mock
	private BufferedReader reader;
	
	@Mock
	private Future<MarketUpdate<Double, Integer>> mockFuture;
	
	@Before
	public void init(){
		when(mockInjectionManager.createSingleThreadedExecutor()).thenReturn(asyncResultProcessor);
		when(mockInjectionManager.createAsyncProcessor(8)).thenReturn(asyncMarketProcessor);
		when(mockInjectionManager.createBufferedReader(any(InputStreamReader.class))).thenReturn(reader);
		commandLineOrderManager = new CommandLineOrderManager(mockInjectionManager);
	}
	
	@Test
	public void testValidInput() throws IOException{
		when(reader.readLine()).thenReturn("B 100 9.9", "quit");
		when(asyncMarketProcessor.process(any(Quote.class))).thenReturn(mockFuture);
		commandLineOrderManager.initialize();
		verify(asyncMarketProcessor, times(1)).process(any(Quote.class));
		verify(asyncResultProcessor, times(1)).submit(any(Runnable.class));
	}
	
	@Test
	public void testInvalidInputs() throws IOException{
		when(reader.readLine()).thenReturn("B S 9.9", "", "quit");
		when(asyncMarketProcessor.process(any(Quote.class))).thenReturn(null);
		commandLineOrderManager.initialize();
		verify(asyncMarketProcessor, times(2)).process(any(Quote.class));
		verify(asyncResultProcessor, times(0)).submit(any(Runnable.class));
	}
	
	@Test
	public void testInvalidInputsFolledByValidInput() throws IOException{
		when(reader.readLine()).thenReturn("B S 9.9", "", "B 100 9.9" , "quit");
		when(asyncMarketProcessor.process(any(Quote.class))).thenReturn(null, null, mockFuture);
		commandLineOrderManager.initialize();
		verify(asyncMarketProcessor, times(3)).process(any(Quote.class));
		verify(asyncResultProcessor, times(1)).submit(any(Runnable.class));
	}
	
	@Test
	public void testExceptionInIO() throws IOException{
		when(reader.readLine()).thenThrow(new IOException());
		commandLineOrderManager.initialize();
		verify(asyncMarketProcessor, times(0)).process(any(Quote.class));
		verify(asyncMarketProcessor, times(1)).shutdown();
		verify(asyncResultProcessor, times(0)).submit(any(Runnable.class));
		verify(asyncResultProcessor, times(1)).shutdown();
	}

}
