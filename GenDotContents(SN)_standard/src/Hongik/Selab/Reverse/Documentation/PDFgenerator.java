package Hongik.Selab.Reverse.Documentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import SELab.RedMineMySQLConnector.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFgenerator {
	String date;
	int command;
	BaseFont mainBaseFont;
	Font mainFont;

	public PDFgenerator(String date, int command) {
		this.date = date;
		this.command = command;
		try {
			mainBaseFont = BaseFont.createFont(".settings/font/malgun.ttf",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			mainFont = new Font(mainBaseFont);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		generate(command);
	}

	private void generate(int command) {
		genDirectory();
		/*
		 * Command에 따라 생성하는 파일이 다름.
		 */
		switch (command) {
		case DocumentSelection.AnalysisReportStructure:
			genAnalysisReportStructure();
			break;
		case DocumentSelection.ProjectProgressReport:
			genProjectProgressReport();
			break;
		case DocumentSelection.Workforceplan:
			genWorkForcePlan();
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		default:
			genTestPage();
			break;
		}
	}

	private void genWorkForcePlan() {
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"./output/pdf/" + date + "/WorkForcePlan(" + date + ").pdf"));
			document.open(); 
			Documents doc = new RedmineMySQLConnector().getDocumentsData(1, "인력관리 계획");
			PdfPTable mainTable = new PdfPTable(4);
			mainTable.addCell("test");
			document.add(new Paragraph("Hello World!"));
			document.add(mainTable);
			document.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void genTestPage() {
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"./output/pdf/" + date + "/test_" + date + ".pdf"));
			document.open();
			PdfPTable mainTable = new PdfPTable(4);
			mainTable.addCell("test");
			document.add(new Paragraph("Hello World!"));
			document.add(mainTable);
			document.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private void genAnalysisReportStructure() {
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"./output/pdf/" + date + "/SWStructAnalysisReport_(" + date
							+ ").pdf"));
			document.open();
			PdfPTable mainTable = new PdfPTable(4);
			mainTable.addCell("test");
			document.add(new Paragraph("Hello World!"));
			document.add(mainTable);
			document.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private void genProjectProgressReport() {
		Document document = new Document(PageSize.A4);
		try {
			File file = new File("./output/pdf/" + date
					+ "/ProjectProgressReport_(" + date + ").pdf");

			PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();

			Paragraph Title = new Paragraph("프로젝트 진척 보고서\n\n\n", mainFont);
			Title.setAlignment(Element.ALIGN_CENTER);

			document.add(Title);

			PdfPTable mainTable = new PdfPTable(4);
			mainTable.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell("test");
			mainTable.addCell("test");
			mainTable.addCell("test");
			mainTable.addCell("test");

			document.add(mainTable);

			document.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private void genDirectory() {
		File file = new File("./output/pdf/" + date + "/");
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
