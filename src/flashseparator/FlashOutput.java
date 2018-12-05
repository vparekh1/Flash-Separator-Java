package flashseparator;

import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;

//Class for how chemicals leaves the flash seperator and in what phase they are in
//Allows the outlet stream to be easily handled. 

public class FlashOutput {
	double[] yI;
	double[] xI;
	double[] zI;
	double L, V, F;
	double Q, flashT, feedT;

	//Constructor
	public FlashOutput(double[] zI, double[] yI, double[] xI, double l,
			double v, double f, double q, double flashT, double feedT) {
		super();
		this.yI = yI;
		this.xI = xI;
		this.L = l;
		this.V = v;
		this.F = f;
		this.Q = q;
		this.flashT = flashT;
		this.feedT = feedT;
	}

	// Copy constructor
	public FlashOutput(FlashOutput ori) {
		this.yI = ori.yI;
		this.xI = ori.xI;
		L = ori.L;
		V = ori.V;
		F = ori.F;
		Q = ori.Q;
		this.flashT = ori.flashT;
		this.feedT = ori.feedT;
	}
	
	// Clone method
	public FlashOutput clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new FlashOutput(this);
	}
	
	//Mutators and Accessors 
	public double[] getyI() {
		return yI;
	}

	public void setyI(double[] yI) {
		this.yI = yI;
	}

	public double[] getxI() {
		return xI;
	}

	public void setxI(double[] xI) {
		this.xI = xI;
	}

	public double getL() {
		return L;
	}

	public void setL(double l) {
		L = l;
	}

	public double getV() {
		return V;
	}

	public void setV(double v) {
		V = v;
	}

	public double getF() {
		return F;
	}

	public void setF(double f) {
		F = f;
	}

	public double getQ() {
		return Q;
	}

	public void setQ(double q) {
		Q = q;
	}

	public double getFlashT() {
		return flashT;
	}

	public void setFlashT(double flashT) {
		this.flashT = flashT;
	}

	public double getFeedT() {
		return feedT;
	}

	public void setFeedT(double feedT) {
		this.feedT = feedT;
	}

	//To string overwrite
	public String toString() {
		return String
				.format("FlashOutput [yI=%s, xI=%s, L=%s, V=%s, F=%s, Q=%s, flashT=%s, feedT=%s]",
						Arrays.toString(yI), Arrays.toString(xI), L, V, F, Q,
						flashT, feedT);
	}

	//To create a CSV file given a folderpath
	public void toCSV(String folderPath) {

	
		try {

			// If file cannot be created for some reason, the following code
			// will throw a FileNotFoundException
			File outputFile = new File(folderPath + "/systemoutput.csv");
			PrintWriter printOutput = new PrintWriter(outputFile);

			// A. Print the headings in the csv file as: Tflash,Tfeed,Q,F,V, L,
			// y1...yn, x1....xn
			String headings = "Tflash,Tfeed,Q,F,V,L";
			int yILength = this.yI.length;
			int xILength = this.xI.length;

			// Check that xIs & yIs arrays are same length, if not, throw
			// exception
			if (yILength == xILength) {
				for (int i = 1; i < yILength + 1; i++) {
					headings += ",y" + Integer.toString(i);
				}

				for (int i = 1; i < xILength + 1; i++) {
					headings += ",x" + Integer.toString(i);
				}
			} else {
				printOutput.close();
				throw new Exception(
						"Error: output streams (xi & yi arrays) do not contain same number of species");
			}

			printOutput.println(headings);

			// B. Print the values (next row) in same order as headings
			String values = Double.toString(this.flashT) + ","
					+ Double.toString(this.feedT) + ","
					+ Double.toString(this.Q) + "," + Double.toString(this.F)
					+ "," + Double.toString(this.V) + ","
					+ Double.toString(this.L);

			for (int i = 0; i < yILength; i++) {
				values += "," + Double.toString(this.yI[i]);
			}

			for (int i = 0; i < xILength; i++) {
				values += "," + Double.toString(this.xI[i]);
			}

			printOutput.println(values);

			printOutput.close();

		} catch (FileNotFoundException e) {
			System.out
					.println("\n Error: output CSV file could not be created. Please try running simulation again.");
			System.exit(1);
		} catch (Exception e) {
			System.out.println("\n" + e.getMessage());
			System.exit(1);
		}

	}

}
