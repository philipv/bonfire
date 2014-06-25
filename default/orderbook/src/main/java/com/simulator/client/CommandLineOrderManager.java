package com.simulator.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.factory.InjectionManager;
import com.simulator.processor.IAsyncProcessor;
import com.simulator.processor.task.UpdateProcessorTask;


/**
 * @author vinith
 * This is a command line based order manager. It uses input from command line to feed to 
 * a live limit order book.
 */
public class CommandLineOrderManager{
	private static final String SEPARATOR = " ";
	private static final int cores = 8;
	private InjectionManager injectionManager;
	private IAsyncProcessor<Quote, MarketUpdate<Double, Long>> asyncMarketProcessor;
	private ExecutorService asyncResultProcessor;
	
	public static void main(String[] args) throws FileNotFoundException{
		CommandLineOrderManager orderManager = new CommandLineOrderManager(new InjectionManager());
		orderManager.initialize();
	}
	
	public CommandLineOrderManager(InjectionManager injectionManager){
		this.injectionManager = injectionManager;
		this.asyncMarketProcessor = injectionManager.createAsyncProcessor(cores);
		this.asyncResultProcessor = injectionManager.createSingleThreadedExecutor();
		System.out.println("Quote input format:<Side> <Quantity> <Price>");
		System.out.println("Enter \"quit\" to exit");
	}

	public void initialize() throws FileNotFoundException {
		;
		try(BufferedReader reader =injectionManager.createBufferedReader(new InputStreamReader(System.in))){
			String inputString = null;
			
			while((inputString = reader.readLine())!=null){
				if(inputString.equals("quit"))
					break;
				
				final Future<MarketUpdate<Double, Long>> future = asyncMarketProcessor.process(processInput(inputString));
				if(future!=null){
					asyncResultProcessor.submit(new UpdateProcessorTask<Double, Long>(future));
				}
			}
		}catch(IOException e){
			System.out.println("Cannot read from the input stream. Program will exit now");
			e.printStackTrace();
		}finally{
			asyncMarketProcessor.shutdown();
			asyncResultProcessor.shutdown();
		}
	
	}

	public Quote processInput(String inputString){
		String[] inputs = inputString.split(SEPARATOR);
		String problem = null;
		if(inputs.length==3){
			try{
				Quote quote = new Quote();
				quote.setPrice(Double.valueOf(inputs[2]));
				quote.setQuantity(Long.valueOf(inputs[1]));
				quote.setSide(Side.valueOf(inputs[0]));
				return quote;
			}catch(Exception e){
				problem = e.getMessage();
			}
		}
		System.out.println("Wrong quote format: " + inputString + 
				(problem==null?"":"(" + problem + ")"));
		return null;
	}
}
