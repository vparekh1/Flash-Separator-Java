package tests;

import numericmethods.MaxIterationException;
import numericmethods.NewtonMethodEquation;
import numericmethods.NewtonsMethod;

//Testing class of the Newton test. 
public class NewtonTest {
	public static void main(String[] arg) throws MaxIterationException {
		NewtonMethodEquation fun = new NewtonMethodEquation() {
			// Generic function
			public double function(double x) {
				return (x * x - 2);
			}

			// Derivative of generic function
			public double derivative(double x) {
				return 2 * x;
			}

			public String name() {
				return "square";
			}
		};

		// Creating a new NewstonsMethod object
		NewtonsMethod newtclient = new NewtonsMethod();

		// Using NewtownsMethod to find the root of the generic function
		System.out.println(newtclient.solve(fun, 10));
	}
}
