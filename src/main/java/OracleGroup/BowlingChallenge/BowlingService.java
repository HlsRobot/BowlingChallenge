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
     * @throws IOException because it calls getPlaysOfRound and getExtraPlays
     * @throws UserQuitException because it calls getPlaysOfRound and getExtraPlays
	 */
    void startBowling() throws IOException, UserQuitException {
        System.out.println("Welcome to the Bowling game.");
        System.out.println("****************************");
        System.out.println("Write 'exit' at any time to quit playing");
        System.out.println("****************************");
        System.out.println("Game start!");
        List<Frame> frameList = this.initiateBowlingRounds();

        for (int i = 0; i < frameList.size(); i++) {
            if (i < frameList.size() - 1) {
        		System.out.println("Round: " + (i+1));
                frameList.set(i, this.getPlaysOfRound(this.bufferedReader));
        	} else {
                if (frameList.get(i - 1).getFirstPlay() == 10 || frameList.get(i - 1).getFirstPlay() + frameList.get(i - 1).getSecondPlay() == 10) {
        			System.out.println("Extra round!");
                    frameList.set(i, this.getExtraPlays(this.bufferedReader, frameList.get(i - 1)));
        		}
        	}
            this.calculateScore(frameList, i);
            this.printScores(frameList);
        }
        System.out.println();
        // rounds.size() -1 for the additional play and -1 to avoid IndexOutOfBoundsException
        System.out.println("Final score: " + this.calculateResult(frameList, frameList.size() - 2));
        System.out.println("*** Game over ***");
	}
	
	/**
	 * Initialization of the rounds since we already know that they are 10
	 * with the possibility of one more extra round
     * @return List<Frame>
	 */
    private List<Frame> initiateBowlingRounds() {
        List<Frame> frameList = new ArrayList<Frame>();
		
		for(int i=0; i<11; i++) {
            frameList.add(new Frame());
        }
        return frameList;
	}
	
	/**
	 * Method that gathers and returns the plays of each round
	 * @param br BufferedReader
     * @return Frame
     * @throws IOException because it calls handleInput
     * @throws UserQuitException because it calls handleInput
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
     * @throws IOException because it calls handleInput
     * @throws UserQuitException because it calls handleInput
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
     * @throws IOException in case of IO error
     * @throws UserQuitException in case the user writes exit
	 */
	private int handleInput(final BufferedReader br, final int maxPins) throws IOException, UserQuitException {
        int pinsHit = 0;
        boolean correctValue = false;
        while (!correctValue) {

            final String input = br.readLine();
            if (input.equals("exit")) {
                System.out.println("Game Over!");
                throw new UserQuitException();
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
     * @param frameList List<Frame>
	 * @param index integer
	 */
    private void calculateScore(List<Frame> frameList, int index) {
		if (index > 0) {
            if (frameList.get(index - 1).isSpare()) {
                this.spareOrSecondStrikeBonus(frameList.get(index - 1), frameList.get(index));
            } else if (frameList.get(index - 1).isStrike()) {
				if (index-2 > -1) {
                    if (frameList.get(index - 2).isStrike()) {
                        this.spareOrSecondStrikeBonus(frameList.get(index - 2), frameList.get(index));
                        frameList.get(index - 1).setNormalScore();
					}
				}
                this.strikeBonus(frameList.get(index - 1), frameList.get(index));
			}
        }
        frameList.get(index).setNormalScore();
    }

    /**
     * Method used to calculate the bonus of a spare or the bonus of a strike that was followed by a second strike
     *
     * @param spareFrame   Frame
     * @param currentFrame Frame
     */
    private void spareOrSecondStrikeBonus(final Frame spareFrame, final Frame currentFrame) {
        spareFrame.setScore(spareFrame.getScore() + currentFrame.getFirstPlay());
    }

    /**
     * Method used to calculate the bonus of a strike
     *
     * @param strikeFrame  Frame
     * @param currentFrame Frame
     */
    private void strikeBonus(final Frame strikeFrame, final Frame currentFrame) {
        strikeFrame.setScore(strikeFrame.getScore() + currentFrame.getFirstPlay() + currentFrame.getSecondPlay());
    }

    /**
     * Calculate the result of each of the objects in the list
     *
     * @param frameList List<Frame>
     * @param index     int
     * @return int
     */
    private int calculateResult(List<Frame> frameList, int index) {
        int score = 0;
        for (int i = 0; i <= index; i++) {
            score += frameList.get(i).getScore();
        }
        return score;
    }
	
	// Used only for generating the view of the bowling inputs and outputs
    private void printScores(List<Frame> frameList) {
        for (int i = 0; i < frameList.size(); i++) {
            if (i < frameList.size() - 1) {
                if (frameList.get(i).isSpare()) {
                    System.out.print("| " + frameList.get(i).getFirstPlay() + " |.:|");
                } else if (frameList.get(i).isStrike()) {
					System.out.print("|   ||||");
                } else {
                    System.out.print("| " + frameList.get(i).getFirstPlay() + " | " + frameList.get(i).getSecondPlay() + " ");
				}
            } else {
                if (frameList.get(i - 1).isSpare()) {
                    System.out.print("| " + frameList.get(i).getFirstPlay() + " |");
                } else if (frameList.get(i - 1).isStrike()) {
                    System.out.print("| " + frameList.get(i).getFirstPlay() + " | " + frameList.get(i).getSecondPlay() + " |");
				} else {
					System.out.print("|");
				}
				
			}
		}
		System.out.println();
        for (int i = 0; i < frameList.size(); i++) {
            if (i < frameList.size()-1) {
                String result = "|    ";
                if (frameList.get(i).getScore() != 0) {
                    result = "|   " + calculateResult(frameList, i);
                }
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