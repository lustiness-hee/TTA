package Hongik.Selab.Reverse.GenDot;

public class parse {
	private String caller;
	private String caller_sym;
	private String callee;
	private String callee_sym;
	private int dat_count;
	private int stmp_count;
	private int con_count;
	private int ext_count;
	private int com_count;
	private int cont_count;
	public parse(String caller,String caller_sym,String callee,String callee_sym, int dat_count, 
			int stmp_count, int con_count, int ext_count, int com_count, int cont_count){
		this.caller = caller;
		this.caller_sym = caller_sym;
		this.callee = callee;
		this.callee_sym = callee_sym;
		this.dat_count = dat_count;
		this.stmp_count = stmp_count;
		this.ext_count = ext_count;
		this.con_count = con_count;
		this.com_count = com_count;
		this.cont_count = cont_count;
	}
	
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	public String getCallee() {
		return callee;
	}
	public void setCallee(String callee) {
		this.callee = callee;
	}

	public int getDat_count() {
		return dat_count;
	}

	public void setDat_count(int dat_count) {
		this.dat_count = dat_count;
	}

	public int getStmp_count() {
		return stmp_count;
	}

	public void setStmp_count(int stmp_count) {
		this.stmp_count = stmp_count;
	}

	public int getCon_count() {
		return con_count;
	}

	public void setCon_count(int con_count) {
		this.con_count = con_count;
	}

	public int getExt_count() {
		return ext_count;
	}

	public void setExt_count(int ext_count) {
		this.ext_count = ext_count;
	}

	public int getCom_count() {
		return com_count;
	}

	public void setCom_count(int com_count) {
		this.com_count = com_count;
	}

	public int getCont_count() {
		return cont_count;
	}

	public void setCont_count(int cont_count) {
		this.cont_count = cont_count;
	}

	public String getCaller_sym() {
		return caller_sym;
	}

	public void setCaller_sym(String caller_sym) {
		this.caller_sym = caller_sym;
	}

	public String getCallee_sym() {
		return callee_sym;
	}

	public void setCallee_sym(String callee_sym) {
		this.callee_sym = callee_sym;
	}
}
