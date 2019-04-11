package FingerPrint;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import File.FiletoByte;

public class SHA256Hash {
	String Filename;
	
	public void setFile(String Filename){
		this.Filename = Filename;
	}

	public String getSHA256() {
		String SHA = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			File f = new File(Filename);

			digest.update(new FiletoByte().getBytesFromFile(f));
			byte byteData[] = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();

		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			SHA = null;
		}
		return SHA;
	}

}
