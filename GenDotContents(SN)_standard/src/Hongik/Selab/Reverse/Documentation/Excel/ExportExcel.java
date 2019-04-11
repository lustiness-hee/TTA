package Hongik.Selab.Reverse.Documentation.Excel;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Hongik.Selab.Reverse.DB.DB;

public class ExportExcel {
	DB db ;
	
	public ExportExcel(DB db) throws Exception {
		// TODO Auto-generated constructor stub
		this.db = db;
		int dat, stmp, con, ext, com, cont;
		String Caller,Callee,Caller_sym,Callee_sym;
		Connection conn = db.getConnection();
		Statement stat = conn.createStatement();
		String query = "select * from temp_link";
		ResultSet rs = stat.executeQuery(query);
		
		FileWriter fw = new FileWriter(new File("./coupling.csv"));
		fw.write("Caller,Caller_sym,Callee,Callee_sym,DAT,STMP,CON,EXT,COM,CONT \r\n");
		
		while(rs.next()){
			Caller = rs.getString("Caller");
			Caller_sym = rs.getString("Caller_SYM");
			Callee = rs.getString("Callee");
			Callee_sym = rs.getString("Callee_SYM");
			dat = rs.getInt("dat_count");
			stmp = rs.getInt("stmp_count");
			con = rs.getInt("con_count");
			ext = rs.getInt("ext_count");
			com = rs.getInt("com_count");
			cont = rs.getInt("cont_count");
			
			fw.write(Caller+","+Caller_sym+","+Callee+","+Callee_sym+","+dat+","+stmp+","+con+","+ext+","+com+","+cont+"\r\n");
		}
		fw.close();
		rs.close();
		
		query = "select class, identifier from SymbolsOfFiles where type = 'mi' and (end_position-start_position) > 25";
		fw = new FileWriter(new File("./LineOver.csv"));
		while(rs.next()){
			String  Class = rs.getString("class");
			String  Method = rs.getString("identifier");
			fw.write(Class+","+Method+"\r\n");
		}
		fw.close();
		rs = stat.executeQuery(query);
	}
}
