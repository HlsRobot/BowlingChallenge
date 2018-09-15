package OracleGroup.BowlingChallenge;

public class Plays {
	
	private int firstPlay;
	private int secondPlay;
	private ScoreType type;
	
	public Plays() {}
	
	public Plays(final int firstPlay, final int secondPlay) {
		this.firstPlay = firstPlay;
		this.secondPlay = secondPlay;
	}
	
	public int getFirstPlay() {
		return firstPlay;
	}
	public void setFirstPlay(int firstPlay) {
		this.firstPlay = firstPlay;
		if (firstPlay == 10) {
			this.type = ScoreType.STRIKE;
		}
	}
	public int getSecondPlay() {
		return secondPlay;
	}
	
	public void setSecondPlay(int secondPlay) {
		this.secondPlay = secondPlay;
		if (this.firstPlay <10) {
			if (this.firstPlay + this.secondPlay == 10) {
				this.type = ScoreType.SPARE;
			} else {
				this.type = ScoreType.NORMAL;
			}
		}
	}

	public ScoreType getType() {
		return type;
	}
}
