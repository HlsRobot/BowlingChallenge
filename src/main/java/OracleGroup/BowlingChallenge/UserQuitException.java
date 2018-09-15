package OracleGroup.BowlingChallenge;

public class UserQuitException extends Exception {
	private static final long serialVersionUID = 9059166691741080934L;
	
	private String message;
	
	public UserQuitException(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
