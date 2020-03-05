package counting_valleys;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the countingValleys function below.
    static int countingValleys(int n, String s) {
    	
    	int noValley=0;
    	int currentValley=0;
    	
    	
    	for (int counter = 0; counter<n; counter+=1) {
    		
    		switch (s.charAt(counter)){
    			
    		case 'D' :{
    			
    			currentValley = currentValley-1;
    			
    		}break;
    		
    		case 'U' :{
    			
    				currentValley = currentValley+1;

    				if (currentValley==0)
    					noValley = noValley+1;
    			    			
    		}break;
    		
    		}
    	}

    	
		return noValley;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
    	//Commented Lines are used my challenge websites autochecker
    	
       // BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String s = scanner.nextLine();

        int result = countingValleys(n, s);

        
       // bufferedWriter.write(String.valueOf(result));
       // bufferedWriter.newLine();

        //bufferedWriter.close();
        System.out.println(result);
        scanner.close();
    }
}