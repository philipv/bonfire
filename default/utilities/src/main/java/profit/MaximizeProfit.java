package profit;

import java.util.Arrays;

public class MaximizeProfit {

	public static void main(String[] args){
		int[] openPrices = new int[]{2, 1, 1, 7, 3};
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
		HistoricData[] result = null;
		//int profit = 0;
		for(HistoricData datum:generatedData){
			if(result == null){
				result = new HistoricData[2];
				result[0] = datum;
				result[1] = datum;
			}else{
				if(result[0].openPrice>datum.openPrice){
					result[0] = datum;
					result[1] = datum;
				}
				if(result[1].openPrice<datum.openPrice)
					result[1] = datum;
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
