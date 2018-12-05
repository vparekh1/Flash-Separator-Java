package eos;

import numericmethods.CubicEquation;
import numericmethods.CubicSolve;

/*Non ideal EOS. This EOS extends the ideal case and overwrites the volatility calculation 
 * IN addition, this class has three new private instance variables; a values, b value, and kappa values
 * which will all be kept in a double array in order to have a value for each of the chemicals within a stream. 
 * This class also introduces methods of calculating z value and fugacity 
 */

//Peng-Robinson
public class NonIdealEOS extends IdealEOS {

	// Instance variables
	private double a[];
	private double b[];
	private double kappa[];

	// Constructor
	public NonIdealEOS(Chemical[] species, double[] compositions) {
		super(species, compositions);
		this.a = new double[super.getSpecies().length];
		this.b = new double[super.getSpecies().length];
		this.kappa = new double[super.getSpecies().length];

		for (int i = 0; i < super.getSpecies().length; i++) {
			this.a[i] = this.calculate_a(i);
			this.b[i] = this.calculate_b(i);
			this.kappa[i] = this.calculateKappa(i);
		}
	}
	
	// Copy constructor
	public NonIdealEOS(NonIdealEOS eos) {
		super(eos);
		this.a = new double[eos.getSpecies().length];
		this.b = new double[eos.getSpecies().length];
		this.kappa = new double[eos.getSpecies().length];

		for (int i = 0; i < eos.getSpecies().length; i++) {
			this.a[i] = eos.calculate_a(i);
			this.b[i] = eos.calculate_b(i);
			this.kappa[i] = eos.calculateKappa(i);
		}
	}
	
