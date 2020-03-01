package smartx.publics.databatch.ui;


import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import java.util.*;

import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.publics.databatch.bs.DataBatchConstant;
import smartx.publics.databatch.bs.DataBatchDefine;
/**
 * <p>Title:批量增删:根据Excel文件,Xml配置文件,目标数据表,导入方式,目标数据源名称</p>
 * <p>protected方法暂时不对外提供.这些方法在JDK1.5下编译简单测试过,后移植到JDK1.4经过修改没有测试</P>
 * <p>Description: NOVA </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author Yang Huan
 * @version 1.0
 */
public class DataBatchDeal {
	private int failedCount;
	
	private int succCount;
	
	private ArrayList failedStart;
	
	private boolean ret ;
	
	private String errorTail = "入参错误:";
	
	private String errorTableName = "表名不能为空！";
	
	private String errorExcel = "Excel为空或者是格式错误！";
	
	private String errorSourceName = "连接用的数据源名不能为空";
	
	private String errorImport = "导入文件失败:";
	
	

	/**
	 * 给内部参数初始话
	 *
	 */
	private void setResult(){
		failedCount = 0;
		succCount = 0;
		failedStart = null;
		ret = false;
	}
	
	
	/**
	 * 调用本类中批量增删方法后,获取失败行数
	 * @return							失败行数
	 */
	public int getFailedCount(){
		return failedCount;
	}
	
	
	/**
	 * 调用本类中批量增删方法后,获取成功行数
	 * @return							成功行数
	 */
	public int getSuccCount(){
		return succCount;
	}
	
	
	/**
	 * 调用本类中批量增删方法后,如果getSuccCount()>0可获取失败起始行的值
	 * @return							失败起始行的值
	 */
	public ArrayList getFailedStart(){
		return failedStart;
	}
	
	
	/**
	 * 批量增删
	 * @param in_excelPath				数据来源Excel文件路径
	 * @param in_tableName				数据导入的目标表名称
	 * @param in_importType				导入方式			0:插入;	-1:删除;
	 * @param in_dataSourceName			Oracle数据源
	 * @return
	 * @throws Exception
	 */
	public boolean DataBatch(String in_excelPath,String in_tableName, int in_importType, String in_dataSourceName)throws Exception{
		setResult();
		if (!in_excelPath.endsWith(DataBatchConstant.IMPORT__FILE_TAIL) || in_excelPath.equals("") || in_excelPath == null) {
			System.out.println(errorTail + errorExcel); 
			throw new Exception(errorTail + errorExcel);
		}
		if (in_tableName.toString().equals("") || in_tableName == null) {
			System.out.println(errorTail + errorTableName); 
			throw new Exception(errorTail + errorTableName);
		}
		if(in_dataSourceName == null || in_dataSourceName.equals("")){
			System.out.println(errorTail + errorSourceName); 
			throw new Exception(errorTail + errorSourceName);
		}
		try{
			InputStream excelStream = new FileInputStream(new File(in_excelPath));
			
			DataBatchParse parese = new DataBatchParse();
			DataBatchDefine define = parese.xmlParse(in_tableName);
			ArrayList arr = parese.excelParse(excelStream, define);
		
			DataBatchDealIFC ifc  =(DataBatchDealIFC) NovaRemoteServiceFactory.getInstance().lookUpService(DataBatchDealIFC.class);
			if(in_importType != 0){
				succCount = ifc.deleteBatch(in_dataSourceName, arr, define);
			}else{
				succCount = ifc.insertBatch(in_dataSourceName, arr, define);
			}
			if(succCount == arr.size()){
				ret = true;
			}else{
				failedCount = arr.size() - succCount;
				failedStart = (ArrayList)arr.get(succCount);
				ret = false;
			}
		}catch(Exception e){
			 System.out.println(errorImport);
			 e.printStackTrace();
			 throw new Exception(errorImport+e.getMessage());
		}
		return ret;
	}
	
	
	/**
	 * 批量增删
	 * @param in_excelPath				数据来源Excel文件路径
	 * @param in_tableName				数据导入的目标表名称
	 * @param in_importType				导入方式			0:插入;	-1:删除;
	 * @param in_dataSourceName			Oracle数据源
	 * @return
	 * @throws Exception
	 */
	protected Map insertDataBatch(InputStream in_excelStream,String in_tableName,  String in_dataSourceName, int in_insertType)throws Exception{
		setResult();
		Map mapResult = null;
		if (in_excelStream == null) {
			System.out.println(errorTail + errorExcel); 
			throw new Exception(errorTail + errorExcel);
		}
		if (in_tableName.toString().equals("") || in_tableName == null) {
			System.out.println(errorTail + errorTableName); 
			throw new Exception(errorTail + errorTableName);
		}
		if(in_dataSourceName == null || in_dataSourceName.equals("")){
			System.out.println(errorTail + errorSourceName); 
			throw new Exception(errorTail + errorSourceName);
		}
		try{
			DataBatchParse parese = new DataBatchParse();
			DataBatchDefine define = parese.xmlParse(in_tableName);
			ArrayList list_exceData = parese.excelParse(in_excelStream, define);
			
			DataBatchDealIFC ifc  =(DataBatchDealIFC) NovaRemoteServiceFactory.getInstance().lookUpService(DataBatchDealIFC.class);
			
			mapResult = (Map)ifc.insertBatch(in_dataSourceName, list_exceData, define, in_insertType);		
		}catch(Exception e){
			 System.out.println(errorImport);
			 e.printStackTrace();
			 throw new Exception(errorImport+e.getMessage());
		}
		return mapResult;
	}
	
