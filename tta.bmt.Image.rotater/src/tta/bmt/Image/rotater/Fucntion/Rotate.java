package tta.bmt.Image.rotater.Fucntion;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Rotate {
	private String filePath = "";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath.replace("\\", "/");
	}

	public void rotate(double angle) throws IOException {
		BufferedImage bi;
		createFolder(angle);
		File dirFile = new File(filePath + "/remove/");
		File[] fileList = dirFile.listFiles();
		for (File file : fileList) {
			if (!file.isDirectory()) {
				bi = getRotateImage(ImageIO.read(file), angle);
				SaveImage(bi, file.getName(), angle);
			}
		}
	}

	private void createFolder(double angle) {
		File file = new File(filePath + "/rotate_" + angle + "/");
		if (!file.exists()) {
			file.mkdir();
			System.out.println(" Create Rotate" + angle + "Folder.");
		} else {
			System.out.println("This folder already exists.");
		}
	}

	private BufferedImage getRotateImage(BufferedImage image, double angle) {
		double _angle = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(_angle));
		double cos = Math.abs(Math.cos(_angle));
		double w = image.getWidth();
		double h = image.getHeight();
		int newW = (int) Math.floor(w * cos + h * sin);
		int newH = (int) Math.floor(w * sin + h * cos);

		BufferedImage result = new BufferedImage(newW, newH, Transparency.OPAQUE);
		Graphics2D g = result.createGraphics();

		g.translate((newW - w) / 2, (newH - h) / 2);
		g.rotate(_angle, w / 2, h / 2);
		g.drawRenderedImage(image, null);
		g.dispose();

		return result;
	}

	private void SaveImage(BufferedImage image, String name, double angle) throws IOException {
		if (name.contains(".jpg")) {
			ImageIO.write(image, "jpg", new File(filePath + "/rotate_" + angle + "/" + name));
		} else if (name.contains(".bmp")) {
			ImageIO.write(image, "bmp", new File(filePath + "/rotate_" + angle + "/" + name));
		}
	}

}
