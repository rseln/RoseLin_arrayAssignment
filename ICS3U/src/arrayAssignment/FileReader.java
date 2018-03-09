package arrayAssignment;

import java.io.File;  
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
	
	//Rose Lin 
	//Array Assignment
	//Mr. Radulovic 
	//ICS 3U

	public static void main(String[] args) {
	}

	public String[][] getArray() {

		//initialize values
		File map = new File("src/arrayAssignment/map.txt");
		String allData = ""; //prelim value (in strings)
		String[][] mapArray; //end value (in string array)

		try {
			Scanner scan = new Scanner(map);

			//read the text file as a string
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				allData += (line + "\n");
				
			}
			scan.close();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//convert the strings into a string array
		String[] rows = allData.split("\n"); //determines where the rows are 
		int numCol = rows[0].split(" ").length; //determines the number of columns

		mapArray = new String[rows.length][numCol]; //setting the dimensions in the string array

		//loops through the values located in the rows
		for (int i = 0; i < mapArray.length; i++) { 
			String[] row = rows[i].split(" "); 
			
			//loops through all the values in the columns
			for (int j = 0; j < mapArray[0].length; j++) { 
				mapArray[i][j] = row[j]; 
			}
		}

		//puts everything together and returns 2D array 
		for (int i=0; i<mapArray.length; i++) {
			for (int j=0; j<mapArray[0].length; j++) {
				
			}
		}
		return mapArray;
	}

}
