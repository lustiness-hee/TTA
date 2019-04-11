package Hongik.Selab.Reverse.QualityProperties.Calculate;

public class Coupling {
	
	public int getDatCouping(String para){
		String[] argu;
		int count = 0;
		if(para.equalsIgnoreCase("")){
			return count;
		}else{
			argu = para.split(",");
			if(argu.length == 1){
				if(para.equalsIgnoreCase("int") ||para.equalsIgnoreCase("LONG")||para.equalsIgnoreCase("float")
						||para.equalsIgnoreCase("double")||para.equalsIgnoreCase("char")
						||para.equalsIgnoreCase("short")|| para.equalsIgnoreCase("BOOL") || para.equalsIgnoreCase("boolean")
						||para.equalsIgnoreCase("void")){
					count++;
				}
				return count;
			}else{
				for(int i =0;i < argu.length; i++){
					if(argu[i].equalsIgnoreCase("int") ||argu[i].equalsIgnoreCase("LONG")||argu[i].equalsIgnoreCase("float")
							||argu[i].equalsIgnoreCase("double")||argu[i].equalsIgnoreCase("char")
							||argu[i].equalsIgnoreCase("short")|| argu[i].equalsIgnoreCase("BOOL")  || para.equalsIgnoreCase("boolean")
							||argu[i].equalsIgnoreCase("void")){
						count++;					
					}
				}
				return count;
			}
		}
	}
	
	public int getStmpCouping(String para){
		String[] argu;
		int count = 0;
		if(para.equalsIgnoreCase("")){
			count++;
			return count;
		}else{
			argu = para.split(",");
			if(argu.length == 1){
				if(!(para.equalsIgnoreCase("int") ||para.equalsIgnoreCase("LONG")||para.equalsIgnoreCase("float")
						||para.equalsIgnoreCase("double")||para.equalsIgnoreCase("char")
						||para.equalsIgnoreCase("short")|| para.equalsIgnoreCase("BOOL") || para.equalsIgnoreCase("boolean")
						||para.equalsIgnoreCase("void"))){
					count++;
				}
				return count;
			}else{
				for(int i =0;i < argu.length; i++){
					if(!(argu[i].equalsIgnoreCase("int") ||argu[i].equalsIgnoreCase("LONG")||argu[i].equalsIgnoreCase("float")
							||argu[i].equalsIgnoreCase("double")||argu[i].equalsIgnoreCase("char")
							||argu[i].equalsIgnoreCase("short")|| argu[i].equalsIgnoreCase("BOOL") || para.equalsIgnoreCase("boolean")
							||argu[i].equalsIgnoreCase("void"))){
						count++;					
					}
				}
				return count;
			}
		}
	}
	
	public int getConCouping(String ret){
		
		if(ret.equalsIgnoreCase("BOOL")||ret.equals("HRESULT")  || ret.equalsIgnoreCase("boolean") ){
			return 1;
		}else{
			return 0;
		}
	}
	
	public int getExtCouping(String para){
		if(para.contains("file")||para.contains("File")||para.contains("Path")){
			return 1;
		}else{
			return 0;
		}
	}
	
	public int getComCouping(int i){
		
		if(i>0){
			return 1;
		}else{
			return 0;
		}
	}
	
	public String SumString(int dat,int stmp,int con,int ext,int com,int cont){
		double sum = 0;
		String S_Sum ="" ;
		sum = sum(dat,stmp,con,ext,com,cont); 
		if(dat!=0){
			S_Sum += "[D * "+dat+"]";
		} 
		if(stmp!=0){
			S_Sum += "[S * "+stmp+"]";
		}
		if(con!=0){
			S_Sum += "[CL * "+con+"]";
		}
		if(ext!=0){
			S_Sum += "[E * "+ext+"]";
		}
		if(com!=0){
			S_Sum += "[CN * "+com+"]";
		}
		if(cont!=0){
			S_Sum += "[CT * "+cont+"]";
		}
		return S_Sum+" = "+sum+" ";
		//return "("+sum+")";
	}
	
	public int sum(int dat,int stmp,int con,int ext,int com,int cont){
		float sum = 0;
		
		sum += dat*1*1;
		sum += stmp*2*1;
		sum += con*3*1;
		sum += ext*5*1.25;
		sum += com*5*1.4;
		sum += cont*6*1.7;
		
		return (int)sum;
	}
}

