/**
 * Kareem Halabi
 * 260 616 162
 */

public class UnusedMethods {

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
		return truncateTrailing(result);
	}

	public java.util.ArrayList<Short> toArrayList(short[] fractionIn) {
		
		java.util.ArrayList<Short> out = new java.util.ArrayList<Short>();
		for(int i = 0; i < fractionIn.length; i++)
			out.add(fractionIn[i]);
		
		return out;
	}
	public short[] truncateTrailing(short[] input) {
		int finalLength = input.length;
		while(input[finalLength-1] == 0 && finalLength > 1)
			finalLength--;
		
		short[] finalResult = new short[finalLength];
		System.arraycopy(input, 0, finalResult, 0, finalLength);
		return finalResult;
	}
}
