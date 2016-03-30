import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Kareem Halabi
 * 260 616 162
 */

public class HiRiQ {
	
	public final static byte[][] possibleConfigs = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 7, 8, 9 }, { 8, 9, 10 },
			{ 9, 10, 11 }, { 10, 11, 12 }, { 13, 14, 15 }, { 14, 15, 16 }, { 15, 16, 17 }, { 16, 17, 18 },
			{ 17, 18, 19 }, { 20, 21, 22 }, { 21, 22, 23 }, { 22, 23, 24 }, { 23, 24, 25 }, { 24, 25, 26 },
			{ 27, 28, 29 }, { 30, 31, 32 }, { 12, 19, 26 }, { 11, 18, 25 }, { 2, 5, 10 }, { 5, 10, 17 }, { 10, 17, 24 },
			{ 17, 24, 29 }, { 24, 29, 32 }, { 1, 4, 9 }, { 4, 9, 16 }, { 9, 16, 23 }, { 16, 23, 28 }, { 23, 28, 31 },
			{ 0, 3, 8 }, { 3, 8, 15 }, { 8, 15, 22 }, { 15, 22, 27 }, { 22, 27, 30 }, { 7, 14, 21 }, { 6, 13, 20 } };
	
	
	// int is used to reduce storage to a minimum...
	public int config;
	public byte weight;
	
	public HiRiQ parent;
	public Move moveToParent;
	public ArrayList<HiRiQ> children;
	
	public class Move {
		
		public byte start;
		public byte middle;
		public byte end;
		
		public Move(byte start, byte middle, byte end) {
			this.start = start;
			this.middle = middle;
			this.end = end;
		}
		
		@Override
		public String toString() {
			return "" + start + "@" + end;
		}
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof Move) {
				Move m = (Move) o;
				return this.start == m.start && this.middle == m.middle && this.end == m.end;
			}
			return false;
		}
		
	}

	public HiRiQ(byte n) {

		if (n == 0) {
			config = 65536 / 2;
			weight = 1;
		} else if (n == 1) {
			config = 4403916;
			weight = 11;
		} else if (n == 2) {
			config = -1026781599;
			weight = 21;
		} else {
			config = -2147450879;
			weight = 32;
		}
	}
	
	public HiRiQ(int config, byte weight) {
		this.config = config;
		this.weight = weight;
	}
	
	public HiRiQ(boolean[] state) {
		this.store(state);
	}
	
	// initialize the configuration to one of 4 START setups
		// n=0,10,20,30
		public boolean IsSolved() {
			return ((config == 65536 / 2) && (weight == 1));
		}

		// transforms the array of 33 booleans to an (int) cinfig and a
		// (byte) weight.
		public void store(boolean[] B) {

			int a = 1;
			config = 0;
			weight = (byte) 0;

			if (B[0]) {
				weight++;
			}

			for (int i = 1; i < 32; i++) {
				if (B[i]) {
					config = config + a;
					weight++;
				}
				a = 2 * a;
			}

			if (B[32]) {
				config = -config;
				weight++;
			}
		}

		// transform the int representation to an array of booleans.
		// the weight (byte) is necessary because only 32 bits are memorized
		// and so the 33rd is decided based on the fact that the config has
		// the
		// correct weight or not.
		public boolean[] load() {
			boolean[] B = new boolean[33];
			byte count = 0;
			int fig = config;
			B[32] = fig < 0;

			if (B[32]) {
				fig = -fig;
				count++;
			}

			int a = 2;
			for (int i = 1; i < 32; i++) {
				B[i] = fig % a > 0;
				if (B[i]) {
					fig = fig - a / 2;
					count++;
				}
				a = 2 * a;
			}
			B[0] = count < weight;
			return (B);
		}

		public void print() {
			byte count = 0;
			int fig = config;
			boolean next, last = fig < 0;
		
			if (last) {
				fig = -fig;
				count++;
			}
			int a = 2;
			for (int i = 1; i < 32; i++) {
				next = fig % a > 0;
		
				if (next) {
					fig = fig - a / 2;
					count++;
				}
				a = 2 * a;
			}
			next = count < weight;
		
			count = 0;
			fig = config;
			if (last) {
				fig = -fig;
				count++;
			}
			a = 2;
		
			System.out.print("      ");
			printB(next);
			for (int i = 1; i < 32; i++) {
				next = fig % a > 0;
				if (next) {
					fig = fig - a / 2;
					count++;
				}
				a = 2 * a;
				printB(next);
				if (i == 2 || i == 5 || i == 12 || i == 19 || i == 26 || i == 29) {
					System.out.println();
				}
				if (i == 2 || i == 26 || i == 29) {
					System.out.print("      ");
				}
			}
			printB(last);
			System.out.println();
		}

		// prints the int representation to an array of booleans.
		// the weight (byte) is necessary because only 32 bits are memorized
		// and so the 33rd is decided based on the fact that the config has
		// the
		// correct weight or not.
		public void printB(boolean Z) {
			if (Z) {
				System.out.print("[ ]");
			} else {
				System.out.print("[@]");
			}
		}

		public static void printMoves(ArrayList<Move> moves) {
			for(int i = 0; i < moves.size()-1; i++) {
				System.out.print(moves.get(i) + " -> ");
			}
			if(moves.size() != 0)
				System.out.print(moves.get(moves.size()-1));
		}
		
		// returns an arraylist of all possible B moves and W moves;
		public ArrayList<Move> findAvailableMoves() {
			boolean[] board = this.load();
			ArrayList<Move> availableMoves = new ArrayList<Move>();
			 
			for(int i = 0; i < possibleConfigs.length; i++ ) {
				byte t1 = possibleConfigs[i][0];
				byte t2 = possibleConfigs[i][1];
				byte t3 = possibleConfigs[i][2];
				
				//BBW
				if (!board[t1] && !board[t2] && board[t3])
					availableMoves.add(new Move(t1, t2, t3)); //t1"B"t3
				
				//WBB
				else if (board[t1] && !board[t2] && !board[t3])
					availableMoves.add(new Move(t3, t2, t1)); //t3"B"t1
				
				//W substitutions are added to the beginning to improve
				//efficiency
				
				//WWB
				else if (board[t1] && board[t2] && !board[t3])
					availableMoves.add(0,new Move(t1, t2, t3)); //t1"W"t3
				
				//BWW
				else if (!board[t1] && board[t2] && board[t3])
					availableMoves.add(0,new Move(t3, t2, t1)); //t3"W"t1
			}
			
			return availableMoves;
		}

		// applies a move to a HiRiQ, throws exception if move is invalid
		public void apply(Move move) throws Exception{
			
			boolean[] board = this.load();
			
			//checks for
			//WWB, BWW,
			//BBW or WBB
			if(    (board[move.middle] == board[move.start] && board[move.middle] != board[move.end])
				|| (board[move.middle] != board[move.start] && board[move.middle] == board[move.end])	) {
				
				board[move.start] = !board[move.start];
				board[move.middle] = !board[move.middle];
				board[move.end] = !board[move.end];
				
				this.store(board);
			}
			else
				throw new Exception("Invalid Move");
		}
		
		
		static Queue<HiRiQ> toCheck = new LinkedList<HiRiQ>();
		
		//caps the Queue size
		static int toCheckCap = 10000000;
		
		//TODO Implement queue cap
		//TODO Implement DFS level cap
		
		long combinationsChecked = 0;
		
		public ArrayList<Move> movesToSolution;
		
		public void solve() throws Exception {
			
			toCheck.add(this);
			while(!toCheck.isEmpty()) {
				
				// takes current node off of the queue
				HiRiQ current = toCheck.poll();
//				current.print();
//				System.out.println();
				
				// check the to see if solved
				if(current.IsSolved()) {
					movesToSolution = new ArrayList<Move>();
					// cycles up the parents to generate a move path
					while(current.moveToParent != null) {
						movesToSolution.add(0,current.moveToParent);
						current = current.parent;
					}
					printMoves(movesToSolution);
					toCheck.clear();
					return;
				}
				
				// if not, add all the node's children to the queue
				
				if (toCheck.size() <= toCheckCap) {
					ArrayList<Move> possibleMoves = current.findAvailableMoves();
					current.children = new ArrayList<HiRiQ>();
					//removes the move to parent from the possible Moves to prevent infinite loop
					possibleMoves.remove(current.moveToParent);
					for (Move m : possibleMoves) {
						HiRiQ child = new HiRiQ(current.config, current.weight);
						child.apply(m);
						child.parent = current;
						child.moveToParent = m;
						current.children.add(child);
						toCheck.add(child);
					} 
				}
				combinationsChecked++;
//				System.out.println("Queue size: " + toCheck.size());
			}
			
			System.err.println("Maxed out queue, could not find a solution");
		}

		
		public static void main(String[] args) {
			
			boolean[] configuration = {
							
								false, 	false,	false,
								false, 	false,	false,
				false,	false,	false,	false,	false,	false,	false,
				false,	false,	false,	true,	false,	false,	false,
				false,	false,	false,	false,	false,	false,	false,
								false,	false,	false,
								false,	false,	false
					
			};
			/*					0		1		2
			 * 					3		4		5
			 * 	6		7		8		9		10		11		12
			 * 	13		14		15		16		17		18		19
			 * 	20		21		22		23		24		25		26
			 * 					27		28		29
			 * 					30		31		32
			 */
			
//			int puzzles = 0;
//			long totalTime = 0;
//			long maxTime = 0;
//			long minTime = Long.MAX_VALUE;
//			long maxCombinations = 0;
//			long minCombinations = Long.MAX_VALUE;
			
			HiRiQ test = new HiRiQ(434624, (byte)7);
			
			HiRiQ original = new HiRiQ(test.config, test.weight);
			
			System.out.println("\n" + test.config + ", " + test.weight);
			test.print();
			
			try {
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
				
//				puzzles++;
//				totalTime += end-start;
//				
//				if((end-start)>maxTime)
//					maxTime = end-start;
//				else if((end-start) < minTime)
//					minTime = end-start;
//				if(test.combinationsChecked > maxCombinations)
//					maxCombinations = test.combinationsChecked;
//				if(test.combinationsChecked < minCombinations)
//					minCombinations = test.combinationsChecked;
//				
//				System.out.println("\n\n"+"Puzzles checked: " + puzzles);
//				System.out.println("Total time so far: " + (totalTime/1000000000f) + " seconds");
//				System.out.println("Average time: " + (totalTime/1000000000f)/puzzles + " seconds");
//				System.out.println("Max time so far: " + (maxTime/1000000000f) + " seconds");
//				System.out.println("Min time so far: " + (minTime/1000000000f) + " seconds");
//				System.out.println("Max combinations checked: " + maxCombinations);
//				System.out.println("Min combinations checked: " + minCombinations);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
