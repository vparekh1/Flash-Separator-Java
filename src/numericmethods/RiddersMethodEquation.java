package numericmethods;

//This interface will be used to provide anonymous functions to 
//the solve() method in the RiddersMethod class to efficiently and
//easily compute the root of complex equations.
public interface RiddersMethodEquation {

	public double function(double x);
	public String name();
}
