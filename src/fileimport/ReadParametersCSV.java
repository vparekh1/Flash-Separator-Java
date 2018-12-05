package fileimport;

//Class reads CSV file with process parameters, converts values into double[] array holding these parameters
//Info order in parameters[]: Ptank, Temp, Fin, Inlet mass fractions (z1... zn) --> Temp given depends on case

public class ReadParametersCSV extends ReadCSV {

	// inherits pathString instance variable from ReadCSV
	private double[] parameters;

	// Constructor
	public ReadParametersCSV(String pathString) {

		// Uses readCSV method to fill array with parameter values in CSV file
		super(pathString + "/caseParameters.csv");
		String csvLine = super.readCSV(pathString + "/caseParameters.csv")[1]; // Just
																				// read
																				// line
																				// with
																				// parameter
																				// values

		// Split line of the CSV file into the individual string type values
		String[] parameterStrings = csvLine.split(",");

		// Size parametersArray based on size of of splitStrings array
		this.parameters = new double[parameterStrings.length];

		// Convert string values in parameterStrings to double values in
		// parametersArray
		for (int i = 0; i < parameterStrings.length; i++) {

			parameters[i] = Double.parseDouble(parameterStrings[i]);
		}
	}
	
	// Copy Constructor
	public ReadParametersCSV(ReadParametersCSV ori) {

		// Uses readCSV method to fill array with parameter values in CSV file
		super(ori);
		String csvLine = ori.readCSV(ori.getPathString() + "/caseParameters.csv")[1]; // Just
																				// read
																				// line
																				// with
																				// parameter
																				// values

		// Split line of the CSV file into the individual string type values
		String[] parameterStrings = csvLine.split(",");

		// Size parametersArray based on size of of splitStrings array
		this.parameters = new double[parameterStrings.length];

		// Convert string values in parameterStrings to double values in
		// parametersArray
		for (int i = 0; i < parameterStrings.length; i++) {

			parameters[i] = Double.parseDouble(parameterStrings[i]);
		}
	}
	
	// Clone method
	public ReadParametersCSV clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new ReadParametersCSV(this);
	}

	// Accessor
	public double[] getParameters() {
		double[] copyArray = new double[this.parameters.length];

		for (int i = 0; i < parameters.length; i++) {
			copyArray[i] = parameters[i];
		}

		return copyArray;
	}

	// Mutator
	public boolean setParameters(double[] newParameters) {
		// TODO exception
		if ((newParameters == null)
				|| (newParameters.length != this.parameters.length)) {
			return false;
		} else {
			for (int i = 0; i < 3; i++) {
				this.parameters[i] = newParameters[i];
			}
			return true;
		}
	}

}