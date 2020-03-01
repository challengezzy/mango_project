package smartx.publics.form.bs.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;
import smartx.framework.common.bs.NovaInitContext;
import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.bs.FrameWorkMetaDataServiceImpl;
import smartx.framework.metadata.bs.NovaServerEnvironment;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
import smartx.publics.file.FileConstant;
import smartx.publics.form.bs.utils.TempletUtil;

public class FileGenerateService {
	
	private Logger logger = NovaLogger.getLogger(FileGenerateService.class);
	
	private NovaInitContext initContext = null; //
	
	private String MAP_FILENAME = "FILENAME";
	
	/**
	 * 查询记录总数
	 */
	private int rowsTotal = 0;
	
	/**
	 * 根据MAP，对列名称进行转换
	 * @param column
	 * @param columnMap
	 * @return
	 */
	public String convertColumnName(String column,Map<String,String> columnMap){
		String mapName = columnMap.get(column.toUpperCase());
		
		//如果未找到映射关系，则使用原值
		if(mapName == null)
			mapName = column.toUpperCase();
		
		return mapName;
	}
	
	/**
	 * 生成OLAP需要的CSV文件，返回文件名和总记录数
	 * @param datasource
	 * @param sql
	 * @param dirPath
	 * @param columnMap
	 * @param charSet
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> generateOlapCsvFile(String datasource,String sql, String dirPath,Map<String, String> columnMap,String charSet) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MAP_FILENAME, generateCSVFile(datasource, sql, dirPath, columnMap, charSet));
		result.put(DMOConst.ROWCOUNT, rowsTotal);
		
		logger.debug("根据SQL[" + sql + "]生成CSV文件结束，数据记录数[" + rowsTotal + "]条.");
		return result;
	}
	
	/**
	 * 使用CommDMO返回数据，内存开销太大，直接使用 resultset生成需要的文件
	 * @param datasource 数据源
	 * @param sql 查询sql
	 * @param dirPath 存储路径
	 * @param columnMap 列别名
	 * @param charSet 字符编码集 如GBK,UTF-8
	 * @return
	 * @throws Exception
	 */
	public String generateCSVFile(String datasource,String sql, String dirPath,Map<String, String> columnMap,String charSet) throws Exception {
		logger.debug("准备生成CSV文件");
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        String filename = null;//生成的文件名
        
        long ll_1 = System.currentTimeMillis();
        rowsTotal = 0;
        
    	try{
    		File file = createNewFile(dirPath,"csv");
			filename = file.getName();   		
    		
    		if (datasource == null) {
    			datasource = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
    	     }
    	    NovaDBConnection conn = getConnection(datasource); 
	    	stmt = conn.prepareStatement(sql);
	        rs = stmt.executeQuery(); // 要出错就在这里!!!
	        ResultSetMetaData rsmd = rs.getMetaData();
	        
	        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),charSet)));
	        //生成CVS文件头数据
	        boolean isFirstColumn = true;//判断是否是第一列数据
	        StringBuffer temp = new StringBuffer();
			for(int i = 1; i < rsmd.getColumnCount() + 1; i++){
				//OLAP控件对于大小写敏感，数据表头统一采用小写
				if(isFirstColumn){
					temp.append("\"" + convertColumnName(rsmd.getColumnName(i), columnMap) + "\"");
					isFirstColumn = false;
					continue;
				}
				temp.append(",\""+  convertColumnName(rsmd.getColumnName(i), columnMap) +"\"");
			}
			writer.println(temp.toString());
	        
			//生成数据项行
	        while (rs.next()) {
	        	
	        	temp = new StringBuffer();
				isFirstColumn = true;
	            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
	                int li_coltype = rsmd.getColumnType(i); // 列类型	                
	                Object value = null; //
	                
	                if (li_coltype == Types.VARCHAR) { // 如果是字符
	                    value = rs.getString(i);
	                } else if (li_coltype == Types.NUMERIC) { // 如果是Number
	                    value = rs.getBigDecimal(i); //
	                } else if (li_coltype == Types.DATE || li_coltype == Types.TIMESTAMP) { // 如果是日期或时间类型,统统精确到秒,Oracle中的Date类型是Types.DATE,但返回的值是Timestamp!!!
	                	java.sql.Timestamp ts = rs.getTimestamp(i);
						if (ts != null) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							value = sdf.format(new Date(ts.getTime()));
						} else {
							value = "";
						}
	                    
	                } else if (li_coltype == Types.SMALLINT) { // 如果是整数
	                    value = new Integer(rs.getBigDecimal(i).intValue()); //
	                } else if (li_coltype == Types.INTEGER) { // 如果是整数
	                    value = new Long(rs.getBigDecimal(i).longValue()); //
	                } else if (li_coltype == Types.DECIMAL || li_coltype == Types.DOUBLE || li_coltype == Types.DOUBLE ||
	                           li_coltype == Types.FLOAT) {
	                    value = rs.getBigDecimal(i); //
	                } else if (li_coltype == Types.CLOB) {//clob字段
	                	Clob clob = rs.getClob(i);
	                	if(clob != null)
	                		value = clob.getSubString(1L, (int)clob.length());	
	                } 
	                else {
	                    value = rs.getObject(i);
	                }
	                
	                String valueStr = "";
	                if(value != null)
	                	valueStr = value.toString().replace("\"", "\"\"");
	                
	                if(isFirstColumn){
						temp.append("\""+valueStr+"\"");
						isFirstColumn = false;
						continue;
					}
					temp.append(",\""+valueStr+"\"");
	            }
	            //读取一行，写一行
	            writer.println(temp.toString());
	            rowsTotal ++;
	            writer.flush();
	        }
	        writer.close();
	        
	        long ll_2 = System.currentTimeMillis();
	        long ll_dealtime = ll_2 - ll_1;
	         
	 		logger.debug("在数据源[" + datasource + "]上执行SQL[" + sql + "]耗时[" + ll_dealtime + "],查询结果数[" + rowsTotal + "]条.");
	        if (ll_dealtime > 3000 ) {
	            logger.warn("警告:在数据源[" + datasource + "]上执行SQL耗时[" + ll_dealtime + "]超过[3000],可能要优化之[" + sql + "]");
	        }
	        
	        return filename;

    	}
    	catch (SQLException ex) {
            throw new SQLException("在数据源[" + datasource + "]上执行SQL[" + sql + "]出错,错误编码[" + ex.getErrorCode() +
                                   "],状态[" + ex.getSQLState() + "],原因:" + ex.getMessage());
        } catch (Exception ex) {
        	logger.debug("error:", ex);
            throw new Exception("根据SQL[" + sql + "]，生成CVS文件出错,原因:" + ex.getMessage());
        } 
    	finally{
    		 try {if(rs!=null) rs.close(); } catch (Exception exx) { }
             try {if(stmt!=null) stmt.close(); } catch (Exception exx) { }
             this.releaseContext(datasource);
    	}
    	 
    }
	
	/**
	 * 生成BillList模块导出的CSV文件
	 * @param templetCode 原模板编码
	 * @param condition 过滤条件
	 * @param nce 客户端环境变量
	 * @param dirPath 保存路径
	 * @param charSet 字符编码集 如GBK,UTF-8
	 * @return
	 * @throws Exception
	 */
	public String generateBillListCSVFile(String templetCode,String condition,NovaClientEnvironment nce,String dirPath,String charSet) 
			throws Exception {	
		//目前数据量超过10条，会导致内存溢出，要做分页导出处理 zhangzy 2012/6/20
		CommDMO dmo = new CommDMO();
		String filename = null;//生成的文件名
		try {
			File file = createNewFile(dirPath,"csv");
			filename = file.getName();
			
			FrameWorkMetaDataServiceImpl service = new FrameWorkMetaDataServiceImpl();
			Pub_Templet_1VO templetVO = service.getPub_Templet_1VO(templetCode,nce);
			// 取表头
			Pub_Templet_1_ItemVO[] templetItemVOs = templetVO.getItemVos();
			
			PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),charSet)));
	        
			HashMap<Integer, Boolean> columnShowMap = new HashMap<Integer, Boolean>();
			//生成CVS文件头数据
	        boolean isFirstColumn = true;//判断是否是第一列数据
	        StringBuffer temp = new StringBuffer();
			for(int i = 0; i < templetItemVOs.length; i++){
				columnShowMap.put(i, templetItemVOs[i].getListisshowable());
				if(!columnShowMap.get(i))//没有设置为列显示
					continue;
				
				if(isFirstColumn){
					temp.append("\"" + templetItemVOs[i].getItemname() + "\"");
					isFirstColumn = false;
					continue;
				}
				temp.append(",\""+  templetItemVOs[i].getItemname() +"\"");
			}
			writer.println(temp.toString());

			String billListSql = FormServiceUtil.getSQL(templetVO, condition,nce);
			// 取页总数
			String ds = templetVO.getDatasourcename();
			HashVO[] tempVos = dmo.getHashVoArrayByDS(ds,"select count(1) c from (" + billListSql + ")");
			int totalCount = tempVos[0].getIntegerValue("c");
			
			int rowCountPerPage = 8000  ;//8000条一页
			int pageCount = totalCount % rowCountPerPage == 0 ? totalCount/rowCountPerPage : totalCount/rowCountPerPage + 1;
			
			rowsTotal = 0;
			for (int p = 0; p < pageCount; p++) {
				int pageNum = p + 1;
				// 起始行号
				int li_begin_pos = rowCountPerPage * (pageNum - 1);
				if (li_begin_pos > totalCount - 1) {
					break;
				}
				// 截止行号
				int li_end_pos = li_begin_pos + rowCountPerPage - 1;
				if (li_end_pos >= totalCount - 1) {
					li_end_pos = totalCount - 1;
				}

				String sql = "select * from (select t.*,rownum rNum from ("+billListSql+") t where rownum<="+(li_end_pos+1)+") where rNum>="+(li_begin_pos+1) ; 
				// 取数据
				Object[][] values = service.getBillListDataByDS(templetVO.getDatasourcename(), sql, templetVO, nce);
				for (int i = 0; i < values.length; i++) {
					Object[] row = values[i];
					temp = new StringBuffer();
					isFirstColumn = true;
					for (int j = 0; j < row.length - 1; j++) {
						if(!columnShowMap.get(j))//没有设置为列显示，不导出该列数据
							continue;
						
						String valueStr = "";
						if (row[j + 1] != null)
							valueStr = row[j + 1].toString().replace("\"", "\"\"");

						if (isFirstColumn) {
							temp.append("\"" + valueStr + "\"");
							isFirstColumn = false;
							continue;
						}
						temp.append(",\"" + valueStr + "\"");
					}
					// 读取一行，写一行
					writer.println(temp.toString());
					writer.flush();
					rowsTotal++;
				}
			}
			writer.close();
			logger.debug("根据原数据模板[" + templetCode + "],条件[" + condition+ "]生成CSV文件OK，记录数[" + rowsTotal + "]条.");
			
			return filename;
		}
		catch(Exception e){
			NovaLogger.getLogger(this).error("generateBillListCSVFile异常！",e);
			throw e;
		}
		finally {
			dmo.releaseContext();
		}
	}
	
	/**
	 * 根据sql查询结果生成csv文件，使用CommDMO封装，效率低一些
	 * @param datasource 数据源
	 * @param sql 查询SQL
	 * @param dirPath 保存路径
	 * @param charSet 字符编码集 如GBK,UTF-8
	 * @return
	 * @throws Exception
	 */
	public String generateCSVFileDmo(String datasource,String sql, String dirPath,String charSet) throws Exception{
		NovaLogger.getLogger(this).debug("准备生成CSV文件");
		CommDMO dmo = new CommDMO();
		String filename = null;//生成的文件名
		try{
			File file = createNewFile(dirPath,"sql");
			filename = file.getName();
			
			HashVO[] vos = dmo.getHashVoArrayByDSUnlimitRows(datasource, sql);
			if(vos.length > 0){
				PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),charSet)));
				HashVO vo = vos[0];
				StringBuffer temp = new StringBuffer();
				boolean isFirst = true;
				for(String key : vo.getKeys()){
					if(isFirst){
						temp.append(key);
						isFirst = false;
						continue;
					}
					temp.append(","+key);
				}
				writer.println(temp.toString());
				for(HashVO v : vos){
					temp = new StringBuffer();
					isFirst = true;
					for(String key : vo.getKeys()){
						String value = v.getStringValue(key);
						if(value == null)
							value = "";
						if(isFirst){
							temp.append("\""+value+"\"");
							isFirst = false;
							continue;
						}
						temp.append(",\""+value+"\"");
					}
					writer.println(temp.toString());
					writer.flush();
				}
				writer.close();
			}
			return filename;
		}
		catch(Exception e){
			NovaLogger.getLogger(this).error("",e);
			throw e;
		}
		finally{
			dmo.releaseContext(datasource);
		}
	}
	
	
	//生成一个不重名的文件
	private File createNewFile(String dirPath,String extName) throws Exception{
		String sysRootPath = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH");
		File dir = new File(sysRootPath+dirPath);
		if(!dir.exists())
			dir.mkdirs();
		
		File file;
		do{//保证文件名不重复
			String filename = UUID.randomUUID().toString()+"." + extName;
			file = new File(sysRootPath+dirPath+"/"+filename);
		}while(file.exists());
		
		file.createNewFile();
		
		return file;
	}
	
	public String generateTempletSqlFile(String condition) throws Exception {
		logger.debug("元原模板的SQL文件");
        String filename = null;//生成的文件名
        CommDMO dmo = new CommDMO();
    	try{
    		File file = createNewFile(FileConstant.DOWNLOAD_DIR,"sql");
			filename = file.getName();   		
	        
	        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),"GBK")));
	        String temp = new String();
	        
	        String sql = "select templetcode from pub_templet_1 order by templetcode";
	        if(condition != null && !"".equals(condition))
	        	sql = "select templetcode from pub_templet_1 where 1=1 and" + condition + " order by templetcode";
	        
			HashVO[] vos = dmo.getHashVoArrayByDS( null, sql );
			for( HashVO vo : vos )
			{
				temp = TempletUtil.exportTempletScript(vo.getStringValue(0));
				char[] chars = temp.toCharArray();
				writer.println(chars);
			}
	        writer.close();
	        
	        return filename;
    	}finally{
             dmo.releaseContext(null);
    	}
    	 
    }
	
	public String generateViewFile(String datasource,String sql,String dirPath, String charSet)throws Exception {
		CommDMO dmo = new CommDMO();
		String filename = null;// 生成的文件名
		try {
			File file = createNewFile(dirPath, "sql");
			filename = file.getName();
			
			Writer writer = new OutputStreamWriter(new FileOutputStream(file,true), charSet); 
			
			StringBuffer buff = new StringBuffer("");
			
			HashVO[] vos = dmo.getHashVoArrayByDSUnlimitRows(datasource, sql);
			if(vos != null && vos.length >0){
				for(HashVO vo : vos){
					String name = vo.getStringValue("view_name");
					String content = (String)vo.getObjectValue("text");
					buff.append("create or replace view "+name+" as \n"+content+";"+"\n");
				}
			}
			buff.append("/");
			writer.write(buff.toString());
			writer.flush();
			writer.close();
			
			return filename;
			
		} catch (Exception e) {
			NovaLogger.getLogger(this).error("generateViewFile异常！", e);
			throw e;
		} finally {
			dmo.releaseContext();
		}
	}
	
	public String generateFile(String content,String fileType,String dirPath, String charSet)throws Exception {
		String filename = null;// 生成的文件名
		try {
			File file = createNewFile(dirPath,fileType);
			filename = file.getName();
			
			Writer writer = new OutputStreamWriter(new FileOutputStream(file,true), charSet); 
			
			StringBuffer buff = new StringBuffer("");
			buff.append(content);
			//buff.append("/");
			writer.write(buff.toString());
			writer.flush();
			writer.close();
			
			return filename;
			
		} catch (Exception e) {
			NovaLogger.getLogger(this).error("generateFile异常！", e);
			throw e;
		}
	}
	
	
	/**
	 * 获取数据库连接
	 * @param _dsname
	 * @return
	 * @throws SQLException
	 */
	private final NovaDBConnection getConnection(String _dsname) throws SQLException {

        if (initContext == null) {
            initContext = new NovaInitContext();
        }
        return initContext.getConn(_dsname);
    }
	
	/**
     * 清空线程缓冲信息
     * 直接调用NovaInitContext类的 release()
     */
    public void releaseContext(String ds) {
    	try{
			if (initContext != null) {
				if (ds == null || ds.trim().equals("")) {
					ds = NovaServerEnvironment.getInstance()
							.getDefaultDataSourceName();
				}
				initContext.release(ds);
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}	
    }

}