	/**
	 * 
	 * @param in_excelStream			数据来源Excel文件的InputStream流	
	 * @param in_tableName				数据导入的目标表名称
	 * @param in_importType				导入方式			0:插入;	-1:删除;
	 * @param in_dataSourceName			连接目标数据库的源名称
	 */
	public boolean DataBatch(InputStream in_excelStream,  String in_tableName, int in_importType, String in_dataSourceName)throws Exception{
		setResult();
		if (in_excelStream == null) {
			System.out.println(errorTail + errorExcel); 
			throw new Exception(errorTail + errorExcel);
		}
		if (in_tableName.toString().equals("") || in_tableName == null) {
			System.out.println(errorTail + errorTableName); 
			throw new Exception(errorTail + errorTableName);
		}
		if(in_dataSourceName == null || in_dataSourceName.equals("")){
			System.out.println(errorTail + errorSourceName); 
			throw new Exception(errorTail + errorSourceName);
		}
		try{			
			DataBatchParse parese = new DataBatchParse();
			DataBatchDefine define = parese.xmlParse(in_tableName);
			
			ArrayList arr = parese.excelParse(in_excelStream, define);
		
			DataBatchDealIFC ifc  =(DataBatchDealIFC) NovaRemoteServiceFactory.getInstance().lookUpService(DataBatchDealIFC.class);
			if(in_importType != 0){
				succCount = ifc.deleteBatch(in_dataSourceName, arr, define);
			}else{
				succCount = ifc.insertBatch(in_dataSourceName, arr, define);
			}
			if(succCount == arr.size()){
				ret = true;
			}else{
				ret = false;
				failedCount = arr.size() - succCount;
				failedStart = (ArrayList)arr.get(succCount);
			}
		}catch(Exception e){
			 System.out.println(errorImport);
			 e.printStackTrace();
			 throw new Exception(errorImport+e.getMessage());
		}
		return ret;
	}
	
	
	/**
	 * 弹出对话框选择要导入文件,从默认位置读取配置文件
	 * @param in_tableName				数据导入的目标表名称
	 * @param in_importType				导入方式			0:插入;	-1:删除;
	 * @param in_dataSourceName
	 * @return
	 * @throws Exception				入参错误抛出的异常
	 */
	public boolean DataBatch(String in_tableName, int in_importType,String in_dataSourceName) throws Exception{
		setResult();
		if (in_tableName.toString().equals("") || in_tableName == null) {
			System.out.println(errorTail + errorTableName); 
			throw new Exception(errorTail + errorTableName);
		}
		if(in_dataSourceName == null || in_dataSourceName.equals("")){
			System.out.println(errorTail + errorSourceName); 
			throw new Exception(errorTail + errorSourceName);
		}
		DataBatchDialog dialog = new DataBatchDialog(in_tableName, in_importType, in_dataSourceName);
		succCount = dialog.getSuccCount();
		failedCount = dialog.getFailedCount();
		failedStart = dialog.getFailedStart();
		if(failedCount==0 && failedStart == null){
			ret = true;
		}else{
			ret = false;
		}
		return ret;
	}
	
	
	/**
	 * 失败项处理
	 * 待扩展项,原意想根据将数据来源Excel文件所有失败项都做上红色标识
	 * @param in_excelFile					数据来源Excel文件路径	
	 * @param in_arr						
	 * @param in_sucRowNum
	 * @return
	 */
	protected boolean failedDeal(File in_excelFile, ArrayList in_arr, int in_sucRowNum){
		return ret;
	}
	
	
	/**
	 * 批量增删
	 * @param in_excelPath				数据来源Excel文件路径
	 * @param in_xmlPath				配置文件,描述了数据来源和目标数据的匹配规则
	 * @param in_tableName				数据导入的目标表名称
	 * @param in_importType				导入方式			0:插入;	-1:删除;
	 * @param in_dataSourceName			连接数据库的源名称
	 * @return							全部成功:boolean; 有失败:false,并且可以根据getFailedCount()和getFailedStart()获取失败详细信息
	 * @throws Exception			
	 */
	protected boolean DataBatch(String in_excelPath, String in_xmlPath, String in_tableName, int in_importType, String in_dataSourceName)throws Exception{
		File xmlFile = null;
		setResult();
		if (!in_excelPath.endsWith(DataBatchConstant.IMPORT__FILE_TAIL) || in_excelPath.equals("") || in_excelPath == null) {
			System.out.println(errorTail + errorExcel); 
			throw new Exception(errorTail + errorExcel);
		}
		if (in_tableName.toString().equals("") || in_tableName.equals(null)) {
			System.out.println(errorTail + errorTableName); 
			throw new Exception(errorTail + errorTableName);
		}
		if(in_dataSourceName == null || in_dataSourceName.equals("")){
			System.out.println(errorTail + errorSourceName); 
			throw new Exception(errorTail + errorSourceName);
		}
		if (!in_xmlPath.endsWith("xml") || in_xmlPath.equals("") || in_xmlPath == null) {
			 xmlFile = null;
		}else{
			xmlFile = new File(in_xmlPath);
		}
		try{
			InputStream excelStream = new FileInputStream(new File(in_excelPath));
			
			DataBatchParse parese = new DataBatchParse();
			DataBatchDefine define = parese.xmlParse(xmlFile, in_tableName);
			ArrayList arr = parese.excelParse(excelStream, define);
		
			DataBatchDealIFC ifc  =(DataBatchDealIFC) NovaRemoteServiceFactory.getInstance().lookUpService(DataBatchDealIFC.class);
			if(in_importType != 0){
				succCount = ifc.deleteBatch(in_dataSourceName, arr, define);
			}else{
				succCount = ifc.insertBatch(in_dataSourceName, arr, define);
			}

			if(succCount == arr.size()){
				ret = true;
			}else{
				failedCount = arr.size() - succCount;
				failedStart = (ArrayList)arr.get(succCount);
				ret = false;
			}
		}catch(Exception e){
			 System.out.println(errorImport);
			 e.printStackTrace();
			 throw new Exception(errorImport+e.getMessage());
		}
		return ret;
	}
	
	
	/**
	 * 
	 * @param in_excelFile				数据来源Excel文件路径	
	 * @param in_xmlFile				配置文件,描述了数据来源和目标数据的匹配规则.为空则读取固定位置的配置文件
	 * @param in_tableName				数据导入的目标表名称
	 * @param in_importType				导入方式			0:插入;	-1:删除;
	 * @param in_dataSourceName			连接目标数据库的源名称
	 */
	protected boolean DataBatch(InputStream in_excelStream, File in_xmlFile, String in_tableName, int in_importType, String in_dataSourceName)throws Exception{
		setResult();
		if (in_excelStream == null) {
			System.out.println(errorTail + errorExcel); 
			throw new Exception(errorTail + errorExcel);
		}
		if (in_tableName.toString().equals("") || in_tableName == null) {
			System.out.println(errorTail + errorTableName); 
			throw new Exception(errorTail + errorTableName);
		}
		if(in_dataSourceName == null || in_dataSourceName.equals("")){
			System.out.println(errorTail + errorSourceName); 
			throw new Exception(errorTail + errorSourceName);
		}
		try{
			DataBatchParse parese = new DataBatchParse();
			
			DataBatchDefine define = parese.xmlParse(in_xmlFile, in_tableName);
			ArrayList arr = parese.excelParse(in_excelStream, define);
		
			DataBatchDealIFC ifc  =(DataBatchDealIFC) NovaRemoteServiceFactory.getInstance().lookUpService(DataBatchDealIFC.class);
			if(in_importType != 0){
				succCount = ifc.deleteBatch(in_dataSourceName, arr, define);
			}else{
				succCount = ifc.insertBatch(in_dataSourceName, arr, define);
			}
			if(succCount == arr.size()){
				ret = true;
			}else{
				ret = false;
				failedCount = arr.size() - succCount;
				failedStart = (ArrayList)arr.get(succCount);
			}
		}catch(Exception e){
			 System.out.println(errorImport);
			 e.printStackTrace();
			 throw new Exception(errorImport+e.getMessage());
		}
		return ret;
	}
	

}
