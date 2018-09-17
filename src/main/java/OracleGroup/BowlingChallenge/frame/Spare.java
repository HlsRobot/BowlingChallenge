package OracleGroup.BowlingChallenge.frame;

public class Spare extends Frame {

    public Spare(final int firstPlay, final int secondPlay) {
        super(firstPlay, secondPlay);
    }

    @Override
    public boolean isSpare() {
        return true;
    }

}
