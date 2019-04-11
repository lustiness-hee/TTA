package File;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FiletoByte {
	
	public byte[] getBytesFromFile(File file) throws IOException {
	     FileInputStream is = new FileInputStream(file);
	     //FileChannel fc = is.getChannel();
	     long length = file.length();

	     if (length > Integer.MAX_VALUE) {
	         return null;
	     }

	     byte[] bytes = new byte[(int)length];
	     int offset = 0;
	     int numRead = 0;
	     while (offset < bytes.length
	            && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	         offset += numRead;
	     }
	     // Ensure all the bytes have been read in
	     if (offset < bytes.length) {
	         throw new IOException("Could not completely read file "+file.getName());
	     }
	     // Close the input stream and return bytes
	     is.close();
	     return bytes;
	 }
	
}
