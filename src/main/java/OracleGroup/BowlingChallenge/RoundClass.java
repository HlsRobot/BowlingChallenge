package OracleGroup.BowlingChallenge;

import OracleGroup.BowlingChallenge.frame.Frame;

public class RoundClass {

	private Frame frame;
	private int score;
	
	public RoundClass() {
		this.frame = new Frame(0, 0);
		this.score = 0;
	}

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
		
}
