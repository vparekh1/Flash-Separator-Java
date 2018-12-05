package numericmethods;

//Ridder's Numerical Method Class 

public class RiddersMethod {

	//Instance Variables
	private double maxIterations;
	private double error;

	//Constructors
	public RiddersMethod(double maxIterations, double error) {
		super();
		this.maxIterations = maxIterations;
		this.error = error;
	}
	
	// Copy Constructor
	public RiddersMethod(RiddersMethod ori) {
		this.maxIterations = ori.maxIterations;
		this.error = ori.error;
	}
	
	// Clone method
	public RiddersMethod clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new RiddersMethod(this);
	}
	
	public RiddersMethod() {
		super();
		this.maxIterations = 1000;
		this.error = 1e-10;
	}

	
	//Accessors and Mutators 
	public double getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(double maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	} 

	//Solving using Ridders method
	public double solve(RiddersMethodEquation func, double lowerBound,
			double upperBound) throws MaxIterationException, RootNotBracketedException, SquareRootZeroException {

		double a, b, c, fa, fb, fc, xold;

		a = lowerBound;
		b = upperBound;
		fa = func.function(a);
		fb = func.function(b);
		xold = 1e300;

		if (fa == 0) {
			return a;
		}
		if (fb == 0) {
			return b;
		}

		if (fa * fb >= 0) {
			throw new RootNotBracketedException("This root is initially not bracketed: "+func.name()+". "+a + "," + b + " -> "+fa + "," + fb + "\n");
		}

		for (int i = 0; i < this.maxIterations; i++) {

			if (fa * fb >= 0) {
				throw new RootNotBracketedException("This root is initially not bracketed: "+func.name()+". "+a + "," + b + " -> "+fa + "," + fb + "\n");
			}

			c = 0.5 * (a + b);
			fc = func.function(c);

			double s = Math.sqrt(fc * fc - fa * fb);
			if (s == 0) {
				throw new SquareRootZeroException("The Ridders square root was calculated to be 0.0 in function: "+func.name());
			}

			double dx = (c - a) * fc / s;
			if ((fa - fb) < 0.) {
				dx = -dx;
			}
			double x = c + dx;
			double fx = func.function(x);

			// if the absolute value of the function evaluated at x is less than
			// 1e-100, the floating
			// point calculations begin to break down.
			if (Math.abs(x - xold) < error * Math.max(Math.abs(x), 1.)
					|| Math.abs(fx) < 1e-100) {
				return x;
			}


			xold = x;
			if (fc * fx > 0.) {
				if (fa * fx < 0.) {
					b = x;
					fb = fx;
				} else {
					a = x;
					fa = fx;
				}
			} else {
				a = c;
				b = x;
				fa = fc;
				fb = fx;
			}
			

		}

		// If the number of iterations exceed maxIterations
		throw new MaxIterationException("Ridders iteration limit exceeded in function: "+func.name());

	}

}
