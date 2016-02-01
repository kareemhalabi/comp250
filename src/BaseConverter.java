/**
 * Kareem Halabi
 * 260 616 162
 */

public class BaseConverter {

	//=========== Arithmetic Operations ===========//
	public short[] add(short[] a, short[] b, short base) {
		
		//create a result array big enough
		//to hold the result of the addition
		int size;
		if(a.length > b.length)
			size = a.length + 1;
		else
			size = b.length +1;
		
		//creates arrays all of the same size
		short[] result = new short[size];
		short[] inA = new short[size];
		System.arraycopy(a, 0, inA, 0, a.length);
		
		short[] inB = new short[size];
		System.arraycopy(b, 0, inB, 0, b.length);
		
		for(int i = 0; i < size - 1; i++) {
			//perform sum
			result[i] = (short) ((inA[i] + inB[i]) % base);
			
			//handle carry
			inA[i+1] += (inA[i] + inB[i]) / base;
		}
			
		//copies last carry digit if non-zero
		if(inA[size-1] != 0) {
			result[size-1] = inA[size-1];
			return result;
		}
		
		//if not truncate array and return
		short[] temp = new short[size-1];
		System.arraycopy(result, 0, temp, 0, (size-1));
		return temp;
	}	

	// assuming a >= b
	// throws Exception if a < b
	public short[] sub(short[] a, short[] b, short base) throws Exception {
		
		short[] result = new short[a.length];
		
		//copy arguments into new arrays
		short[] inA = new short[a.length];
		System.arraycopy(a, 0, inA, 0, a.length);
		short[] inB = new short[a.length];
		System.arraycopy(b, 0, inB, 0, b.length);
		
		for(int i = 0; i < inA.length; i++) {
			//checks if a borrow is neccessary
			if(inA[i]-inB[i] < 0) {
				inA[i+1] -= 1;
				inA[i] += base;
			}
			//perform actual subtraction
			result[i] = (short) (inA[i]-inB[i]);
		}
		
		//truncate resulting array
		return truncateTrailing(result);
	}
	//assuming a >= b, b != 0
	//performs a/b
	//returns {quotient, remainder}
	public short[][] div(short[] a, short[] b, short base) {
		
		//stores intermediate subtraction result
		short[] temp = new short[a.length];
		System.arraycopy(a, 0, temp, 0, a.length);
		
		//stores quotient
		short[] q = new short[a.length];
		
		//indicies to keep track of the size of the arrays
		int i = (temp.length-b.length);
		int j = 0;
		while(i>=0) {
			short[] subArray = new short[a.length-j];
			System.arraycopy(b, 0, subArray, i, b.length);
			
			try {
				while(true) {
					//once b>subArray, exception is thrown ending loop
					temp = sub(temp, subArray, base);
					short[] one = new short[q.length];
					one[i] = 1;
					q = add(q, one, base);
				}
			} catch(Exception e) {
				i--;
				j++;
				temp = truncateTrailing(temp);
				subArray = truncateTrailing(subArray);
			}
		}
		
		return new short[][] {truncateTrailing(q), truncateTrailing(temp)};
	}
	
	//This version of div only handles fractions < 1 i.e
	//assuming a<b b != 0
	//performs a/b
	//returns {nonRep, rep}
	public short[][] divModified(short[] a, short[] b, short base) {
		
		//stores intermediate subtraction result
		short[] temp = new short[a.length];
		System.arraycopy(a, 0, temp, 0, a.length);
		
		//stores quotient
		java.util.ArrayList<Short> q = new java.util.ArrayList<Short>();
		//stores remainders
		java.util.ArrayList<Short> remainders = new java.util.ArrayList<Short>();
		
		short remainder;
		outer:while(true) {
			
			q.add(0, (short) 0);
			try {
				while(true) {
					//once b>subArray, exception is thrown ending loop
					temp = sub(temp, b, base);
					q.set(0, (short) (q.get(0)+1)); //q[i]++
				}
			} catch(Exception e) {}
			
			remainder = digitsToShort(temp, base);
			if(remainders.contains(remainder))
				break outer;
			remainders.add(remainder);
			
			//copy temp into new array with a 0 at first index
			temp = truncateTrailing(temp);
			short[] temp2 = new short[temp.length + 1];
			System.arraycopy(temp, 0, temp2, 1, temp.length);
			temp = temp2;
		}
		
		int repeatingIndex = search(remainders, remainder);
		
		//TODO fix the arrayCopies
		short[] rep;
		short[] nonRep;
				
		if (repeatingIndex != 0) {
			rep = convertToArray(q, 0, q.size() - repeatingIndex - 1);
			nonRep = convertToArray(q, q.size()-repeatingIndex,
					(q.size() - 1));
					//^^^^ the last index of q is the 0 to the left of the decimal place
					//so we don't need it
			
			return new short[][] {nonRep, rep};
		}
		
		nonRep = new short[0];
		rep = convertToArray(q, 0, q.size()-1);
		return new short[][] {nonRep, rep};
	}
	
