package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file = new File("CarFuel.csv");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			Stream<String> stream = reader.lines();
			
			List<String> list = stream.collect(Collectors.toList());

			for(int i = 0; i<list.size(); i++) {
				System.out.println(list.get(i));
			}
				
			
			System.out.print(list.get(0));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
