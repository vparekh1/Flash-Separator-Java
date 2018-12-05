package tests;

import numericmethods.CubicEquation;
import numericmethods.CubicSolve;

/*This is to test the CubicEquation class
 * There are a total of three tests that must be done. The first must check if the single real 
 * root value works, the second if the 3 distinct roots works and finally if having a duplicate root
 * works. 
 */

public class CubicTest {
	public static void main(String[] arg) {

		// Check that single value real function works
		CubicEquation fun = new CubicEquation() {
			public double getA() {
				return 0.;
			}

			public double getB() {
				return 0;
			}

			public double getC() {
				return -8.;
			}
		};

		CubicSolve cubeclient = new CubicSolve();
		System.out.println(cubeclient.solve(fun)[1] == 2.);

		// Check that triple value real function works. The roots
		// should be 1,2, and 3
		CubicEquation fun2 = new CubicEquation() {
			public double getA() {
				return -6.;
			}

			public double getB() {
				return 11.;
			}

			public double getC() {
				return -6.;
			}
		};

		System.out.println(cubeclient.solve(fun2)[0] == 1.
				&& cubeclient.solve(fun2)[1] == 3.);

		// Check that double value real function works. The roots
		// should be 1 (double root), and 2
		CubicEquation fun3 = new CubicEquation() {
			public double getA() {
				return -0.9192151517498098;
			}

			public double getB() {
				return 0.5174604755042446;
			}

			public double getC() {
				return -0.04938359250925987;
			}
		};

		System.out.println(cubeclient.solve(fun3)[0] + ","
				+ cubeclient.solve(fun3)[1]);
	}
}
