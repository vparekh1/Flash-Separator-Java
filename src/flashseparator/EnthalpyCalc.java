package flashseparator;

import java.util.Arrays;
import numericmethods.IncrementalSearch;
import numericmethods.MaxIterationException;
import numericmethods.RiddersMethod;
import numericmethods.RiddersMethodEquation;
import numericmethods.RootNotBracketedException;
import numericmethods.SquareRootZeroException;
import numericmethods.ValuesOutOfBoundsException;
import eos.EquationOfState;
import eos.ImpossibleAdiabaticFlashException;

/*Enthalpy calculation class. Calculation of Q and Temperature of feed based on operating conditions of the separator and whether 
 * or not the system is ideal as indicated by the user.
 */

public class EnthalpyCalc {

	//Calculation of Q taking in EOS, temperature of Feed and Flash, operating pressure, and F value
	public static FlashOutput calculateQ(EquationOfState eos, double feedT, double flashT, double opP, double F) throws ValuesOutOfBoundsException, RootNotBracketedException, MaxIterationException, SquareRootZeroException {
		
		// First calculate the specific enthalpies of the gas and liquid streams entering and
		// exiting the flash separator
		double[][] enthalpies = eos.enthalpies(feedT, flashT, opP);
		
		// Then calculate the total flash values and the compositions of each component in each
		// phase
		double[][] flashParams = FlashCalc.flashCalc(eos, flashT, opP);
		
		// Deep copying of the composition array
		double[] compositions = Arrays.copyOf(eos.getCompositions(), eos.getCompositions().length);
		
		int n = compositions.length;
		
		// Checks to see if mixture is two-phase.  If it is below or above the dew/bubble point of the mixture
		// all of the mixture is either in the gas or liquid phase.  This can cause some issues in accurate 
		// simulation of results
		if (opP < FlashCalc.dewP(eos, flashT)) {
			System.out.println("Warning: All of the mixture is in the gas phase: flashT = "+flashT+", opP = "+opP);
		}
		if (opP > FlashCalc.bublP(eos, flashT)) {
			System.out.println("Warning: All of the mixture is in liquid phase: flashT = "+flashT+", opP = "+opP);
		}
		
		// Perform an enthalpy balance
		double Q = 0;
		for (int i=0; i<n; i++) {
			Q += enthalpies[i][0]*F*flashParams[0][0]*flashParams[i+1][0]
					+ enthalpies[i][1]*F*flashParams[0][1]*flashParams[i+1][1]
					- enthalpies[i][2]*F*compositions[i];
		}
		
		double[] yI = new double[n];
		double[] xI = new double[n];
		for (int i=0; i<n; i++) {
			xI[i] = flashParams[i+1][0];
			yI[i] = flashParams[i+1][1];
		}
		
		FlashOutput result = new FlashOutput(compositions, yI, xI, flashParams[0][0]*F, flashParams[0][1]*F, F,
			Q, flashT, feedT);
		return result;
	}
	
	//Calculation of the Feed temperature given the EOS, the Q, the flash temperature, the operating pressure and F values
	//Made final as these should not be able to be changed once calculation has begun.
	public static FlashOutput calculateTfeed(final EquationOfState eos, final double Q, final double flashT, final double opP, final double F) throws RootNotBracketedException, MaxIterationException, SquareRootZeroException {
		
		// Then calculate the total flash values and the compositions of each component in each
		// phase.
		final double[][] flashParams = FlashCalc.flashCalc(eos, flashT, opP);
		final double[] compositions = Arrays.copyOf(eos.getCompositions(), eos.getCompositions().length);
		final int n = compositions.length;
		
		// Ensure that the stream is actually flashable.  If it is outside the range of the dew and bubble point,
		// some of the calculations would not give accurate results.
		if (opP < FlashCalc.dewP(eos, flashT)) {
			System.out.println("All of the mixture is in the gas phase: flashT = "+flashT+", opP = "+opP);
		}
		if (opP > FlashCalc.bublP(eos, flashT)) {
			System.out.println("All of the mixture is in liquid phase: flashT = "+flashT+", opP = "+opP);
		}
		
		// enthalpyBalance is the zero root equation for which the feed temperature needs to be
		// solved for using a numerical Ridders Method.
		RiddersMethodEquation enthalpyBalance = new RiddersMethodEquation() {
			
			// The function is still the same Enthalpy balance as in the calculation of the
			// Q value, however, the enthalpy of the feed stream is a function of the feed temperature
			// and therefore needs to be solved numerically.
			public double function(double feedT) {
				double Q_gen = 0;
				double[][] enthalpies = null;
				try {
					enthalpies = eos.enthalpies(feedT, flashT, opP);
				} catch (ValuesOutOfBoundsException e) {

					e.printStackTrace();
				}
				for (int i=0; i<n; i++) {
					Q_gen += enthalpies[i][0]*F*flashParams[0][0]*flashParams[i+1][0]
							+ enthalpies[i][1]*F*flashParams[0][1]*flashParams[i+1][1]
							- enthalpies[i][2]*F*compositions[i];
				}
				
				return (Q_gen - Q);
			}
			
			public String name() {
				return "Enthalpy balance";
			}
			
		};
		
		// Perform an incremental search on the bounds starting from the flash temperature and going up
		double[] bounds = (new IncrementalSearch(1000)).findBounds(enthalpyBalance, flashT, 100);
		
		// Once bounds are found, the feed temperature can be solved for using Ridder's method
		double feedT = (new RiddersMethod()).solve(enthalpyBalance, bounds[0], bounds[1]);
		
		double[] yI = new double[n];
		double[] xI = new double[n];
		for (int i=0; i<n; i++) {
			xI[i] = flashParams[i+1][0];
			yI[i] = flashParams[i+1][1];
		}
		
		FlashOutput result = new FlashOutput(compositions, yI, xI, flashParams[0][0]*F, flashParams[0][1]*F, F,
			Q, flashT, feedT);
		return result;
	}
	
