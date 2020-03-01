package smartx.system.web.service;

import java.util.HashMap;

public class ParameterUtil {

	/**
	 * 判断参数是否传递
	 * @return true-参数存在且不为空串
	 */
	public static boolean hasParameter(HashMap param,String pname){
		if(!param.containsKey(pname)){
			return false;
		}
		Object v=param.get(pname);
		if(v instanceof String){
			return !((String)v).trim().equals("");			
		}else if(v instanceof String[]){
			//至少要传递一个非空串的参数
			String[] vs=(String[])v;
			if(vs.length==0) return false;
			for(int i=0;i<vs.length;i++){
				if(!vs[i].trim().equals("")){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
		
	}
	
	
	/**
	 * 获得用户Insert语句的参数值
	 * 1)参数没有或者空串：返回"null"
	 * @param param
	 * @param pname
	 * @param type 数据类型：String、Date、Datetime、Number
	 */
	public static String getInsertParameterValue(HashMap param,String pname,String type){
		String tmp=param.containsKey(pname)?(((String[])param.get(pname))[0]):null;
		tmp=tmp.trim().equals("")?null:tmp;
		if(type.equalsIgnoreCase("string")){
			return tmp==null?"null":("'"+tmp+"'");
		}else if(type.equalsIgnoreCase("date")){
			return tmp==null?"null":("to_date('"+tmp+"','yyyy-MM-dd')");
		}else if(type.equalsIgnoreCase("datetime")){
			return tmp==null?"null":("to_date('"+tmp+"','yyyy-MM-dd HH24:mm:dd')");
		}else{
			return tmp==null?"null":tmp;			
		}
	}
	
	/**
	 * 获得用户Update语句的参数值
	 * 1)参数没有或者空串：返回"null"
	 * @param param
	 * @param type 数据类型：String、Date、Datetime、Number
	 */
	public static String getUpdateParameterValue(HashMap param,String pname,String type){
		String tmp=param.containsKey(pname)?(((String[])param.get(pname))[0]):null;
		tmp=(tmp==null||tmp.trim().equals(""))?null:tmp;
		if(type.equalsIgnoreCase("string")){
			return tmp==null?"":(","+pname+"='"+tmp+"'");
		}else if(type.equalsIgnoreCase("date")){
			return tmp==null?"":(","+pname+"=to_date('"+tmp.substring(0,10)+"','yyyy-MM-dd')");
		}else if(type.equalsIgnoreCase("datetime")){
			return tmp==null?"":(","+pname+"=to_date('"+tmp+"','yyyy-MM-dd HH24:mm:dd')");
		}else{
			return tmp==null?"":(","+pname+"="+tmp);			
		}
	}
	
	/**
	 * 获得用户Where语句的参数值
	 * 1)参数没有或者空串：返回"null"
	 * @param param
	 * @param type 数据类型：String、Date、Datetime、Number
	 */
	public static String getWhereParameterValue(HashMap param,String type){
		return null;
	}
}
