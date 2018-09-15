package OracleGroup.BowlingChallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class BowlingRunner {
	
	public static void main(String[] args) throws IOException, UserQuitException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		final BowlingService bowlingService = new BowlingService(br);
		bowlingService.startBowling();
	}
	
}
