package ca.mcgill.comp250.a2;
/**
 * Kareem Halabi
 * 260 616 162
 */


public class Assignment2Q4Test {

	public static void main(String[] args) {
		PlotWindow test = new PlotWindow("Algo1");
		
		int[] dataPoints = new int[1000];
		for(int i = 0; i<dataPoints.length; i++)
			dataPoints[i] = algorithm(i);
		
		test.addPlot("Algo1", dataPoints);

	}

	public static int algorithm(int n) {
		int ops = 0;
		int k = 1;
		for(int i = 1; i<=1000; i++) {
			for(int j = 1; j<=i; j++) {
				k = (k+i-j)*(2+i+j); ops++;
			}
		}

		return ops;
	}
	
}
