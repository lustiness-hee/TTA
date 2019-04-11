import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import tta.bmt.Image.exif.ExifRemove;
import tta.bmt.Image.rotater.Fucntion.Rotate;

public class _Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filepath = "";
		String a = "";
		InputStream in = System.in;
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(reader);
		System.out.println("이미지 파일이 저장된 경로를 입력해주세요.");
		try {
			filepath = br.readLine();
			System.out.println(filepath);
			ExifRemove exifremove = new ExifRemove();
			Rotate r = new Rotate();
			exifremove.setFilePath(filepath);
			exifremove.removeExifTag();
			r.setFilePath(filepath);

			System.out.println("Choose one of the following.");
			System.out.println("1) 90 Degree Rotate.");
			System.out.println("2) 180 Degree Rotate.");
			System.out.println("3) 270 Degree Rotate.");
			System.out.println("4) ALL of that");

			a = br.readLine();

			switch (Integer.parseInt(a)) {
			case 1:
				r.rotate(90);
				break;
			case 2:
				r.rotate(180);
				break;
			case 3:
				r.rotate(270);
				break;
			case 4:
				r.rotate(90);
				r.rotate(180);
				r.rotate(270);
				break;
			}
			exifremove.deleteFolder();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
