package flashseparator;

import java.util.Arrays;

import eos.EquationOfState;
import numericmethods.MaxIterationException;
import numericmethods.RootNotBracketedException;

import numericmethods.IncrementalSearch;
import numericmethods.RiddersMethod;
import numericmethods.RiddersMethodEquation;
import numericmethods.SquareRootZeroException;
import numericmethods.ValuesOutOfBoundsException;

/* Flash calculations class
 * Includes dew point and bubble point calculations and equilibrium flash point 
 */
public class FlashCalc {
	
	// Rachford-Rice equation used to calculate equilibrium Flash dew point of the solution
	// Main Flash Evaporator Calculation
	public static double dewP(final EquationOfState eos, final double T) throws MaxIterationException, RootNotBracketedException, SquareRootZeroException {
		
		// For the dew point calculation, it is necessary to find a pressure at which sum(zi/Ki) = 1
		RiddersMethodEquation dewPEqn = new RiddersMethodEquation() {
			public double function(double P) {
				double sum = 0;
				double[] volatility = eos.volatility(T, P);
				for (int i=0; i<eos.getCompositions().length; i++) {
					sum += eos.getCompositions()[i]/volatility[i];
				}

				return sum - 1;
			}
			
			public String name() { 
				return "DewP";
			}
		};
		
		// Perform an incremental search to find bounds on the dew point
		IncrementalSearch incClient = new IncrementalSearch(1000);
		double[] bounds = incClient.findBounds(dewPEqn, 1e-5, 1e4);
		
		// Once a bound is found, the true value can be found using Ridder's Method
		RiddersMethod riddClient = new RiddersMethod();
		
		return riddClient.solve(dewPEqn, bounds[0], bounds[1]);
	}
	
	// Rachford-Rice equation used to calculate equilibrium Flash bubble point of the solution
	// Main Flash Evaporator Calculation
	public static double bublP(final EquationOfState eos, final double T) throws MaxIterationException, RootNotBracketedException, SquareRootZeroException {
		
		// For the dew point calculation, it is necessary to find a pressure at which sum(zi*Ki) = 1
		RiddersMethodEquation bublPEqn = new RiddersMethodEquation() {
			public double function(double P) {
				double sum = 0;
				double[] volatility = eos.volatility(T, P);
				for (int i=0; i<eos.getCompositions().length; i++) {
					sum += eos.getCompositions()[i]*volatility[i];
				}
				//System.out.println(P+","+(sum-1));
				return sum - 1;
			}
			
			public String name() {
				return "BublP";
			}
		};
		
		// A good estimate for the lower bound of the bubble point is the lowest vapor pressure
		// of all of the components in the mixture
		double lowerBound = Double.MAX_VALUE;
		for (int i=0; i < eos.getCompositions().length; i++) {
			double tmp = Double.MAX_VALUE;
			try {
				tmp = eos.satP(T, i);
			} catch (ValuesOutOfBoundsException e) {}
			
			if (eos.getCompositions()[i] > 1e-5 && tmp < lowerBound) {
				lowerBound = tmp;
			}
		}
		
		IncrementalSearch incClient = new IncrementalSearch(1000);
		double[] bounds = incClient.findBounds(bublPEqn, lowerBound, 1e5);
		//System.out.println(Arrays.toString(bounds));
		
		RiddersMethod riddClient = new RiddersMethod();

		double result = riddClient.solve(bublPEqn, bounds[0], bounds[1]);
		return result;
	}
	
