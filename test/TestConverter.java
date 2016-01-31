import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Kareem Halabi
 * 260 616 162
 */

public class TestConverter {

	BaseConverter bc;
	
	@Before
	public void setUp() throws Exception {
		bc = new BaseConverter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		short[] number = {7,6,5,4,3,2,1};
		short base = 30;
		bc.printNumber(number);
		bc.printNumber(bc.convertInt(number, base, (short) 5));
	}

}
