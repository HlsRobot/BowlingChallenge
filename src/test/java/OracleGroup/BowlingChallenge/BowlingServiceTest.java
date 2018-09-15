package OracleGroup.BowlingChallenge;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BowlingServiceTest {
	
	private BufferedReader bufferedReader;

	private BowlingService bowlingService;
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		System.setOut(new PrintStream(this.outContent));
	    System.setErr(new PrintStream(this.errContent));
		this.bufferedReader = Mockito.mock(BufferedReader.class);
		this.bowlingService = new BowlingService(this.bufferedReader);
	}

	@After
	public void restoreStreams() {
	    System.setOut(this.originalOut);
	    System.setErr(this.originalErr);
	}
	
	@Test
	public void testErrorMessagesForInvalidFormat() throws IOException {
		Mockito.when(this.bufferedReader.readLine()).thenReturn("abc", "exit");
		try {
			this.bowlingService.startBowling();
		} catch (Exception e) {
			assertTrue(this.errContent.toString().contains("Invalid Format!"));
		}
	}
	
	@Test
	public void testErrorMessagesForMoreThanTenPins() throws IOException {
		Mockito.when(this.bufferedReader.readLine()).thenReturn("5", "6", "exit");
		try {
			this.bowlingService.startBowling();
		} catch (Exception e) {
			assertTrue(this.errContent.toString().contains("Please provide a value between (including) 0 and 5"));
		}
	}

	@Test
	public void testAllStrikes() throws IOException, UserQuitException {
		Mockito.when(this.bufferedReader.readLine()).thenReturn("10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10");
		this.bowlingService.startBowling();
		assertTrue(this.outContent.toString().contains("Final score: 300"));
		assertEquals("", this.errContent.toString());
	}
	
	@Test
	public void testDescriptionExample() throws IOException, UserQuitException {
		Mockito.when(this.bufferedReader.readLine()).thenReturn("1", "4", "4", "5", "6", "4", "5", "5", "10", "1", "0", "7", "3", "6", "4", "10", "2", "8", "6");
		this.bowlingService.startBowling();
		assertTrue(this.outContent.toString().contains("Final score: 133"));
		assertEquals("", this.errContent.toString());
	}
	
	@Test
	public void randomTest1() throws IOException, UserQuitException {
		Mockito.when(this.bufferedReader.readLine()).thenReturn("10", "0", "10", "10", "5", "0", "8", "2", "9", "0", "10", "8", "1", "1", "0", "4", "6", "10");
		this.bowlingService.startBowling();
		assertTrue(this.outContent.toString().contains("Final score: 137"));
		assertEquals("", this.errContent.toString());
	}
	
	@Test
	public void randomTest2() throws IOException, UserQuitException {
		Mockito.when(this.bufferedReader.readLine()).thenReturn("6", "2", "7", "1", "10", "9", "0", "8", "2", "10", "10", "3", "5", "7", "2", "5", "5", "8");
		this.bowlingService.startBowling();
		assertTrue(this.outContent.toString().contains("Final score: 140"));
		assertEquals("", this.errContent.toString());
	}
	
//	Additional tests can be created as above to tests custom test suits.
}
