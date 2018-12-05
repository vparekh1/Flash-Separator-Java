package numericmethods;

// This interface will be used to provide anonymous functions to 
// the solve() method in the NewtonsMethod class to efficiently and
// easily compute the root of complex equations.
public interface NewtonMethodEquation {

	public double function(double x);
	public double derivative(double x);
	public String name();
}
