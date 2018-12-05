package numericmethods;

//Custom exception done to give the user more information of why the program would stop working
//In this case two instance variables were created to tell the user what the upper and lower bounds of the
//values should be. 
public class ValuesOutOfBoundsException extends Exception {

	private double lowerBound;
	private double upperBound;
	
	public ValuesOutOfBoundsException() {
		super();
	}

	public ValuesOutOfBoundsException(String message, double lowerBound, double upperBound) {
		super(message);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

}
