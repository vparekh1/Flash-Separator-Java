package tests;

import java.util.Arrays;
import java.util.Scanner;

import numericmethods.ValuesOutOfBoundsException;

import eos.NonIdealEOS;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;

//Non ideal EOS testing class
public class NonIdealEOSTest {

	public static void main(String[] args) throws ValuesOutOfBoundsException,
			CSVFileSizeException {

		// Defining a generic composition
		double[] compositions = { 0.25, 0.25, 0.25, 0.25, 0., 0 };
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();
		InputChemicalSpecies chemReader = new InputChemicalSpecies(folderPath);

		// Defining new non-ieal EOS object
		NonIdealEOS nonidealEOS = new NonIdealEOS(chemReader.speciesInfo(),
				compositions);

		// Printing out compressibilities
		System.out.println("Compressibility");
		System.out.println(Arrays.toString(nonidealEOS.calculateZ(322.29,
				0.02e6)));

		// Printing out volatilities
		System.out.println("Volatility");
		System.out.println(Arrays.toString(nonidealEOS.volatility(322.29,
				0.02e6)));

		// Printing out fugacities
		System.out.println("Fugacities");
		double[][] fugacities = nonidealEOS.calculateFugacity(322.29, 0.02e6);
		for (int i = 0; i < fugacities.length; i++) {
			for (int j = 0; j < fugacities[i].length; j++) {
				System.out.print(fugacities[i][j] + ",");
			}
			System.out.println("");
		}

		// Printing out constants
		System.out.println("a values");
		System.out.println(Arrays.toString(nonidealEOS.getA()));
		System.out.println("b values");
		System.out.println(Arrays.toString(nonidealEOS.getB()));
		System.out.println("alpha values");
		System.out.println(nonidealEOS.calculateAlpha(2, 322.29) + ","
				+ nonidealEOS.calculateAlpha(3, 322.29));
		System.out.println("A and B");
		System.out.println(nonidealEOS.calculateA(322.39, 0.02e6) + ","
				+ nonidealEOS.calculateB(322.39, 0.02e6));

		// Printing out enthalpies
		System.out.println("Enthalpies");
		double[][] enthalpies = nonidealEOS.enthalpies(280, 322.29, 0.02e6);
		for (int i = 0; i < enthalpies.length; i++) {
			for (int j = 0; j < enthalpies[i].length; j++) {
				System.out.print(enthalpies[i][j] + ",");
			}
			System.out.println("");
		}

	}

}