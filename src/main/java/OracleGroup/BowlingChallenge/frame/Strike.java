package OracleGroup.BowlingChallenge.frame;

public class Strike extends Frame {

    public Strike(final int play) {
        super(play);
    }

    @Override
    public boolean isStrike() {
        return true;
    }

}
