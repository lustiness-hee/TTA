package Hongik.Selab.Reverse.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import Hongik.Selab.Reverse.GenDot.parse;
import Hongik.Selab.Reverse.Xml.Performance.Data;

public class DB {
	private final int step = 1000;
	
	private String dbName =  "";
	private Connection conn;
	
	public DB(String dbName) {
		// TODO Auto-generated constructor stub
		this.dbName = dbName;
	}
	
	public boolean close() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			return false; 
		}
		return true;
	}

	/**
	 * DB를 초기화한다. 
	 * @return 실패 시 false를 반환한다.
	 */
	private boolean initDB(){	
		try {
			// #1. JDBC driver loading
			Class.forName("SQLite.JDBCDriver").newInstance();
			conn = DriverManager.getConnection("jdbc:sqlite:/"+dbName);
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * DB Connection 을 반환한다.
	 * @return
	 * @throws Exception 
	 */
	public Connection getConnection() throws Exception{
		if (conn != null)
			return conn;
		initDB();
		return conn;
	}
	
	public boolean init() throws Exception{
		Connection conn = getConnection();

		// #1. Table 등록
		Statement stat;
		try {
			stat = conn.createStatement();
			stat.executeUpdate("drop table if exists TEMP_LINK;");
			stat.executeUpdate("create table TEMP_LINK"
					+" (Caller TEXT ,Caller_SYM,Callee TEXT,Callee_SYM, DAT_Count INTEGER, STMP_Count INTEGER, CON_Count INTEGER, EXT_Count INTEGER, COM_Count INTEGER, CONT_Count INTEGER);");
			stat.executeUpdate("create table if not exists Performance (Class text, Method text, Time Float,Count Integer,TimeStamp Text);;");
			ReplaceSlash();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void insertLinks(Vector<parse> links) throws Exception {
		Connection conn = getConnection();
		try {
			PreparedStatement prep = conn.prepareStatement("insert or IGNORE into TEMP_LINK values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			conn.setAutoCommit(false);	

			for(int i=0;i<links.size(); i++) {
				parse p = links.get(i);
				prep.setString(1, p.getCaller());
				prep.setString(2, p.getCaller_sym());
				prep.setString(3, p.getCallee());
				prep.setString(4, p.getCallee_sym());
				prep.setInt(5, p.getDat_count());
				prep.setInt(6, p.getStmp_count());
				prep.setInt(7, p.getCon_count());
				prep.setInt(8, p.getExt_count());
				prep.setInt(9, p.getCom_count());
				prep.setInt(10, p.getCont_count());
				prep.addBatch();
				
				if ((i % step) == 0) {
					prep.executeBatch();
					conn.commit();
					prep.clearBatch();
					System.out.print(".");
				}
			}

			prep.executeBatch();
			conn.commit();
			prep.clearBatch();
			
			conn.setAutoCommit(true);
			prep.close();
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public void insertTimes(Vector<Data> links, String timestamp) throws Exception {
		Connection conn = getConnection();
		try {
			String class_name = "";
			String method_name = "";
			PreparedStatement prep = conn.prepareStatement("insert or IGNORE into Performance values (?, ?, ?, ?, ?);");
			conn.setAutoCommit(false);	

			for(int i=0;i<links.size(); i++) {
				Data p = links.get(i);
				String[] data = p.getMethod_name().split("\\.");
				System.out.println(p.getMethod_name());
				class_name = data[data.length-2];
				method_name = data[data.length-1];
				
				prep.setString(1, class_name);
				prep.setString(2, method_name);
				prep.setFloat(3, p.getSelf());
				prep.setInt(4, p.getCount());
				prep.setString(5, timestamp);
				prep.addBatch();
				
				if ((i % step) == 0) {
					prep.executeBatch();
					conn.commit();
					prep.clearBatch();
					System.out.print(".");
				}
			}

			prep.executeBatch();
			conn.commit();
			prep.clearBatch();
			
			conn.setAutoCommit(true);
			prep.close();
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public void ReplaceSlash(){
		Connection conn;
		Statement stat;
		try {
			conn = getConnection();
			stat = conn.createStatement();
			System.out.println("Replace Start!");
			stat.executeUpdate("update Constants set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update ProjectFiles set highlight_file = replace(highlight_file,\"/\",\"\\\");");
			stat.executeUpdate("update Enumerations set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update EnumConstants set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update FunctionDefinitions set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update Friends set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update Functions set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update Variables set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update Inheritances set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update instanceVariables set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update Macros set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update MethodDefinitions set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update MethodImplementations set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update typedefs set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update RefersTo set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update ReferredBy set filename = replace(filename,\"/\",\"\\\");");
			stat.executeUpdate("update Classes set filename = replace(filename,\"/\",\"\\\");");
			System.out.println("Replace End!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

	
	