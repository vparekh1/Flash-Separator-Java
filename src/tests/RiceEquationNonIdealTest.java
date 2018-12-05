package tests;

import java.util.Scanner;

import numericmethods.MaxIterationException;
import numericmethods.RootNotBracketedException;
import numericmethods.SquareRootZeroException;
import eos.NonIdealEOS;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;
import flashseparator.FlashCalc;

public class RiceEquationNonIdealTest {

	public static void main(String[] args) throws RootNotBracketedException,
			MaxIterationException, SquareRootZeroException,
			CSVFileSizeException {
		// Defining a generic composition
		double[] compositions = { 0.25, 0., 0.25, 0.25, 0, 0.25 };

		// Defining a user input folder path for the location of relevant files
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();
		InputChemicalSpecies chemReader = new InputChemicalSpecies(folderPath);

		// Defining a non-ideal EOS object
		NonIdealEOS nonidealEOS = new NonIdealEOS(chemReader.speciesInfo(),
				compositions);

		// Printing out non-ideal flash conditions
		double[][] flashConditions = FlashCalc.flashCalc(nonidealEOS, 298,
				101325);

		for (int i = 0; i < flashConditions.length; i++) {
			for (int j = 0; j < flashConditions[i].length; j++) {
				System.out.print(flashConditions[i][j] + ",");
			}
			System.out.println("");
		}

	}

}
