package com.simulator.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.Test;

import com.simulator.BaseUnitTest;
import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.factory.InjectionManager;
import com.simulator.processor.AsyncParallelProcessor;

public class PerformanceTest extends BaseUnitTest{
	private static final int max_ticks = 1000000;
	private static final CountDownLatch counter = new CountDownLatch(max_ticks);
	@Test
	public void testDifferentSymbolSides() throws InterruptedException{
		InjectionManager injectionManager = new InjectionManager();
		AsyncParallelProcessor asyncParallelProcessor = new AsyncParallelProcessor(8, injectionManager);
		ExecutorService updateReceiver = injectionManager.createSingleThreadedExecutor();
		String[] symbols = new String[]{"S1", "S2", "S3", "S4"};
		Quote[] quotes = new Quote[]{
				createQuote(10.0, 100, Side.B),
				createQuote(10.1, 100, Side.S),
				createQuote(10.1, 100, Side.S),
				createQuote(10.2, 250, Side.B),
				createQuote(10.0, 50, Side.S),
				createQuote(9.9, 100, Side.S),
				createQuote(10.2, 100, Side.B),
				createQuote(10.0, 250, Side.S)
		};
		
		long startTime = System.nanoTime();
		for(int i=0;i<max_ticks;i++){
			Quote sampleQuote = quotes[i%quotes.length];
			Quote quote = createQuote(sampleQuote.getPrice(), sampleQuote.getQuantity(), sampleQuote.getSide());
			quote.setSymbol(symbols[i%symbols.length]);
			final Future<MarketUpdate<Double, Long>> future = asyncParallelProcessor.process(quote);
			updateReceiver.submit(new Runnable() {
				@Override
				public void run() {
					try{
						future.get();
						counter.countDown();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		}
		counter.await();
		System.out.println("countdown latch count: " + counter.getCount());
		System.out.println("Time taken per tick(in ns)" + (System.nanoTime() - startTime)/max_ticks);
	}
}
