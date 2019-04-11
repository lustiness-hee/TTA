package Hongik.Selab.Reverse.GenDot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Hongik.Selab.Reverse.DB.DB;

public class Generate_performance_view {
	private String dot, filepath;
	private DB db;
	private String timestamps[];

	public Generate_performance_view(String dot, String filepath, DB db) {
		this.dot = dot;
		this.filepath = filepath;
		this.db = db;
	}

	public void Draw() {
		try {
			Connection conn = db.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select distinct class from performance");
			while (rs.next()) {
				DrawAll(conn, rs.getString("class"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void DrawAll(Connection conn, String class_name) throws SQLException {
		String contents = "";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(
				"select distinct timestamp from performance " + "where class = '" + class_name + "' order by timestamp desc");
		String[] timestamps = {"",""};
		int i = 0;
		while (rs.next()) {
			timestamps[i] = rs.getString("timestamp");
			i++;
		}
		contents += "digraph XX { \n rankdir=LR; ranksep = 5.0;\n label =\""+class_name+"\"";
		contents += DrawBefore(conn, class_name, timestamps[0]);
		contents += DrawAfter(conn, class_name, timestamps[0]);
		contents += DrawArraw(conn, class_name, timestamps);
		contents += "\n}";
		new GenDot().genDotImage(dot, "./img/performance/"+class_name+".svg", contents);

	}

	private String DrawBefore(Connection conn, String class_name, String timestamp) throws SQLException {
		String contents = "\n\t subgraph clusterBefore{ \n label=\"Before\"";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select method, time, count from performance " + "where class = '" + class_name
				+ "' and " + "timestamp='" + timestamp + "'");
		while (rs.next()) {
			String method = rs.getString("method");
			String time = rs.getString("time");
			String count = rs.getString("count");

			contents += "\n" + method + "_before [shape=BOX, style = bold, label=\"" + method + "( time = " + time
					+ "| count =" + count + ")\"]\n";
		}
		contents+="}";
		return contents;
	}

	private String DrawAfter(Connection conn, String class_name, String timestamp) throws SQLException {
		String contents = "\n\t subgraph clusterAfter{ \n label=\"After\"";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select method, time, count from performance " + "where class = '" + class_name
				+ "' and " + "timestamp='" + timestamp + "'");
		while (rs.next()) {
			String method = rs.getString("method");
			String time = rs.getString("time");
			String count = rs.getString("count");

			contents += "\n" + method + "_after [shape=BOX, style = bold, label=\"" + method + "( time = " + time
					+ "| count =" + count + ")\"]\n";
		}
		contents+="}";
		return contents;
	}

	private String DrawArraw(Connection conn, String class_name, String[] timestamp) throws SQLException {
		String contents = "";
		Statement st = conn.createStatement();
		Statement st2 = conn.createStatement();
		ResultSet rs2 = null;
		ResultSet rs = st.executeQuery("select distinct method, sum(time), sum(count) from performance where class='"+class_name+"' and timestamp='"+timestamp[0]+"' group by method");
		while (rs.next()) {
			String method_before = rs.getString("method");
			String method_after = "";
			float time_before = rs.getFloat("sum(time)");
			float time_after;
			contents += method_before+"_before ->";
			
			rs2 = st2.executeQuery("select distinct count(method),method,sum(time),sum(count) from performance where class = '"+class_name+"' and timestamp = '"+timestamp[0]+"' and method = '"+method_before+"' group by method");
			while(rs2.next()){
				int count = rs2.getInt("count(method)");
				method_after = rs2.getString("method");
				time_after = rs2.getFloat("sum(time)");
				if(count !=0){
					contents += method_after+"_after ";
					contents += "[label = \""+(time_before-time_after)+"\"]";
				}else if (count == 0){
					contents += method_before+"_removal";
					contents += " [label=\" removal\"]";
				}
			}	
		}
		return contents;
	}

}
