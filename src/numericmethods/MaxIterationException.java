package numericmethods;


// Custom iteration exception done to give the user more information of why the program would stop working
// This signals that the Ridders or Newton method iterations reached their maximum number of iterations
public class MaxIterationException extends Exception {

	public MaxIterationException() {
		super();
	}

	public MaxIterationException(String message) {
		super(message);
	}

}
