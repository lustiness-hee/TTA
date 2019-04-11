package Hongik.Selab.Reverse.imformation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GetInformation {
	BufferedReader br;
	FileReader fr;

	public GetInformation() {
		// TODO Auto-generated constructor stub
		try {
			br = new BufferedReader(fr = new FileReader("./dbFileName.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTimeStamp() {
		String result = "";
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				result = line;
			}
			result = result.replace("recoveryDB", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String getDBname() {
		String result = "";
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				result = line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
