package Hongik.Selab.Reverse.Main;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;

import Hongik.Selab.Reverse.DB.DB;
import Hongik.Selab.Reverse.Documentation.DocumentSelection;
import Hongik.Selab.Reverse.Documentation.SWProjectPlanExtractor;
import Hongik.Selab.Reverse.Documentation.PDFgenerator;
import Hongik.Selab.Reverse.Documentation.SWStructureAnalysisReport;
import Hongik.Selab.Reverse.Documentation.Excel.ExportExcel;
import Hongik.Selab.Reverse.GenDot.ClassDiagramDrawer;
import Hongik.Selab.Reverse.GenDot.GenerateDot;
import Hongik.Selab.Reverse.GenDot.GenerateDot_Java;
import Hongik.Selab.Reverse.GenDot.Generate_performance_view;
import Hongik.Selab.Reverse.TransFiles.TransSvgToPng;
import Hongik.Selab.Reverse.Xml.XmlExport;
import Hongik.Selab.Reverse.Xml.XmlImport;
import Hongik.Selab.Reverse.Xml.Performance.ImportXml;
import Hongik.Selab.Reverse.imformation.GetInformation;


public class GenerateContents_Main extends Thread {
	public static void main(String[] args)  {
		
		String timeStamp = new GetInformation().getTimeStamp();
		//System.out.println(timeStamp);
		DB db = new DB(new GetInformation().getDBname());
		try {
			db.init();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String arg2 = "";
		String arg3 = "";
		String arg4 = "C";
		
		if (args.length == 2) {
			arg2 = args[1].toString();
		} else if (args.length > 2) {
			arg2 = args[1].toString();
			arg3 = args[2].toString();
			arg4 = args[3].toString();
		}
		
		
		try {
			new ImportXml(db).getData(timeStamp);
			//new Generate_performance_view(args[0], "./", db).Draw();;
			if(arg4.equalsIgnoreCase("C")){
				GenerateDot gd = new GenerateDot(args[0].toString(), arg2, timeStamp, db, arg3);
				gd.genArchitenture_Class();
			}else{
				GenerateDot_Java gd_j = new GenerateDot_Java(args[0].toString(), arg2, timeStamp, db, arg3);
				gd_j.genArchitenture_Class();
			}
			//new XmlExport(new File("./Xml/output.xml"),db, timeStamp, new XmlImport().Read("./Xml/output.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//ClassDiagramDrawer cd = new ClassDiagramDrawer(db, timeStamp);
			//cd.generateClassdiagram();
			System.out.println("Image file Transform start");
			File f = new File("./img/");
			File[] fileList = f.listFiles();
			for(int i =0;i<fileList.length;i++){
				if(fileList[i].getName().contains("svg")){
					new TransSvgToPng("./img/"+fileList[i].getName().split("\\.")[0]);
				}
			}
			System.out.println("Image file Transform end");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

