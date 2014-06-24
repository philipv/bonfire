package com.simulator.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	}
	
	@Test
	public void testCreatedParallelMarketOrders(){
		asyncParallelProcessor = new AsyncParallelProcessor(cores, factoryUtility);
		verify(factoryUtility, times(cores)).createMarketProcessor();
		verify(factoryUtility, times(cores)).createSingleThreadedExecutor();
	}
	
	@Test
	public void testRepetiveQuotesForSameSymbolGoToSameProcessor() throws ProcessingFailedException{
		MarketUpdate<Double, Integer> sampleResult = new MarketUpdate<Double, Integer>(null, null, null);
		MarketProcessor[] mockProcessors = new MarketProcessor[4];
		createMockProcessors(sampleResult, mockProcessors);
		when(factoryUtility.createMarketProcessor()).thenReturn(mockProcessors[0], mockProcessors[1], mockProcessors[2], mockProcessors[3]);
		when(factoryUtility.createSingleThreadedExecutor()).thenCallRealMethod();
		
		asyncParallelProcessor = new AsyncParallelProcessor(cores, factoryUtility);
		asyncParallelProcessor.process(createQuote(23.0, 100));
		asyncParallelProcessor.process(createQuote(23.0, 100));
		verify(mockProcessors[0], times(2)).createMarketOrder(any(Quote.class));
	}

	public void createMockProcessors(
			MarketUpdate<Double, Integer> sampleResult,
			MarketProcessor[] mockProcessors)
			throws ProcessingFailedException {
		for(int i=0;i<cores;i++){
			mockProcessors[i] = mock(MarketProcessor.class);
			when(mockProcessors[i].createMarketOrder(any(Quote.class))).thenReturn(sampleResult);
		}
	}
	
	
}
