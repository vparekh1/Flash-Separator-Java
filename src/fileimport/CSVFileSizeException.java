package fileimport;

//A custom exception to be thrown that relays more information to the user of what guideline was not followed while running the simulator
public class CSVFileSizeException extends Exception {

	public CSVFileSizeException(String string) {
		super(string);
	}

}
