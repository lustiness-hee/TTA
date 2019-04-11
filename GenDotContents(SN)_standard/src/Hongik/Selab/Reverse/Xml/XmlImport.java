package Hongik.Selab.Reverse.Xml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author 건희 Xml을 읽어 Vector로 저장 하는 클래스
 */

public class XmlImport {

	public Vector<Form> Read(String filepath) throws Exception {
		File file = new File(filepath);

		if (file.exists()) {
			FileReader fr = new FileReader(new File(filepath));
			InputSource is = new InputSource(fr);
			Document document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(is);
			Vector<Form> v = new Vector<Form>();

			XPath xpath = XPathFactory.newInstance().newXPath();
			/*
			 * Xpath를 이용하여 NodeList를 구성하고 해당 경로에 대한 데이터를 불러온다.
			 */
			NodeList nl1 = (NodeList) xpath.evaluate("//cycle_list/cycle/no",
					document, XPathConstants.NODESET);
			NodeList nl2 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/coupling", document,
					XPathConstants.NODESET);
			NodeList nl3 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/coding_rule", document,
					XPathConstants.NODESET);
			NodeList nl4 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/file_name", document,
					XPathConstants.NODESET);
			NodeList nl5 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/style", document,
					XPathConstants.NODESET);
			NodeList nl6 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/warning", document,
					XPathConstants.NODESET);
			NodeList nl7 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/performance", document,
					XPathConstants.NODESET);
			NodeList nl8 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/total_line", document,
					XPathConstants.NODESET);
			NodeList nl9 = (NodeList) xpath.evaluate(
					"//cycle_list/cycle/time_stamp", document,
					XPathConstants.NODESET);
			/*
			 * 데이터를 vector<form>에 저장하여 리턴하고 else 아무 값도 없을 때에는 새로운 빈 vector를
			 * 리턴한다.
			 */
			for (int i = 0; i < nl1.getLength(); i++) {
				v.add(new Form(nl1.item(i).getTextContent(), nl2.item(i)
						.getTextContent(), nl3.item(i).getTextContent(), nl4
						.item(i).getTextContent(),
						nl5.item(i).getTextContent(), 
						nl6.item(i).getTextContent(), 
						nl7.item(i).getTextContent(),
						nl8.item(i).getTextContent(),
						nl9.item(i).getTextContent()));
			}
			return v;
		} else {
			return new Vector<Form>();
		}
	}
}
