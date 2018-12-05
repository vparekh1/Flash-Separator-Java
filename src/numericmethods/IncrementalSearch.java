package numericmethods;

//Incremental Search Class done to find the bounds in which other numerical methods should use. 
public class IncrementalSearch {
	
	//Instance Variable
	private int maxIterations;
	
	//Constructor 
	public IncrementalSearch(int maxIterations) {
		super();
		this.maxIterations = maxIterations;
	}
	
	// Copy Constructor
	public IncrementalSearch(IncrementalSearch ori) {
		this.maxIterations = ori.maxIterations;
	}
	
	// Clone method
	public IncrementalSearch clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new IncrementalSearch(this);
	}

	//Accessor 
	public double getMaxIterations() {
		return maxIterations;
	}

	//Mutator
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	//Method to find the bounds of the equation
	public double[] findBounds(RiddersMethodEquation f, double start, double step) throws MaxIterationException {
		double[] result = new double[2];
		double lowerBound = start;
		double upperBound = start+step;
		double fl = f.function(lowerBound);
		double fu = f.function(upperBound);
		
		for(int i=0; i < this.maxIterations; i++) {
			if (Math.signum(fl) != Math.signum(fu)) {
				result[0] = lowerBound;
				result[1] = upperBound;
				return result;
			}
			lowerBound = upperBound;
			fl = fu;
			
			upperBound += step;
			fu = f.function(upperBound);
		}

		throw new MaxIterationException("Max iterations of incremental search exceeded in function: "+f.name());
	}

}
