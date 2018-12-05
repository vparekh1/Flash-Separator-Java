package tests;

import java.util.Scanner;

import numericmethods.MaxIterationException;
import numericmethods.RootNotBracketedException;
import numericmethods.SquareRootZeroException;
import eos.EquationOfState;
import eos.IdealEOS;
import eos.NonIdealEOS;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;
import flashseparator.FlashCalc;

/*Testing of the DewPBubbleP calculation Test. First a compositions double array is created to mimic
 * the stream being analyzed. Chemreader brings in the chemical components and their properties. 
 * This test is done by creating a non-ideal EOS and an ideal EOS to test the peng robinson calcualtions
 * and the ideal calculations for both dew point and bubble point. 
 * 
 * To test at different temperatures, change lines 29/30, 36/37. 
 */

public class DewPBubblePTest {

	public static void main(String[] args) throws MaxIterationException,
			RootNotBracketedException, SquareRootZeroException,
			CSVFileSizeException {

		double[] compositions = { 0, 0.0331, 0., 0.8168, 0.1501, 0 };

		// Defining a user input folder path for the location of relevant files
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();

		// Obtaining chemical species from the specified folder path
		InputChemicalSpecies chemReader = new InputChemicalSpecies(folderPath);

		NonIdealEOS nonidealEOS = new NonIdealEOS(chemReader.speciesInfo(),
				compositions);

		// Testing non-ideal (Peng-Robinson) dewP and bublP calculations
		System.out.println("The PENG-ROBINSON DEW POINT IS "
				+ FlashCalc.dewP(nonidealEOS, 300));
		System.out.println("The PENG-ROBINSON BUBBLE POINT IS "
				+ FlashCalc.bublP(nonidealEOS, 350));

		EquationOfState idealEOS = new IdealEOS(chemReader.speciesInfo(),
				compositions);

		// Testing ideal dewP and bublP calculations
		System.out.println("THE IDEAL BUBBLE POINT IS "
				+ FlashCalc.bublP(idealEOS, 300));
		System.out.println("The IDEAL DEW POINT IS "
				+ FlashCalc.dewP(idealEOS, 330));

	}

}