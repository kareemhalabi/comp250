import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



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
	public List<HiRiQ> children;
	
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
			System.out.print(moves.get(moves.size()-1));
		}
		
		// returns an arraylist of all possible B moves and W moves;
		public ArrayList<HashSet<Move>> findAvailableMoves() {
			boolean[] board = this.load();
			HashSet<Move> bMoves = new HashSet<Move>();
			HashSet<Move> wMoves = new HashSet<Move>();
			 
			for(int i = 0; i < possibleConfigs.length; i++ ) {
				byte t1 = possibleConfigs[i][0];
				byte t2 = possibleConfigs[i][1];
				byte t3 = possibleConfigs[i][2];
				
				//BBW
				if (!board[t1] && !board[t2] && board[t3])
					bMoves.add(new Move(t1, t2, t3)); //t1"B"t3
				
				//WBB
				else if (board[t1] && !board[t2] && !board[t3])
					bMoves.add(new Move(t3, t2, t1)); //t3"B"t1
				
				//WWB
				else if (board[t1] && board[t2] && !board[t3])
					wMoves.add(new Move(t1, t2, t3)); //t1"W"t3
				
				//BWW
				else if (!board[t1] && board[t2] && board[t3])
					wMoves.add(new Move(t3, t2, t1)); //t3"W"t1
			}

			ArrayList<HashSet<Move>> allMoves = new ArrayList<HashSet<Move>>();
			allMoves.add(bMoves);
			allMoves.add(wMoves);
			
			return allMoves;
		}

		// applies a move to a HiRiQ
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
		
		public void solve() {
			
		}

		
		public static void main(String[] args) {
			
			boolean[] configuration = {
							
								true, 	true,	true,
								true, 	true,	true,
				true,	true,	true,	true,	true,	true,	true,
				true,	true,	true,	false,	true,	true,	true,
				true,	true,	true,	true,	true,	true,	true,
								true,	true,	true,
								true,	true,	true
					
			};
			/*					0		1		2
			 * 					3		4		5
			 * 	6		7		8		9		10		11		12
			 * 	13		14		15		16		17		18		19
			 * 	20		21		22		23		24		25		26
			 * 					27		28		29
			 * 					30		31		32
			 */
			
			
			HiRiQ board = new HiRiQ(configuration);
			
			Move m = board.new Move((byte)16, (byte)17,(byte)18);
			
			try {
				board.apply(m);
				board.print();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
}
