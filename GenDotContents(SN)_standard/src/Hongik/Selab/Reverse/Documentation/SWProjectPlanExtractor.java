package Hongik.Selab.Reverse.Documentation;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import Hongik.Selab.Reverse.MonMonthCalculator.DateData;
import Hongik.Selab.Reverse.MonMonthCalculator.ManMonth;
import SELab.RedMineMySQLConnector.Documents;
import SELab.RedMineMySQLConnector.Issue;
import SELab.RedMineMySQLConnector.Members;
import SELab.RedMineMySQLConnector.RedmineMySQLConnector;

public class SWProjectPlanExtractor {
	String Date;
	int issue_num = 89;
	RedmineMySQLConnector conn = new RedmineMySQLConnector();
	public SWProjectPlanExtractor(String Date) {
		// TODO Auto-generated constructor stub
		this.Date = Date;
	}
	
	public void createProjectPlan() throws ParserConfigurationException, TransformerException, DOMException, ClassNotFoundException, SQLException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		String Start_date = conn.getIssueData(issue_num).getStart_date();
		String End_date = conn.getIssueData(issue_num).getDue_date();
		Document doc = docBuilder.newDocument();
		doc.setXmlStandalone(true);
		ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"project_plan.xsl\"");
		Element list = doc.createElement("list");
		    
		Element summary = doc.createElement("summary");
		
			Element title = doc.createElement("title");
			Element pm_env = doc.createElement("pm_env");
			Element pm_man = doc.createElement("pm_man");
			Element version = doc.createElement("version");
			Element date = doc.createElement("date");
			Element name = doc.createElement("name");
			Element period = doc.createElement("period");
			Element goal = doc.createElement("goal");
			Element effect = doc.createElement("effect");
			
			title.setTextContent(""+conn.getProjectName(1));
			pm_env.setTextContent(getPMname());
			pm_man.setTextContent(getPMname());
			date.setTextContent(Date);
			version.setTextContent(getVersion());
			name.setTextContent("본 프로젝트의 명칭은 \""+conn.getProjectName(1)+"\"라 한다.");
			period.setTextContent(Start_date+" ~ "+End_date);
			goal.setTextContent(getGoal());
			effect.setTextContent(getEffect());
			
			summary.appendChild(title);
			summary.appendChild(pm_env);
			summary.appendChild(pm_man);
			summary.appendChild(date);
			summary.appendChild(version);
			summary.appendChild(name);
			summary.appendChild(period);
			summary.appendChild(goal);
			summary.appendChild(effect);

		list.appendChild(summary);
		
		Element area = doc.createElement("area");
			
			Element process_specs = doc.createElement("process_specs");
				String[] Scope = getScope();
				for(int i = 1;i < Scope.length;i++){
					String[] spec = Scope[i].split("!");
					Element process_spec = doc.createElement("process_spec");
					process_spec.setAttribute("name", spec[0]);
					for(int j = 1;j<spec.length;j++){
						Element process = doc.createElement("process");
						process.setAttribute("title", spec[j].split(":")[0]);
						process.setTextContent(spec[j].split(":")[1]);
						process_spec.appendChild(process);
					}
					process_specs.appendChild(process_spec);
				}
			area.appendChild(process_specs);
				
		list.appendChild(area);
		
		Element hw_info = doc.createElement("hardware_info");
		hw_info.setTextContent("http://203.249.87.143:13500/redmine/attachments/download/20/%ED%83%80%EA%B2%9F.png");
		Element sw_info = doc.createElement("software_info");
		sw_info.setTextContent("http://203.249.87.143:13500/redmine/attachments/download/19/%ED%88%B4%EC%B2%B4%EC%9D%B8.png");
		Element org_info = doc.createElement("organization_info");
		org_info.setTextContent("http://203.249.87.143:13500/redmine/attachments/download/65/%EC%A1%B0%EC%A7%81%EB%8F%84.png");
		
		list.appendChild(hw_info);
		list.appendChild(sw_info);
		list.appendChild(org_info);
		
		Element roles = doc.createElement("roles");
		String[] man = getManAllocation();
		for(int i=1;i<man.length;i++){
			Element role = doc.createElement("role");
			role.setAttribute("position", man[i].split(":")[0]);
			role.setTextContent(man[i].split(":")[1]);
			roles.appendChild(role);
		}
		list.appendChild(roles);
		
		
		Element man_month_info = doc.createElement("man_month_info");
		Element mm_header = doc.createElement("mm_header");
		Element mm_header_option = doc.createElement("mm_header_option");
		
		mm_header_option.setTextContent("구분");
		mm_header.appendChild(mm_header_option);
		ManMonth mm = new ManMonth(Start_date,End_date, new LinkedList<DateData>());
		for(DateData data: mm.getManMonth()){
			mm_header_option = doc.createElement("mm_header_option");
			mm_header_option.setTextContent(data.getStartmonth());
			mm_header.appendChild(mm_header_option);
		}
		mm_header_option = doc.createElement("mm_header_option");
		mm_header_option.setTextContent("Total");
		mm_header.appendChild(mm_header_option);
		
		man_month_info.appendChild(mm_header);
		/*변경요..*/
		for(int i = 1;i<man.length;i++){
			Element mm_data = doc.createElement("mm_data");
			Element mm_data_option = doc.createElement("mm_data_option");
			LinkedList<DateData> dates = null;
			if(man[i].split(":")[0].contains("손현승")){
				dates = getManMonth(7);
				mm_data_option = doc.createElement("mm_data_option");
				mm_data_option.setTextContent(man[i].split(":")[0]);
				mm_data.appendChild(mm_data_option);	
			}else if (man[i].split(":")[0].contains("강건희")){
				dates = getManMonth(1);
				mm_data_option = doc.createElement("mm_data_option");
				mm_data_option.setTextContent(man[i].split(":")[0]);
				mm_data.appendChild(mm_data_option);	
			}
			ManMonth m = new ManMonth(Start_date, End_date, dates);
			float total = 0;
			for(DateData data: m.getManMonth()){
				mm_data_option = doc.createElement("mm_data_option");
				mm_data_option.setTextContent(String.format("%.2f", data.getManMonth()));
				total+= data.getManMonth();
				mm_data.appendChild(mm_data_option);
			}
			mm_data_option = doc.createElement("mm_data_option");
			mm_data_option.setTextContent(String.format("%.2f", total));
			mm_data.appendChild(mm_data_option);
			
			man_month_info.appendChild(mm_data);
		}
		/**/
		//human_employ_plan_info.setTextContent("http://203.249.87.143:13500/redmine/attachments/download/67/%EC%9D%B8%EB%A0%A5%ED%88%AC%EC%9E%85%EA%B3%84%ED%9A%8D.png");
		list.appendChild(man_month_info);
		
		/*세부일정계획*/
		Element schedule_info = doc.createElement("schedule_info");
		//schedule_info.setTextContent("http://203.249.87.143:13500/redmine/attachments/download/66/%EC%84%B8%EB%B6%80%EC%9D%BC%EC%A0%95%20%EC%B6%94%EC%A7%84%EA%B3%84%ED%9A%8D.png");
		Element si_header = doc.createElement("si_header");

		for(DateData data: mm.getManMonth()){
			Element si_header_option = doc.createElement("si_header_option");
			si_header_option.setTextContent(data.getStartmonth());
			si_header.appendChild(si_header_option);
		}
		schedule_info.appendChild(si_header);
		
		/*
		 * 계획 = 6
		 * 분석 = 8
		 * 설계 = 9
		 * 구현 = 10
		 * 시험 = 11
		 * 종료 = 12
		 */
		
		LinkedList<DateData> m;
		LinkedList<Issue> issue =  conn.getTrackerTypeData(1, 6);
		
		for(Issue is : issue){
			Element si_data = doc.createElement("si_data");
			m = new LinkedList<DateData>();
			si_data.setAttribute("depth", "계획");
			si_data.setAttribute("action", is.getSubject());

			DateData d = new DateData(is.getStart_date(), is.getDue_date());
			m.add(d);
			
			ManMonth mt = new ManMonth(Start_date, End_date, m);
			
			for(DateData dd: mt.getManMonth()){
				if(dd.getManMonth() == 0){
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("X");
					si_data.appendChild(si_data_option);
				}else{
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("O");
					si_data.appendChild(si_data_option);
				}
			}
			schedule_info.appendChild(si_data);
		}
		
		issue =  conn.getTrackerTypeData(1, 8);
		for(Issue is : issue){
			Element si_data = doc.createElement("si_data");
			m = new LinkedList<DateData>();
			si_data.setAttribute("depth", "분석");
			si_data.setAttribute("action", is.getSubject());
			
			
			DateData d = new DateData(is.getStart_date(), is.getDue_date());
			m.add(d);
			
			ManMonth mt = new ManMonth(Start_date, End_date, m);
			
			for(DateData dd: mt.getManMonth()){
				if(dd.getManMonth() == 0){
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("X");
					si_data.appendChild(si_data_option);
				}else{
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("O");
					si_data.appendChild(si_data_option);
				}
			}
			schedule_info.appendChild(si_data);
		}
		
		issue =  conn.getTrackerTypeData(1, 9);
		for(Issue is : issue){
			Element si_data = doc.createElement("si_data");
			m = new LinkedList<DateData>();
			si_data.setAttribute("depth", "설계");
			si_data.setAttribute("action", is.getSubject());
			
			
			DateData d = new DateData(is.getStart_date(), is.getDue_date());
			m.add(d);
			
			ManMonth mt = new ManMonth(Start_date, End_date, m);
			
			for(DateData dd: mt.getManMonth()){
				if(dd.getManMonth() == 0){
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("X");
					si_data.appendChild(si_data_option);
				}else{
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("O");
					si_data.appendChild(si_data_option);
				}
			}
			schedule_info.appendChild(si_data);
		}
		
		issue =  conn.getTrackerTypeData(1, 10);
		for(Issue is : issue){
			Element si_data = doc.createElement("si_data");
			m = new LinkedList<DateData>();
			si_data.setAttribute("depth", "구현");
			si_data.setAttribute("action", is.getSubject());
			
			
			DateData d = new DateData(is.getStart_date(), is.getDue_date());
			m.add(d);
			
			ManMonth mt = new ManMonth(Start_date, End_date, m);
			
			for(DateData dd: mt.getManMonth()){
				if(dd.getManMonth() == 0){
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("X");
					si_data.appendChild(si_data_option);
				}else{
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("O");
					si_data.appendChild(si_data_option);
				}
			}
			schedule_info.appendChild(si_data);
		}
		
		issue =  conn.getTrackerTypeData(1, 11);
		for(Issue is : issue){
			Element si_data = doc.createElement("si_data");
			m = new LinkedList<DateData>();
			si_data.setAttribute("depth", "시험");
			si_data.setAttribute("action", is.getSubject());
			
			
			DateData d = new DateData(is.getStart_date(), is.getDue_date());
			m.add(d);
			
			ManMonth mt = new ManMonth(Start_date, End_date, m);
			
			for(DateData dd: mt.getManMonth()){
				if(dd.getManMonth() == 0){
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("X");
					si_data.appendChild(si_data_option);
				}else{
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("O");
					si_data.appendChild(si_data_option);
				}
			}
			schedule_info.appendChild(si_data);
		}

		issue =  conn.getTrackerTypeData(1, 12);
		for(Issue is : issue){
			Element si_data = doc.createElement("si_data");
			m = new LinkedList<DateData>();
			si_data.setAttribute("depth", "종료");
			si_data.setAttribute("action", is.getSubject());
			
			
			DateData d = new DateData(is.getStart_date(), is.getDue_date());
			m.add(d);
			
			ManMonth mt = new ManMonth(Start_date, End_date, m);
			
			for(DateData dd: mt.getManMonth()){
				if(dd.getManMonth() == 0){
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("X");
					si_data.appendChild(si_data_option);
				}else{
					Element si_data_option = doc.createElement("si_data_option");
					si_data_option.setTextContent("O");
					si_data.appendChild(si_data_option);
				}
			}
			schedule_info.appendChild(si_data);
		}
		
		list.appendChild(schedule_info);
		
		Element output_docs = doc.createElement("output_docs");
		String[] output = getOutput();	
		for(int i = 1 ; i < output.length ; i++ ){
			Element output_doc = doc.createElement("output_doc");
			output_doc.setAttribute("depth", output[i].split(":")[0]);
			output_doc.setAttribute("doc_name", output[i].split(":")[1]);
			output_doc.setAttribute("output_date", output[i].split(":")[2]);
			output_doc.setAttribute("etc", output[i].split(":")[3]);
			output_docs.appendChild(output_doc);
		}
		
		list.appendChild(output_docs);
		
		Element output_checks = doc.createElement("output_checks");
		String[] output_check_ = getOutputInspection();
		for(int i = 1;i < output_check_.length; i++){
			Element output_check = doc.createElement("output_check");
			output_check.setAttribute("name", output_check_[i].split(":")[0]);
			output_check.setTextContent(output_check_[i].split(":")[1]);
			output_checks.appendChild(output_check);
		}
		
		list.appendChild(output_checks);
		
		Element project_report_plans = doc.createElement("project_report_plans");
		String[] report_plan = getReportPlan();
		for(int i = 1;i < report_plan.length; i++){
			Element project_report_plan = doc.createElement("project_report_plan");
			project_report_plan.setAttribute("type", report_plan[i].split(":")[0]);
			project_report_plan.setAttribute("human_list", report_plan[i].split(":")[3]);
			project_report_plan.setAttribute("date", report_plan[i].split(":")[1]);
			project_report_plan.setTextContent(report_plan[i].split(":")[2]);
			project_report_plans.appendChild(project_report_plan);
		}
		list.appendChild(project_report_plans);
		
		Element educations = doc.createElement("educations");
		String[] education_ = getEducation();
		for(int i = 1;i<education_.length;i++){
			Element education = doc.createElement("education");
			education.setAttribute("organ", education_[i].split(":")[0]);
			education.setTextContent(education_[i].split(":")[1]);
			educations.appendChild(education);
		}
		
		list.appendChild(educations);
		
		Element tech_transfer = doc.createElement("tech_transfer");
		tech_transfer.setTextContent(getTechTransfer()[1]);
		
		list.appendChild(tech_transfer);
		
		doc.appendChild(list);
		doc.insertBefore(pi, list);
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("./project_plan.xml"));
 
		transformer.transform(source, result);
	}
	
	private String getPMname() throws ClassNotFoundException, SQLException{
		String name ="";
		LinkedList<Members> member = conn.getProjectMembers(1);
		
		for(Members m : member){
			if(m.getPosition().equals("관리자")){
				name = m.getName();
			}
		}
		return name;
	}

	private String getVersion() throws ClassNotFoundException, SQLException {
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		return result[0].split(":")[1];
	}
	
	private String getGoal() throws ClassNotFoundException, SQLException{
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		return result[1].split(":")[1];
	}
	
	private String getEffect() throws ClassNotFoundException, SQLException{
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		return result[2].split(":")[1];
	}
	
	private String[] getScope() throws ClassNotFoundException, SQLException{
		String[] scope;
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		scope = result[3].split("~");
		return scope;
	}
	
	private String[] getManAllocation() throws ClassNotFoundException, SQLException{
		String[] man;
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		man = result[4].split("!");
		return man;
	}
	
	private String[] getOutput() throws ClassNotFoundException, SQLException{
		String[] output;
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		output = result[5].split("!");
		
		return output;
	}
	
	private String[] getOutputInspection() throws ClassNotFoundException, SQLException{
		String[] inspection;
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		inspection = result[6].split("!");
		return inspection;
	}
	private String[] getReportPlan() throws ClassNotFoundException, SQLException{
		String[] plan;
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		plan = result[7].split("!");
		return plan;
	}
	
	private String[] getEducation() throws ClassNotFoundException, SQLException{
		String[] education;
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		education = result[8].split("!");
		return education;
	}
	
	private String[] getTechTransfer() throws ClassNotFoundException, SQLException{
		String[] tech;
		Issue doc_data = conn.getIssueData(issue_num);
		String data = doc_data.getDescription();
		String[] result = data.split(";");
		tech = result[9].split("!");
		return tech;
	}
	
	private LinkedList<DateData> getManMonth(int id) throws ClassNotFoundException, SQLException{
		LinkedList<DateData> m = new LinkedList<DateData>();
		LinkedList<Issue> issue =  conn.getIssueMemberData(1, id);
		for(int i=0 ;i<issue.size();i++){
			Issue is = issue.get(i);
			DateData d = new DateData(is.getStart_date(), is.getDue_date());
			m.add(d);
		}
		return m;
	}
}
