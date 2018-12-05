package tests;

import numericmethods.RiddersMethod;
import numericmethods.RiddersMethodEquation;

//Testing the ridders numerical test by creating a quadratic function it must solve for
public class RiddersTest {

	public static void main(String[] args) {
		RiddersMethod riddClient = new RiddersMethod();

		// Defining new ridders method object
		RiddersMethodEquation quad = new RiddersMethodEquation() {
			// Generic function
			public double function(double x) {
				return (x * x - 2);
			}

			public String name() {
				return "Quadratic function";
			}
		};

		// Applying ridders method to find the root of the generic function
		try {
			System.out.println(riddClient.solve(quad, 0, 10));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
