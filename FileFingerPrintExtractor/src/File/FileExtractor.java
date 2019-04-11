package File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExtractor {
	String Filename = "";
	String contents = "";
	
	public void setPath(String Filename){
		this.Filename = Filename;
	}
	
	public void add(String data){
		contents += data+"\n";
	}
	
	public String getContents(){
		return contents;
	}
	
	public void Write(){
		try {
			FileWriter fw = new FileWriter(Filename);
			fw.write(contents);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
