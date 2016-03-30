import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Kareem Halabi
 * 260 616 162
 */

public class FileLoader {

	public static FileReader fr;
	public static BufferedReader br;
	
	public static void main(String[] args) {
		try {
			fr = new FileReader("combinations/CacheLevel6.txt");
			br = new BufferedReader(fr);
			
			int puzzles = 0;
			long totalTime = 0;
			long maxTime = 0;
			long minTime = Long.MAX_VALUE;
			long maxCombinations = 0;
			long minCombinations = Long.MAX_VALUE;
			
//			ArrayList<String> skipped = new ArrayList<String>();
			
			String line = br.readLine();
			HiRiQ test = new HiRiQ((byte)0);
//			loop:
			while(line != null) {
				
				//Skips broken configs
//				if(line.contains("30B32") || line.contains("30W32")) {
//					skipped.add(line);
//					line = br.readLine();
//					continue loop;
//				}
				
				//Get the data
				String[] parts = line.split(":")[0].split(",");
				test.config = Integer.parseInt(parts[0]);
				test.weight = Byte.parseByte(parts[1]);
				HiRiQ original = new HiRiQ(test.config, test.weight);
				
				System.out.println("\n" + test.config + ", " + test.weight);
				test.print();
				
				long start = System.nanoTime();
				test.solve();
				long end = System.nanoTime();
				
				System.out.println("\nCombinations checked: " + test.combinationsChecked);
				System.out.println("Took: " + (end-start)/1000000000f + " seconds");
				
				//restore original and verify
				
				for(HiRiQ.Move m: test.movesToSolution) {
					original.apply(m);
				}
				if(!original.IsSolved()) {
					throw new Exception("Solution incorrect");
				}
				
				System.out.println("Verified: " + original.IsSolved());
				
				line = br.readLine();
				puzzles++;
				totalTime += end-start;
				
				if((end-start)>maxTime)
					maxTime = end-start;
				else if((end-start) < minTime)
					minTime = end-start;
				if(test.combinationsChecked > maxCombinations)
					maxCombinations = test.combinationsChecked;
				if(test.combinationsChecked < minCombinations)
					minCombinations = test.combinationsChecked;
				
				System.out.println("\n\n"+"Puzzles checked: " + puzzles);
				System.out.println("Total time so far: " + (totalTime/1000000000f) + " seconds");
				System.out.println("Average time: " + (totalTime/1000000000f)/puzzles + " seconds");
				System.out.println("Max time so far: " + (maxTime/1000000000f) + " seconds");
				System.out.println("Min time so far: " + (minTime/1000000000f) + " seconds");
				System.out.println("Max combinations checked: " + maxCombinations);
				System.out.println("Min combinations checked: " + minCombinations);
			}
			
			System.out.println("DONE");
//			for(String s : skipped) {
//				System.out.println(s);
//			}
			
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
