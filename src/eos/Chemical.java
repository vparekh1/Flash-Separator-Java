package eos;

import java.util.Arrays; //to use copyOf method for arrays

/* Chemical Object class
 * Holds a variety of chemical properties that can be accessed
 */
public class Chemical {
	String speciesName;
	double tc; // From PR csv
	double pc; // From PR csv
	double omega; // Accentric Factor for Peng Robinson, from PR csv

	double h0; // From enthalpy csv
	double tref; // From enthalpy csv
	double hvap; // From enthalpy csv
	double tboil; // From enthalpy csv
	double isGasRef; // From enthalpy csv

	double[] antoine; // A, B, C, Tmin, Tmax
	double[] cpIG; // A, B, C, D, Tmin, Tmax
	double[] cpLQ; // A, B, C, D, Tmin, Tmax

	// Constructor
	public Chemical(String speciesName, double tc, double pc, double omega,
			double h0, double tref, double hvap, double tboil,
			double[] antoine, double[] cpIG, double[] cpLQ, double isGasRef) {

		this.antoine = new double[5];
		this.cpIG = new double[6];
		this.cpLQ = new double[6];

		this.speciesName = speciesName;
		this.tc = tc;
		this.pc = pc;
		this.omega = omega;
		this.h0 = h0;
		this.tref = tref;
		this.hvap = hvap;
		this.tboil = tboil;
		this.isGasRef = isGasRef;

		this.antoine = Arrays.copyOf(antoine, this.antoine.length);
		this.cpIG = Arrays.copyOf(cpIG, this.cpIG.length);
		this.cpLQ = Arrays.copyOf(cpLQ, this.cpLQ.length);

	}

	// Copy Constructor
	public Chemical(Chemical copy) {
		this.speciesName = copy.speciesName;
		this.tc = copy.tc;
		this.pc = copy.pc;
		this.omega = copy.omega;
		this.h0 = copy.h0;
		this.tref = copy.tref;
		this.hvap = copy.hvap;
		this.tboil = copy.tboil;
		this.isGasRef = copy.isGasRef;

		this.antoine = Arrays.copyOf(copy.antoine, this.antoine.length);
		this.cpIG = Arrays.copyOf(copy.cpIG, this.cpIG.length);
		this.cpLQ = Arrays.copyOf(copy.cpLQ, this.cpLQ.length);
	}

	// Clone Method
	public Chemical clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new Chemical(this);
	}

	// Accessors
	public String getSpeciesName() {
		return this.speciesName;
	}

	public double getTc() {
		return this.tc;
	}

	public double getPc() {
		return this.pc;
	}

	public double getOmega() {
		return this.omega;
	}

	public double getH0() {
		return this.h0;
	}

	public double getTref() {
		return this.tref;
	}

	public double getHvap() {
		return this.hvap;
	}

	public double getTboil() {
		return this.tboil;
	}

	public double getIsGasRef() {
		return this.isGasRef;
	}

	public double[] getAntoine() {
		double[] copyArray = Arrays.copyOf(this.antoine, this.antoine.length);
		return copyArray;
	}

	public double[] getCpIG() {
		double[] copyArray = Arrays.copyOf(this.cpIG, this.antoine.length);
		return copyArray;
	}

	public double[] getCpLQ() {
		double[] copyArray = Arrays.copyOf(this.cpLQ, this.cpLQ.length);
		return copyArray;
	}

	// Mutators
	public boolean setSpeciesName(String speciesName) {

		if ((speciesName == null) || (speciesName == "")) {
			return false;
		} else {
			this.speciesName = speciesName;
			return true;
		}
	}

	public boolean setTc(double tc) {

		if (tc < 0) // Temp is in Kelvins, can't be below absolute 0
		{
			return false;
		} else {
			this.tc = tc;
			return true;
		}
	}

	public void setPc(double pc) {
		this.pc = pc;
	}

	public void setOmega(double omega) {
		this.omega = omega;
	}

	public void setH0(double h0) {
		this.h0 = h0;
	}

	public boolean setTref(double tref) {

		if (tref < 0) // Temp is in Kelvins, can't be below absolute 0
		{
			return false;
		} else {
			this.tref = tref;
			return true;
		}
	}

	public void setHvap(double hvap) {
		this.hvap = hvap;
	}

	public boolean setTboil(double tboil) {

		if (tboil < 0) // Temp is in Kelvins, can't be below absolute 0
		{
			return false;
		} else {
			this.tboil = tboil;
			return true;
		}
	}

	public boolean setIsGasRef(double isGasRef) {
		if ((isGasRef != 0) && (isGasRef != 1)) // isGasRef has to be 1 or 0
												// (it's binary)
		{
			return false;
		} else {
			this.isGasRef = isGasRef;
			return true;
		}
	}

	public boolean setAntoine(double[] antoine) {
		if ((this.antoine.length != antoine.length) || (antoine == null)) {
			return false;
		} else {

			// Deep copy array (set new values):
			for (int i = 0; i < antoine.length; i++) {
				this.antoine[i] = antoine[i];
			}

			return true;
		}
	}

	public boolean setCpIG(double[] cpIG) {
		if ((this.cpIG.length != cpIG.length) || (cpIG == null)) {
			return false;
		} else {

			// Deep copy array (set new values):
			for (int i = 0; i < cpIG.length; i++) {
				this.cpIG[i] = cpIG[i];
			}

			return true;
		}
	}

	public boolean setCpLQ(double[] cpLQ) {
		if ((this.cpLQ.length != cpLQ.length) || (cpLQ == null)) {
			return false;
		} else {

			// Deep copy array (set new values):
			for (int i = 0; i < cpLQ.length; i++) {
				this.cpLQ[i] = cpLQ[i];
			}

			return true;
		}
	}

	// toString method to see values assigned to a given chemical species
	public String toString() {
		return String
				.format("Chemical [speciesName=%s, tc=%s, pc=%s, omega=%s, h0=%s, tref=%s, hvap=%s, tboil=%s, antoine=%s, cpIG=%s, cpLQ=%s, isGasRef=%s]",
						speciesName, tc, pc, omega, h0, tref, hvap, tboil,
						Arrays.toString(antoine), Arrays.toString(cpIG),
						Arrays.toString(cpLQ), isGasRef);
	}

}