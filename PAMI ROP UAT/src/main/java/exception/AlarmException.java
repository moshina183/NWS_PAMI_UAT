package exception;

public class AlarmException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AlarmException(String message) {
		super();
		this.message = message;
	}

	
	public AlarmException() {
		super();
	}



	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
