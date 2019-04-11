package FingerPrint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import File.FiletoByte;

public class MD5Hash {
	String Filename;

	public void setFile(String Filename) {
		this.Filename = Filename;
	}

	public String getMD5() {
		String MD5 = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			File f = new File(Filename);

			digest.update(new FiletoByte().getBytesFromFile(f));

			byte byteData[] = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MD5;
	}

	public byte[] getBytesFromFile(File file) throws IOException {
		FileInputStream is = new FileInputStream(file);
		// FileChannel fc = is.getChannel();
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			return null;
		}

		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

}
