package OracleGroup.BowlingChallenge;

public class RoundClass {

	private Plays plays;
	private int score;
	
	public RoundClass() {
		this.plays = new Plays(0, 0);
		this.score = 0;
	}

	public Plays getPlays() {
		return plays;
	}

	public void setPlays(Plays plays) {
		this.plays = plays;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
		
}
