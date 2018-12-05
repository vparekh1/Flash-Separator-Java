package numericmethods;

//This interface will be used to provide anonymous functions to 
//the solve() method in the CubicSolve class to efficiently and
//easily compute the root of cubic equations.
public interface CubicEquation {

	// x^2 + a*x^2 + b*x + c = 0
	public double getA();
	public double getB();
	public double getC();
	
}