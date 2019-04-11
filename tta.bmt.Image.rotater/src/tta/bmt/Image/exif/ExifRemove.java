package tta.bmt.Image.exif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ExifRemove {
	private String filePath = "";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath.replace("\\", "/");
	}

	public void removeExifTag() {
		createFolder();
		File dirFile = new File(filePath);
		File[] fileList = dirFile.listFiles();
		for (File file : fileList) {
			remove(file);
		}
		System.out.println("Exif Tag Remove");
	}

	private void remove(File file) {
		BufferedImage image;
		try {
			if (file.getName().contains(".jpg")) {
				image = ImageIO.read(file);
				image.getScaledInstance(image.getWidth(), image.getHeight(), image.SCALE_SMOOTH);
				ImageIO.write(image, "jpg", new File(filePath + "/remove/" + file.getName()));
			} else if (file.getName().contains(".jpg")) {
				image = ImageIO.read(file);
				ImageIO.write(image, "bmp", new File(filePath + "/remove/" + file.getName()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createFolder() {
		File file = new File(filePath + "/Remove");
		if (!file.exists()) {
			file.mkdir();
			System.out.println(" Create EXIF TAG Remove Folder.");
		} else {
			System.out.println("This folder already exists.");
		}
	}

	public void deleteFolder() {
		File dirFile = new File(filePath+"/remove/");
		File[] fileList = dirFile.listFiles();
		for (File file_ : fileList) {
			file_.delete();
		}
		if (dirFile.exists()) {
			dirFile.delete();
			System.out.println("Temp Folder Delete.");
		} else {
			System.out.println("The folder does not exist.");
		}
	}
}
