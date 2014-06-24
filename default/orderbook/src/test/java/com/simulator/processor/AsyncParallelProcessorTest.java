package com.simulator.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.simulator.factory.FactoryUtility;

public class AsyncParallelProcessorTest extends BaseUnitTest{

	private AsyncParallelProcessor asyncParallelProcessor;
	private FactoryUtility factoryUtility;
	private static final int cores = 4;

	@Before
	public void init(){
		factoryUtility = mock(FactoryUtility.class);
		when(factoryUtility.createSingleThreadedExecutor()).thenCallRealMethod();
	}
	
	@Test
	public void testCreatedParallelMarketOrders(){
		when(factoryUtility.createMultiMarketProcessors(cores)).thenCallRealMethod();
		when(factoryUtility.createMultiExecutors(cores)).thenCallRealMethod();
		asyncParallelProcessor = new AsyncParallelProcessor(cores, factoryUtility);
		verify(factoryUtility, times(cores)).createMarketProcessor();
		verify(factoryUtility, times(cores)).createSingleThreadedExecutor();
	}
	
	@Test
	public void testRepetiveQuotesForSameSymbolGoToSameProcessor() throws ProcessingFailedException, InterruptedException, ExecutionException{
		MarketProcessor[] mockProcessors = new MarketProcessor[4];
		createMockProcessors(new MarketUpdate<Double, Integer>(null), mockProcessors);
		when(factoryUtility.createMarketProcessor()).thenReturn(mockProcessors[0], mockProcessors[1], mockProcessors[2], mockProcessors[3]);
		when(factoryUtility.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		when(factoryUtility.createMultiExecutors(cores)).thenCallRealMethod();
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, factoryUtility);
		asyncParallelProcessor.process(createQuote(23.0, 100)).get();
		asyncParallelProcessor.process(createQuote(23.0, 100)).get();
		verify(mockProcessors[0], times(2)).createMarketOrder(any(Quote.class));
	}
	
	@Test
	public void testPassingExceptionToClient() throws ProcessingFailedException, InterruptedException, ExecutionException{
		MarketProcessor[] mockProcessors = new MarketProcessor[4];
		createMockProcessors(new ProcessingFailedException(), mockProcessors);
		when(factoryUtility.createMarketProcessor()).thenReturn(mockProcessors[0], mockProcessors[1], mockProcessors[2], mockProcessors[3]);
		when(factoryUtility.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		when(factoryUtility.createMultiExecutors(cores)).thenCallRealMethod();
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, factoryUtility);
		Future<MarketUpdate<Double, Integer>> future = asyncParallelProcessor.process(createQuote(23.0, 100));
		try{
			future.get();
			Assert.fail("Should not reach this point");
		}catch(ExecutionException|InterruptedException e){
			Assert.assertTrue(e instanceof ExecutionException);
			Assert.assertTrue(e.getCause() instanceof ProcessingFailedException);
		}
		verify(mockProcessors[0], times(1)).createMarketOrder(any(Quote.class));
	}
	
	@Test
	public void testExecutorShutdown() throws ProcessingFailedException, InterruptedException, ExecutionException{
		MarketProcessor[] mockProcessors = new MarketProcessor[4];
		createMockProcessors(new ProcessingFailedException(), mockProcessors);
		when(factoryUtility.createMarketProcessor()).thenReturn(mockProcessors[0], mockProcessors[1], mockProcessors[2], mockProcessors[3]);
		when(factoryUtility.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		when(factoryUtility.createMultiExecutors(cores)).thenCallRealMethod();
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, factoryUtility);
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
		MarketProcessor[] mockProcessors = new MarketProcessor[4];
		createMockProcessors(new ProcessingFailedException(), mockProcessors);
		when(factoryUtility.createMarketProcessor()).thenReturn(mockProcessors[0], mockProcessors[1], mockProcessors[2], mockProcessors[3]);
		when(factoryUtility.createMultiMarketProcessors(cores)).thenReturn(mockProcessors);
		when(factoryUtility.createMultiExecutors(cores)).thenCallRealMethod();
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, factoryUtility);
		Assert.assertNull(asyncParallelProcessor.process(null));
		verify(mockProcessors[0], times(0)).createMarketOrder(any(Quote.class));
	}

	public void createMockProcessors(
			Object sampleResult,
			MarketProcessor[] mockProcessors)
			throws ProcessingFailedException {
		for(int i=0;i<cores;i++){
			mockProcessors[i] = mock(MarketProcessor.class);
			if(sampleResult instanceof MarketUpdate){
				when(mockProcessors[i].createMarketOrder(any(Quote.class))).thenReturn((MarketUpdate<Double, Integer>)sampleResult);
			}else if(sampleResult instanceof Exception){
				when(mockProcessors[i].createMarketOrder(any(Quote.class))).thenThrow((Exception)sampleResult);
			}
		}
	}
}
