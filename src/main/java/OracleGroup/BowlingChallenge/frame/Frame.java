package OracleGroup.BowlingChallenge.frame;

public class Frame {

    private int firstPlay;
    private int secondPlay;
    private int score = 0;

    public Frame() {
        this(0);
    }

    protected Frame(final int firstPlay) {
        this(firstPlay, 0);
    }

    public Frame(final int firstPlay, final int secondPlay) {
        this.firstPlay = firstPlay;
        this.secondPlay = secondPlay;
    }

    public int getFirstPlay() {
        return firstPlay;
    }

    public void setFirstPlay(int firstPlay) {
        this.firstPlay = firstPlay;
    }

    public int getSecondPlay() {
        return secondPlay;
    }

    public void setSecondPlay(int secondPlay) {
        this.secondPlay = secondPlay;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isStrike() {
        return this.firstPlay == 10;
    }

    public boolean isSpare() {
        return this.firstPlay + this.secondPlay == 10 && !this.isStrike();
    }

}
