package statisticsParser;

import org.jfree.data.xy.XYSeries;

public class parserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XYSeries series1 = new XYSeries("Sarja1", false, false);
		XYSeries series2 = new XYSeries("Sarja2", false, false);
		XYSeries[] array = new XYSeries[2];
		array[0] = series1;
		array[1] = series2;
		
		//series1.add(3, 2);
		//series1.add(2, 3);
		
		//series2.add(4, 2);
		//series2.add(-1, 3);
		
		series1.add(0,0);
		
		Scanner scanner = new Scanner("exp ac0 + 2");
		Parser parser = new Parser(scanner, array);
		parser.Parse();
		System.out.printf("Max: %.2f, Min: %.2f\n",array[0].getMaxY(),array[0].getMinY());
		
	}

}
