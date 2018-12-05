package fileimport;

import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.IOException;

//Class reads CSV file, converts each line to string, & returns an array of Strings holding these string values

public class ReadCSV {

	private String pathString;

	// Constructor
	public ReadCSV(String pathString) {
		this.pathString = pathString;
	}
	
	// Copy Constructor
	public ReadCSV(ReadCSV ori) {
		this.pathString = ori.pathString;
	}
	
	// Clone method
	public ReadCSV clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new ReadCSV(this);
	}

	// Accessor
	public String getPathString() {
		return this.pathString;
	}

	// Mutator
	// Named setCharacteristics, because it will be overloaded mutator in child
	// classes
	public boolean setCharacteristics(String pathString) {
		// Check that passed path is not empty string
		if ((pathString == null) || (pathString == "")) {
			return false;
		} else {
			this.pathString = pathString;
			return true;
		}
	}

	// Method returns the size of the CSV file (number of rows, including row of
	// headings)
	public int size() {
		int n = 0;
		try {
			List<String> lines = Files.readAllLines(Paths.get(this.pathString),
					StandardCharsets.UTF_8);
			n = lines.size();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return n;
	}

	// Method reads the CSV file, converts each line of the CSV file to a single
	// string & returns as an array of Strings
	public String[] readCSV(String pathString) {

		String[] stringArray = new String[this.size()];

		try {
			List<String> lines = Files.readAllLines(Paths.get(pathString),
					StandardCharsets.UTF_8);

			stringArray = lines.toArray(stringArray);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return stringArray;
	}

}