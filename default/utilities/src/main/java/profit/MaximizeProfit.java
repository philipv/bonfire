package profit;

import java.util.Arrays;

public class MaximizeProfit {

	public static void main(String[] args){
		int[] openPrices = new int[]{10, 1, 2, 6, 3};
		 HistoricData[] generatedData = new HistoricData[openPrices.length];
		 for(int i=0;i<openPrices.length;i++){
			 generatedData[i] = new HistoricData();
			 generatedData[i].relativeDay = i;
			 generatedData[i].openPrice = openPrices[i];
		 }
		 
		 HistoricData[] result = maximizeProfit(generatedData);
		 System.out.println(Arrays.toString(result));
		 System.out.println("Profit = " + (result[1].openPrice - result[0].openPrice));
	}
	
	private static HistoricData[] maximizeProfit(HistoricData[] generatedData) {
		HistoricData[] result = new HistoricData[]{generatedData[0], generatedData[0]};
		HistoricData[] currentData = new HistoricData[]{generatedData[0], generatedData[0]};
		int profit = 0;
		for(int i=1;i<generatedData.length;i++){
			if(result[0].openPrice>generatedData[i].openPrice){
				currentData[0] = generatedData[i];
				currentData[1] = generatedData[i];
			}else if(result[1].openPrice<generatedData[i].openPrice)
				currentData[1] = generatedData[i];
			
			if(profit<currentData[1].openPrice - result[0].openPrice){
				result[1] = currentData[1];
				profit = result[i].openPrice - result[0].openPrice;
			}else if(profit<currentData[1].openPrice - currentData[0].openPrice){
				result = currentData;
				profit = result[i].openPrice - result[0].openPrice;
			}
			
		}
		return result;
	}

	static class HistoricData{
		int relativeDay; //from the start
		int openPrice;
		
		public String toString(){
			return "Day No. = " + relativeDay + " openPrice = " + openPrice;
		}
	}
}
