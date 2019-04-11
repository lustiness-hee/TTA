package Hongik.Selab.Reverse.Xml;

/**
 * @author 건희 Xml 형식을 정의 하기위한 데이터 클래스
 */

public class Form {
	private String no;
	private String coupling;
	private String method_line;
	private String file_name;
	private String style;
	private String warning;
	private String performance;
	private String total_line;
	private String time_stamp;
	public Form(String no, String coupling, String method_line,
			String file_name, String style, String warning, String performance, String total_line,String time_stamp) {
		// TODO Auto-generated constructor stub
		this.setNo(no);
		this.setCoupling(coupling);
		this.setMethod_line(method_line);
		this.setFile_name(file_name);
		this.setStyle(style);
		this.setWarning(warning);
		this.setPerformance(performance);
		this.setTotal_line(total_line);
		this.setTime_stamp(time_stamp);
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getCoupling() {
		return coupling;
	}

	public void setCoupling(String coupling) {
		this.coupling = coupling;
	}

	public String getMethod_line() {
		return method_line;
	}

	public void setMethod_line(String method_line) {
		this.method_line = method_line;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	public String getTotal_line() {
		return total_line;
	}

	public void setTotal_line(String total_line) {
		this.total_line = total_line;
	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}
}