	// Returns a two dimensional double array containing the compositions of the flashed mixture.
	// The first entry of the array, flashCalc[0][0] contains the fraction of the stream in the liquid phase
	// The subsequent values in the column of the array, flashCalc[i+1][0] contains the mol fractions of component i
	// in the the stream in the liquid phase of the flash separation.  Similarly, the values are given 
	// for the gas phase. 
	public static double[][] flashCalc(final EquationOfState eos, final double T, final double P) throws RootNotBracketedException, MaxIterationException, SquareRootZeroException {
		
		// Exception Handling
		final int n = eos.getSpecies().length;
		double beta = 0;
		double[][] postFlashComp = new double[n+1][2];
		final double[] compositions = Arrays.copyOf(eos.getCompositions(), n);
		final double[] volatility = Arrays.copyOf(eos.volatility(T, P), n);
		
		if (P < FlashCalc.dewP(eos, T)) {
			
			postFlashComp[0][0] = 1e-30;
			postFlashComp[0][1] = 1-1e-30;
			
			// Calculating composition of liquid and gas stream. The liquid
			// compositions
			// are in the first column, and the gas compositions are in the second
			
			// In the case of the system being in a state past the dew point, the liquid phase
			// will be assumed to be equal compositions of all components in the mixture, and
			// the gas phase will be assumed to be equivalent to the total composition of the
			// components in the mixture
			for (int i = 1; i < compositions.length+1; i++) {
				postFlashComp[i][0] = 1. / ((double) compositions.length);
				postFlashComp[i][1] = compositions[i-1];
			}
			return postFlashComp;
		}
		if (P > FlashCalc.bublP(eos, T)) {
			
			postFlashComp[0][0] = 1-1e-30;
			postFlashComp[0][1] = 1e-30;
			
			// Calculating composition of liquid and gas stream. The liquid
			// compositions
			// are in the first column, and the gas compositions are in the second
			
			// In the case of the system being in a state past the bubble point, the gas phase
			// will be assumed to be equal compositions of all components in the mixture, and
			// the liquid phase will be assumed to be equivalent to the total composition of the
			// components in the mixture
			for (int i = 1; i < compositions.length+1; i++) {
				postFlashComp[i][0] = compositions[i-1];
				postFlashComp[i][1] = 1. / ( (double) compositions.length);
			}
			return postFlashComp;
		}
		
		// If the pressure of the system is between the dew point and the bubble point,
		// Ridder's method will be used to solve for the overall vapor and liquid mol fraction
		// in the flash system.
		RiddersMethod eqnClient = new RiddersMethod();
		RiddersMethodEquation riceEqn = new RiddersMethodEquation() {

			public double function(double V) {
				double sum = 0;
				for (int i = 0; i < n; i++) {
					sum += compositions[i] * (volatility[i] - 1.)
							/ (1. + V * (volatility[i] - 1.));
				}
				return sum;
			}

			public String name() {
				return "Ratchford-Rice Equation";
			}
			
		};


		// Get K_min and K_max
		double kMax = -Double.MAX_VALUE;
		double kMin = Double.MAX_VALUE;

		for (double i : volatility) {
			if (i > kMax) {
				kMax = i;
			}
			if (i < kMin) {
				kMin = i;
			}
		}

		// Finding suitable bounds for the flash calculation iteration.  It is certain that the 
		// lower and upper bound of iteration will be determined by the values of the maximum and minimum
		// volatility of the species in solution
		beta = eqnClient.solve(riceEqn,
				Math.max(0.,Math.abs(1. / (1. - kMax))), Math.min(1., Math.abs(1. / (1. - kMin))));

		if (beta < 0 || beta > 1) {
			throw new RootNotBracketedException("The beta values calculated are not between 0 and 1.  It was calculated to be "
							+ beta);
		}
		
		// The first entry of the returned array is going to be the total liquid fraction
		// in the first column, and the total vapor fraction in the second column
		postFlashComp[0][0] = (1-beta);
		postFlashComp[0][1] = beta;
		
		// Calculating composition of liquid and gas stream. The liquid
		// compositions
		// are in the first column, and the gas compositions are in the second
		for (int i = 1; i < compositions.length+1; i++) {
			postFlashComp[i][0] = compositions[i-1]
					/ (1 + beta * (volatility[i-1] - 1));
			postFlashComp[i][1] = volatility[i-1] * postFlashComp[i][0];
		}
		return postFlashComp;
	}

}
