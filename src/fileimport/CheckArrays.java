package fileimport;

/*Class made to check arrays being imported and whether or not the array has content and whether it is set up in a
*2D rectangular array.
*/ 

public class CheckArrays
{
  //No IVs, empty constructor:
  public CheckArrays(){
  }
  
  
  //Method to check if 2D array is rectangular
  public static boolean rectangle(double[][] twoDArray)
  {
    
    boolean rectangular = true;
    int nRows = twoDArray.length;
    int nCols = twoDArray[0].length;
    
    for(int i=0; i<nRows; i++)
    {
      //Check length of each row against length of the first row
      //If lengths differ, rectangular set to false
       if(twoDArray[i].length != nCols)
         rectangular = false;
    }
    
    return rectangular; 
  } 
  
  
  //Method to check if a rectangular 2D array is null (returns true if it is null, assumes passed array is rectangular)
  public static boolean checkNull(double[][] twoDArray)
  {
    
   
    boolean check = false;
    
    //Check overall array
    if(twoDArray == null) check = true;
    
    //Check each row of array
    for(int i=0; i<twoDArray[0].length; i++)
    {
      if(twoDArray[i]== null) check = true;
    }
    
    return check;
    
  }
  
  
}