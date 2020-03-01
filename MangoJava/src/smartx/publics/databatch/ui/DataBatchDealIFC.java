package smartx.publics.databatch.ui;


import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import smartx.framework.common.ui.NovaRemoteCallServiceIfc;
import smartx.publics.databatch.bs.DataBatchDefine;

/**
 * <p>Title:导入到数据库的远程接口</p>
 * <p>Description: NOVA </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author Yang Huan
 * @version 1.0
 */
public interface DataBatchDealIFC extends NovaRemoteCallServiceIfc {
	

	/**
	 * 批量增删:插入数据库
	 * @param in_dataSourceName				数据源名称		
	 * @param in_data						待导入数据
	 * @param in_define						导入约束
	 * @return								成功插入的条数
	 * @throws Exception
	 */
	public int insertBatch(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define) throws Exception;
	
	
	/**
	 * 
	 * @param in_dataSourceName				数据源名称		
	 * @param in_data						待导入数据
	 * @param in_define						导入约束
	 * @param in_insertType					插入方式：
	 * @return
	 * @throws Exception
	 */
	public Map insertBatch(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define, int in_insertType)throws Exception;
	/**
	 * 批量增删:更新数据库
	 * @param in_dataSourceName				数据源名称		
	 * @param in_data						待导入数据
	 * @param in_define						导入约束
	 * @throws Exception
	 */
	public void updateBatch(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define) throws Exception;
	
	/**
	 * 批量增删:删除数据
	 * @param in_dataSourceName				数据源名称		
	 * @param in_data						待导入数据
	 * @param in_define						导入约束
	 * @return								成功删除的条数
	 * @throws Exception
	 */
	public int deleteBatch(String in_dataSourceName, ArrayList in_data, DataBatchDefine in_define) throws Exception;
	
	
	/**
	 * 解析xml配置文件
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public DataBatchDefine parseXml(String in_tableName)throws Exception;
	
	
	/**
	 * 解析xml配置文件
	 * @param in_file						XML配置文件
	 * @param in_tableName
	 * @return
	 * @throws Exception
	 */
	public DataBatchDefine parseXml(File in_file, String in_tableName)throws Exception;
	
}