	//Calculation of the flash temperature given EOS, Q value, feed temperature, operating pressure and F value for the separator 
	public static FlashOutput calculateTflash(final EquationOfState eos, final double Q, final double feedT, final double opP, final double F) throws ImpossibleAdiabaticFlashException, MaxIterationException, RootNotBracketedException, SquareRootZeroException {
		
		// deep copying the compositions of the equation of state
		final double[] compositions = Arrays.copyOf(eos.getCompositions(), eos.getCompositions().length);
		final int n = compositions.length;
		
		// Ensure that the stream is actually flashable.  If it is outside the range of the dew and bubble point,
		// some of the calculations would not give accurate results.
		if (opP < FlashCalc.dewP(eos, feedT)) {
			System.out.println("All of the mixture is in the gas phase: flashT = "+feedT+", opP = "+opP);
		}
		if (opP > FlashCalc.bublP(eos, feedT)) {
			System.out.println("All of the mixture is in liquid phase: flashT = "+feedT+", opP = "+opP);
		}
		
		// enthalpyBalance is the zero root equation for which the feed temperature needs to be
		// solved for using a numerical Ridders Method.
		RiddersMethodEquation enthalpyBalance = new RiddersMethodEquation() {

			// The function is still the same Enthalpy balance as in the calculation of the
			// Q value, however, the enthalpy of the feed stream is a function of the feed temperature
			// and therefore needs to be solved numerically.
			public double function(double flashT) {
				double Q_gen = 0;
				double[][] flashParams = null;

				try {
					flashParams = FlashCalc.flashCalc(eos, flashT, opP);
				} catch (Exception e1) {
					
					System.out.println("Warning: Flash calculation has not converged adequately, simulation may return\n" +
							"incorrect results.");
					
					flashParams = new double[eos.getCompositions().length+1][2];
					flashParams[0][0] = 1-1e-30;
					flashParams[0][1] = 1e-30;
					for (int i=1; i < eos.getCompositions().length+1; i++) {
						flashParams[i][0] = eos.getCompositions()[i-1];
						flashParams[i][1] = 1e-30;
					}
				}
		
				double[][] enthalpies = null;
				try {
					enthalpies = eos.enthalpies(feedT, flashT, opP);
				} catch (ValuesOutOfBoundsException e) {

					e.printStackTrace();
				}

				for (int i=0; i<n; i++) {
					Q_gen += enthalpies[i][0]*F*flashParams[0][0]*flashParams[i+1][0]
							+ enthalpies[i][1]*F*flashParams[0][1]*flashParams[i+1][1]
							- enthalpies[i][2]*F*compositions[i];
				}
				

				return (Q_gen - Q);
			}
			
			public String name() {
				return "Enthalpy balance";
			}
			
		};
		
		double[] bounds = new IncrementalSearch(1000).findBounds(enthalpyBalance, feedT, -1);
		if (bounds[0] == Double.MAX_VALUE) {
			System.out.print("Adiabatic Flash not possible: ");
			if (bounds[1] > 0) {
				throw new ImpossibleAdiabaticFlashException("System is completely condensed");
			} else {
				throw new ImpossibleAdiabaticFlashException("System is completely volatile");
			}
		}
		
		double flashT =  (new RiddersMethod()).solve(enthalpyBalance, bounds[1], bounds[0]);
		double[][] flashParams = FlashCalc.flashCalc(eos, flashT, opP);
		double[] yI = new double[n];
		double[] xI = new double[n];
		for (int i=0; i<n; i++) {
			xI[i] = flashParams[i+1][0];
			yI[i] = flashParams[i+1][1];
		}
		
		FlashOutput result = new FlashOutput(compositions, yI, xI, flashParams[0][0]*F, flashParams[0][1]*F, F,
			Q, flashT, feedT);
		return result;
	}

}
