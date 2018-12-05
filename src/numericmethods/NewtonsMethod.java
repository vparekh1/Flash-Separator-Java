package numericmethods;

//Class to use if Newton's Method is the desired numerical method to solve equation.

public class NewtonsMethod {
	
	// Instance Variables
	private int maxIterations;
	private double error;

	// Constructors
	public NewtonsMethod(int maxIterations, double error) {
		this.maxIterations = maxIterations;
		this.error = error;
	}
	
	// Copy Constructor
	public NewtonsMethod(NewtonsMethod ori) {
		this.maxIterations = ori.maxIterations;
		this.error = ori.error;
	}
	
	// Clone method
	public NewtonsMethod clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new NewtonsMethod(this);
	}

	// set the default number of maximum number of iterations
	// to 1000, and the acceptable error to 1e-10
	public NewtonsMethod() {
		this.maxIterations = 1000;
		this.error = 1e-10;
	}

	//Accessors and Mutators
	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	//Solving using Newtons Method
	public double solve(NewtonMethodEquation function, double guess) throws MaxIterationException {
		double tmp = guess;

		// The updating function for Newton's method is x_(n+1) = x_(n) - f(x_(n))/f'(x_(n))
		for (int i = 0; i < maxIterations; i++) {
			tmp = tmp - function.function(tmp) / function.derivative(tmp);

			if (Math.abs(function.function(tmp)) < error) {
				break;
			}
		}

		if (Math.abs(function.function(tmp)) > error) {
			throw new MaxIterationException("Newton Method not converged on value within error for function: "+function.name());
		}

		return tmp;
	}
}
