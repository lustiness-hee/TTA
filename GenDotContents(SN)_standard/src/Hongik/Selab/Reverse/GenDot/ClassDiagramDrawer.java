package Hongik.Selab.Reverse.GenDot;


import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import Hongik.Selab.Reverse.DB.DB;

public class ClassDiagramDrawer {
	DB db = null;
	String timestamp = "";
	
	public ClassDiagramDrawer(DB db, String timestamp) {
		// TODO Auto-generated constructor stub
		this.db = db;
		this.timestamp = timestamp;
	}
	
	public void generateClassdiagram(){
		String contents = "";
		try {
			contents += DrawClass();
			contents += DrawClassDependency();
			contents += "\n @enduml";
			FileWriter fw = new FileWriter("./test.txt");
			fw.append(contents);
			fw.close();
			System.out.println(contents);
			
			genDotImage(contents);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void genDotImage(String genDotGraphContents) {
		SourceStringReader sourceReader;
		try {
			FileOutputStream fis = new FileOutputStream("./img/ClassDiagram("+timestamp+").svg");
			sourceReader = new SourceStringReader(genDotGraphContents);
			sourceReader.generateImage(fis, new FileFormatOption(FileFormat.SVG));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String DrawClass() throws Exception{
		String contents = "@startuml \n skinparam classAttributeIconSize 0 \n";
		Connection conn = db.getConnection();
		Statement st = conn.createStatement();
		String class_query = "Select name from classes where attributes = '0x0'";
		ResultSet rs = st.executeQuery(class_query);
		while(rs.next()){
			String class_name = rs.getString("name");
			contents += "class "+class_name +"{\n";
			
			Statement st2 = conn.createStatement();
			String Attr_query = "select variable_name, attributes, type from instanceVariables where class = '"+class_name+"'";
			ResultSet rs2 = st2.executeQuery(Attr_query);
			while(rs2.next()){
				String variable_name = rs2.getString("variable_name");
				String attributes = rs2.getString("attributes");
				String type = rs2.getString("type");
				
				contents += getAttrPriority(attributes)+variable_name+" : "+type+"\n";
			}
			rs2.close();
			
			String method_query = "select name, ret_type from methodimplementations where class  = '"+class_name+"'"; 
			rs2 = st2.executeQuery(method_query);
			while(rs2.next()){
				String method_name = rs2.getString("name");
				String ret_type = rs2.getString("ret_type");
				contents += ret_type+ " " +method_name+"()\n";
			}
			rs2.close();
			contents += "}\n";
		}
		rs.close();
		return contents;
	}
	
	private String DrawClassDependency(){
		String contents = "";
		try {
			contents += drawInheritance();
			contents += drawDependency();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contents;
	}
	/*select distinct class, ref_symbol from refersto where ref_type = 'cl' and ref_symbol In (select name from classes where attributes = '0x0')*/
	private String drawDependency() throws Exception {
		// TODO Auto-generated method stub
		String contents = "";
		
		Connection conn = db.getConnection();
		Statement st = conn.createStatement();
		String class_query = "select distinct class, ref_symbol from refersto where ref_type = 'cl' and ref_symbol In (select name from classes where attributes = '0x0')";
		ResultSet rs = st.executeQuery(class_query);
		while(rs.next()){
			String class_name = rs.getString("class");
			String ref_class = rs.getString("ref_symbol");
			
			contents += class_name +" *-- "+ref_class+"\n"; 
		}
		return contents;
	}

	private String drawInheritance() throws Exception {
		// TODO Auto-generated method stub
		String contents ="";
		
		Connection conn = db.getConnection();
		Statement st = conn.createStatement();
		String class_query = "Select class,base_class from inheritances";
		ResultSet rs = st.executeQuery(class_query);
		
		while(rs.next()){
			String class_name = rs.getString("class");
			String base_class_name = rs.getString("base_class");
			contents += base_class_name + " <|-- " +class_name + "\n";
		}
			
		return contents;
	}

	private String getAttrPriority(String attr){
		String attr_p = "";
		
		switch(attr){
		case "0x1":
			attr_p = "-";
			break;
		case "0x2":
			attr_p = "#";
			break;
		case "0x4":
			attr_p = "+";
			break;
		default:
			break;	
		}
		return attr_p;
	}
}