	public short[] convertToArray(java.util.ArrayList<Short> list, int start, int end) {
		
		short[] array = new short[end-start];
		for(int i = 0; i < array.length; i++)
			array[i] = list.get(start+i);
		
		return array;
	}
	
	public int search(java.util.ArrayList<Short> list, short key) {
		
		for(int i = 0; i < list.size(); i++)
			if(list.get(i)==key)
				return i;
		return -1;
	}
	
	public Short digitsToShort(short[] digits, short base) {
		
		short remainder = 0;
		for(int k = 0; k < digits.length; k++)
			remainder += digits[k]*Math.pow(base, k);
		return remainder;
	}

	//=========== Helper Methods ===========//
	public short[] truncateTrailing(short[] input) {
		int finalLength = input.length;
		while(input[finalLength-1] == 0 && finalLength > 1)
			finalLength--;
		
		short[] finalResult = new short[finalLength];
		System.arraycopy(input, 0, finalResult, 0, finalLength);
		return finalResult;
	}
	
	public void truncateLeading(java.util.ArrayList<Short> input) {	
		while(input.get(0) == 0)
			input.remove(0);
	}

	//represent "destBaseArray" in the base of "num"
	public short[] destBaseArray(short srcBase, short destBase) {
		
		short[] destBaseInSrcBase = new short[6];
		//for this assignment the largest neccesary array would be 6 digits
		//i.e (representing 60 in base 2 takes 6 digits)
		short m = destBase;
		int j = 0;
		while(m > 0) {
			destBaseInSrcBase[j] = (short) (m % srcBase);
			m = (short) (m/srcBase);
			j++;
		}
		destBaseInSrcBase = truncateTrailing(destBaseInSrcBase);
		return destBaseInSrcBase;
	}

	//=========== Converters ===========//
	
	//assuming: 2<=srcBase<=60, 2<=srcBase<=60,
	//"num" is non-empty, not null and contains all positive digits
	//that do not exceed "srcBase"-1
	public short[] convertInt(short[] num, short srcBase, short destBase) {
		
		short[] destBaseInSrcBase = destBaseArray(srcBase, destBase);
		
		//copy the input to a new array
		short[] number = new short[num.length];
		System.arraycopy(num, 0, number, 0, num.length);
		
		//create result array large enough to hold result
		short[] result = new short
				[(int) (Math.ceil(Math.log(srcBase)/Math.log(destBase)))*num.length];
				// ceil( log(srcBase)
				//       /log(destBase))*number_of_digits
		
		int i = 0;
		
		//ignoring negatives so only have to worry about equating to an array of 0s
		while(!java.util.Arrays.equals(number, new short[number.length])) {
			short[][] division = div(number, destBaseInSrcBase, srcBase);
			
			//represent remainder in new base
			result[i] = digitsToShort(division[1], srcBase);
			
			number = division[0]; // number = number / base
			i++;
		}
		
		return truncateTrailing(result);
	}

	public short[][] convertFraction(short[] fractionIn, short srcBase, short destBase) {
		
		short[] p = convertInt(fractionIn, srcBase, destBase);
		
		short[] z = new short[fractionIn.length + 1];
		z[z.length-1] = 1;
		
		short[] q = convertInt(z, srcBase, destBase);
		
		return divModified(p,q,destBase);	
	}

	public void printNumber(short [] num) {
		
		//Handles 0 length array
		if(num.length == 0) {
			System.out.println();
			return;
		}
			
		
		for(int i = num.length-1; i > 0; i--)
			System.out.print(num[i] + ",");
		System.out.println(num[0]);
	}
	
	public static void main(String[] args) {
		BaseConverter bc = new BaseConverter();
		
		short[][] result = bc.convertFraction(new short[] {1}, (short)26, (short)10);
		bc.printNumber(result[0]);
		bc.printNumber(result[1]);
		
	}	
}