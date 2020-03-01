package smartx.publics.databatch.bs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import java.io.*;

import smartx.publics.databatch.ui.*;



/**
 * <p>Title:导入到数据库的实现类和解析配置文件方法</p>
 * <p>Description: NOVA </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author Yang Huan
 * @version 1.0
 */

public class DataBatchDeaImpl implements DataBatchDealIFC
{
	private Connection conn = null;
	
	private PreparedStatement p_stmt = null; 
	
    private int sucRowNum ;
    
    private int failedRowNum;
    
    private int	reRowNum;
    
    private String errorConn = "数据连接异常:";
    private String errorUpdate = "更新数据操作失败:";
    private String errorInsert = "插入数据操作失败:";
    private String errorDelete = "删除数据操作失败:";
    private String errorAllConstraint = "更新数据库必须有非条件项目(即不能所有列的isConstraint都为true)!";
    private String errorNullConstraint = "更新数据库必须有条件项目(即不能所有列的isConstraint都为false)!";
    
    private String errorXmlNull = "解析出xml文件无效:对应属性值为空";
    private String errorXml = "解析xml文件失败:";
    
    
    /**
     * 连接数据库
     * @param in_dataSourceName				数据源名
     * @return								连接
     * @throws Exception
     */
    public Connection getConn(String in_dataSourceName) throws Exception{
    	try{
    		String str_dburl = "jdbc:apache:commons:dbcp:" + in_dataSourceName; 
    		conn = DriverManager.getConnection(str_dburl); 
    		conn.setAutoCommit(false); 
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new Exception(this.errorConn);
    	}
    	return conn;
    }

    
    /**
     * 取出已经重复项的序号
     * @param in_data
     * @param in_def
     * @throws Exception
     */
    protected List queryBatch(String in_datasourcename,ArrayList in_data, DataBatchDefine in_def)throws Exception{
    	List listRowNum = new ArrayList();
    	StringBuffer str_sql = new StringBuffer();
		int colCount = in_def.getColCount();
		String tablename = in_def.getTableName();
		String[] columnName = in_def.getColumnName();
		String[] type = in_def.getType();
		boolean[] isConstraint = in_def.getIsConstraint();
		if (colCount==0 || columnName.length == 0 || columnName == null)
		{
			Exception e = new Exception(
					DataBatchConstant.INVALID_CONFIG_FILE);
			throw e;
		}
		
		str_sql.append("select count(*) from "+tablename+" where ");
		List listConstrain = new ArrayList();
		for(int i=0;i<colCount-1;i++){
			if(isConstraint[i]){
				str_sql.append(columnName[i]+"=? and ");
				listConstrain.add(new Integer(i));
			}
		}
		if(isConstraint[colCount-1]){
			str_sql.append(columnName[colCount-1]+"=? ");
		}else{
			int in = str_sql.lastIndexOf("and");
			String sql = str_sql.substring(0, in);
			str_sql = new StringBuffer(sql);
		}
		
		System.out.println("...queryBatch...str_sql="+str_sql);
		
		try{
		    conn = getConn(in_datasourcename); 
	        p_stmt = conn.prepareStatement(str_sql.toString());
	        for(int rowNum=0;rowNum<in_data.size();rowNum++){
	        	ArrayList data_row = (ArrayList)in_data.get(rowNum);
	        	//int col_total = data_row.size();
	        	//根据表列数 循环填充一行
	        	for(int i=0;i<listConstrain.size();i++){
		        	int col_num = ((Integer)listConstrain.get(i)).intValue();
	        		String currRowData = data_row.get(col_num).toString();
	        		
	        		if(type[col_num].equals("Num") || type[col_num].equals("num")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(col_num+1, 2);//2:java.sql.Types 
     	        		}else{
     	        			p_stmt.setInt(col_num+1, Integer.parseInt(currRowData));
     	        		}
	        		}else if(type[col_num].equals("Date") || type[col_num].equals("date")){
         				if(currRowData == null || currRowData.equals("")){
     	        			p_stmt.setNull(col_num+1, 91);
     	        		}else{
     	        			java.util.Date date_util = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(currRowData);
     	        			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");//
      			   			java.sql.Date curDate = java.sql.Date.valueOf(sdf.format(date_util));  
      			   			p_stmt.setDate((col_num+1), curDate);
     	        		}
	         		}else{
	         			if(currRowData == null || currRowData.equals("")){
	     	        		p_stmt.setNull(col_num+1, 12); 
	     	        	}else{
	     	        		p_stmt.setString(col_num+1, currRowData);
	     	        	}
	         		}
	 	        }
	        	ResultSet rs = p_stmt.executeQuery();
	        	int count = 0;
	        	while(rs.next()){
	        		count = rs.getInt(1);
	        		//System.out.println("....count=" + count);
	        	}
	        	if(count > 0){
	        		listRowNum.add(Integer.toString(rowNum));
	        	}	
	        }
	        conn.commit();
		}catch(Exception e){
			System.out.println("..查询数据异常");
			e.printStackTrace();
		}finally{
			try{if(p_stmt!=null){p_stmt.close();}}catch(Exception e){}
			try{if(conn!=null){conn.close();}}catch(Exception e){}
		}	
		 return listRowNum;
    }

    
   
    /**
     * 根据插入方式(强制全部插入,常规插入只插入不重复的项,)
     * 待扩展的方法
     * @param in_dataSourceName
     * @param in_data
     * @param in_def
     * @param in_insertType:				插入方式:
     * 			 							0	常规插入:自动判断，重复项不插入
     * 										-1	强制,不判断全部插入
     * 			 							1	待扩展
     * @throws Exception 根据客户端传过的数据 导入到数据库
     */
    public Map insertBatch(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define, int in_insertType)throws Exception{
    	Map mapResult = new Hashtable();
    	//mapResult.put(all,0);
    	mapResult.put("totalNum", new Integer(0));
    	mapResult.put("reRowNum", new Integer(0));
    	mapResult.put("sucRowNum", new Integer(0));
    	mapResult.put("failedRowNum", new Integer(0));
    	
    	mapResult.put("totalNum", Integer.toString(in_data.size()));
    	if(in_insertType == 0){
    		ArrayList listRowNum = (ArrayList)this.queryBatch(in_dataSourceName, in_data, in_define);
    		if(listRowNum == null || listRowNum.size()== 0){
    			sucRowNum = insertBatch(in_dataSourceName, in_data, in_define);
    		}else{
    			reRowNum = listRowNum.size();
    			mapResult.put("reRowNum", new Integer(reRowNum));
    			//System.out.println("......listRowNum="+listRowNum);
    			sucRowNum = insertNewBatch(in_dataSourceName,listRowNum, in_data, in_define);
    			mapResult.put("sucRowNum", new Integer(sucRowNum));
    			//System.out.println("......mapResult="+mapResult);
    		}
    	}else if(in_insertType == -1){
    		sucRowNum = insertBatch(in_dataSourceName, in_data, in_define);
    	}
    	failedRowNum = (in_data.size()-sucRowNum-reRowNum);
    	mapResult.put("failedRowNum", new Integer(failedRowNum));
    	return mapResult;
    }
    
    
    /**
     * 插入没有重复的项
     * @param in_dataSourceName			数据源名称
     * @param in_rowNum					已经重复项目的位置
     * @param in_data					待插入数据
     * @param in_define					导入约束
     * @return
     * @throws Exception
     */
    protected int insertNewBatch(String in_dataSourceName, ArrayList in_listRowNum,ArrayList in_data, DataBatchDefine in_define)throws Exception{
    	System.out.println("..insertNewBatch.");
    	sucRowNum = 0;
		StringBuffer str_sql = new StringBuffer();
		int colCount = in_define.getColCount();
		String tablename = in_define.getTableName();
		String[] columnName = in_define.getColumnName();
		String[] type = in_define.getType();
		List function = in_define.getFunction();
		if (colCount==0 || columnName.length == 0 || columnName == null)
		{
			Exception e = new Exception(errorInsert+
					DataBatchConstant.INVALID_CONFIG_FILE);
			throw e;
		}
		
		str_sql.append("insert into "+tablename+"(");
		for(int i=0;i<colCount-1;i++){
			str_sql.append(columnName[i]+",");
		}
		str_sql.append(columnName[colCount-1]);
		if(function!=null || function.size()!=0){
			str_sql.append(",");
			for(int i=0;i<function.size()-1;i++){
				String[] fun = (String[])function.get(i);
				str_sql.append(fun[0]+",");
			}
			String[] fun = (String[])function.get(function.size()-1);
			str_sql.append(fun[0]);
		}
		str_sql.append(") values(");
		for(int i=0;i<colCount-1;i++){
			str_sql.append("?,");
		}
		str_sql.append("?");
		if(function!=null || function.size()!=0){
			str_sql.append(",");
			for(int i=0;i<function.size()-1;i++){
				String[] fun = (String[])function.get(i);
				str_sql.append(fun[1]+",");
			}
			String[] fun = (String[])function.get(function.size()-1);
			str_sql.append(fun[1]);
		}
		str_sql.append(")");
		System.out.println("...insertBatch...str_sql="+str_sql);
		
		try{	
			conn = getConn(in_dataSourceName); 
	        p_stmt = conn.prepareStatement(str_sql.toString());
	        int rowNum = 0;
	        int rowForCommit = 0;
	        for(;rowNum<in_data.size();rowNum++){
	        	//该行的行序号在已经重复行号中没有记录
	        	int count = in_listRowNum.indexOf(Integer.toString(rowNum));
	        	 if(count == -1){
	        		 ArrayList data_row = (ArrayList)in_data.get(rowNum);
	        		 
	        		 //根据表列数 循环填充一行
	        		 for(int col_num=0;col_num<colCount;col_num++){
		 	        		String currRowData = data_row.get(col_num).toString();
		 	        		if(type[col_num].equals("Num") || type[col_num].equals("num")){
		         				if(currRowData == null || currRowData.equals("")){
		     	        			p_stmt.setNull(col_num+1, 2);//2:java.sql.Types 
		     	        		}else{
		     	        			p_stmt.setInt(col_num+1, Integer.parseInt(currRowData));
		     	        		}
		         			}else if(type[col_num].equals("Date") || type[col_num].equals("date")){
		         				if(currRowData == null || currRowData.equals("")){
		     	        			p_stmt.setNull(col_num+1, 91);
		     	        		}else{
		     	        			java.util.Date date_util = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(currRowData);
		     	        			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");//
		      			   			java.sql.Date curDate = java.sql.Date.valueOf(sdf.format(date_util));  
		      			   			p_stmt.setDate((col_num+1), curDate);
		     	        		}
		         			}else{
		         				if(currRowData == null || currRowData.equals("")){
		     	        			p_stmt.setNull(col_num+1, 12); 
		     	        		}else{
		     	        			p_stmt.setString(col_num+1, currRowData);
		     	        		}
		         			}
		 	        	}
		 	        	p_stmt.executeUpdate();
		 	        	rowForCommit++ ;
		 	        	if((rowForCommit)%200 == 0){
		 	        		conn.commit();
		 	        		sucRowNum = rowForCommit;
		 	        	}
	 	        } 
	 	        if((rowForCommit)%200 != 0 && rowForCommit!=0){
	         		conn.commit();
	         		sucRowNum = rowForCommit;
	 	        }
        	}
		}catch(Exception e){
			System.out.println(errorInsert);
			e.printStackTrace();
			throw new Exception(errorInsert+e.getMessage());
		}finally{
			try{if(p_stmt!=null){p_stmt.close();}}catch(Exception e){}
			try{if(conn!=null){conn.close();}}catch(Exception e){}
		}	
		return sucRowNum;
    }
    /**
     * 插入数据库
     * @param in_datasourcename				目标数据库连接源名称
     * @param in_data						待导入的数据
     * @param in_define						导入约束
     * @param 
     * @throws Exception 
     */
    public int insertBatch(String in_dataSourceName,ArrayList in_data, DataBatchDefine in_define)throws Exception{
		sucRowNum = 0;
		StringBuffer str_sql = new StringBuffer();
		int colCount = in_define.getColCount();
		String tablename = in_define.getTableName();
		String[] columnName = in_define.getColumnName();
		String[] type = in_define.getType();
		List function = in_define.getFunction();
		if (colCount==0 || columnName.length == 0 || columnName == null)
		{
			Exception e = new Exception(errorInsert+
					DataBatchConstant.INVALID_CONFIG_FILE);
			throw e;
		}
		
		str_sql.append("insert into "+tablename+"(");
		for(int i=0;i<colCount-1;i++){
			str_sql.append(columnName[i]+",");
		}
		str_sql.append(columnName[colCount-1]);
		if(function!=null || function.size()!=0){
			str_sql.append(",");
			for(int i=0;i<function.size()-1;i++){
				String[] fun = (String[])function.get(i);
				str_sql.append(fun[0]+",");
			}
			String[] fun = (String[])function.get(function.size()-1);
			str_sql.append(fun[0]);
		}
		str_sql.append(") values(");
		for(int i=0;i<colCount-1;i++){
			str_sql.append("?,");
		}
		str_sql.append("?");
		if(function!=null || function.size()!=0){
			str_sql.append(",");
			for(int i=0;i<function.size()-1;i++){
				String[] fun = (String[])function.get(i);
				str_sql.append(fun[1]+",");
			}
			String[] fun = (String[])function.get(function.size()-1);
			str_sql.append(fun[1]);
		}
		str_sql.append(")");
		//System.out.println("...insertBatch...str_sql="+str_sql);
		
		try{	
			conn = getConn(in_dataSourceName); 
	        p_stmt = conn.prepareStatement(str_sql.toString());
	        int rowNum = 0;
	        for(;rowNum<in_data.size();rowNum++){
	        	ArrayList data_row = (ArrayList)in_data.get(rowNum);
	        
	        	//根据表列数 循环填充一行
	        	for(int col_num=0;col_num<colCount;col_num++){
	        		String currRowData = data_row.get(col_num).toString();
	        		if(type[col_num].equals("Num") || type[col_num].equals("num")){
        				if(currRowData == null || currRowData.equals("")){
    	        			p_stmt.setNull(col_num+1, 2);//2:java.sql.Types 
    	        		}else{
    	        			p_stmt.setInt(col_num+1, Integer.parseInt(currRowData));
    	        		}
        			}else if(type[col_num].equals("Date") || type[col_num].equals("date")){
        				if(currRowData == null || currRowData.equals("")){
    	        			p_stmt.setNull(col_num+1, 91);
    	        		}else{
    	        			java.util.Date date_util = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(currRowData);
    	        			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");//
     			   			java.sql.Date curDate = java.sql.Date.valueOf(sdf.format(date_util));  
     			   			p_stmt.setDate((col_num+1), curDate);
    	        		}
        			}else{
        				if(currRowData == null || currRowData.equals("")){
    	        			p_stmt.setNull(col_num+1, 12); 
    	        		}else{
    	        			p_stmt.setString(col_num+1, currRowData);
    	        		}
        			}
	        		
	        	}
	        	p_stmt.executeUpdate();
	        	if((rowNum+1)%200 == 0){
	        		conn.commit();
	        		sucRowNum = rowNum;
	        	}
	        }
	        if((rowNum+1)%200 != 0){
        		conn.commit();
        		sucRowNum = rowNum;
        	}
		}catch(Exception e){
			System.out.println(errorInsert);
			e.printStackTrace();
			throw new Exception(errorInsert+e.getMessage());
		}finally{
			try{if(p_stmt!=null){p_stmt.close();}}catch(Exception e){}
			try{if(conn!=null){conn.close();}}catch(Exception e){}
		}	
		return sucRowNum;
	}
	
	
	/**
	 * 
	 * 根据客户端传过的数据 更新数据库
	 * 只需要考虑import的列
	 * @param in_dataSourceName					 	数据源名称,连接数据库用
     * @param in_data								待导入数据
     * @param in_define								导入约束
     * @param 
     * @throws Exception 
	 */
	public void updateBatch(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define)throws Exception{
		StringBuffer str_sql = new StringBuffer();
		String sql = "";
		int colCount = in_define.getColCount();
		String tablename = in_define.getTableName();
		String[] columnName = in_define.getColumnName();
		String[] type = in_define.getType();
		boolean[]	isConstraint = in_define.getIsConstraint();
		if (colCount==0 || columnName.length == 0 || columnName == null)
		{
			Exception e = new Exception(errorUpdate+
					DataBatchConstant.INVALID_CONFIG_FILE);
			throw e;
		}
		List setCount = new ArrayList();
		List whereCount = new ArrayList();
		
		//该部分先判断配置文件的合法性能
		int isc = 0;
		for(int i=0;i<isConstraint.length;i++){
			if(!isConstraint[i]){
				isc = 1;
			}
		}
		if(isc==0){
			throw new Exception(errorAllConstraint);
		}
		isc = 0;
		for(int i=0;i<isConstraint.length;i++){
			if(isConstraint[i]){
				isc = 2;
			}
		}
		if(isc==0){
			throw new Exception(errorNullConstraint);
		}
		str_sql.append("update "+tablename+" set ");
		for(int i=0;i<colCount-1;i++){
			if(!isConstraint[i]){
				str_sql.append(columnName[i]+"=? , ");
				setCount.add(new Integer(i));
			}
		}
		//System.out.println("...updateBatch...str_sql="+str_sql);
		if(!isConstraint[colCount-1]){
			str_sql.append(columnName[colCount-1]+"=? ");
			setCount.add(new Integer(colCount-1));
		}else{		
			int in = str_sql.lastIndexOf(",");
			sql = str_sql.substring(0, in-1);
			str_sql = new StringBuffer(sql);
		}
		str_sql.append(" where ");
		for(int i=0;i<colCount-1;i++){
			if(isConstraint[i]){
				str_sql.append(columnName[i]+"=? and ");
				whereCount.add(new Integer(i));
			}
		}
		if(isConstraint[colCount-1]){
			str_sql.append(columnName[colCount-1]+"=? ");
			whereCount.add(new Integer(colCount-1));
		}else{
			int in = str_sql.lastIndexOf("and");
			sql = str_sql.substring(0, in);
			str_sql = new StringBuffer(sql);
		}
		//System.out.println("...updateBatch...sql="+sql);
		
		try{	
	        conn = getConn(in_dataSourceName); 
	        p_stmt = conn.prepareStatement(str_sql.toString()); 
	        int rowNum = 0;
	        for(;rowNum<in_data.size();rowNum++){
	        	ArrayList data_row = (ArrayList)in_data.get(rowNum);	
	        
	        	//根据表列数 循环填充
	        	for(int i=0;i<setCount.size();i++){
	        		int colNum = Integer.parseInt((String)setCount.get(i));
	        		int pos = i+1;
	        		String currRowData = data_row.get(colNum).toString();
	        		if(type[colNum].equals("Num") || type[colNum].equals("num")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(pos, 2);
	        			}else{
	        				p_stmt.setInt(pos, Integer.parseInt(currRowData));
	        			}
	        		}else if(type[colNum].equals("Date") || type[colNum].equals("date")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(pos, 91);
	        			}else{
	        				java.util.Date date_util = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(currRowData);
    	        			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");//
     			   			java.sql.Date curDate = java.sql.Date.valueOf(sdf.format(date_util)); 
	        				p_stmt.setDate(pos, curDate);
	        			}
	        		}else{
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(pos, 12);
	        			}else{
	        				p_stmt.setString(pos, data_row.get(colNum).toString());
	        			}
	        		}	
	        	}
	        	for(int i=0;i<whereCount.size();i++){
	        		int colNum = Integer.parseInt((String)whereCount.get(i));
	        		int pos = setCount.size()+i+1;
	        		String currRowData = data_row.get(colNum).toString();
	        		if(type[colNum].equals("Num") || type[colNum].equals("num")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(pos, 2);
	        			}else{
	        				p_stmt.setInt(pos, Integer.parseInt(currRowData));
	        			}
	        		}else if(type[colNum].equals("Date") || type[colNum].equals("date")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(pos, 91);
	        			}else{
	        				java.util.Date date_util = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(currRowData);
    	        			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");//
     			   			java.sql.Date curDate = java.sql.Date.valueOf(sdf.format(date_util)); 
	        				p_stmt.setDate(pos, curDate);
	        			}
	        		}else{
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(pos, 12);
	        			}else{
	        				p_stmt.setString(pos, data_row.get(colNum).toString());
	        			}
	        		}	
	        	}
		        p_stmt.executeUpdate();
	        	conn.commit();
	        	if(rowNum%200 == 0){
	        		conn.commit();
	        	}
	        }
	        if(rowNum%200 != 0){
        		conn.commit();
        	}
		}catch(Exception e){
			System.out.println(errorUpdate);
			e.printStackTrace();
		}finally{
			try{if(p_stmt!=null){p_stmt.close();}}catch(Exception e){throw e;}
			try{if(conn!=null){conn.close();}}catch(Exception e){throw e;}
		}	
	}
	
	/**
	 * 
	 * 根据客户端传过的数据 删除数据库
	 * 不考虑配置文件中的isConstrait，只要解析出的列全部需要匹配才能够删除
	 * @param in_datasourcename				目标数据库连接源名称
     * @param in_data						待导入的数据
     * @param in_define						导入约束
     * @return								成功删除的条数
	 */
	public int deleteBatch(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define)throws Exception{
		sucRowNum = 0;
		StringBuffer str_sql = new StringBuffer();
		int colCount = in_define.getColCount();
		String tablename = in_define.getTableName();
		String[] columnName = in_define.getColumnName();
		String[] type = in_define.getType();
		if (colCount==0 || columnName.length == 0 || columnName == null)
		{
			Exception e = new Exception(
					DataBatchConstant.INVALID_CONFIG_FILE);
			throw e;
		}
		
		str_sql.append("delete "+tablename+" where ");
		for(int i=0;i<colCount-1;i++){
			str_sql.append(columnName[i]+"=? and ");
		}
		str_sql.append(columnName[colCount-1]+"=?");
		//System.out.println("...deal...str_sql="+str_sql);
		
		try{	
	        conn = getConn(in_dataSourceName); 
	        p_stmt = conn.prepareStatement(str_sql.toString()); 
	        int rowNum=0;
	        for(;rowNum<in_data.size();rowNum++){
	        	ArrayList data_row = (ArrayList)in_data.get(rowNum);
	        
	        	//根据表列数 循环填充一行
	        	for(int col_num=0;col_num<colCount;col_num++){
	        		String currRowData = data_row.get(col_num).toString();
	        		if(type[col_num].equals("Num") || type[col_num].equals("num")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(col_num+1, 2);
	        			}else{
	        				p_stmt.setInt(col_num+1, Integer.parseInt(currRowData));
	        			}
	        		}else if(type[col_num].equals("Date") || type[col_num].equals("date")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(col_num+1, 91);
	        			}else{
	        				java.util.Date date_util = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(currRowData);
    	        			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");//
     			   			java.sql.Date curDate = java.sql.Date.valueOf(sdf.format(date_util)); 
	        				p_stmt.setDate(col_num+1, curDate);
	        			}
	        		}else{
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(col_num+1, 12);
	        			}else{
	        				p_stmt.setString(col_num+1, currRowData);
	        			}
	        		}
	        	}
	        	p_stmt.executeUpdate();
	        	if((rowNum+1)%200 == 0){
	        		conn.commit();
	        		sucRowNum = rowNum;
	        	}
	        } 
	        if((rowNum+1)%200 != 0){
        		conn.commit();
        		sucRowNum = rowNum;
        	}
		}catch(Exception e){
			System.out.println(errorDelete);
			e.printStackTrace();
			throw new Exception(errorDelete+e.getMessage());
		}finally{
			try{if(p_stmt!=null){p_stmt.close();}}catch(Exception e){ throw e; }
			try{if(conn!=null){conn.close();}}catch(Exception e){ throw e; }
		}	
		return sucRowNum;
	}
	
	/**
	 * 批量删除,待完成
	 * 只需要匹配配置文件中的isConstrait为true的列
	 * @param in_dataSourceName
	 * @param in_data
	 * @param in_define
	 * @return
	 * @throws Exception
	 */
	public Map deleteBatchByKey(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define)throws Exception{
		sucRowNum = 0;
		StringBuffer str_sql = new StringBuffer();
		int colCount = in_define.getColCount();
		String tablename = in_define.getTableName();
		String[] columnName = in_define.getColumnName();
		String[] type = in_define.getType();
		boolean[] isConstraint = in_define.getIsConstraint();
		if (colCount==0 || columnName.length == 0 || columnName == null)
		{
			Exception e = new Exception(
					DataBatchConstant.INVALID_CONFIG_FILE);
			throw e;
		}
		
		str_sql.append("delete "+tablename+" where ");
		List listConstrain = new ArrayList();
		for(int i=0;i<colCount-1;i++){
			if(isConstraint[i]){
				str_sql.append(columnName[i]+"=? and ");
				listConstrain.add(new Integer(i));
			}
		}
		if(isConstraint[colCount-1]){
			str_sql.append(columnName[colCount-1]+"=? ");
		}else{
			int in = str_sql.lastIndexOf("and");
			String sql = str_sql.substring(0, in);
			str_sql = new StringBuffer(sql);
		}
		//System.out.println("...deal...str_sql="+str_sql);
		
		try{	
	        conn = getConn(in_dataSourceName); 
	        p_stmt = conn.prepareStatement(str_sql.toString()); 
	        int rowNum=0;
	        for(;rowNum<in_data.size();rowNum++){
	        	ArrayList data_row = (ArrayList)in_data.get(rowNum);
	        
	        	//根据表列数 循环填充一行
//	        	根据表列数 循环填充一行
	        	for(int i=0;i<listConstrain.size();i++){
		        	int col_num = Integer.parseInt((String)listConstrain.get(i));
	        		String currRowData = data_row.get(col_num).toString();
	        		
	        		if(type[col_num].equals("Num") || type[col_num].equals("num")){
	        			if(currRowData == null || currRowData.equals("")){
	        				p_stmt.setNull(col_num+1, 2);//2:java.sql.Types 
     	        		}else{
     	        			p_stmt.setInt(col_num+1, Integer.parseInt(currRowData));
     	        		}
	        		}else if(type[col_num].equals("Date") || type[col_num].equals("date")){
         				if(currRowData == null || currRowData.equals("")){
     	        			p_stmt.setNull(col_num+1, 91);
     	        		}else{
     	        			java.util.Date date_util = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(currRowData);
     	        			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");//
      			   			java.sql.Date curDate = java.sql.Date.valueOf(sdf.format(date_util));  
      			   			p_stmt.setDate((col_num+1), curDate);
     	        		}
	         		}else{
	         			if(currRowData == null || currRowData.equals("")){
	     	        		p_stmt.setNull(col_num+1, 12); 
	     	        	}else{
	     	        		p_stmt.setString(col_num+1, currRowData);
	     	        	}
	         		}
	        	}
	        	p_stmt.executeUpdate();
	        	if((rowNum+1)%200 == 0){
	        		conn.commit();
	        		sucRowNum = rowNum;
	        	}
	        } 
	        if((rowNum+1)%200 != 0){
        		conn.commit();
        		sucRowNum = rowNum;
        	}
		}catch(Exception e){
			System.out.println(errorDelete);
			e.printStackTrace();
			throw new Exception(errorDelete+e.getMessage());
		}finally{
			try{if(p_stmt!=null){p_stmt.close();}}catch(Exception e){ throw e; }
			try{if(conn!=null){conn.close();}}catch(Exception e){ throw e; }
		}	
		return null;
	}
	
	
	/**
	 * 解析XMl配置文件
	 * @param in_file
	 * @param in_tableName
	 * @return
	 * @throws Exception
	 */
	public DataBatchDefine parseXml(File in_file, String in_tableName)throws Exception{
		DataBatchDefine define = null;
		try{
			DataBatchUtil test = DataBatchUtil.getInstance(in_file, in_tableName);
			define = test.getFileTransferDef();
			if(define.getColCount()==0 || define.getColumnName().length==0 || define.getColumnNum().length==0){
				throw new Exception(errorXmlNull);
			}
		}catch(Exception e){
			throw new Exception(errorXml+e.getMessage());
		}
		return define;
	}	
	
	
	/**
	 * 解析XMl配置文件
	 * @param  in_tableName					数据导入目标表的名称			
	 * @return								导入约束
	 * @throws Exception
	 */
	public DataBatchDefine parseXml(String in_tableName)throws Exception{
		DataBatchDefine define = null;
		try{
			DataBatchUtil test = DataBatchUtil.getInstance(in_tableName);
			define = test.getFileTransferDef();
			if(define.getColCount()==0 || define.getColumnName().length==0 || define.getColumnNum().length==0){
				throw new Exception(errorXmlNull);
			}
		}catch(Exception e){
			throw new Exception(errorXml+e.getMessage());
		}
		return define;
	}	
	
	
}
