package tests;

import java.util.Scanner;

import numericmethods.MaxIterationException;
import numericmethods.RootNotBracketedException;
import numericmethods.SquareRootZeroException;
import numericmethods.ValuesOutOfBoundsException;

import eos.EquationOfState;
import eos.IdealEOS;
import eos.ImpossibleAdiabaticFlashException;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;
import flashseparator.EnthalpyCalc;

/*Testing the enthalpy calculation, a non ideal case is made using a composition of 4 chemicals
 * whose properties are imported using the ChemicalSpecies class. 
 * 
 * The testing is done on three methods: calculateQ, calculateTfeed, and calculateTflash
 */

public class IdealEnthalpyCalcTest {

	public static void main(String[] args) throws ValuesOutOfBoundsException,
			RootNotBracketedException, MaxIterationException,
			SquareRootZeroException, ImpossibleAdiabaticFlashException,
			CSVFileSizeException {

		// Defining generic composition for the purpose of testing
		double[] compositions = { 0.25, 0.25, 0.25, 0.25, 0., 0 };

		// Defining a user input folder path for the location of relevant files
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();
		InputChemicalSpecies chemReader = new InputChemicalSpecies(folderPath);

		// Defining a non-ideal EOS object for chemical species
		EquationOfState nonidealEOS = new IdealEOS(chemReader.speciesInfo(),
				compositions);

		// Testing non-ideal enthalpy calculations
		System.out.println(EnthalpyCalc.calculateQ(nonidealEOS, 280, 322.29,
				0.02e6, 1));
		System.out.println(EnthalpyCalc.calculateTfeed(nonidealEOS, 0, 323,
				0.02e6, 1));
		System.out.println(EnthalpyCalc.calculateTflash(nonidealEOS, 0, 278,
				0.02e6, 1));

	}

}
