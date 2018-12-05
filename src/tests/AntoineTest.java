/*Testing the Antoine Equation Class, this is done by running an Ideal EOS with a made up composition stream.
 * The test can be modified to try different cases and ensure exceptions can be caught. 
 * 
 * To test new cases, change the compositions double array to whichever values and run 
 * idealEOS.satP(temp, atmPressure). Initialize the idealEOS using the compositions array and 
 * the chemReader ChemicalSpecies list. 
 */

package tests;

import java.util.Scanner;

import numericmethods.MaxIterationException;
import numericmethods.RootNotBracketedException;
import numericmethods.SquareRootZeroException;
import numericmethods.ValuesOutOfBoundsException;
import eos.EquationOfState;
import eos.IdealEOS;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;

public class AntoineTest {
	public static void main(String[] args) throws MaxIterationException,
			RootNotBracketedException, SquareRootZeroException,
			CSVFileSizeException, ValuesOutOfBoundsException {

		// Generic compositions for the purposes of the test
		double[] compositions = { 0, 0, 0.0331, 0.8168, 0.1501, 0 };

		// Defining a user input folder path for the location of relevant files
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();

		// Obtaining chemical species from the specified folder path
		InputChemicalSpecies chemReader = new InputChemicalSpecies(folderPath);

		// Creating an ideal EOS object for the chemical species
		EquationOfState idealEOS = new IdealEOS(chemReader.speciesInfo(),
				compositions);

		// Testing the output idealEOS method with parameters obtained from the
		// user specified file path
		System.out.println("The vapor pressure of water at 373.15 K is "
				+ idealEOS.satP(373.15, 1));

	}
}
