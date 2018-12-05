package tests;

import java.util.Scanner;

import numericmethods.MaxIterationException;
import numericmethods.RootNotBracketedException;
import numericmethods.SquareRootZeroException;
import eos.EquationOfState;
import eos.IdealEOS;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;
import flashseparator.FlashCalc;

//Testing the Ideal Rachford Rice Equation. An ideal EOS is created using random chemicals
//and flash conditions are calculated
public class RiceEquationIdealTest {

	public static void main(String[] args) throws RootNotBracketedException,
			MaxIterationException, SquareRootZeroException,
			CSVFileSizeException {
		// Generic compositions for the purpose of testing
		double[] compositions = { 0, 0.25, 0.25, 0.25, 0, 0.25 };
		// Defining user input folder path for the location of chemical species
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();
		InputChemicalSpecies chemReader = new InputChemicalSpecies(folderPath);

		// Creating an ideal EOS object
		EquationOfState nonidealEOS = new IdealEOS(chemReader.speciesInfo(),
				compositions);

		// Defining a multi-dimensional array to hold flash conditions
		double[][] flashConditions = FlashCalc.flashCalc(nonidealEOS, 298,
				101325);

		// Printing out the contents of the multi-dimensional flashConditions
		// array
		for (int i = 0; i < flashConditions.length; i++) {
			for (int j = 0; j < flashConditions[i].length; j++) {
				System.out.print(flashConditions[i][j] + ",");
			}
			System.out.println("");
		}

	}

}
