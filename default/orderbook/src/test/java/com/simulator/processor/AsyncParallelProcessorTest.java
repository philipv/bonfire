package com.simulator.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.simulator.BaseUnitTest;
import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.exception.ProcessingFailedException;

public class AsyncParallelProcessorTest extends BaseUnitTest{

	private AsyncParallelProcessor asyncParallelProcessor;
	private static final int cores = 4;

	@Before
	public void init(){
		when(mockInjectionManager.createSingleThreadedExecutor()).thenCallRealMethod();
		when(mockInjectionManager.createMultiExecutors(cores)).thenCallRealMethod();
	}
	
	@Test
	public void testCreatedParallelMarketOrders(){
		when(mockInjectionManager.createMultiMarketProcessors(cores)).thenCallRealMethod();
		asyncParallelProcessor = new AsyncParallelProcessor(cores, mockInjectionManager);
		verify(mockInjectionManager, times(cores)).createMarketProcessor();
		verify(mockInjectionManager, times(cores)).createSingleThreadedExecutor();
	}
	
	@Test
	public void testRepetiveQuotesForSameSymbolGoToSameProcessor() throws ProcessingFailedException, InterruptedException, ExecutionException{
		List<MarketProcessor> mockProcessors = new ArrayList<>(4);
		createMockProcessors(new MarketUpdate<Double, Integer>(null), mockProcessors);
		when(mockInjectionManager.createMarketProcessor()).thenReturn(mockProcessors.get(0), mockProcessors.get(1), mockProcessors.get(2), mockProcessors.get(3));
		when(mockInjectionManager.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, mockInjectionManager);
		asyncParallelProcessor.process(createQuote(23.0, 100)).get();
		asyncParallelProcessor.process(createQuote(23.0, 100)).get();
		verify(mockProcessors.get(0), times(2)).createMarketOrder(any(Quote.class));
	}
	
	@Test
	public void testPassingExceptionToClient() throws ProcessingFailedException, InterruptedException, ExecutionException{
		List<MarketProcessor> mockProcessors = new ArrayList<>(4);
		createMockProcessors(new ProcessingFailedException(), mockProcessors);
		when(mockInjectionManager.createMarketProcessor()).thenReturn(mockProcessors.get(0), mockProcessors.get(1), mockProcessors.get(2), mockProcessors.get(3));
		when(mockInjectionManager.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, mockInjectionManager);
		Future<MarketUpdate<Double, Long>> future = asyncParallelProcessor.process(createQuote(23.0, 100));
		try{
			future.get();
			Assert.fail("Should not reach this point");
		}catch(ExecutionException|InterruptedException e){
			Assert.assertTrue(e instanceof ExecutionException);
			Assert.assertTrue(e.getCause() instanceof ProcessingFailedException);
		}
		verify(mockProcessors.get(0), times(1)).createMarketOrder(any(Quote.class));
	}
	
	@Test
	public void testExecutorShutdown() throws ProcessingFailedException, InterruptedException, ExecutionException{
		List<MarketProcessor> mockProcessors = new ArrayList<>(4);
		createMockProcessors(new ProcessingFailedException(), mockProcessors);
		when(mockInjectionManager.createMarketProcessor()).thenReturn(mockProcessors.get(0), mockProcessors.get(1), mockProcessors.get(2), mockProcessors.get(3));
		when(mockInjectionManager.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, mockInjectionManager);
		asyncParallelProcessor.shutdown();
		try{
			asyncParallelProcessor.process(createQuote(23.0, 100));
			Assert.fail("Should not reach this point");
		}catch(RejectedExecutionException e){
			Assert.assertTrue(e instanceof RejectedExecutionException);
		}
	}
	
	@Test
	public void testInvalidInput() throws ProcessingFailedException, InterruptedException, ExecutionException{
		List<MarketProcessor> mockProcessors = new ArrayList<>(4);
		createMockProcessors(new ProcessingFailedException(), mockProcessors);
		when(mockInjectionManager.createMarketProcessor()).thenReturn(mockProcessors.get(0), mockProcessors.get(1), mockProcessors.get(2), mockProcessors.get(3));
		when(mockInjectionManager.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, mockInjectionManager);
		Assert.assertNull(asyncParallelProcessor.process(null));
		verify(mockProcessors.get(0), times(0)).createMarketOrder(any(Quote.class));
	}

	public void createMockProcessors(
			Object sampleResult,
			List<MarketProcessor> mockProcessors)
			throws ProcessingFailedException {
		for(int i=0;i<cores;i++){
			mockProcessors.add(mock(MarketProcessor.class));
			if(sampleResult instanceof MarketUpdate){
				when(mockProcessors.get(i).createMarketOrder(any(Quote.class))).thenReturn((MarketUpdate<Double, Long>)sampleResult);
			}else if(sampleResult instanceof Exception){
				when(mockProcessors.get(i).createMarketOrder(any(Quote.class))).thenThrow((Exception)sampleResult);
			}
		}
	}
}
