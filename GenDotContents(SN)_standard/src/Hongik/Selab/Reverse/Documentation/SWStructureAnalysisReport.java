package Hongik.Selab.Reverse.Documentation;

import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import Hongik.Selab.Reverse.Xml.Form;
import Hongik.Selab.Reverse.Xml.XmlImport;
import SELab.RedMineMySQLConnector.Message;
import SELab.RedMineMySQLConnector.RedmineMySQLConnector;


public class SWStructureAnalysisReport {
	String Date;
	RedmineMySQLConnector conn = new RedmineMySQLConnector();
	
	public SWStructureAnalysisReport(String Date) {
		// TODO Auto-generated constructor stub
		this.Date = Date;
	}
	
	public void createSWStructureAnalysis() throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		doc.setXmlStandalone(true);
		ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"sw_structure_analysis.xsl\"");
		
		Element list = doc.createElement("list");
		
		
		Element info = doc.createElement("info");
		Element Date_info = doc.createElement("date");
		Date_info.setTextContent(Date);
		info.appendChild(Date_info);
		
		Element department = doc.createElement("department");
		department.setTextContent(conn.getProjectName(1));
		info.appendChild(department);
		
		LinkedList<Message> m = conn.getMessage(26);
		Element modify = doc.createElement("modify");
		modify.setTextContent(m.get(m.size()-1).getContent().split(";")[0]);
		info.appendChild(modify);
		
		Element standard = doc.createElement("standard");
		Vector<Form> v = new XmlImport().Read("./xml/output.xml");
		Form f = v.get(v.size()-1);
		standard.setTextContent("Style : "+f.getStyle()+"\n Warning : " + f.getWarning()+ "\n Performance : "+f.getPerformance());
		info.appendChild(standard);
		
		Element quality = doc.createElement("quality");
		quality.setTextContent(f.getCoupling());
		info.appendChild(quality);
		
		Element etc = doc.createElement("etc");
		etc.setTextContent(m.get(m.size()-1).getContent().split(";")[1]);
		info.appendChild(etc);
		list.appendChild(info);
		
		Element image = doc.createElement("image");
		Element classdiagram = doc.createElement("classdiagram");
		classdiagram.setTextContent("http://203.249.87.143:13000/jenkins/userContent/SWV1/classdiagram("+Date+").png");
		image.appendChild(classdiagram);
		Element callgraph = doc.createElement("callgraph");
		callgraph.setTextContent("http://203.249.87.143:13000/jenkins/userContent/SWV1/Architecture(class)("+Date+").png");
		image.appendChild(callgraph);
		
		list.appendChild(image);
		doc.appendChild(list);
		doc.insertBefore(pi, list);
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("./SWStructureAnalysis_"+Date+".xml"));
 
		transformer.transform(source, result);
	}
}
