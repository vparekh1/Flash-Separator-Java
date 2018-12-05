package fileimport;

import eos.Chemical;

/*Extension of the Input file I/O class.
 * Adds specific methods with their relation to chemicals and their properties
 */
public class InputChemicalSpecies extends Input {
	// Class uses folder path with ReadCSV class to read in the properties for
	// the
	// chemical species to be used in the simulation.

	// Inherits folderPath (string) instance variable from "Input"
	// Inherits accessor, mutator, copy constructor & clone methods for
	// folderPath

	// Constructor
	public InputChemicalSpecies(String folderPath) {
		super(folderPath);
	}
	
	// Copy Construtor
	public InputChemicalSpecies(InputChemicalSpecies ori) {
		super(ori);
	}
	
	// Clone method
	public InputChemicalSpecies clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new InputChemicalSpecies(this);
	}

	// Method to create an array of species, representing all of the species in
	// the system & their respective property values
	public Chemical[] speciesInfo() {
		// Each csv file is created as a new instance of ReadCSV class
		ReadSpeciesCSV prCSV = new ReadSpeciesCSV(super.getFolderPath()
				+ "/pengRobinson.csv");
		ReadSpeciesCSV enthalpyCSV = new ReadSpeciesCSV(super.getFolderPath()
				+ "/enthalpy.csv");
		ReadSpeciesCSV antoineCSV = new ReadSpeciesCSV(super.getFolderPath()
				+ "/antoine.csv");
		ReadSpeciesCSV gasCpCSV = new ReadSpeciesCSV(super.getFolderPath()
				+ "/gasCp.csv");
		ReadSpeciesCSV liqCpCSV = new ReadSpeciesCSV(super.getFolderPath()
				+ "/liqCp.csv");

		int nSpecies = prCSV.size() - 1; // Size
		Chemical[] species = new Chemical[nSpecies];

		// Check CSV files all have same number of species
		if ((enthalpyCSV.size() != nSpecies + 1)
				|| (antoineCSV.size() != nSpecies + 1)
				|| (gasCpCSV.size() != nSpecies + 1)
				|| (liqCpCSV.size() != nSpecies + 1)) {
			System.out
					.println("The CSV file sizes do not match. Please check the CSV files & try again");
		} else {
			for (int i = 0; i < nSpecies; i++) {
				// Species name
				String speciesName = prCSV.getNamesArray()[i];

				// getValuesArray()
				// Values from pengRobinson csv
				double tc = prCSV.getValuesArray()[i][0];
				double pc = prCSV.getValuesArray()[i][1];
				double omega = prCSV.getValuesArray()[i][2];

				// Values from enthalpy csv
				double h0 = enthalpyCSV.getValuesArray()[i][0];
				double tref = enthalpyCSV.getValuesArray()[i][1];
				double hvap = enthalpyCSV.getValuesArray()[i][2];
				double tboil = enthalpyCSV.getValuesArray()[i][3];
				double isGasRef = enthalpyCSV.getValuesArray()[i][4];

				// Values from antoine csv, stored in array
				double[] antoine = new double[5];
				for (int j = 0; j < 5; j++) {
					antoine[j] = antoineCSV.getValuesArray()[i][j];
				}

				// Values from GasCp csv, stored in array
				double[] cpIG = new double[6];
				for (int j = 0; j < 6; j++) {
					cpIG[j] = gasCpCSV.getValuesArray()[i][j];
				}

				// Values from LiqCp csv, stored in array
				double[] cpLQ = new double[6];
				for (int j = 0; j < 6; j++) {
					cpLQ[j] = liqCpCSV.getValuesArray()[i][j];
				}

				// Create species
				species[i] = new Chemical(speciesName, tc, pc, omega, h0, tref,
						hvap, tboil, antoine, cpIG, cpLQ, isGasRef);
			}

		}

		return species;
	}

}