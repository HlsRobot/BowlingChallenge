package OracleGroup.BowlingChallenge;

import OracleGroup.BowlingChallenge.frame.Frame;
import OracleGroup.BowlingChallenge.frame.Spare;
import OracleGroup.BowlingChallenge.frame.Strike;

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
    void startBowling() throws IOException, UserQuitException {
        System.out.println("Welcome to the Bowling game.");
        System.out.println("****************************");
        System.out.println("Write 'exit' at any time to quit playing");
        System.out.println("****************************");
        System.out.println("Game start!");
        List<RoundClass> rounds = this.initiateBowlingRounds();
        
        for (int i=0; i<rounds.size(); i++) {
        	if (i<rounds.size()-1) {
        		System.out.println("Round: " + (i+1));
                rounds.get(i).setFrame(this.getPlaysOfRound(this.bufferedReader));
        	} else {
                if (rounds.get(i - 1).getFrame().getFirstPlay() == 10 || rounds.get(i - 1).getFrame().getFirstPlay() + rounds.get(i - 1).getFrame().getSecondPlay() == 10) {
        			System.out.println("Extra round!");
                    rounds.get(i).setFrame(this.getExtraPlays(this.bufferedReader, rounds.get(i - 1).getFrame()));
        		}
        	}
        	this.calculateScore(rounds, i);
        	this.printScores(rounds);
        }
        System.out.println();
        // rounds.size() -1 for the additional play and -1 to avoid IndexOutOfBoundsException
        System.out.println("Final score: " + this.calculateResult(rounds, rounds.size() - 2));
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
     * @return Frame
	 * @throws IOException
	 * @throws UserQuitException 
	 */
    private Frame getPlaysOfRound(final BufferedReader br) throws IOException, UserQuitException {
        System.out.println("Please throw your first ball!");
        final int firstPlay = (this.handleInput(br, 10));
        if (firstPlay < 10) {
            System.out.println("Please throw your second ball!");
            final int secondPlay = this.handleInput(br, 10 - firstPlay);
            if (firstPlay + secondPlay == 10) {
                return new Spare(firstPlay, secondPlay);
            }
            return new Frame(firstPlay, secondPlay);
		} else {
			System.out.println("Strike!!!");
            return new Strike(firstPlay);
        }
	}
	
	/**
	 * Method for the extra round in case the player scored a spare or a strike at the last round
	 * @param br BufferedReader
     * @return Frame
	 * @throws IOException
	 * @throws UserQuitException 
	 */
    private Frame getExtraPlays(final BufferedReader br, final Frame frame) throws IOException, UserQuitException {
        final Frame extraFrame = new Frame(0, 0);
        if (frame.isStrike()) {
            System.out.println("Please throw your first extra ball!");
            extraFrame.setFirstPlay(this.handleInput(br, 10));
				System.out.println("Please throw your second extra ball!");
            extraFrame.setSecondPlay(this.handleInput(br, 10));
            return extraFrame;
        } else if (frame.isSpare()) {
            System.out.println("Please throw your extra ball!");
            extraFrame.setFirstPlay(this.handleInput(br, 10));
            return extraFrame;
        }
        return extraFrame;
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
        int pinsHit = 0;
        boolean correctValue = false;
        while (!correctValue) {

            final String input = br.readLine();
            if (input.equals("exit")) {
                System.out.println("Game Over!");
                throw new UserQuitException("User decided to quit before finishing the game. Looser!");
            }
            try {
                pinsHit = Integer.parseInt(input);
                if (pinsHit < 0 || pinsHit > maxPins) {
                    System.err.println("Please provide a value between (including) 0 and " + maxPins);
                } else {
                    correctValue = true;
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid Format!");
            }

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
    private void calculateScore(List<RoundClass> rounds, int index) {
		if (index > 0) {
            if (rounds.get(index - 1).getFrame().isSpare()) {
                rounds.get(index - 1).setScore(rounds.get(index - 1).getScore() + rounds.get(index).getFrame().getFirstPlay());
            } else if (rounds.get(index - 1).getFrame().isStrike()) {
				if (index-2 > -1) {
                    if (rounds.get(index - 2).getFrame().isStrike()) {
                        rounds.get(index - 2).setScore(rounds.get(index - 2).getScore() + rounds.get(index).getFrame().getFirstPlay());
                        rounds.get(index - 1).setScore(rounds.get(index - 1).getFrame().getFirstPlay() + rounds.get(index - 1).getFrame().getSecondPlay());
					}
				}
                rounds.get(index - 1).setScore(rounds.get(index - 1).getScore() + rounds.get(index).getFrame().getFirstPlay() + rounds.get(index).getFrame().getSecondPlay());
			}
        }
        rounds.get(index).setScore(rounds.get(index).getFrame().getFirstPlay() + rounds.get(index).getFrame().getSecondPlay());
    }

    private int calculateResult(List<RoundClass> rounds, int index) {
        int score = 0;
        for (int i = 0; i <= index; i++) {
            score += rounds.get(i).getScore();
        }
        return score;
    }
	
	// Used only for generating the view of the bowling inputs and outputs
    private void printScores(List<RoundClass> rounds) {
		for (int i=0; i<rounds.size(); i++) {
			if (i< rounds.size()-1) {
                if (rounds.get(i).getFrame().isSpare()) {
                    System.out.print("| " + rounds.get(i).getFrame().getFirstPlay() + " |.:|");
                } else if (rounds.get(i).getFrame().isStrike()) {
					System.out.print("|   ||||");
				} else {
                    System.out.print("| " + rounds.get(i).getFrame().getFirstPlay() + " | " + rounds.get(i).getFrame().getSecondPlay() + " ");
				}
			} else {
                if (rounds.get(i - 1).getFrame().isSpare()) {
                    System.out.print("| " + rounds.get(i).getFrame().getFirstPlay() + " |");
                } else if (rounds.get(i - 1).getFrame().isStrike()) {
                    System.out.print("| " + rounds.get(i).getFrame().getFirstPlay() + " | " + rounds.get(i).getFrame().getSecondPlay() + " |");
				} else {
					System.out.print("|");
				}
				
			}
		}
		System.out.println();
		for (int i=0; i<rounds.size(); i++) {
			if (i< rounds.size()-1) {
                String result = "|    ";
                result = "|   " + calculateResult(rounds, i);
//                if (rounds.get(i).getFrame().getType() != null) {
//                }
				System.out.print(result);
				for(int j=result.length()-1; j<7; j++) {
					System.out.print(" ");
				}
			} else {
				System.out.print("|");
			}
		}
		System.out.println();
        System.out.println();
        System.out.println();
	}
}