package tests;

import java.util.Scanner;

import eos.Chemical;
import fileimport.InputChemicalSpecies;
import fileimport.ReadParametersCSV;

//Testing the CSV reading class
public class ReadCSVTest {
	// Main method:
	public static void main(String[] args) {
		// A. Establish where path for csv files will be
		String folderPath = "";
		Scanner rdr = new Scanner(System.in);
		System.out
				.println("Please print out the folder path where all the relevant files are located: ");
		folderPath = rdr.next();
		rdr.close();

		// B. Import & assign species properties from csv files
		InputChemicalSpecies readSpecies = new InputChemicalSpecies(folderPath);
		Chemical[] species = readSpecies.speciesInfo();

		// C. Import & assign system parameters from csv file
		ReadParametersCSV parameters = new ReadParametersCSV(folderPath);
		int caseN = (int) parameters.getParameters()[0]; // Need to downcast
		// since
		// parameters[] is
		// of type double
		double tankP = parameters.getParameters()[1];
		double temp = parameters.getParameters()[2];
		double flowIn = parameters.getParameters()[3];

		double[] massFracs = new double[parameters.getParameters().length - 4];

		for (int i = 0; i < (parameters.getParameters().length - 4); i++) {
			massFracs[i] = parameters.getParameters()[i + 4];
		}

		// D. Test:

		System.out.println("Species");

		for (int i = 0; i < species.length; i++) {
			System.out.println(species[i]);
			System.out.println("Mass fraction: " + massFracs[i] + "\n");
		}

		System.out.println("\nParameters");
		System.out.println("Case: " + caseN + "\t Tank Pressure: " + tankP
				+ "\t Temperature: " + temp + "\t Flow in: " + flowIn);

	}
}