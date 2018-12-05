package flashseparator;

//Code to actually run the simulation while prompting the user for their specifications

import java.util.Scanner;

import numericmethods.MaxIterationException;
import numericmethods.RootNotBracketedException;
import numericmethods.SquareRootZeroException;
import numericmethods.ValuesOutOfBoundsException;

import eos.Chemical;
import eos.EquationOfState;
import eos.IdealEOS;
import eos.ImpossibleAdiabaticFlashException;
import eos.NonIdealEOS;
import fileimport.CSVFileSizeException;
import fileimport.InputChemicalSpecies;
import fileimport.ReadParametersCSV;

public class RunProgram {

	public void run() throws CSVFileSizeException {
		
		// Scanner to read in the user input
		Scanner rdr = new Scanner(System.in).useDelimiter(",");

		System.out
				.println("Please enter the folder path which contains all of the necessary input values\n "
						+ "for the simulation, followed directly by \",\"");

		// Ask the user to enter a folder path
		String folderPath = "";
		try {
			folderPath = rdr.next();
		} catch (Exception e) {
			System.out.println("Please enter a valid string");
		}

		ReadParametersCSV parameters = null;
		Chemical[] species = null;
		try {
			// Import & assign species properties from csv files
			InputChemicalSpecies readSpecies = new InputChemicalSpecies(
					folderPath);

			species = readSpecies.speciesInfo();

			// Import & assign system parameters from csv file
			parameters = new ReadParametersCSV(folderPath);
		} catch (Exception e) {
			System.out
					.println(folderPath
							+ " The folder name you entered was invalid.  Please enter a valid folder name and try running "
							+ "the program again. ");
			System.exit(1);
		}
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

		double feedT = 0;
		double flashT = 0;
		double Q = 0;

		int ideality = this.getIdeality();

		EquationOfState eos;
		if (ideality == 1) {
			eos = new IdealEOS(species, massFracs);
		} else {
			eos = new NonIdealEOS(species, massFracs);
		}

		switch (caseN) {
		case 1:

			// T = specified constant operating temp
			// looking for Q required to maintain process temp

			feedT = temp;
			flashT = temp;

			try {
				EnthalpyCalc.calculateQ(eos, feedT, flashT, tankP, flowIn)
						.toCSV(folderPath);
				System.out.println("System output has been saved in "
						+ folderPath + " as systemoutput.csv");
			} catch (ValuesOutOfBoundsException e) {
				System.out.println(e.getMessage());
				System.out
						.println("The incremental bounds roots are out of bounds: There seems to have been a problem in the calculation of Q in the iterative evaluation of the parameters.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			} catch (RootNotBracketedException e) {
				System.out.println(e.getMessage());
				System.out
						.println("The root is not bracketed: There seems to have been a problem in the calculation of Q in the iterative evaluation of the parameters.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			} catch (MaxIterationException e) {
				System.out.println(e.getMessage());
				System.out
						.println("The system did not converge to a satisfactory solution within an acceptable number of iterations.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			} catch (SquareRootZeroException e) {
				System.out.println(e.getMessage());
				System.out
						.println("Ridder's square root is zero: There seems to have been a problem in the calculation of Q in the iterative evaluation of the parameters.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			}
			break;

		case 2:
			// T = feed temp, looking for adiabatic flash
			// temp
			feedT = temp;
			Q = 0;

			try {
				EnthalpyCalc.calculateTflash(eos, Q, feedT, tankP, flowIn)
						.toCSV(folderPath);
				System.out.println("System output has been saved in "
						+ folderPath + " as systemoutput.csv");
			} catch (ImpossibleAdiabaticFlashException e) {
				System.out.println(e.getMessage());
				System.out
						.println("The temperature and pressure of the system is such that the pressure was above or\n"
								+ "below the bubble/dew point of the system.  Therefore, an adiabatic flash is not possible in this case.\n"
								+ "In a real world process, the feed stream and the exit stream would be in the same phase, and there would be \n"
								+ "no flash operation occuring in the tank.");
				System.exit(1);
			} catch (MaxIterationException e) {
				System.out.println(e.getMessage());
				System.out
						.println("The system did not converge to a satisfactory solution within an acceptable number of iterations.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			} catch (RootNotBracketedException e) {
				System.out.println(e.getMessage());
				System.out
						.println("Root is not bracketed: There seems to have been a problem in the calculation of Q in the iterative evaluation of the parameters.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			} catch (SquareRootZeroException e) {
				System.out.println(e.getMessage());
				System.out
						.println("Square Root in Ridders Method is Zero: There seems to have been a problem in the calculation of Q in the iterative evaluation of the parameters.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			}

			break;
		case 3:
			// T = adiabatic flash temp, looking for feed temp
			flashT = temp;
			Q = 0;

			try {
				EnthalpyCalc.calculateTfeed(eos, Q, flashT, tankP, flowIn)
						.toCSV(folderPath);
				System.out.println("System output has been saved in "
						+ folderPath + " as systemoutput.csv");
			} catch (RootNotBracketedException e) {
				System.out.println(e.getMessage());
				System.out
						.println("Root is not bracketed: There seems to have been a problem in the calculation of Q in the iterative evaluation of the parameters.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			} catch (MaxIterationException e) {
				System.out.println(e.getMessage());
				System.out
						.println("The system did not converge to a satisfactory solution within an acceptable number of iterations.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			} catch (SquareRootZeroException e) {
				System.out.println(e.getMessage());
				System.out
						.println("Square Root in Ridders Method is Zero: There seems to have been a problem in the calculation of Q in the iterative evaluation of the parameters.\n"
								+ "It is possible that this system is at a temperature out of bound for proper operation of the\n"
								+ "equation of state.  If it is a non-ideal system, please try changing it to an ideal case for a more rough estimate.");
				System.exit(1);
			}

			break;
		default:
			System.out
					.println("Invalid case number, program is exiting.  Please input a valid case number into the\n"
							+ "caseParameters.csv file and run the program again.");
			System.exit(1);

		}

		rdr.close();
	}

	private int getIdeality() {
		int ideality = 0;
		Scanner rdr = new Scanner(System.in);

		while (true) {
			System.out
					.println("Do you want this simulation to be done ideally (1), or non-ideally (with the\n"
							+ "Peng Robinson equation of state) (2).  Enter the appropriate number to make the selection:");
			ideality = rdr.nextInt();
			if (ideality != 1 && ideality != 2) {
				System.out.println("Please enter a valid condition:");
			} else {
				break;
			}
		}

		rdr.close();

		return ideality;
	}

}