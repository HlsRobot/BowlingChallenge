package OracleGroup.BowlingChallenge;

public class UserQuitException extends Exception {
	private static final long serialVersionUID = 9059166691741080934L;

	UserQuitException() {
	}

	@Override
	public String getMessage() {
		return "User decided to quit before finishing the game. Looser!";
	}
}
