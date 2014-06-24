package com.simulator.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.factory.FactoryUtility;
import com.simulator.processor.AsyncParallelProcessor;

/*
 * This component is responsible for:
 *  - Getting the update from different sources.
 *  - Convert the update into a position
 *  - Notify the required listeners.
 *  - This class could be overridden by any other source of updates. Just override convert and getObserver (only if required)
 */
public class OrderManager{
	private final class UpdateProcessorTask implements Runnable {
		private final Future<MarketUpdate<Double, Integer>> future;

		private UpdateProcessorTask(Future<MarketUpdate<Double, Integer>> future) {
			this.future = future;
		}

		@Override
		public void run() {
			try{
				MarketUpdate<Double, Integer> marketUpdate = future.get();
				if(marketUpdate!=null){
					System.out.println(marketUpdate.getTrades());
					System.out.println(marketUpdate.getBidUpdates());
					System.out.println(marketUpdate.getAskUpdates());
				}
			}catch(ExecutionException|InterruptedException ex){
				System.out.println("Couldn't process result");
				ex.printStackTrace();
			}
		}
	}

	private static final String SEPARATOR = " ";
	private static final int cores = 8;
	private FactoryUtility factoryUtility = new FactoryUtility();
	private AsyncParallelProcessor processor;
	private ExecutorService resultProcessor;
	
	public static void main(String[] args) throws FileNotFoundException{
		OrderManager orderManager = new OrderManager();
		orderManager.initialize();
	}
	
	public OrderManager(){
		this.processor = new AsyncParallelProcessor(cores);
		this.resultProcessor = Executors.newSingleThreadExecutor();
	}

	public void initialize() throws FileNotFoundException {
		read(new InputStreamReader(System.in));
	}

	private void read(InputStreamReader inputStreamReader) {
		BufferedReader reader = null;
		try{
			reader = factoryUtility.createBufferedReader(inputStreamReader);
			String inputString = null;
			
			while((inputString = reader.readLine())!=null){
				if(inputString.equals("quit"))
					break;
				
				final Future<MarketUpdate<Double, Integer>> future = processor.process(processInput(inputString));
				if(future!=null){
					resultProcessor.submit(new UpdateProcessorTask(future));
				}
			}
		}catch(IOException e){
			System.out.println("Cannot read from the input stream. Program will exit now");
			e.printStackTrace();
		}finally{
			try {
				reader.close();
				inputStreamReader.close();
				processor.shutdown();
				resultProcessor.shutdown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFactoryUtility(FactoryUtility factoryUtility) {
		this.factoryUtility = factoryUtility;
	}

	public Quote processInput(String inputString){
		String[] inputs = inputString.split(SEPARATOR);
		if(inputs.length<3){
			System.out.println("Wrong input format. Correct format is <Side> <Quantity> <Price>");
			return null;
		}else{
			Quote quote = new Quote();
			try{
				quote.setPrice(Double.valueOf(inputs[2]));
				quote.setQuantity(Integer.valueOf(inputs[1]));
				quote.setSide(Side.valueOf(inputs[0]));
			}catch(Exception e){
				System.out.println("Wrong input format: " + inputString);
			}
			return quote;
		}
	}
}
