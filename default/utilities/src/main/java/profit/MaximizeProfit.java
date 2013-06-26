package profit;

public class MaximizeProfit {

	public static void main(String[] args){
		int[] openPrices = new int[]{6, 2, 1, 5, 1, 4, 3};
		 HistoricData[] generatedData = new HistoricData[openPrices.length];
		 for(int i=0;i<openPrices.length;i++){
			 generatedData[i] = new HistoricData();
			 generatedData[i].relativeDay = i;
			 generatedData[i].openPrice = openPrices[i];
		 }
		 
		 HistoricData[] result = maximizeProfit(generatedData);
	}
	
	private static HistoricData[] maximizeProfit(HistoricData[] generatedData) {
		if(generatedData.length == 2)
			return generatedData;
		else{
			int halfIndex = generatedData.length/2;
			HistoricData[] firstHalf = new HistoricData[halfIndex];
			System.arraycopy(generatedData, 0, firstHalf, 0, halfIndex);
			HistoricData[] secondHalf = new HistoricData[generatedData.length - halfIndex];
			System.arraycopy(generatedData, halfIndex, secondHalf, 0, generatedData.length - halfIndex);
			
		}
		return null;
	}

	static class HistoricData{
		int relativeDay; //from the start
		int openPrice;
	}
}