	// Clone method
	public NonIdealEOS clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new NonIdealEOS(this);
	}

	// Accessors and mutators
	public double[] getA() {
		return this.a;
	}

	public void setA(double[] a) {
		this.a = a;
	}

	public double[] getB() {
		return this.b;
	}

	public void setB(double[] b) {
		this.b = b;
	}

	public double[] getKappa() {
		return this.kappa;
	}

	public void setKappa(double[] kappa) {
		this.kappa = kappa;
	}

	// Calculate the volatility of the compound as per the Peng-Robinson equation of state
	public double[] volatility(double T, double P) {
		
		double[] result = new double[super.getSpecies().length];
		
		double[][] fugacity = new double[super.getSpecies().length][2];
		
		// Calcualate the fugacity of each component in the gas and liquid as required by Peng-Robinson
		// to calculate the volatility of the solution.
		fugacity = this.calculateFugacity(T, P);
		
		// As per the PR eos, the volatility of the solution is equal to the quotient of the fugacities
		for (int i = 0; i < super.getSpecies().length; i++) {
			double tmp = fugacity[i][0] / fugacity[i][1];
			if (Double.isNaN(tmp) || Double.isInfinite(tmp)) {
				result[i] = 1e30;
			} else if (tmp < 1e-30) {
				result[i] = 1e-30;
			} else {
				result[i] = tmp;
			}
		}

		return result;
	}

	// returns an array of length 2 for the liquid and vapor Z values
	// If the first value in the array is Double.MAX, there is only one Z value
	// calculated
	public double[] calculateZ(double T, double P) {

		// Calcualte the A and B parameters as per the Peng Robinson Equation of State
		double[] Z = new double[2];
		final double A = calculateA(T, P);
		final double B = calculateB(T, P);

		// Z^3 + (B-1)*Z^2 + (A-2B-3B^2)*Z +(-AB - B^2 - B^3) = 0
		CubicEquation prEOS = new CubicEquation() {
			public double getA() {
				return (B - 1);
			}

			public double getB() {
				return (A - 2. * B - 3. * Math.pow(B, 2.));
			}

			public double getC() {
				return (-A * B + Math.pow(B, 2.) + Math.pow(B, 3.));
			}
		};

		// Solve the cubic equation as per the CubicSolve class
		CubicSolve cubeClient = new CubicSolve();
		Z = cubeClient.solve(prEOS);
		
		return Z;
	}

	// Calculate the alpha parameter as per the Peng-Robinson equation of state
	public double calculateAlpha(int speciesNum, double T) {
		return Math
				.pow((1. + kappa[speciesNum]
						* (1 - Math
								.pow(T / super.getSpecies()[speciesNum].getTc(),
										0.5))),
										2.);
	}

	// Calculate the fugacity parameter of each phase as per the Peng-robinson equation of state
	public double[][] calculateFugacity(double T, double P) {

		int n = super.getSpecies().length;

		// Calcualte the A and B parameters as per the Peng Robinson Equation of State
		double A = calculateA(T, P);
		double B = calculateB(T, P);

		double[] AA = new double[n];
		double[] BB = new double[n];

		double bMixture = 0;
		for (int i = 0; i < n; i++) {
			bMixture += super.getCompositions()[i] * this.b[i];
		}

		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum = 0; 
			for (int j = 0; j < n; j++) {
				sum += super.getCompositions()[j]
						* Math.sqrt(this.a[i] * this.a[j]
								* this.calculateAlpha(i, T)
								* this.calculateAlpha(j, T)) * P
								/ Math.pow(super.getR() * T, 2.);
			}
			AA[i] = 2. / A * sum;
			BB[i] = b[i] / bMixture;
		}

		// get Z values for fugacity calculation
		double[] Z = new double[2];
		Z = this.calculateZ(T, P);

		// first row is liquid fugacity, second row is gas fugacity
		double[][] fugacities = new double[n][2];
		
		
		// If there is only one root
		for (int i = 0; i < n; i++) {
			if (Z[0] == Double.MAX_VALUE) {
				// Check if it is in the gas phase (the Z value is likely above
				// 0.5)
				if (Z[1] > 0.5) {
					// Set the liquid fugacities to a very high number, and the gas fugacity
					// to the one calculated as per the PREOS equations

					fugacities[i][0] = 1e10;
					fugacities[i][1] = this.speciesFugacity(T, P, i, 1, A, B,
							AA, BB, Z);

				} else {
					// Set the gas fugacities to a very high number, and the liquid ones to
					// the one calculated by the PREOS equations

					fugacities[i][0] = this.speciesFugacity(T, P, i, 1, A, B,
							AA, BB, Z);
					fugacities[i][1] = 1e10;
				}
			} else {
				// set the first set of fugacities to the liquid fugacity
				// and the second set to the gas fugacities

				fugacities[i][0] = this.speciesFugacity(T, P, i, 0, A, B, AA,
						BB, Z);
				fugacities[i][1] = this.speciesFugacity(T, P, i, 1, A, B, AA,
						BB, Z);

			}
		}
		return fugacities;

	}

	// Implementation of the partial component fugacity in a phase based on the
	// temperature, pressure and compressibility of the overall mixture
	// if liquidGas is 0, it is liquid, if it is 1, it is in the gas phase
	private double speciesFugacity(double T, double P, int speciesNumber,
			int liquidGas, double A, double B, double[] AA, double[] BB,
			double[] Z) {

		return Math.exp(BB[speciesNumber]
				* (Z[liquidGas] - 1.)
				- Math.log(Z[liquidGas] - B)
				- A
				/ (2. * Math.sqrt(2.) * B)
				* (AA[speciesNumber] - BB[speciesNumber])
				* Math.log((Z[liquidGas] + (Math.sqrt(2.) + 1.) * B)
						/ (Z[liquidGas] - (Math.sqrt(2.) - 1.) * B)));
	}


	// Calculate the a parameter in the Peng-Robinson EOS
	private double calculate_a(int speciesNum) {
		return (0.457235529 * Math.pow(super.getR()
				* super.getSpecies()[speciesNum].getTc(), 2.) / super
				.getSpecies()[speciesNum].getPc());
	}

	// Calculate the b parameter in the Peng-robinson Equation of State
	private double calculate_b(int speciesNum) {
		return (0.077796074 * super.getR()
				* super.getSpecies()[speciesNum].getTc() / getSpecies()[speciesNum]
						.getPc());
	}

	// Calculate the kappa parameter in the Peng-Robinson Equation of State
	private double calculateKappa(int speciesNum) {
		return (0.37646 + 1.54226 * getSpecies()[speciesNum].getOmega() - 0.26992 * Math
				.pow(getSpecies()[speciesNum].getOmega(), 2.));
	}

	// Calculate the (alpha*a)_mixture of the solution as per the Peng-Robinson Equation of State
	private double calculateAlphaAMixture(double T) {
		int n = super.getSpecies().length;
		double alphaAMixture = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				alphaAMixture += super.getCompositions()[i]
						* super.getCompositions()[j]
								* Math.sqrt(this.a[i] * this.calculateAlpha(i, T)
										* this.a[j] * this.calculateAlpha(j, T));
			}
		}
		return alphaAMixture;
	}

	// Calculate the A value of the mixture as per the Peng-Robinson Equation of State
	public double calculateA(double T, double P) {
		double alphaAMixture = this.calculateAlphaAMixture(T);
		return alphaAMixture * P / (Math.pow(super.getR() * T, 2.));
	}

	// Calculate the B value of the mixture as per the Peng-Robinson Equation of State
	public double calculateB(double T, double P) {
		// calculate b of the mixture
		int n = super.getSpecies().length;
		double bMixture = 0;
		for (int i = 0; i < n; i++) {
			bMixture += super.getCompositions()[i] * this.b[i];
		}

		return bMixture * P / (super.getR() * T);

	}

}