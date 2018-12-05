package eos;

import numericmethods.ValuesOutOfBoundsException;
/* Extension of the Base EOS class 
 * 
 */
public class IdealEOS extends EquationOfState {

 //Constructors
 public IdealEOS(Chemical[] species, double[] compositions) {
  super(species, compositions);
 }
 
 // Clone Constructor
 public IdealEOS(IdealEOS idealEOS) {
  super(idealEOS);
 }
 
 // Clone method
 public IdealEOS clone() {
  // Override clone method to call copy constructor, passes itself as
  // object & returns duplicate
  return new IdealEOS(this);
 }

 // It is going to return the outlet specific liquid enthalpy in the first
 // row, the gas enthalpy in the second row
 // and the feed enthalpy in the third row, for each component in the
 // mixture. Assume the feed stream
 // is at a high pressure and all enthalpies (below critical temperature) use
 // liquid heat capacities
 public double[][] enthalpies(double feedT, double flashT, double opP) throws ValuesOutOfBoundsException {
  

  int n = super.getSpecies().length;

  double[][] result = new double[n][3];
  for (int i = 0; i < n; i++) {
   double[] cpLQ = super.getSpecies()[i].getCpLQ();
   double[] cpIG = super.getSpecies()[i].getCpIG();
   double refTemp = super.getSpecies()[i].getTref();
   double H0 = super.getSpecies()[i].getH0();
   double tBoil = super.getSpecies()[i].getTboil();
   double isGasRef = super.getSpecies()[i].getIsGasRef();
   double hVap = super.getSpecies()[i].getHvap();
   double Tc = super.getSpecies()[i].getTc();

   // Calculate feed enthalpies (all liquid if they are below critical
   // temp)
   // If they are above critical temp, they are noncondensable, and gas
   // enthalpy
   // will be used instead
   
   // If the reference state of the component is a gas
   if (isGasRef == 1.0) {
     
    // If the flash temperature is higher than the boiling point, only the gas phase heat
    // capacity will be used, and the heat of vaporization will be subtracted out.
    if (flashT > tBoil) {
     result[i][0] = IdealEOS.integrateCp(cpIG, refTemp, flashT) - hVap + H0;
    } else {
     // If the flash temperature is lower than the boiling temperature, we will use both
     // the gas and the liquid heat capacities.
     result[i][0] = IdealEOS.integrateCp(cpIG, refTemp, tBoil) - hVap + IdealEOS.integrateCp(cpLQ, tBoil, flashT) + H0;      
    }
    
    result[i][1] = IdealEOS.integrateCp(cpIG, refTemp, flashT) + H0;
    
    
    // If the feed temperature is greater than the critical temperature, the
    // component is noncondensable, and will remain in the gas phase.  Since the
    // reference state is a gas, no heat of vaporization will be added
    if (feedT > Tc) {
     result[i][2] = IdealEOS.integrateCp(cpIG, refTemp, feedT) + H0;
    } else {
     // The component is condenseable, so the final state in the feed will be a liquid
     // Therefore, a heat of vaporization will be added
     
     // If the feed temperature is higher than the boiling point, only the gas phase heat
     // capacity will be used, and the heat of vaporization will be subtracted out.
     if (feedT > tBoil) {
      result[i][2] = IdealEOS.integrateCp(cpIG, refTemp, feedT) - hVap + H0;
     } else {
      // If the feed temperature is lower than the boiling temperature, we will use both
      // the gas and the liquid heat capacities.
      result[i][2] = IdealEOS.integrateCp(cpIG, refTemp, tBoil) - hVap + IdealEOS.integrateCp(cpLQ, tBoil, feedT) + H0;
     }
    }
    
   } else {
   // If the reference state of the component is a liquid
    
    result[i][0] = IdealEOS.integrateCp(cpIG, refTemp, flashT) + H0;
    
    // If the flash temperature is higher than the boiling point, only the gas phase heat
    // capacity will be used, and the heat of vaporization will be subtracted out.
    if (flashT > tBoil) {
     result[i][1] = IdealEOS.integrateCp(cpLQ, refTemp, tBoil) + hVap + IdealEOS.integrateCp(cpIG, tBoil, flashT) + H0;
    } else {
     // If the flash temperature is lower than the boiling temperature, we will use both
     // the gas and the liquid heat capacities.
     result[i][1] = IdealEOS.integrateCp(cpIG, refTemp, flashT) + hVap + H0;
     
    }

    // If the feed temperature is higher than the boiling point, only the gas phase heat
    // capacity will be used, and the heat of vaporization will be subtracted out.
    if (feedT > tBoil) {
     result[i][2] = IdealEOS.integrateCp(cpLQ, refTemp, tBoil) + hVap + IdealEOS.integrateCp(cpIG, tBoil, feedT)+ H0;
    } else {
     // If the feed temperature is lower than the boiling temperature, we will use both
     // the gas and the liquid heat capacities.
     result[i][2] = IdealEOS.integrateCp(cpLQ, refTemp, feedT) + H0;
    }
   }

  }

  return result;
 }

 
 public static double integrateCp(double[] cpArray, double startT, double endT) {
  return (cpArray[0] * (endT - startT) + cpArray[1]
    * (1./2.)*(Math.pow(endT, 2.) - Math.pow(startT, 2.))
    + cpArray[2]
    * (1./3.)*(Math.pow(endT, 3.) - Math.pow(startT, 3.)) - cpArray[3]
    * (1. / endT - 1. / startT));
 }

 
 //Calculation of the volatility 
 public double[] volatility(double T, double P) {
  double[] result = new double[super.getSpecies().length];
  for (int i = 0; i < result.length; i++) {
   try {
    result[i] = this.satP(T, i) / P;
   } catch (ValuesOutOfBoundsException e) {
    // If the temperature of the system is outside the bounds of the antoine parameters
    // of one of the components in the mixture, a warning will be emitted.
    System.err.println("Warning: "+e.getMessage());
    if (e.getLowerBound() > T) {
     result[i]=1e-7;
    } else {
     result[i] = 1e7;
    }
   }
  }

  return result;
 }

}
