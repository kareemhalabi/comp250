import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.mcgill.comp250.BaseConverter;

/**
 * Kareem Halabi
 * 260 616 162
 */

public class TestConverter {

	BaseConverter bc;
	short[] result;
	short[] number = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
	short srcBase = 42;
	short destBase = 20;
	
	@Before
	public void setUp() throws Exception {
		bc = new BaseConverter();
		bc.printNumber(number);
	}

	@After
	public void tearDown() throws Exception {
		bc.printNumber(result);
	}

	@Test
	public void test() {
		result = bc.convertInt(number, srcBase, destBase);
	}

}
