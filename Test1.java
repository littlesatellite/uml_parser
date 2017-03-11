package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Test1 {

	public static void main(String[] args) {
		try {
			File folder = new File("/Users/jinchengcheng/Documents/workspace/uml_parser/src/uml_parser_test1/");
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				File file = listOfFiles[i];
				if (file.isFile() && file.getName().endsWith(".java")) {
					String path = file.getAbsolutePath();
					BufferedReader br = new BufferedReader(new FileReader(path));
					String line;
					
					while ((line = br.readLine()) != null) {
						String[] tokens = line.split("[ \t]");

						for(String str : tokens) {
							System.out.println(str);
						}
					}
					br.close();
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
