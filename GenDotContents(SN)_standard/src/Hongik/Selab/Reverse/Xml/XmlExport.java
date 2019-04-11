package Hongik.Selab.Reverse.Xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import Hongik.Selab.Reverse.DB.DB;
import Hongik.Selab.Reverse.QualityProperties.Calculate.Coupling;

/**
 * 
 * @author ����
 *
 *         Xml �� ��½�Ű������ ExportŬ����
 * 
 */

public class XmlExport {
	private DB db ;
	private File f;
	private Vector<Form> v;
	private String timestamp;
	public XmlExport(File filepath,DB db,String timestamp ,Vector<Form> v) throws Exception {
		// TODO Auto-generated constructor stub
		this.f = filepath;
		this.v = v;
		this.db = db;
		this.timestamp = timestamp;
		Export();
	}

	private void Export() throws Exception {
		Document document = new Document();
		Element root = new Element("cycle_list");
		/*
		 * �Է½� ���� Vector�� ������� ���� �� ���� XML����
		 */

		if (!v.isEmpty()) {
			for (int i = 0; i < v.size(); i++) {
				
				Element cycle = new Element("cycle");
				Element no = new Element("no");
				Element coupling = new Element("coupling");
				Element coding_rule = new Element("coding_rule");
				Element file_name = new Element("file_name");
				Element style = new Element("style");
				Element warning = new Element("warning");
				Element performance = new Element("performance");
				Element total_line = new Element("total_line");
				Element time_stamp = new Element("time_stamp");
				
				no.setText(v.get(i).getNo());
				coupling.setText(v.get(i).getCoupling());
				coding_rule.setText(v.get(i).getMethod_line());
				file_name.setText(v.get(i).getFile_name());
				style.setText(v.get(i).getStyle());
				warning.setText(v.get(i).getWarning());
				performance.setText(v.get(i).getPerformance());
				total_line.setText(v.get(i).getTotal_line());
				time_stamp.setText(v.get(i).getTime_stamp());

				cycle.addContent(no);
				cycle.addContent(coupling);
				cycle.addContent(coding_rule);
				cycle.addContent(file_name);
				cycle.addContent(style);
				cycle.addContent(warning);
				cycle.addContent(performance);
				cycle.addContent(total_line);
				cycle.addContent(time_stamp);

				root.addContent(cycle);
			}
		}
		/*
		 * ���� �̾Ƴ� ��� �߰�. <���� ������ �ƹ��͵� ���ٸ� ���� �� ��Ȳ�� ����>
		 */
		Element cycle = new Element("cycle");
		Element no = new Element("no");
		Element coupling = new Element("coupling");
		Element coding_rule = new Element("coding_rule");
		Element file_name = new Element("file_name");
		Element style = new Element("style");
		Element warning = new Element("warning");
		Element performance = new Element("performance");
		Element total_line = new Element("total_line");
		Element time_stamp = new Element("time_stamp");
		
		no.setText("" + (v.size() + 1));
		coupling.setText("" + getCoupling());
		coding_rule.setText("" + getMethod_line());
		file_name.setText("Architecture(class)(" + timestamp + ").svg");
		style.setText("" + getStyle());
		warning.setText("" + getWarning());
		performance.setText("" + getPerformance());
		total_line.setText(""+getTotal_line());
		time_stamp.setText(timestamp);
		
		cycle.addContent(no);
		cycle.addContent(coupling);
		cycle.addContent(coding_rule);
		cycle.addContent(file_name);
		cycle.addContent(style);
		cycle.addContent(warning);
		cycle.addContent(performance);
		cycle.addContent(total_line);
		cycle.addContent(time_stamp);
		
		root.addContent(cycle);

		document.setRootElement(root);

		XMLOutputter xout = new XMLOutputter();
		Format fo = xout.getFormat();
		fo.setEncoding("UTF-8"); // �ѱ����ڵ�
		fo.setIndent("\t");// �鿩����
		fo.setLineSeparator("\r\n");// �ٹٲ�
		fo.setTextMode(Format.TextMode.TRIM);

		try {
			xout.setFormat(fo);
			xout.output(document, new FileWriter(f));
			System.out.println("end");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * �����ǥ�� ����ϱ����� Method
	 */
	
	private int getTotal_line() throws Exception {
		int result = 0;
		Connection conn;

		conn = db.getConnection();
		Statement stat = conn.createStatement();
		String query = "select sum(end_position-start_position) as total from SymbolsOfFiles";
		ResultSet rs = stat.executeQuery(query);
		
		while (rs.next()) {
			result = (int)rs.getFloat("total");
		}
		rs.close();
		return result;	
	}

	private int getPerformance() {
		// TODO Auto-generated method stub
		int value = 0;
		String b;
		FileReader fr;
		FileWriter fw; 
		try {
			fr = new FileReader("./err.txt");
			fw = new FileWriter("./error/err_performance_"+timestamp+".txt");
			BufferedReader br = new BufferedReader(fr);

			while ((b = br.readLine()) != null) {
				if (b.contains("(performance)")) {
					value++;
					fw.write(b+"\r\n");
				}
			}
			fw.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	private int getWarning() {
		// TODO Auto-generated method stub
		int value = 0;
		String b;
		FileReader fr;
		FileWriter fw; 
		try {
			fr = new FileReader("./err.txt");
			fw = new FileWriter("./error/err_warning_"+timestamp+".txt");
			BufferedReader br = new BufferedReader(fr);

			while ((b = br.readLine()) != null) {
				if (b.contains("(warning)")) {
					value++;
					fw.write(b+"\r\n");
				}
			}
			fw.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	private int getStyle() {
		// TODO Auto-generated method stub
		int value = 0;
		String b;
		FileReader fr;
		FileWriter fw; 
		try {
			fr = new FileReader("./err.txt");
			fw = new FileWriter("./error/err_style_"+timestamp+".txt");
			BufferedReader br = new BufferedReader(fr);

			while ((b = br.readLine()) != null) {
				if (b.contains("(style)")) {
					value++;
					fw.write(b+"\r\n");
				}
			}
			fw.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	private int getCoupling() throws Exception {

		int dat, stmp, con, ext, com, cont, result = 0;
		Coupling cou = new Coupling();
		Connection conn = db.getConnection();
		Statement stat = conn.createStatement();
		String query = "select sum(dat_count),sum(stmp_count),sum(con_count),sum(ext_count),sum(com_count),sum(cont_count) from temp_link";
		ResultSet rs = stat.executeQuery(query);

		while (rs.next()) {
			dat = rs.getInt("sum(dat_count)");
			stmp = rs.getInt("sum(stmp_count)");
			con = rs.getInt("sum(con_count)");
			ext = rs.getInt("sum(ext_count)");
			com = rs.getInt("sum(com_count)");
			cont = rs.getInt("sum(cont_count)");
			result = cou.sum(dat, stmp, con, ext, com, cont);
		}
		return result;
	}

	private int getMethod_line() throws Exception {

		int result = 0;
		Connection conn = db.getConnection();
		Statement stat = conn.createStatement();
		String query = "select count(class) from SymbolsOfFiles where type = 'mi' and (end_position-start_position) > 25";
		ResultSet rs = stat.executeQuery(query);

		while (rs.next()) {
			result = rs.getInt("count(class)");
		}
		rs.close();
		return result;
	}
}
