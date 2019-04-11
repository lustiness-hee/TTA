package Hongik.Selab.Reverse.GenDot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class GenDot {
	

	protected String Replace(String path) {
		String temp;
		temp = path.replaceAll("\\\\", "/");
		return temp;
	}

	protected String StringDeletePointerArray(String input) {
		input = input.replace("[", "");
		input = input.replace("]", "");
		input = input.replace("*", "");
		input = input.replace("&", "");
		input = input.replace(" ", "");
		input = input.replace("const", "");
		return input.trim();
	}
	
	public void genDotImage(String dot, String graphicFileName, String genDotGraphContents) {
		String dot_contents = genDotGraphContents;
		try {
			
			File temp = File.createTempFile("graph", ".dot");
			
			temp.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			out.write(dot_contents);
			out.close();

			System.out.println("dotting");
			Process p = Runtime.getRuntime().exec(dot + " -Tsvg " + temp.getAbsolutePath() + " -o " + graphicFileName);
			p.waitFor();
			System.out.println(graphicFileName + " : done");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
