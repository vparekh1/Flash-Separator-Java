package fileimport;

import java.util.Arrays; //to use copyOf method for arrays

//Class reads CSV files with species properties, converts to String[] array with names 
//& a double[][] array with property values

//Extends ReadCSV (which extends Input) --> inherits pathString instance variable, & its accessors, mutators, copy constructor & clone methods

public class ReadSpeciesCSV extends ReadCSV {
	// Instance variables
	private String[] namesArray;
	private double[][] valuesArray;

	// Constructor
	public ReadSpeciesCSV(String pathString) {

		// Creates array that is size of CSV file found at pathString,
		// uses readCSV method to fill array with each line of CSV file
		super(pathString);
		int pathStringSize = super.size();
		String[] stringArray = new String[pathStringSize];
		stringArray = super.readCSV(pathString);

		// Split each line of the CSV file into the individual string type
		// values
		// (stringArray will become 2D array splitStrings)
		String[][] splitStrings = new String[pathStringSize][];
		for (int i = 0; i < pathStringSize; i++) {
			splitStrings[i] = stringArray[i].split(",");
		}

		// Size namesArray & valuesArray based on size of CSV file (number of
		// rows) & size of splitStrings array
		this.namesArray = new String[pathStringSize - 1];
		this.valuesArray = new double[pathStringSize - 1][splitStrings[0].length - 1];

		// Convert string values in splitStrings to double values to be assigned
		// in valueArrays
		// Starting iteration at 1 because first line is just headers
		for (int i = 1; i < pathStringSize; i++) {
			namesArray[i - 1] = splitStrings[i][0];
			for (int j = 1; j < splitStrings[0].length; j++) {
				valuesArray[i - 1][j - 1] = Double
						.parseDouble(splitStrings[i][j]);
			}
		}
	}
	
	// Copy Constructor
	public ReadSpeciesCSV(ReadSpeciesCSV ori) {

		// Creates array that is size of CSV file found at pathString,
		// uses readCSV method to fill array with each line of CSV file
		super(ori);
		int pathStringSize = ori.size();
		String[] stringArray = new String[pathStringSize];
		stringArray = ori.readCSV(ori.getPathString());

		// Split each line of the CSV file into the individual string type
		// values
		// (stringArray will become 2D array splitStrings)
		String[][] splitStrings = new String[pathStringSize][];
		for (int i = 0; i < pathStringSize; i++) {
			splitStrings[i] = stringArray[i].split(",");
		}

		// Size namesArray & valuesArray based on size of CSV file (number of
		// rows) & size of splitStrings array
		this.namesArray = new String[pathStringSize - 1];
		this.valuesArray = new double[pathStringSize - 1][splitStrings[0].length - 1];

		// Convert string values in splitStrings to double values to be assigned
		// in valueArrays
		// Starting iteration at 1 because first line is just headers
		for (int i = 1; i < pathStringSize; i++) {
			namesArray[i - 1] = splitStrings[i][0];
			for (int j = 1; j < splitStrings[0].length; j++) {
				valuesArray[i - 1][j - 1] = Double
						.parseDouble(splitStrings[i][j]);
			}
		}
	}
	
	// Clone method
	public ReadSpeciesCSV clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new ReadSpeciesCSV(this);
	}
	
	// Accessors
	public String[] getNamesArray() {
		String[] copyArray = Arrays.copyOf(this.namesArray,
				this.namesArray.length);
		return copyArray;
	}

	public double[][] getValuesArray() {

		double[][] copyArray = new double[this.valuesArray.length][this.valuesArray[0].length];

		for (int i = 0; i < this.valuesArray.length; i++) // Loop through rows
															// (species)
		{
			for (int j = 0; j < this.valuesArray[0].length; j++) // Loop through
																	// columns
																	// (values)
			{
				copyArray[i][j] = this.valuesArray[i][j];
			}
		}

		return copyArray;
	}

	// Mutators: 2 overloaded mutators for changing (i)One species info or
	// (ii)All species info
	// Note: setPathString mutator (used to change CSV file used as input) is
	// inherited from grandparent class "Input"

	// (i)
	public boolean setCharacteristics(String newName, double[] newValues, int n)// n
																				// is
																				// the
																				// species
																				// number
																				// in
																				// current
																				// list
																				// (0
																				// to
																				// #species-1)
	{

		// Check that newValues array is correct size, current valuesArray is
		// rectangular, newName and newValues aren't null,
		// & that species n exists in the current valuesArray
		if ((newValues.length != this.valuesArray[0].length)
				|| (!CheckArrays.rectangle(this.valuesArray))
				|| (newName == null) || (newName == "") || (newValues == null)
				|| (n >= this.valuesArray.length)) {
			return false;
		} else {
			this.namesArray[n] = newName;
			for (int i = 0; i < this.valuesArray[0].length; i++) {
				this.valuesArray[n][i] = newValues[i];
			}
			return true;
		}
	}

	// (ii)
	public boolean setCharacteristics(String[] newNames, double[][] newValues) {
		// Check that newNames & newValues are correct size, that current
		// valuesArray & newValues are rectangular arrays,
		// and that newNames & newValues are not null
		if ((newNames.length != newValues.length)
				|| (newNames.length != this.namesArray.length)
				|| (newValues.length != this.valuesArray.length)
				|| (newValues[0].length != this.valuesArray[0].length)
				|| (!CheckArrays.rectangle(this.valuesArray))
				|| (!CheckArrays.rectangle(newValues)) || (newNames == null)
				|| (CheckArrays.checkNull(newValues))) {
			return false;
		} else {
			for (int i = 0; i < newValues.length; i++) {
				this.namesArray[i] = newNames[i];
				for (int j = 0; j < newValues[0].length; j++) {
					this.valuesArray[i][j] = newValues[i][j];
				}
			}
			return true;
		}

	}
}
