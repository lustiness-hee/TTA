package Hongik.Selab.Reverse.Xml.Performance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Hongik.Selab.Reverse.DB.DB;


public class ImportXml {
	DB db;
	public ImportXml(DB db) {
		// TODO Auto-generated constructor stub
		this.db = db;
	}
	public void getData(String Timestamp){
		Vector<Data> data = new Vector<Data>();
		FileReader fr;
		
		File file = new File("./performance_measure.xml");
		if(file.exists()){
			
			try {
				fr = new FileReader(file);
				InputSource is = new InputSource(fr);
				Document document = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().parse(is);
				XPath xpath = XPathFactory.newInstance().newXPath();
				NodeList nl1 = (NodeList) xpath.evaluate("//performance_list/performance/method_name",
						document, XPathConstants.NODESET);
				NodeList nl2 = (NodeList) xpath.evaluate("//performance_list/performance/self", 
						document, XPathConstants.NODESET);
				NodeList nl3 = (NodeList) xpath.evaluate("//performance_list/performance/accum", 
						document, XPathConstants.NODESET);
				NodeList nl4 = (NodeList) xpath.evaluate("//performance_list/performance/count", 
						document, XPathConstants.NODESET);
				System.out.println(nl1.getLength());
				for (int i = 0; i < nl1.getLength(); i++) {
					System.out.println("asdasd");
					data.add(new Data(nl1.item(i).getTextContent(), Float.parseFloat(nl2.item(i).getTextContent()), 
							Float.parseFloat(nl3.item(i).getTextContent()), Integer.parseInt(nl4.item(i).getTextContent())));
					System.out.println(data.get(i).getMethod_name());
				}
				
				db.insertTimes(data,Timestamp);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
