import java.util.Arrays;

/**
 * Kareem Halabi
 * 260 616 162
 */

public class BaseConverter {

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
	
	public short[] mul(short[] a, short[] b, short base) {
		
		//ensures the larger array is first
		short[] inA;
		short[] inB;
		if(a.length > b.length){
			inA = a;
			inB = b;
		} else {
			inA = b;
			inB = a;
		}
		
		//create a 2d array to hold the intermediate products
		short[][] temp = new short[inB.length][2*inA.length];
		
		for(int j = 0; j < inB.length; j++) {
			short carry = 0;
			for(int i = 0; i < inA.length; i++) {
				short product = (short) (inA[i] * inB[j] + carry);
				temp[j][i+j] = (short) (product % base);
				carry = (short) (product/base);
			}
			temp[j][inA.length + j] = carry;
		}
		
		//add the intermediate steps
		return addAll(temp, base);	
	}

	public short[] addAll(short[][] numbers, short base) {
		
		//Finds the number with the most digits
		int maxLength = 0;
		for(int i = 0; i < numbers.length; i++)
			if(numbers[i].length > maxLength)
				maxLength = numbers[i].length;
		
		//Creates a new array to hold all the sums
		short[][] temp = new short[numbers.length][maxLength];
		for(int i = 0; i < temp.length; i++)
			System.arraycopy(numbers[i], 0, temp[i], 0, numbers[i].length);
		
		short[] result = new short[temp[0].length];
		short carry = 0;
		for(int i = 0; i < result.length - 1; i++) {
			
			short sum = carry;
			for(int j = 0; j < temp.length; j++) 
				sum += temp[j][i];
			
			result[i] = (short) (sum % base);
			carry = (short) (sum/base);
		}
		
		//copies the last carry into the last index of the result
		result[result.length-1] = carry;
		
		//truncate resulting array
		return truncate(result);
	}
	
	// assuming a > b
	//TODO try and get to work with negative
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
		return truncate(result);
	}
	
	//assuming a > b, b != 0
	//performs a/b
	//returns {quotient, remainder}
	public short[][] div(short[] a, short[] b, short base) {
		
		short[] q = new short[a.length];
		short[] r = new short[a.length];
		// r = a
		System.arraycopy(a, 0, r, 0, a.length);

		
		try {
			while(true) {
				//once r<d, exception is thrown ending loop
				short[] result = sub(r,b,base);
				q = add(q, new short[] {1}, base);
				r = result;
			}
		} catch(Exception e) {}
		
		return new short[][] {truncate(q), truncate(r)};
		
	}
	
	public short[] convertInt(short[] num, short srcBase, short destBase) {
		
		//represent "destBaseArray" in the base of "num"
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
		destBaseInSrcBase = truncate(destBaseInSrcBase);
		
		
		short[] number = new short[num.length];
		System.arraycopy(num, 0, number, 0, num.length);
		
		//create result array large enough to hold result
		short[] result = new short
				[(int) (Math.ceil(Math.log(srcBase)/Math.log(destBase)))*num.length];
				// ceil( log(srcBase)
				//       /log(destBase))*number_of_digits
		
		int i = 0;
		
		//ignoring negatives so only have to worry about equating to an array of 0s
		while(!Arrays.equals(number, new short[number.length])) {
			short[][] division = div(number, destBaseInSrcBase, srcBase);
			result[i] = division[1][0]; // digit = number % base
			number = division[0]; // number = number / base
			i++;
		}
		
		return truncate(result);
	}

	public short[] truncate(short[] result) {
		int finalLength = result.length;
		while(result[finalLength-1] == 0 && finalLength > 1)
			finalLength--;
		
		short[] finalResult = new short[finalLength];
		System.arraycopy(result, 0, finalResult, 0, finalLength);
		return finalResult;
	}
	
	public void printNumber(short [] num) {
		for(int i = num.length-1; i > 0; i--)
			System.out.print(num[i] + ",");
		System.out.println(num[0]);
	}
	
	public static void main(String[] args) {
		BaseConverter bc = new BaseConverter();
//		short[] arg1 = {1,0,9,8,7,6,5,4,3,2,1};
//		bc.printNumber(arg1);
//		short[] arg2 = {5,6};
//		bc.printNumber(arg2);
//		
//		bc.printNumber(bc.mul(arg1,arg2,(short) 10));
		
//		short[][] quotient_remainder = bc.div(arg1, arg2, (short) 10);
//		bc.printNumber(quotient_remainder[0]);
//		bc.printNumber(quotient_remainder[1]);
		
		short[] number = {7,6,5,4,3,2,1};
		short base = 30;
		bc.printNumber(number);
		bc.printNumber(bc.convertInt(number, base, (short) 5));
		
	}	
}