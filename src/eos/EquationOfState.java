package eos;

import numericmethods.ValuesOutOfBoundsException;

/*Abstract base equation of state class. Ideal and non-ideal cases will extend from this class
 * Requires child classes to overwrite enthalpy and volatility calculations
 */
public abstract class EquationOfState {

	// Set the ideal gas constant as a fixed value in all calculations. The
	// units for all calculations will be maintained SI.
	private static final double R = 8.3144598;

	private Chemical[] species;
	private double[] compositions;
	
	// This will calculate the enthalpies of the mixture in a [number of components][3] array.
	// in the [i][0] column the feed enthalpies will be provided.  In the [i][1] column, the liquid enthalpies of 
	// the flash separator will be provided, and in the [i][2] column, the gas phase enthalpies of the flash
	// separator will be provided for the ith component of the mixture.
	public abstract double[][] enthalpies(double feedT, double flashT, double opP) throws ValuesOutOfBoundsException;
	
	// The ith value in the returned array will provide the volatility of the ith component in the mixture
	public abstract double[] volatility(double T, double P);
	
	// The default implementation of the saturation pressure for the equation
	// of state will be the Antoine Equation
	// Since the default behaviour for the saturation behaviour of a system is
	// the Antoine equation, the antoine parameters will be introduced in the
	// abstract equation of state.
	public double satP(double T, int speciesNumber) throws ValuesOutOfBoundsException {
		double[] antoineParams = new double[5];
		antoineParams = this.getSpecies()[speciesNumber].getAntoine();
		if (this.compositions[speciesNumber] > 1e-5 && (antoineParams[3] > T || antoineParams[4] < T)) {
			//System.err.println("Warning: The following species has Antoine Values out of bounds: " + species[speciesNumber].getSpeciesName());
		}


		// Convert values from mmHg to Pa and apply the Antoine equation
		return Math.pow(10, antoineParams[0]
				- antoineParams[1] / (T + antoineParams[2]));
	}
	
	// Constructor
	public EquationOfState(Chemical[] species, double[] compositions) {
		this.species = species;
		this.compositions = compositions;
	}
	
	// Copy constructor
	public EquationOfState(EquationOfState eos) {
		this.species = eos.species;
		this.compositions = eos.compositions;
	}
	
	// Accessors and Mutators
	
	public double[] getCompositions() {
		return compositions;
	}

	public double getR() {
		return EquationOfState.R;
	}

	public Chemical[] getSpecies() {
		return species;
	}
	
	public void setCompositions(double[] compositions) {
		this.compositions = compositions;
	}

	public void setSpecies(Chemical[] species) {
		this.species = species;
	}
	
}
