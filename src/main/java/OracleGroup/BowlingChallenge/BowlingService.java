package OracleGroup.BowlingChallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BowlingService {
	
	private BufferedReader bufferedReader;
	
	public BowlingService(final BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}
	
	/**
	 * Public method called from the main to start the game
	 * Calls all the required methods for each work
	 * @throws IOException
	 * @throws UserQuitException 
	 */
	public void startBowling() throws IOException, UserQuitException {
        System.out.println("Welcome to the Bowling game.");
        System.out.println("****************************");
        System.out.println("Write 'exit' at any time to quit playing");
        System.out.println("****************************");
        System.out.println("Game start!");
        List<RoundClass> rounds = this.initiateBowlingRounds();
        
        for (int i=0; i<rounds.size(); i++) {
        	if (i<rounds.size()-1) {
        		System.out.println("Round: " + (i+1));
            	rounds.get(i).setPlays(this.getPlaysOfRound(this.bufferedReader));
        	} else {
        		if (rounds.get(i-1).getPlays().getFirstPlay() == 10 || rounds.get(i-1).getPlays().getFirstPlay() + rounds.get(i-1).getPlays().getSecondPlay() == 10) {
        			System.out.println("Extra round!");
        			rounds.get(i).setPlays(this.getExtraPlays(this.bufferedReader, rounds.get(i-1).getPlays().getType()));
        		}
        	}
        	this.calculateScore(rounds, i);
        	this.printScores(rounds);
        }
        System.out.println();
        System.out.println("Final score: " + rounds.get(9).getScore());
        System.out.println("*** Game over ***");
	}
	
	/**
	 * Initialization of the rounds since we already know that they are 10
	 * with the possibility of one more extra round
	 * @return List<RoundClass>
	 */
	private List<RoundClass> initiateBowlingRounds() {
		List<RoundClass> rounds = new ArrayList<RoundClass>();
		
		for(int i=0; i<11; i++) {
        	rounds.add(new RoundClass());
		}
		return rounds;
	}
	
	/**
	 * Method that gathers and returns the plays of each round
	 * @param br BufferedReader
	 * @return Plays
	 * @throws IOException
	 * @throws UserQuitException 
	 */
	private Plays getPlaysOfRound(final BufferedReader br) throws IOException, UserQuitException {
		final Plays plays = new Plays(-1, -1);
		
		while (plays.getFirstPlay() == -1) {
			System.out.println("Please throw your first ball!");
			plays.setFirstPlay(this.handleInput(br, 10));
		}
		if (plays.getFirstPlay() < 10) {
			while (plays.getSecondPlay() == -1) {
				System.out.println("Please throw your second ball!");
				plays.setSecondPlay(this.handleInput(br, 10 - plays.getFirstPlay()));
			}
		} else {
			System.out.println("Strike!!!");
			plays.setSecondPlay(0);
		}
		return plays;
	}
	
	/**
	 * Method for the extra round in case the player scored a spare or a strike at the last round
	 * @param br BufferedReader
	 * @param type ScoreType
	 * @return Plays
	 * @throws IOException
	 * @throws UserQuitException 
	 */
	private Plays getExtraPlays(final BufferedReader br, final ScoreType type) throws IOException, UserQuitException {
		if (type == ScoreType.STRIKE) {
			final Plays plays = new Plays(-1, -1);
			while (plays.getFirstPlay() == -1) {
				System.out.println("Please throw your first extra ball!");
				plays.setFirstPlay(this.handleInput(br, 10));
			}
			while (plays.getSecondPlay() == -1) {
				System.out.println("Please throw your second extra ball!");
				plays.setSecondPlay(this.handleInput(br, 10));
			}
			return plays;
		} else if (type == ScoreType.SPARE) {
			final Plays plays = new Plays(-1, 0);
			while (plays.getFirstPlay() == -1) {
				System.out.println("Please throw your extra ball!");
				plays.setFirstPlay(this.handleInput(br, 10));
			}
			return plays;
		} else {
			return new Plays(0, 0);
		}
	}
	
	/**
	 * Method that handles the user input from the console
	 * @param br BufferedReader
	 * @param maxPins integer
	 * @return integer
	 * @throws IOException
	 * @throws UserQuitException 
	 */
	private int handleInput(final BufferedReader br, final int maxPins) throws IOException, UserQuitException {
		int pinsHit;
		
		final String input = br.readLine();
		if (input.equals("exit")) {
			System.out.println("Game Over!");
			throw new UserQuitException("User decided to quit before finishing the game. Looser!");
		}
		try {
			pinsHit = Integer.parseInt(input);
			if (pinsHit < 0 || pinsHit > maxPins) {
				pinsHit = -1;
				System.err.println("Please provide a value between (including) 0 and " + maxPins);
			}
        } catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
            pinsHit = -1;
        }
		return pinsHit;
	}
	
	/**
	 * Method that calculates the score of each round, 
     * the round before in the case of strike or spare 
     * and the round before that in case of two strikes in a row.
	 * @param rounds List<RoundClass>
	 * @param index integer
	 * @return integer
	 */
	private int calculateScore(List<RoundClass> rounds, int index) {
		int finalScore;
		if (index > 0) {
			if (rounds.get(index-1).getPlays().getType() == ScoreType.SPARE) {
				rounds.get(index-1).setScore(rounds.get(index-1).getScore() + rounds.get(index).getPlays().getFirstPlay());
			} else if (rounds.get(index-1).getPlays().getType() == ScoreType.STRIKE) {
				if (index-2 > -1) {
					if (rounds.get(index-2).getPlays().getType() == ScoreType.STRIKE) {
						rounds.get(index-2).setScore(rounds.get(index-2).getScore() + rounds.get(index).getPlays().getFirstPlay());
						rounds.get(index-1).setScore(rounds.get(index-2).getScore() + rounds.get(index-1).getPlays().getFirstPlay() + rounds.get(index-1).getPlays().getSecondPlay());
					}
				}
				rounds.get(index-1).setScore(rounds.get(index-1).getScore() + rounds.get(index).getPlays().getFirstPlay() + rounds.get(index).getPlays().getSecondPlay());
			}
			if (index < 10) {
				finalScore = rounds.get(index-1).getScore() + rounds.get(index).getPlays().getFirstPlay() + rounds.get(index).getPlays().getSecondPlay();
				rounds.get(index).setScore(finalScore);
			} else {
				finalScore = rounds.get(index-1).getScore();
			}
		} else {
			finalScore = rounds.get(index).getPlays().getFirstPlay() + rounds.get(index).getPlays().getSecondPlay();
			rounds.get(index).setScore(finalScore);
		}
		return finalScore;
	}
	
	// Used only for generating the view of the bowling inputs and outputs
	public void printScores(List<RoundClass> rounds) {
		System.out.println();
		for (int i=0; i<rounds.size(); i++) {
			if (i< rounds.size()-1) {
				if (rounds.get(i).getPlays().getType() == ScoreType.SPARE) {
					System.out.print("| " + rounds.get(i).getPlays().getFirstPlay() + " |.:|");
				} else if (rounds.get(i).getPlays().getType() == ScoreType.STRIKE) {
					System.out.print("|   ||||");
				} else {
					System.out.print("| " + rounds.get(i).getPlays().getFirstPlay() + " | " + rounds.get(i).getPlays().getSecondPlay() + " ");
				}
			} else {
				if (rounds.get(i-1).getPlays().getType() == ScoreType.SPARE) {
					System.out.print("| " + rounds.get(i).getPlays().getFirstPlay() + " |");
				} else if (rounds.get(i-1).getPlays().getType() == ScoreType.STRIKE) {
					System.out.print("| " + rounds.get(i).getPlays().getFirstPlay() + " | " + rounds.get(i).getPlays().getSecondPlay() + " |");
				} else {
					System.out.print("|");
				}
				
			}
		}
		System.out.println();
		for (int i=0; i<rounds.size(); i++) {
			if (i< rounds.size()-1) {
				final String result = "|   " + rounds.get(i).getScore();
				System.out.print(result);
				for(int j=result.length()-1; j<7; j++) {
					System.out.print(" ");
				}
			} else {
				System.out.print("|");
			}
		}
		System.out.println();
	}
}