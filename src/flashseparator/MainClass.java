package flashseparator;

import java.util.Scanner;

import fileimport.CSVFileSizeException;


//User interaction with the simulation
//Asks for user inputs and if wrong input is given, program loops. 

public class MainClass {
 public static void main(String[] args) throws CSVFileSizeException {
  
  Scanner readInput = new Scanner(System.in);
  Scanner rtn = new Scanner(System.in);
  boolean check = false;

  System.out.println("CHG4343 - Flash Separation Simulator");
  

  do {
   System.out
     .println("\nWelcome to the flash separation simulator. Please enter a letter corresponding to one of the following options:");
   System.out
     .println("Enter \"i\" for instructions on how to use this program.");
   System.out.println("Enter \"r\" to run a simulation.");

   String entry = readInput.next();
   String menu;

   if (entry.equals("i")) {

    System.out.println("\nInstructions");
    System.out
      .println("\nThis program allows users to simulate a flash separation process for three different cases. "
        + "In each case the tank pressure, feed composition, feed flowrate and a temperature must be specified. "
        + "These inputs are used to calculate the vapour and liquid product compositions and flowrates, "
        + "as well as a third parameter. The third parameter calculated depends on the user-specified temperature. "
        + "The three possible cases are described below:");
    System.out
      .println("\n\tCase 1 - Specified: constant operating temperature, Determined: heat to maintain the operating temperature."
        + "\n\tCase 2 - Specified: feed temperature, Determined: flash temperature."
        + "\n\tCase 3 - Specified: flash temperature, Determined: feed temperature.");
    System.out
      .println("\nIn addition to the specified parameters, species properties for the species in the inlet stream must also be specified. "
        + "This program has an accompanying excel file (Flash Simulation IO Form), where the species properties and system parameters are "
        + "specified. This excel file is used to generate .csv files containing all the information needed to run the program. "
        + "The program is designed to automatically read these CSV files upon running. Please refer to the excel file for instructions "
        + "pertaining to its use.");
    System.out
      .println("\nTo perform a simulation, complete the excel form & create the CSV files. Once this is complete, the simulation may be run by "
        + "entering \"r\" in the welcome menu.");

    System.out
      .println("Please enter enter \"r\" to run the simulation immediately, or enter any other letter/symbol to return to the main menu.");

    menu = rtn.next();
    if (menu.equals("r")) {
      // Code to run simulation
     RunProgram simulation = new RunProgram();
     simulation.run();
     }
    else {
     check = true;
    }
   
   } else if (entry.equals("r")) {
    // Code to run simulation
     
    RunProgram simulation = new RunProgram();
    simulation.run();
     
     
   } else {
    System.out.println("Invalid Entry. Please try again.");
    check = true;
   }
  } while (check);

  readInput.close();
  rtn.close();

 }
}
