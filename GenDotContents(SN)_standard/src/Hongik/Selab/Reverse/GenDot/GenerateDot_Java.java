package Hongik.Selab.Reverse.GenDot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import Hongik.Selab.Reverse.DB.DB;
import Hongik.Selab.Reverse.QualityProperties.Calculate.Coupling;

public class GenerateDot_Java {
	String ranksep = "ranksep=4.0;";
	DB db = null;
	Coupling cp = new Coupling();
	String dot;
	String couplingOption = "11111";
	String timestamp = "";

	public GenerateDot_Java(String dot, String ranksep, String timestamp, DB db, String option) {
		// TODO Auto-generated constructor stub
		this.dot = dot;
		this.timestamp = timestamp;
		this.db = db;

		if (ranksep.length() > 0) {
			this.ranksep = ranksep;
		} else if (ranksep.equals("none")) {
			this.ranksep = "none";
		}
		if (option.length() > 0) {
			this.couplingOption = option;
		}
	}

	private void genDotImage(String dot, String graphicFileName, String genDotGraphContents) {
		String dot_contents = genDotGraphContents;
		System.out.println(timestamp);
		try {
			File temp = File.createTempFile("graph", ".dot");

			temp.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			out.write(dot_contents);
			out.close();

			System.out.println("dotting");
			Process p = Runtime.getRuntime().exec(dot + " -Tsvg " + temp.getAbsolutePath() + " -o " + graphicFileName);
			p.waitFor();
			System.out.println(graphicFileName + " : done");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String DrawRect() throws Exception {
		String content = "";
		String dir[];
		Connection conn = db.getConnection();
		String class_name;
		Statement stat = conn.createStatement();
		Statement stat2 = conn.createStatement();
		Statement stat3 = conn.createStatement();
		String module_query = "select name, path from SNDB_CL";

		ResultSet rs = stat.executeQuery(module_query);
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;

		if (ranksep.equals("none"))
			content = "digraph xx { \n ";
		else
			content = "digraph xx { \n rankdir=RL; " + ranksep;
		/*
		 * 모듈 그리기
		 */
		HashMap<String, ClusterData> clusterGraphMap = new HashMap<>();
		while (rs.next()) {
			class_name = rs.getString("name");
			String temp = Replace(rs.getString("path"));
			dir = temp.split("/");
			String dir_name = dir[dir.length - 2];
			ClusterData data = clusterGraphMap.get(dir_name);
			if (data == null) {
				ClusterData newData = new ClusterData();
				newData.m_directoryName = dir_name;
				newData.m_items.add(class_name);
				clusterGraphMap.put(dir_name, newData);
			} else {
				data.m_items.add(class_name);
			}
		}
		Collection<ClusterData> dataList = clusterGraphMap.values();
		Iterator<ClusterData> iter_cluster = dataList.iterator();

		while (iter_cluster.hasNext()) {
			ClusterData data = iter_cluster.next();
			if (ranksep.equals("none")) {
				content += "\t subgraph cluster" + data.m_directoryName + "{ \n\t label = \"[" + data.m_directoryName
						+ "]\"; \n\t" + "color = blue; \n\t fontsize = 40; \n\t fontcolor = blue; \n";
			} else {
				content += "\t subgraph cluster" + data.m_directoryName + "{ rank=same; \n\t label = \"["
						+ data.m_directoryName + "]\"; \n\t"
						+ "color = blue; \n\t fontsize = 40; \n\t fontcolor = blue; \n";
			}
			int size = data.m_items.size();
			for (int i = 0; i < size; i++) {
				rs = stat.executeQuery("select count(method),sum(count),sum(time) from performance where class='"
						+ data.m_items.get(i) + "' group by class");
				content += " \"" + data.m_items.get(i) + "\" ";
				content += "[shape=BOX, style = bold, label=\"" + data.m_items.get(i);
				while (rs.next()) {
					String count = rs.getString("sum(count)");
					String time = rs.getString("sum(time)");
					String isExist = rs.getString("count(method)");
					content += "(" + count + "," + time + "[ms]) \"]\n";
				}
				content += "\"]\n";
			}
		}
		content += ";\t\n\t}\n";
		System.out.println("Module_end");
		return content;
	}

	public void DrawLine(String content) throws Exception {
		String query = "select Caller, Callee, sum(DAT_Count),sum(STMP_Count),sum(CON_Count),sum(EXT_Count),sum(COM_Count),sum(CONT_Count) "
				+ "from TEMP_LINK group by Caller, Callee";

		Connection conn = db.getConnection();
		Statement stat = conn.createStatement();
		Statement stat2 = conn.createStatement();
		ResultSet rs = stat.executeQuery(query);
		ResultSet rs2 =null;
		while (rs.next()) {
			int dat = rs.getInt("sum(DAT_Count)");
			int stmp = rs.getInt("sum(STMP_Count)");
			int con = rs.getInt("sum(CON_Count)");
			int ext = rs.getInt("sum(EXT_Count)");
			int com = rs.getInt("sum(COM_Count)");
			int cont = rs.getInt("sum(CONT_Count)");
			String Caller = rs.getString("Caller");
			String Callee = rs.getString("Callee");
			
			
			if (cp.sum(dat, stmp, con, ext, com, cont) > 30) {
				content += "\t \"" + Caller + "" + "\"->\"" + Callee + "" + "\" [shape=BOX,style = bold, label=\""
						+ cp.SumString(dat, stmp, con, ext, com, cont) + "\" ,color = red];\n";
			} else {
				content += "\t \"" + Caller + "" + "\"->\"" + Callee + "" + "\" [shape=BOX, label=\""
						+ cp.SumString(dat, stmp, con, ext, com, cont) + "\"];\n";
			}
		}
		rs.close();
		content += "} ";
		genDotImage(dot, ".\\img\\Architecture(class)(" + timestamp + ").svg", content);
	}

	public void MakeControlCoupring(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		Statement stat = conn.createStatement();

		String module_query = "select class, symbol_name, ref_class, ref_symbol, filename, ref_type " + "from RefersTo "
				+ "where  ref_type <> 'ma' and ref_type <> 'e' and ref_type <> 'ec' "
				+ "and class <> '#' and ref_type <> 'lv' and ref_type <> 'cl' and ref_type <> 't'";
		ResultSet rs = stat.executeQuery(module_query);

		while (rs.next()) {
			String Caller_class = rs.getString("class");
			String Callee_class = rs.getString("ref_class");
			String Caller = rs.getString("symbol_name");
			String Callee = rs.getString("ref_symbol");
			String return_type = "";

			String query2 = "";
			if (Callee_class.contains("#")) {
				query2 = "select class,name,ret_type from MethodImplementatiions where name='" + Callee + "'";
			} else {
				query2 = "select class,name,ret_type from MethodImplementations where name='" + Callee + "' and class='"
						+ Callee_class + "'";
			}
			// System.out.println(query2);
			Statement stat2 = conn.createStatement();
			ResultSet rs2 = stat2.executeQuery(query2);
			if (rs2.next()) {
				Callee_class = rs2.getString("class");
				return_type = rs2.getString("ret_type");
			}
			rs2.close();

			if (Callee_class.equals("#"))
				continue;

			return_type = StringDeletePointerArray(return_type);

			int control_num = cp.getConCouping(return_type.trim());
			if (control_num == 0)
				continue;

			if (!Caller_class.equals(Callee_class)) {
				ref_class.add(new parse(Caller_class, Caller, Callee_class, Callee, 0, 0,
						cp.getConCouping(return_type.trim()), 0, 0, 0));
			}
		}
		rs.close();
		db.insertLinks(ref_class);

	}

	private void MakeCommonCoupring(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		String query = "Select class, symbol_name, ref_class, ref_symbol from RefersTo where ref_type = 'gv'";
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(query);

		while (rs.next()) {
			String Caller_class = rs.getString("class");
			String Callee_class = rs.getString("ref_class");
			String Caller = rs.getString("symbol_name");
			String Callee = rs.getString("ref_symbol");
			String filename = "";

			if (Callee_class.contains("#")) {
				String query1 = "select filename from Variables where name = '" + Callee + "'";
				Statement stat2 = conn.createStatement();
				ResultSet rs2 = stat2.executeQuery(query1);
				while (rs2.next()) {
					filename = rs2.getString("filename");
					int index = filename.lastIndexOf("\\");
					String filename_name = filename.substring(index + 1);

					String filename2 = filename_name.replace(".cpp", "");
					filename2 = filename2.replace(".h", "");

					if (filename2.equalsIgnoreCase("MainFrm")) {
						Callee_class = Caller_class;
					} else {
						Callee_class = "C" + filename2;
					}
				}
				rs2.close();
			}

			if (!Caller_class.equals(Callee_class)) {
				ref_class.add(new parse(Caller_class, Caller, Callee_class, Callee, 0, 0, 0, 0, 1, 0));
			}
		}
		rs.close();

		db.insertLinks(ref_class);
		ref_class.clear();
	}

	private void MakeExternalCoupring(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		String module_query = "";
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(module_query);

		while (rs.next()) {

					
		rs.close();

		db.insertLinks(ref_class);
		ref_class.clear();
	
		}
	}

	private void MakeContentCoupling(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		String query = "";

		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(query);

		
		while (rs.next()) {
			
		}
		rs.close();
		db.insertLinks(ref_class);
	}

	private void MakeStampCoupling(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		String module_query = "select REFER_CLASS_NAME, REFER_SYMBOL_NAME, REFERRED_CLASS_NAME, Referred_symbol_name, refer_argument_types, path, Refer_type "
				+ "from SNDB_BY " + "where refer_type = 'mi'";
		
		Statement stat = conn.createStatement();
		Statement stat2 = conn.createStatement();
		ResultSet rs = stat.executeQuery(module_query);
		ResultSet rs2 =null;
		while (rs.next()) {
			String Caller_class = rs.getString("Refer_class_name");
			String Callee_class = rs.getString("Referred_class_name");
			String Caller = rs.getString("Refer_symbol_name");
			String Callee = rs.getString("Referred_symbol_name");
			String parameter = rs.getString("refer_argument_types");

			if (Callee_class.equals("#"))
				continue;

			parameter = StringDeletePointerArray(parameter);
			if (!Caller_class.equals(Callee_class)) {
				rs2 = stat2.executeQuery("select count(*) from SNDB_CL where NAME = '"+Callee_class+"'");
				while(rs2.next()){
					int count = rs2.getInt("count(*)");
					if(count ==0){
						Callee_class = "SYSTEM LIB";
					}
					ref_class.add(new parse(Caller_class, Caller, Callee_class, Callee, cp.getDatCouping(parameter),
							cp.getStmpCouping(parameter), 0, 0, 0, 0));
				}
				
			}
		}
		rs.close();
		db.insertLinks(ref_class);
	}

	public void genArchitenture_Class() throws Exception {

		Connection conn = db.getConnection();

		try {
			String content = DrawRect();
			/*
			 * 화살표 (결합도)
			 */

			if (couplingOption.charAt(0) == '1')
				MakeStampCoupling(conn);

			/* control */
			if (couplingOption.charAt(1) == '1')
				// MakeControlCoupring(conn);

				/* External */
			if (couplingOption.charAt(2) == '1')
				// MakeExternalCoupring(conn);

					/* common */
			if (couplingOption.charAt(3) == '1')
				// MakeCommonCoupring(conn);

						/* Contents */
			if (couplingOption.charAt(4) == '1')
				// MakeContentCoupling(conn);

			/* 화살표 */
			DrawLine(content);

			content = "";
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String Replace(String path) {
		String temp;
		temp = path.replaceAll("\\\\", "/");
		return temp;
	}
	private String StringDeletePointerArray(String input) {
		input = input.replace("[", "");
		input = input.replace("]", "");
		input = input.replace("*", "");
		input = input.replace("&", "");
		input = input.replace(" ", "");
		input = input.replace("const", "");
		return input.trim();
	}
}
