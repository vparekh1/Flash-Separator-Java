package tests;

import java.util.Arrays;
import java.util.Scanner;

import numericmethods.ValuesOutOfBoundsException;

import eos.IdealEOS;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;

//Testing the IdealEOS methods.

public class IdealEOSTest {

	public static void main(String[] args) throws ValuesOutOfBoundsException,
			CSVFileSizeException {

		// Defining generic compositions for the purposes of the test
		double[] compositions = { 0, 0, 0.0331, 0.8168, 0.1501, 0 };
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();
		InputChemicalSpecies chemReader = new InputChemicalSpecies(folderPath);

		// Defining new ideaEOS object
		IdealEOS idealEOS = new IdealEOS(chemReader.speciesInfo(), compositions);

		// Printing an array of volatilities
		System.out.println("Volatility");
		System.out
				.println(Arrays.toString(idealEOS.volatility(322.29, 101325)));

		// Printing an array of enthalpies
		System.out.println("Enthalpies");
		double[][] enthalpies = idealEOS.enthalpies(280, 322.29, 0.02e6);
		for (int i = 0; i < enthalpies.length; i++) {
			for (int j = 0; j < enthalpies[i].length; j++) {
				System.out.print(enthalpies[i][j] + ",");
			}
			System.out.println("");
		}

	}

}
