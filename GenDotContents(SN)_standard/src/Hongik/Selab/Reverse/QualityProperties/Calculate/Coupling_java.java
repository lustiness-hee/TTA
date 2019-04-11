package Hongik.Selab.Reverse.QualityProperties.Calculate;

public class Coupling_java {
	public int getDatCouping(String para) {
		String[] argu;
		int count = 0;
		if (para.equalsIgnoreCase("")) {
			return count;
		} else {
			argu = para.split(",");
			if (argu.length == 1) {
				if (isData_Stamp_True(para)) {
					count++;
				}
				return count;
			} else {
				for (int i = 0; i < argu.length; i++) {
					if (isData_Stamp_True(argu[i])) {
						count++;
					}
				}
				return count;
			}
		}
	}

	public int getStmpCouping(String para) {
		String[] argu;
		int count = 0;
		if (para.equalsIgnoreCase("")) {
			count++;
			return count;
		} else {
			argu = para.split(",");
			if (argu.length == 1) {
				if (!isData_Stamp_True(para)&&!para.contains("boolean")) {
					count++;
				}
				return count;
			} else {
				for (int i = 0; i < argu.length; i++) {
					if (!isData_Stamp_True(argu[i])&&!para.contains("boolean")) {
						count++;
					}
				}
				return count;
			}
		}
	}

	public int getConCouping(String return_value) {
		if (return_value.equalsIgnoreCase("boolean")) {
			return 1;
		} else {
			return 0;
		}
	}

	public int getExtCouping(String object_value) {
		if (object_value.equalsIgnoreCase("File") || object_value.equalsIgnoreCase("InputStream")
				|| object_value.equalsIgnoreCase("OutputStream") || object_value.equalsIgnoreCase("Reader")
				|| object_value.equalsIgnoreCase("Writer") || object_value.equalsIgnoreCase("FileDescriptor")
				|| object_value.equalsIgnoreCase("FilePermission")|| object_value.equalsIgnoreCase("http")
				|| object_value.equalsIgnoreCase("Connection")) {
			return 1;
		} else {
			return 0;
		}
	}

	public String SumString(int dat, int stmp, int con, int ext, int com, int cont) {
		double sum = 0;
		String S_Sum = "";
		sum = sum(dat, stmp, con, ext, com, cont);
		if (dat != 0) {
			S_Sum += "[D * " + dat + "]";
		}
		if (stmp != 0) {
			S_Sum += "[S * " + stmp + "]";
		}
		if (con != 0) {
			S_Sum += "[CL * " + con + "]";
		}
		if (ext != 0) {
			S_Sum += "[E * " + ext + "]";
		}
		if (com != 0) {
			S_Sum += "[CN * " + com + "]";
		}
		if (cont != 0) {
			S_Sum += "[CT * " + cont + "]";
		}
		return S_Sum + " = " + sum + " ";
	}

	public int sum(int dat, int stmp, int con, int ext, int com, int cont) {
		float sum = 0;

		sum += dat * 1 * 1;
		sum += stmp * 2 * 1;
		sum += con * 3 * 1;
		sum += ext * 5 * 1.25;
		sum += com * 5 * 1.4;
		sum += cont * 6 * 1.7;

		return (int) sum;
	}

	private boolean isData_Stamp_True(String parameter) {
		boolean is_true = parameter.equalsIgnoreCase("int") || parameter.equalsIgnoreCase("long")
				|| parameter.equalsIgnoreCase("float") || parameter.equalsIgnoreCase("double")
				|| parameter.equalsIgnoreCase("char") || parameter.equalsIgnoreCase("short")
				|| parameter.equalsIgnoreCase("void");
		return is_true;
	}
}
