import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class _main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File csv = new File(args[0]);
            BufferedReader br = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
