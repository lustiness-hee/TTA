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
 * @author ���� Xml�� �о� Vector�� ���� �ϴ� Ŭ����
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
			 * Xpath�� �̿��Ͽ� NodeList�� �����ϰ� �ش� ��ο� ���� �����͸� �ҷ��´�.
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
			 * �����͸� vector<form>�� �����Ͽ� �����ϰ� else �ƹ� ���� ���� ������ ���ο� �� vector��
			 * �����Ѵ�.
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
