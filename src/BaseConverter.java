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
		
		//creates new arrays all of the same size
		short[] inA = new short[size];
		System.arraycopy(a, 0, inA, 0, a.length);
		short[] inB = new short[size];
		System.arraycopy(b, 0, inB, 0, b.length);
		short[] result = new short[size];
		
		//cycle through each digit
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
		
		//if not remove last 0 digit and return
		//no need for truncate method since the
		//decrement in size is known to always be 1
		short[] temp = new short[size-1];
		System.arraycopy(result, 0, temp, 0, (size-1));
		return temp;
	}	

	// assuming a >= b
	// throws Exception if a < b
	public short[] sub(short[] a, short[] b, short base) throws Exception {
		
		short[] result = new short[a.length];
		
		//copy arguments into new arrays
		//of same size
		short[] inA = new short[a.length];
		System.arraycopy(a, 0, inA, 0, a.length);
		short[] inB = new short[a.length];
		System.arraycopy(b, 0, inB, 0, b.length);
		
		//cycle through each digit
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
		return truncate(result);
	}
	
	//assuming a >= b, b != 0
	//performs a/b
	//returns {quotient, remainder}
	public short[][] divQuotientRemainder(short[] a, short[] b, short base) {
		
		//stores intermediate subtraction result
		short[] subtractTemp = new short[a.length];
		System.arraycopy(a, 0, subtractTemp, 0, a.length);
		
		//stores quotient
		short[] q = new short[a.length];
		
		//pointer indicies to keep track of where
		//to insert a digit into q
		//and which index of subtractTemp
		//to subtract the divisor b
		int qPos = (subtractTemp.length - b.length);
		int subArrayLength = a.length;
		while(qPos>=0) {
			short[] subArray = new short[subArrayLength];
			System.arraycopy(b, 0, subArray, qPos, b.length);
			
			//repeated subtraction at highest indexed
			//digit of subtractTemp
			try {
				while(true) {
					//once b>subArray, exception is thrown ending loop
					subtractTemp = sub(subtractTemp, subArray, base);
					
					//if subtraction is successful, 1 is added
					//to q at the appropriate index
					short[] one = new short[q.length];
					one[qPos] = 1;
					q = add(q, one, base);
				}
			} catch(Exception e) {
				//decrement the pointer of the next digit to add to q
				qPos--;  
				//decrement the position of the next subtraction
				//by the divisor b
				subArrayLength--;
				
				//remove trailing 0s
				subtractTemp = truncate(subtractTemp);
				subArray = truncate(subArray);
			}
		}
		
		return new short[][] {truncate(q), truncate(subtractTemp)};
	}
	
	//This version of div only handles fractions < 1 i.e
	//assuming a<b b != 0
	//performs a/b
	//returns {nonRep, rep}
	public short[][] divNonRep_Rep(short[] a, short[] b, short base) {
		
		//stores intermediate subtraction result
		short[] subtractTemp = new short[a.length];
		System.arraycopy(a, 0, subtractTemp, 0, a.length);
		
		//stores quotient
		java.util.ArrayList<Short> q = new java.util.ArrayList<Short>();
		//stores remainders
		java.util.ArrayList<Short> remainders = new java.util.ArrayList<Short>();
		
		//stores current remainder
		short remainder;
		
		//repeated subtraction
		outer:while(true) {
			
			//add a new digit to the quotient
			q.add(0, (short) 0);
			try {
				while(true) {
					//once b>subArray, exception is thrown ending loop
					subtractTemp = sub(subtractTemp, b, base);
					q.set(0, (short) (q.get(0)+1)); //q[i]++
				}
			} catch(Exception e) {}
			
			/* 
			 * convert whatever is left in subtractTemp in base "base"
			 * into a base 10 short that can be stored in the
			 * ArrayList "remainders" so that it can be searched
			 * for in further iterations
			 */
			
			remainder = digitsToBase10Short(subtractTemp, base);
			//stop the division loop if remainder has
			//been seen before
			if(remainders.contains(remainder))
				break outer;
			
			//otherwise add to list
			remainders.add(remainder);
			
			//copy subtractTemp into new array with a 0 at first index
			subtractTemp = truncate(subtractTemp);
			short[] temp2 = new short[subtractTemp.length + 1];
			System.arraycopy(subtractTemp, 0, temp2, 1, subtractTemp.length);
			subtractTemp = temp2;
		}
		
		/* 
		 * This index represents which step was the start
		 * of the repeating series (exclusive)
		 */
		int repeatingIndex = search(remainders, remainder);
		
		/*
		 * Explanation of the array copies:
		 * q lists the digits in the reverse way that we humans read them
		 * it also includes the 0 to the left of the decimal place
		 * 
		 * the repeating part of q starts at index 0 and ends at
		 * (q.size()-1)-repeatingIndex-1 (inclusive)
		 * 
		 * the non repeating part of q starts at (q.size()-1) - repeatingIndex
		 * and ends at (q.size()-1)-1 (inclusive)
		 * (omitting the 0 to the left of the decimal place)
		 * 
		 */
		short[] rep = convertToArray(q, 0, (q.size()-1) - repeatingIndex-1);
		short[] nonRep = convertToArray(q, (q.size()-1) - repeatingIndex, (q.size()-1)-1);
		
		return new short[][] {nonRep, rep};
	}
	
	//copies elements from index "start" to index "end" (inclusive)
	public short[] convertToArray(java.util.ArrayList<Short> list, int start, int end) {
		
		short[] array = new short[end-start+1];
		for(int i = 0; i < array.length; i++)
			array[i] = list.get(start+i);
		
		return array;
	}
	
	//searches for first occurrence of "key" in "list"
	public int search(java.util.ArrayList<Short> list, short key) {
		
		for(int i = 0; i < list.size(); i++)
			if(list.get(i)==key)
				return i;
		return -1;
	}
	
	public Short digitsToBase10Short(short[] digits, short base) {
		
		short base10 = 0;
		for(int k = 0; k < digits.length; k++)
			base10 += digits[k]*Math.pow(base, k);
		return base10;
	}

	//=========== Helper Methods ===========//
	
	//assuming non-null input with length > 0
	public short[] truncate(short[] input) {
		//initialize the final length as the starting length
		int finalLength = input.length;
		while(input[finalLength-1] == 0 && finalLength > 1)
			finalLength--; //decrement length if 0 is found
		
		//truncate array
		short[] finalResult = new short[finalLength];
		System.arraycopy(input, 0, finalResult, 0, finalLength);
		return finalResult;
	}
	
	//returns a short[] of "destBase" in the base of "srcBase"
	public short[] destBaseArray(short srcBase, short destBase) {
		
		short[] destBaseInSrcBase = new short[6];
		//for this assignment the largest necessary array would be 6 digits
		//i.e (representing 60 in base 2 takes 6 digits)
		short m = destBase;
		int j = 0;
		while(m > 0) {
			destBaseInSrcBase[j] = (short) (m % srcBase);
			m = (short) (m/srcBase);
			j++;
		}
		destBaseInSrcBase = truncate(destBaseInSrcBase);
		return destBaseInSrcBase;
	}

	//=========== Base Converter Methods ===========//
	
	/*
	 * Assumptions:
	 * 2<=srcBase<=60, 2<=destBase<=60
	 * "intIn" is non-empty, not null
	 * contains all positive digits that do not exceed "srcBase"-1
	 */
	public short[] convertInt(short[] intIn, short srcBase, short destBase) {
		
		//represent "destBase" in the base of "srcBase"
		short[] destBaseInSrcBase = destBaseArray(srcBase, destBase);
		
		//copy the input to a new array
		short[] number = new short[intIn.length];
		System.arraycopy(intIn, 0, number, 0, intIn.length);
		
		//create result array large enough to hold result
		short[] result = new short
				[(int) (Math.ceil(Math.log(srcBase)/Math.log(destBase)))*intIn.length];
				// ceil( log(srcBase)
				//       /log(destBase))*number_of_digits
		
		//index to keep track of where to insert the next digit
		int i = 0;
		
		//ignoring negatives so only have to worry about equating to an array of 0s
		while(!java.util.Arrays.equals(number, new short[number.length])) {
			
			short[][] division = divQuotientRemainder(number, destBaseInSrcBase, srcBase);
			//division[0] = quotient
			//division[1] = remainder
			
			//represent remainder in new base
			result[i] = digitsToBase10Short(division[1], srcBase);
			
			number = division[0]; // number = number / base
			i++;
		}
		
		return truncate(result);
	}

	/*
	 * Assumptions:
	 * 2<=srcBase<=60, 2<=destBase<=60
	 * "fractionIn" is non-empty, not null
	 * contains all positive digits that do not exceed "srcBase"-1
	 */
	public short[][] convertFraction(short[] fractionIn, short srcBase, short destBase) {
		
		//following the algorithm in the notes		
		short[] p = convertInt(fractionIn, srcBase, destBase);
		
		short[] z = new short[fractionIn.length + 1];
		z[z.length-1] = 1;
		
		short[] q = convertInt(z, srcBase, destBase);
		
		return divNonRep_Rep(p,q,destBase);	
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
		
		short[][] result = bc.convertFraction(new short[] {1,1}, (short)26, (short)10);
		bc.printNumber(result[0]);
		bc.printNumber(result[1]);
		
	}	
}