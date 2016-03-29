import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Kareem Halabi
 * 260 616 162
 */

public class FileLoader {

	public static FileReader fr;
	public static BufferedReader br;
	
	public static void main(String[] args) {
		try {
			fr = new FileReader("combinations/CacheLevel3.txt");
			br = new BufferedReader(fr);
			
			int puzzles = 0;
			long totalTime = 0;
			
			String line = br.readLine();
			HiRiQ test = new HiRiQ((byte)0);
			while(line != null) {
				String[] parts = line.split(":")[0].split(",");
				test.config = Integer.parseInt(parts[0]);
				test.weight = Byte.parseByte(parts[1]);
				
				System.out.println("\n" + test.config + ", " + test.weight);
				
				test.print();
				long start = System.nanoTime();
				test.solve();
				long end = System.nanoTime();
				System.out.println("\nCombinations checked: " + test.combinationsChecked);
				System.out.println("Took: " + (end-start)/1000000000f + " seconds");
				
				line = br.readLine();
				puzzles++;
				totalTime += end-start;
				
				System.out.println("Average time: " + (totalTime/1000000000f)/puzzles);
			}
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
