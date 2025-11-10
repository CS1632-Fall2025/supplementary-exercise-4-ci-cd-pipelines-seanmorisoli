package edu.pitt.cs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RentACatUnitTest {

	/**
	 * The test fixture for this JUnit test. Test fixture: a fixed state of a set of
	 * objects used as a baseline for running tests. The test fixture is initialized
	 * using the @Before setUp method which runs before every test case. The test
	 * fixture is removed using the @After tearDown method which runs after each
	 * test case.
	 */

	RentACat r; // Object to test
	Cat c1; // First cat object
	Cat c2; // Second cat object
	Cat c3; // Third cat object

	ByteArrayOutputStream out; // Output stream for testing system output
	PrintStream stdout; // Print stream to hold the original stdout stream
	String newline = System.lineSeparator(); // Platform independent newline ("\n" or "\r\n") for use in assertEquals

	@Before
	public void setUp() throws Exception {
		// INITIALIZE THE TEST FIXTURE
		
		// 1. Create a new RentACat object and assign to r using a call to RentACat.createInstance(InstanceType).
		// Passing InstanceType.IMPL as the first parameter will create a real RentACat object using your RentACatImpl implementation.
		// Passing InstanceType.MOCK as the first parameter will create a mock RentACat object using Mockito.
		// Which type is the correct choice for this unit test?  I'll leave it up to you.  The answer is in the Unit Testing Part 2 lecture. :)
		// For unit testing RentACat, we want to test the actual RentACat implementation
		r = RentACat.createInstance(InstanceType.IMPL);

		c1 = Cat.createInstance(InstanceType.MOCK, 1, "Jennyanydots");

		// 3. Create a Cat with ID 2 and name "Old Deuteronomy", assign to c2 using a call to Cat.createInstance(InstanceType, int, String).
		c2 = Cat.createInstance(InstanceType.MOCK, 2, "Old Deuteronomy");

		// 4. Create a Cat with ID 3 and name "Mistoffelees", assign to c3 using a call to Cat.createInstance(InstanceType, int, String).
		c3 = Cat.createInstance(InstanceType.MOCK, 3, "Mistoffelees");

		// Configure mock cat behavior for IDs (crucial for getCat to work)
		when(c1.getId()).thenReturn(1);
		when(c2.getId()).thenReturn(2);
		when(c3.getId()).thenReturn(3);

		// 5. Redirect system output from stdout to the "out" stream
		// First, make a back up of System.out (which is the stdout to the console)
		stdout = System.out;
		// Second, update System.out to the PrintStream created from "out"
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}

	@After
	public void tearDown() throws Exception {
		// Restore System.out to the original stdout
		System.setOut(stdout);

		// Not necessary strictly speaking since the references will be overwritten in
		// the next setUp call anyway and Java has automatic garbage collection.
		r = null;
		c1 = null;
		c2 = null;
		c3 = null;
	}

	/**
	 * Test case for Cat getCat(int id).
	 * 
	 * <pre>
	 * Preconditions: r has no cats.
	 * Execution steps: Call getCat(2).
	 * Postconditions: Return value is null.
	 *                 System output is "Invalid cat ID." + newline.
	 * </pre>
	 * 
	 * Hint: You will need to use Java reflection to invoke the private getCat(int)
	 * method. efer to the Unit Testing Part 1 lecture and the textbook appendix 
	 * hapter on using reflection on how to do this.  Please use r.getClass() to get
	 * the class object of r instead of hardcoding it as RentACatImpl.
	 */
	@Test
	public void testGetCatNullNumCats0() {
		try {
			// Use reflection to access the private getCat method
			Method getCatMethod = r.getClass().getDeclaredMethod("getCat", int.class);
			getCatMethod.setAccessible(true);
			
			// Execution step: Call getCat(2)
			Cat result = (Cat) getCatMethod.invoke(r, 2);
			
			// Postcondition: Return value is null
			assertNull("getCat should return null when no cats exist", result);
			
			// Postcondition: System output is "Invalid cat ID." + newline
			assertEquals("Should print invalid cat ID message", "Invalid cat ID." + newline, out.toString());
		} catch (Exception e) {
			fail("Exception occurred during reflection: " + e.getMessage());
		}
	}

	/**
	 * Test case for Cat getCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call getCat(2).
	 * Postconditions: Return value is not null.
	 *                 Returned cat has an ID of 2.
	 * </pre>
	 * 
	 * Hint: You will need to use Java reflection to invoke the private getCat(int)
	 * method. efer to the Unit Testing Part 1 lecture and the textbook appendix 
	 * hapter on using reflection on how to do this.  Please use r.getClass() to get
	 * the class object of r instead of hardcoding it as RentACatImpl.
	 */
	@Test
	public void testGetCatNumCats3() {
		// Preconditions: c1, c2, and c3 are added to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);
		
		try {
			// Use reflection to access the private getCat method
			Method getCatMethod = r.getClass().getDeclaredMethod("getCat", int.class);
			getCatMethod.setAccessible(true);
			
			// Execution step: Call getCat(2)
			Cat result = (Cat) getCatMethod.invoke(r, 2);
			
			// Postcondition: Return value is not null
			assertNotNull("getCat should return a cat when ID exists", result);
			
			// Postcondition: Returned cat has an ID of 2
			assertEquals("Returned cat should have ID 2", c2, result);
		} catch (Exception e) {
			fail("Exception occurred during reflection: " + e.getMessage());
		}
	}

	/**
	 * Test case for String listCats().
	 * 
	 * <pre>
	 * Preconditions: r has no cats.
	 * Execution steps: Call listCats().
	 * Postconditions: Return value is "".
	 * </pre>
	 */
	@Test
	public void testListCatsNumCats0() {
		// Execution step: Call listCats()
		String result = r.listCats();
		
		// Postcondition: Return value is ""
		assertEquals("listCats should return empty string when no cats exist", "", result);
	}

	/**
	 * Test case for String listCats().
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call listCats().
	 * Postconditions: Return value is "ID 1. Jennyanydots\nID 2. Old
	 *                 Deuteronomy\nID 3. Mistoffelees\n".
	 * </pre>
	 */
	@Test
	public void testListCatsNumCats3() {
		// Set up mock behavior for the cats
		when(c1.getRented()).thenReturn(false);
		when(c2.getRented()).thenReturn(false);
		when(c3.getRented()).thenReturn(false);
		when(c1.toString()).thenReturn("ID 1. Jennyanydots");
		when(c2.toString()).thenReturn("ID 2. Old Deuteronomy");
		when(c3.toString()).thenReturn("ID 3. Mistoffelees");
		
		// Preconditions: c1, c2, and c3 are added to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);
		
		// Execution step: Call listCats()
		String result = r.listCats();
		
		// Postcondition: Return value is "ID 1. Jennyanydots\nID 2. Old Deuteronomy\nID 3. Mistoffelees\n"
		String expected = "ID 1. Jennyanydots" + newline + "ID 2. Old Deuteronomy" + newline + "ID 3. Mistoffelees" + newline;
		assertEquals("listCats should return formatted list of all available cats", expected, result);
	}

	/**
	 * Test case for boolean renameCat(int id, String name).
	 * 
	 * <pre>
	 * Preconditions: r has no cats.
	 * Execution steps: Call renameCat(2, "Garfield").
	 * Postconditions: Return value is false.
	 *                 c2 is not renamed to "Garfield".
	 *                 System output is "Invalid cat ID." + newline.
	 * </pre>
	 * 
	 * Hint: You may need to use behavior verification for this one. See
	 * sample_code/junit_example/LinkedListUnitTest.java in the course repository to
	 * see examples.
	 */
	@Test
	public void testRenameFailureNumCats0() {
		// Execution step: Call renameCat(2, "Garfield")
		boolean result = r.renameCat(2, "Garfield");
		
		// Postcondition: Return value is false
		assertFalse("renameCat should return false when cat doesn't exist", result);
		
		// Postcondition: System output is "Invalid cat ID." + newline
		assertEquals("Should print invalid cat ID message", "Invalid cat ID." + newline, out.toString());
		
		// Postcondition: c2 is not renamed to "Garfield" (verified by never calling renameCat on any mock)
		verify(c2, never()).renameCat(anyString());
	}

	/**
	 * Test case for boolean renameCat(int id, String name).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call renameCat(2, "Garfield").
	 * Postconditions: Return value is true.
	 *                 c2 is renamed to "Garfield".
	 * </pre>
	 * 
	 * Hint: You may need to use behavior verification for this one. See
	 * sample_code/junit_example/LinkedListUnitTest.java in the course repository to
	 * see examples.
	 */
	@Test
	public void testRenameNumCat3() {
		// Preconditions: c1, c2, and c3 are added to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);
		
		// Execution step: Call renameCat(2, "Garfield")
		boolean result = r.renameCat(2, "Garfield");
		
		// Postcondition: Return value is true
		assertTrue("renameCat should return true when cat exists", result);
		
		// Postcondition: c2 is renamed to "Garfield"
		verify(c2).renameCat("Garfield");
	}

	/**
	 * Test case for boolean rentCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call rentCat(2).
	 * Postconditions: Return value is true.
	 *                 c2 is rented as a result of the execution steps.
	 *                 System output is "Old Deuteronomy has been rented." + newline
	 * </pre>
	 * 
	 * Hint: You may need to use behavior verification for this one. See
	 * sample_code/junit_example/LinkedListUnitTest.java in the course repository to
	 * see examples.
	 */
	@Test
	public void testRentCatNumCats3() {
		// Set up mock behavior - c2 is not currently rented
		when(c2.getRented()).thenReturn(false);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		
		// Preconditions: c1, c2, and c3 are added to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);
		
		// Execution step: Call rentCat(2)
		boolean result = r.rentCat(2);
		
		// Postcondition: Return value is true
		assertTrue("rentCat should return true when cat exists and is available", result);
		
		// Postcondition: c2 is rented as a result of the execution steps
		verify(c2).rentCat();
		
		// Postcondition: System output is "Old Deuteronomy has been rented." + newline
		assertEquals("Should print rental confirmation", "Old Deuteronomy has been rented." + newline, out.toString());
	}

	/**
	 * Test case for boolean rentCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 *                c2 is rented.
	 * Execution steps: Call rentCat(2).
	 * Postconditions: Return value is false.
	 *                 c2 is not rented as a result of the execution steps.
	 *                 System output is "Sorry, Old Deuteronomy is not here!" + newline
	 * </pre>
	 * 
	 * Hint: You may need to use behavior verification for this one. See
	 * sample_code/junit_example/LinkedListUnitTest.java in the course repository to
	 * see examples.
	 */
	@Test
	public void testRentCatFailureNumCats3() {
		// Set up mock behavior - c2 is already rented
		when(c2.getRented()).thenReturn(true);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		
		// Preconditions: c1, c2, and c3 are added to r using addCat(Cat c)
		// c2 is rented (simulated by mock behavior above)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);
		
		// Execution step: Call rentCat(2)
		boolean result = r.rentCat(2);
		
		// Postcondition: Return value is false
		assertFalse("rentCat should return false when cat is already rented", result);
		
		// Postcondition: c2 is not rented as a result of the execution steps (rentCat not called again)
		verify(c2, never()).rentCat();
		
		// Postcondition: System output is "Sorry, Old Deuteronomy is not here!" + newline
		assertEquals("Should print cat not available message", "Sorry, Old Deuteronomy is not here!" + newline, out.toString());
	}

	/**
	 * Test case for boolean returnCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 *                c2 is rented.
	 * Execution steps: Call returnCat(2).
	 * Postconditions: Return value is true.
	 *                 c2 is returned as a result of the execution steps.
	 *                 System output is "Welcome back, Old Deuteronomy!" + newline
	 * </pre>
	 * 
	 * Hint: You may need to use behavior verification for this one. See
	 * sample_code/junit_example/LinkedListUnitTest.java in the course repository to
	 * see examples.
	 */
	@Test
	public void testReturnCatNumCats3() {
		// Set up mock behavior - c2 is currently rented
		when(c2.getRented()).thenReturn(true);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		
		// Preconditions: c1, c2, and c3 are added to r using addCat(Cat c)
		// c2 is rented (simulated by mock behavior above)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);
		
		// Execution step: Call returnCat(2)
		boolean result = r.returnCat(2);
		
		// Postcondition: Return value is true
		assertTrue("returnCat should return true when cat exists and is rented", result);
		
		// Postcondition: c2 is returned as a result of the execution steps
		verify(c2).returnCat();
		
		// Postcondition: System output is "Welcome back, Old Deuteronomy!" + newline
		assertEquals("Should print return confirmation", "Welcome back, Old Deuteronomy!" + newline, out.toString());
	}

	/**
	 * Test case for boolean returnCat(int id).
	 * 
	 * <pre>
	 * Preconditions: c1, c2, and c3 are added to r using addCat(Cat c).
	 * Execution steps: Call returnCat(2).
	 * Postconditions: Return value is false.
	 *                 c2 is not returned as a result of the execution steps.
	 *                 System output is "Old Deuteronomy is already here!" + newline
	 * </pre>
	 * 
	 * Hint: You may need to use behavior verification for this one. See
	 * sample_code/junit_example/LinkedListUnitTest.java in the course repository to
	 * see examples.
	 */
	@Test
	public void testReturnFailureCatNumCats3() {
		// Set up mock behavior - c2 is not currently rented
		when(c2.getRented()).thenReturn(false);
		when(c2.getName()).thenReturn("Old Deuteronomy");
		
		// Preconditions: c1, c2, and c3 are added to r using addCat(Cat c)
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);
		
		// Execution step: Call returnCat(2)
		boolean result = r.returnCat(2);
		
		// Postcondition: Return value is false
		assertFalse("returnCat should return false when cat is not rented", result);
		
		// Postcondition: c2 is not returned as a result of the execution steps (returnCat not called)
		verify(c2, never()).returnCat();
		
		// Postcondition: System output is "Old Deuteronomy is already here!" + newline
		assertEquals("Should print cat already here message", "Old Deuteronomy is already here!" + newline, out.toString());
	}

}