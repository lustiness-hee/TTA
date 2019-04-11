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

public class GenerateDot_Package {

	String ranksep = "ranksep=4.0;";
	DB db = null;
	Coupling cp = new Coupling();
	String dot;
	String couplingOption = "11111";
	String timestamp = "";

	public GenerateDot_Package(String dot, String ranksep, String timestamp,
			DB db, String option) {
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

	private void genDotImage(String dot, String graphicFileName,
			String genDotGraphContents) {
		String dot_contents = genDotGraphContents;
		System.out.println(timestamp);
		try {
			File temp = File.createTempFile("graph", ".dot");

			temp.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			out.write(dot_contents);
			out.close();

			System.out.println("dotting");
			Process p = Runtime.getRuntime().exec(
					dot + " -Tsvg " + temp.getAbsolutePath() + " -o "
							+ graphicFileName);
			p.waitFor();
			System.out.println("done");

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
		String module_query = "select name, filename from Classes";

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
			String temp = Replace(rs.getString("filename"));
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
				content += "\t subgraph cluster"
						+ data.m_directoryName
						+ "{ \n\t label = \"["
						+ data.m_directoryName
						+ "]\"; \n\t"
						+ "color = blue; \n\t fontsize = 40; \n\t fontcolor = blue; \n";
			} else {
				content += "\t subgraph cluster"
						+ data.m_directoryName
						+ "{ rank=same; \n\t label = \"["
						+ data.m_directoryName
						+ "]\"; \n\t"
						+ "color = blue; \n\t fontsize = 40; \n\t fontcolor = blue; \n";
			}
			int size = data.m_items.size();
			for (int i = 0; i < size; i++) {
				String query1 = "select count(class),sum(end_position)-sum(start_position) as Lines from SymbolsOfFiles where class ='"
						+ data.m_items.get(i) + "' and type = 'mi'";
				rs2 = stat2.executeQuery(query1);
				while (rs2.next()) {
					int count_method = rs2.getInt("count(class)");
					int count_lines = (int) rs2.getFloat("Lines");

					String query2 = "select count(class) from SymbolsOfFiles where class ='"
							+ data.m_items.get(i)
							+ "' and type = 'mi' and (end_position-start_position) > 25";
					rs3 = stat2.executeQuery(query2);

					while (rs3.next()) {
						int count_method_line = rs3.getInt("count(class)");

						String query3 = "select count(class), class, Time from Play_time where class ='"
								+ data.m_items.get(i) + "'";
						rs4 = stat3.executeQuery(query3);
						while (rs4.next()) {
							int count = rs4.getInt("count(class)");
							String time = rs4.getString("time");
							if (count == 0) {
								if (i == 0) {
									if (count_method > 20 || count_lines > 1000) {
										content += "\t \""
												+ data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX,color = red,style = bold,fontcolor = red,label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ") \"]";
									} else {
										content += "\t \""
												+ data.m_items.get(i) + "\" "
												+ "[shape=BOX,label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ") \"]";
									}
								} else if (i == (size - 1)) {
									if (count_method > 20 || count_lines > 1000) {
										content += " \""
												+ data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX,color = red,style = bold,fontcolor = red,label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ") \"]";

									} else {
										content += " \"" + data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX, label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ") \"]";
									}
								} else {
									if (count_method > 20 || count_lines > 1000) {
										content += " \""
												+ data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX, color = red,style = bold,fontcolor = red, label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ") \"]";
									} else {
										content += " \"" + data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX,  label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ") \"]";
									}
								}
							} else if (count == 1) {
								if (i == 0) {
									System.out.println(data.m_items.get(i)
											+ " : " + time);
									if (count_method > 20 || count_lines > 1000) {
										content += "\t \""
												+ data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX,color = red,style = bold,fontcolor = red,label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ","
												+ time + "[ms]) \"]";
									} else {
										content += "\t \""
												+ data.m_items.get(i) + "\" "
												+ "[shape=BOX,label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + "," + time
												+ "[ms])  \"]";
									}
								} else if (i == (size - 1)) {
									if (count_method > 20 || count_lines > 1000) {
										content += " \""
												+ data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX,color = red,style = bold,fontcolor = red,label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ","
												+ time + "[ms])  \"]";

									} else {
										content += " \"" + data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX, label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + "," + time
												+ "[ms]) \"]";
									}
								} else {
									if (count_method > 20 || count_lines > 1000) {
										content += " \""
												+ data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX, color = red,style = bold,fontcolor = red, label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ","
												+ time + "[ms]) \"]";
									} else {
										content += " \"" + data.m_items.get(i)
												+ "\" "
												+ "[shape=BOX,  label=\""
												+ data.m_items.get(i) + "("
												+ count_method + ","
												+ count_lines + ","
												+ count_method_line + ","
												+ time + "[ms])\"]";
									}
								}
							}
						}
					}
					rs3.close();
				}
				rs2.close();
			}
			content += ";\t\n\t}\n";
		}
		rs.close();
		return content;
	}

	public void DrawLine(String content) throws Exception {
		String query = "select Caller, Callee, sum(DAT_Count),sum(STMP_Count),sum(CON_Count),sum(EXT_Count),sum(COM_Count),sum(CONT_Count) "
				+ "from TEMP_LINK group by Caller, Callee";

		Connection conn = db.getConnection();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(query);

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
				content += "\t \"" + Caller + "" + "\"->\"" + Callee + ""
						+ "\" [shape=BOX,style = bold, label=\""
						+ cp.SumString(dat, stmp, con, ext, com, cont)
						+ "\" ,color = red];\n";
			} else {
				content += "\t \"" + Caller + "" + "\"->\"" + Callee + ""
						+ "\" [shape=BOX, label=\""
						+ cp.SumString(dat, stmp, con, ext, com, cont)
						+ "\"];\n";
			}
		}
		rs.close();
		content += "} ";
		genDotImage(dot, ".\\img\\Architecture(class)(" + timestamp + ").svg",
				content);
	}

	private void MakeControlCoupring(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		Statement stat = conn.createStatement();

		String module_query = "select class, symbol_name, ref_class, ref_symbol, filename, ref_type "
				+ "from RefersTo "
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
				query2 = "select class,name,ret_type from MethodImplementations where name='"
						+ Callee + "'";
			} else {
				query2 = "select class,name,ret_type from MethodImplementations where name='"
						+ Callee + "' and class='" + Callee_class + "'";
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
				ref_class.add(new parse(Caller_class, Caller, Callee_class,
						Callee, 0, 0, cp.getConCouping(return_type.trim()), 0,
						0, 0));
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
				String query1 = "select filename from Variables where name = '"
						+ Callee + "'";
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
				ref_class.add(new parse(Caller_class, Caller, Callee_class,
						Callee, 0, 0, 0, 0, 1, 0));
			}
		}
		rs.close();

		db.insertLinks(ref_class);
		ref_class.clear();
	}

	public String StringDeletePointerArray(String input) {
		input = input.replace("[", "");
		input = input.replace("]", "");
		input = input.replace("*", "");
		input = input.replace("&", "");
		input = input.replace(" ", "");
		input = input.replace("const", "");
		return input.trim();
	}

	private void MakeExternalCoupring(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		String module_query = "select class, symbol_name, ref_class, ref_symbol, caller_argument_types, filename, ref_type "
				+ "from RefersTo "
				+ "where  ref_type <> 'ma' and ref_type <> 'e' and ref_type <> 'ec' and ref_type <> 'lv' and ref_type <> 'gv' and class <> '#'";
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(module_query);

		while (rs.next()) {
			int ext_sum = 0;
			String Caller_class = rs.getString("class");
			String Callee_class = rs.getString("ref_class");
			String Caller = rs.getString("symbol_name");
			String Callee = rs.getString("ref_symbol");
			// String filename = rs.getString("filename");
			String ref_type = rs.getString("ref_type");

			if (Callee_class.contains("#")) {
				if (ref_type.equals("cl") || ref_type.equals("t")) {
					Callee_class = Callee;
				} else if (ref_type.equals("ud")) {
					String query1 = "select type from instancevariables where variable_name = '"
							+ Callee + "'";

					Statement stat2 = conn.createStatement();
					ResultSet rs2 = stat2.executeQuery(query1);

					while (rs2.next()) {
						String c_type = rs2.getString("type");
						if (c_type.startsWith("C")
								&& !c_type.contains("CString")) {
							c_type = StringDeletePointerArray(c_type);
							Callee_class = c_type;
							break;
						}
					}
					rs2.close();
				}
			}

			if (Callee_class.equals("#"))
				continue;

			String query = "select count(name) from Classes where name = '"
					+ Callee_class + "'";
			Statement stat2 = conn.createStatement();
			ResultSet rs2 = stat2.executeQuery(query);

			while (rs2.next()) {

				int count = rs2.getInt("count(name)");

				if (count == 0) {
					query = "select count(name) from Enumerations where name = '"
							+ Callee_class + "'";
					Statement stat3 = conn.createStatement();
					ResultSet rs3 = stat3.executeQuery(query);

					while (rs3.next()) {
						count = rs3.getInt("count(name)");
						if (count == 0) {
							ext_sum++;
						}
					}
					rs3.close();
				}
				break;
			}
			rs2.close();

			if (!Caller_class.equals(Callee_class)) {
				if (ext_sum > 0) {
					ref_class.add(new parse(Caller_class, Caller, Callee_class,
							Callee, 0, 0, 0, ext_sum, 0, 0));
				}
			}
			ext_sum = 0;
		}
		rs.close();

		db.insertLinks(ref_class);
		ref_class.clear();
	}

	private void MakeContentCoupling(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		String query = "select R.class, R.symbol_name, R.ref_class, R.ref_symbol, R.filename, R.ref_type, R.position "
				+ "from RefersTo as R LEFT JOIN MethodImplementations as M on R.ref_symbol = M.name "
				+ "where R.ref_type = 'ud' and R.class <> '#' "
				+ "and M.name IS NULL "
				+ "and R.ref_argument_types = '' "
				+ "and R.access <> 'p' "
				+ "order by R.class, R.symbol_name, R.position"; // and

		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(query);

		String old_position = "";
		String old_Caller_class = "";
		int position_count = 0;
		Vector<String> ref_symbol_list = new Vector<String>();
		while (rs.next()) {
			String Caller_class = rs.getString("class");
			String Callee_class = rs.getString("ref_class");
			String Caller = rs.getString("symbol_name");
			String Callee = rs.getString("ref_symbol");
			String position = rs.getString("position");

			if (Callee.equalsIgnoreCase("NULL")
					|| Callee.equalsIgnoreCase("TRUE")
					|| Callee.equalsIgnoreCase("FALSE")
					|| Callee.equalsIgnoreCase("L"))
				continue;

			if (!position.equals(old_position)) {
				if (ref_symbol_list.size() > 1) {
					for (int i = 0; i < ref_symbol_list.size(); i++) {
						String ref_symbol = ref_symbol_list.get(i);
						String query1 = "select count(variable_name), type from instancevariables where class = '"
								+ old_Caller_class
								+ "' and variable_name = '"
								+ ref_symbol + "'";
						Statement stat2 = conn.createStatement();
						ResultSet rs2 = stat2.executeQuery(query1);
						rs2.next();
						int val = rs2.getInt(1);

						if (val > 0) {
							String c_type = StringDeletePointerArray(rs2
									.getString(2));
							String query2 = "select count(name) from Classes where name = '"
									+ c_type + "'";
							Statement stat3 = conn.createStatement();
							ResultSet rs3 = stat3.executeQuery(query2);
							rs3.next();
							int classCheckVal = rs3.getInt(1);
							if (classCheckVal > 0) {
								Callee_class = c_type;
								ref_class.add(new parse(old_Caller_class,
										Caller, Callee_class, ref_symbol, 0, 0,
										0, 0, 0, 1));
							}
						}
					}
				}
				ref_symbol_list.clear();
				ref_symbol_list.add(Callee);
				position_count = 0;
			} else {
				ref_symbol_list.add(Callee);
				position_count++;
			}
			old_position = position;
			old_Caller_class = Caller_class;
		}
		rs.close();
		db.insertLinks(ref_class);
	}

	private void MakeStampCoupling(Connection conn) throws Exception {
		Vector<parse> ref_class = new Vector<parse>();
		String module_query = "select class, symbol_name, ref_class, ref_symbol, ref_argument_types, filename, ref_type "
				+ "from RefersTo "
				+ "where  ref_type <> 'ma' and ref_type <> 'e' and ref_type <> 'ec' and class <> '#' and ref_argument_types <> ''";

		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(module_query);

		while (rs.next()) {
			String Caller_class = rs.getString("class");
			String Callee_class = rs.getString("ref_class");
			String Caller = rs.getString("symbol_name");
			String Callee = rs.getString("ref_symbol");
			String parameter = rs.getString("ref_argument_types");
			String ref_type = rs.getString("ref_type");

			if (Callee_class.contains("#")) {
				if (ref_type.equals("cl") || ref_type.equals("t")) {
					Callee_class = Callee;
				} else {
					String query1 = "select type from instancevariables where variable_name = '"
							+ Callee + "'";
					Statement stat2 = conn.createStatement();
					ResultSet rs2 = stat2.executeQuery(query1);

					while (rs2.next()) {
						String c_type = rs2.getString("type");
						if (c_type.startsWith("C")
								&& !c_type.contains("CString")) { // !c_type.contains("CString")
																	// ||

							c_type = StringDeletePointerArray(c_type);
							Callee_class = c_type;
							break;
						}
					}
					rs2.close();
					if (Callee_class.contains("#")) {
						query1 = "select class,identifier from SymbolsOfFiles where identifier = '"
								+ Callee + "'";
						rs2 = stat2.executeQuery(query1);

						while (rs2.next()) {
							Callee_class = rs2.getString("class");
							break;
						}
					}
					rs2.close();
				}
			}

			if (Callee_class.equals("#"))
				continue;

			parameter = StringDeletePointerArray(parameter);
			if (!Caller_class.equals(Callee_class)) {

				ref_class.add(new parse(Caller_class, Caller, Callee_class,
						Callee, cp.getDatCouping(parameter), cp
								.getStmpCouping(parameter), 0, 0, 0, 0));
			}
		}
		rs.close();
		db.insertLinks(ref_class);
	}

	public void genArchitenture_package() throws Exception {

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
				MakeControlCoupring(conn);

			/* External */
			if (couplingOption.charAt(2) == '1')
				MakeExternalCoupring(conn);

			/* common */
			if (couplingOption.charAt(3) == '1')
				MakeCommonCoupring(conn);

			/* Contents */
			if (couplingOption.charAt(4) == '1')
				MakeContentCoupling(conn);

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
}
