package smartx.publics.excelexport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.FileUtil;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.excel.ExcelGenerateService;
import smartx.publics.excelexport.vo.ExcelItemVo;
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;
import smartx.publics.metadata.vo.MetadataTemplet;


/**
 *@author zzy
 *@date Apr 2, 2012
 **/
public class ExportXlsService {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	private LinkedHashMap<String, ExcelItemVo> itemMap;//EXCEL导出定义
	
	private String[] titles;
	private String[] types;	
	private int[] widths;
	private String initSql;//查询原始视图或者表
	private String datasource;//数据源
	
	private String preFileName = "XLS_";
	
	private void dataInit(String definition) throws Exception{
		Document doc = DocumentHelper.parseText(definition);
		Element root = doc.getRootElement();
		Element excelitemsE = root.element("excelitems");
		
		itemMap = new LinkedHashMap<String, ExcelItemVo>();
		
		@SuppressWarnings("unchecked")
		List<Element> excelitems = excelitemsE.elements();
		int seq = 0; //决定EXCEL中各列的顺序
		for (int i = 0; i < excelitems.size(); i++) {
			Element item = excelitems.get(i);
			String isexport = item.attributeValue("isexport");
			String column = item.attributeValue("column");
			if("false".equalsIgnoreCase(isexport)){
				//logger.debug("字段"+column+"不在导出字段内！");
				continue;
			}
			String title = item.attributeValue("title");
			int width = new Integer(item.attributeValue("width"));
			String type = item.attributeValue("type");
			//对于需要导出的字段压入itemMap
			ExcelItemVo itemVo = new ExcelItemVo(seq, column, title, type, width);
			itemMap.put(itemVo.getColumn(), itemVo);
			
			seq++;
		}
		
		titles = new String[itemMap.size()];
		types = new String[itemMap.size()];
		widths = new int[itemMap.size()];
		
		Set<String> keys = itemMap.keySet();
		for (String key : keys) {
			ExcelItemVo item = itemMap.get(key);
			titles[item.getSeq()] = item.getTitle();
			types[item.getSeq()] = item.getType();
			widths[item.getSeq()] = item.getWidth();
		}
		
		//数据源解析
		Element datasourceE = root.element("datasource");
		datasource = datasourceE.attributeValue("name");
		String datatype = datasourceE.elementText("datatype");
		if("sql".equalsIgnoreCase(datatype)){
			initSql = "("+datasourceE.elementText("sql")+")";
		}else{
			initSql = datasourceE.elementText("tablename");
		}
		
		logger.debug("Excel导出定义解析完成");
	}
	
	/**
	 * 生成EXCEL文件
	 * @param excelMTCode excel文件定义
	 * @param condition 外部传入查询条件
	 * @return 生成的文件名
	 * @throws Exception
	 */
	public String generateExcel(String excelMTCode,String condition) throws Exception{
		MetadataTemplet metadata = SmartXMetadataTempletService.getInstance().findMetadataTemplet(excelMTCode);
		dataInit(metadata.getContent());//初始表头数据
		
		String querySql = "select * from "+ initSql +" where 1=1 " + condition;
        
		String fileName = null;
		try{
			fileName = exportToExcel(querySql);
		}catch (Exception e) {
			logger.error("生成EXCEL文件异常", e);
			throw e;
		}finally{
			new CommDMO().releaseContext(datasource);
		}
		
		return fileName;
	}
	
	public String generateExcel(String excelMTCode,String queryDetailSql,String detailTablePk,
			String xlsViewPk,String detailTableDatasource,String[] extCols) throws Exception{
		
		MetadataTemplet metadata = SmartXMetadataTempletService.getInstance().findMetadataTemplet(excelMTCode);
		
		dataInit(metadata.getContent());//初始表头数据
		
		String fileName = null;
		int tempcount = 0;
		
		CommDMO dmo = new CommDMO();
		try{
			
			if(!StringUtil.isEmpty(detailTableDatasource) && detailTableDatasource.equalsIgnoreCase(datasource)){
				
				String querySql = "select v.* from "+ initSql +" v,("+queryDetailSql+") d where v."+xlsViewPk+"=d."+detailTablePk;
				
				fileName = exportToExcel(querySql);
				
			}else{
				
				String detailPkSQL = "select "+detailTablePk+" from ("+queryDetailSql+")";
				if(extCols != null && extCols.length > 0){
					String extColStr = "";
					for(String extCol : extCols){
						extColStr = extColStr+","+extCol+"";
					}
					detailPkSQL = "select "+detailTablePk + extColStr +" from ("+queryDetailSql+")";
				}
			
				HashVO[] keysVo = dmo.getHashVoArrayByDSUnlimitRows(detailTableDatasource, detailPkSQL);
				
				if(keysVo != null){
					ArrayList<String> fileList = new ArrayList<String>();
					ArrayList<Object[][]> objValuesList = new ArrayList<Object[][]>();
					
					String keysStr = "";
					HashMap<String, HashMap<String, String>> detailValueMap = new HashMap<String, HashMap<String, String>>();
					int count = 0;
					int index = 1;
					int tempCount = 0;
					final int maxRowNum = 60000;//EXCEL最大行数
					
					for(HashVO vo : keysVo){
						String dPK = vo.getStringValue(detailTablePk);
						if(StringUtil.isEmpty(keysStr)){
							keysStr = "'"+dPK+"'";
						}else{
							keysStr = keysStr+",'"+dPK+"'";
						}
						
						//添加扩展导出字段
						if(extCols != null && extCols.length > 0){
							HashMap<String, String> tmap = new HashMap<String, String>();
							for(String extCol : extCols){
								tmap.put(extCol, vo.getStringValueNotNull(extCol));
								//System.out.println(extCol + " :  " + vo.getStringValueNotNull(extCol));
							}
							detailValueMap.put(dPK, tmap);
						}
						
						tempCount++;
						count++;
						if(count == 800 || count == keysVo.length || tempCount == keysVo.length){
							
							String querySql = "select v.* from "+ initSql +" v where v."+xlsViewPk+" in ("+keysStr+")";
							
							HashVO[] tempVos = dmo.getHashVoArrayByDS(datasource,"select count(1) c from (" + querySql + ")");
							tempcount = tempVos[0].getIntegerValue("c");
							if(tempcount == 0){
								dmo.releaseContext(datasource);
								throw new Exception("根据当前SQL条件[ "+querySql+" ]查到0条记录");
							}
							
							int rowCountPerPage = 50000  ;//最多5W条记录
							int pageCount = tempcount % rowCountPerPage == 0 ? tempcount
									/ rowCountPerPage : tempcount / rowCountPerPage + 1;
							
							for(int i=0;i<pageCount;i++){
								int pageNum = i+ 1;
								// 起始行号
								int li_begin_pos = rowCountPerPage * (pageNum - 1);
								if (li_begin_pos > tempcount - 1) {
									break;
								}
								// 截止行号
								int li_end_pos = li_begin_pos + rowCountPerPage - 1;
								if (li_end_pos >= tempcount - 1) {
									li_end_pos = tempcount - 1;
								}
								
								String sql = "select * from (select t.*,rownum rNum from ("+querySql+") t where rownum<="+(li_end_pos+1)+") where rNum>="+(li_begin_pos+1) ;
								
						        Object[][] objValues;
						        if(extCols != null && extCols.length > 0){
						        	objValues = getExportData(sql,extCols,detailValueMap,xlsViewPk);
						        }else{
						        	objValues = getExportData(sql);
						        }
						        
						        objValuesList.add(objValues);
						        
							}
							
							Object[][] combineObjValues = ExportUtil.combine(objValuesList);
							//如果合并后的数量超过了EXCEL最大行数，则生成一个新的EXCEL
							if(combineObjValues.length % maxRowNum == 1 || tempCount == keysVo.length){
								logger.info("开始生成EXCEL文件......");
						        String newfileName = ExportUtil.getNewFileName("download",preFileName+index,"xls");
						        new ExcelGenerateService(newfileName, titles, types, widths,combineObjValues);
						        fileList.add(newfileName);
						        logger.info("导出文件["+newfileName+"]生成成功");
						        
						        index ++;
						        objValuesList.clear();
							}
							
							keysStr = "";
							count = 0;
							//生成一个文件后，清空
							detailValueMap = new HashMap<String, HashMap<String, String>>();
						}
					}
					
					String zipFileName = ExportUtil.getNewFileName("download",preFileName,"zip");
					FileUtil.ZipAndDelFile(fileList, zipFileName);
					logger.info("生成打包文件  " + zipFileName);
					File zipFile = new File(zipFileName);
					
					fileName = zipFile.getName();
				}
				
			}
			
		}catch (Exception e) {
			if(tempcount == 0){
				throw new Exception("查询到0条数据记录！");
			}else{
				logger.error("生成EXCEL文件异常", e);
				throw e;
			}
		}finally{
			dmo.releaseContext(detailTableDatasource);
			new CommDMO().releaseContext(datasource);
		}
		
		return fileName;
	}
	
	/**
	 * 数据到Excel表格
	 * @param condition
	 * @return
	 */
	private String exportToExcel(String querySql) throws Exception{
		
		
		// 取页总数
		CommDMO dmo = new CommDMO();
		HashVO[] tempVos = dmo.getHashVoArrayByDS(datasource,"select count(1) c from (" + querySql + ")");
		int count = tempVos[0].getIntegerValue("c");
		if(count == 0){
			dmo.releaseContext(datasource);
			throw new Exception("根据当前SQL条件[ "+querySql+" ]查到0条记录");
		}
		
		int rowCountPerPage = 20000  ;//最多5W条记录
		int pageCount = count % rowCountPerPage == 0 ? count
				/ rowCountPerPage : count / rowCountPerPage + 1;
		
		ArrayList<String> fileList = new ArrayList<String>();
		for(int i=0;i<pageCount;i++){
			int pageNum = i+ 1;
			// 起始行号
			int li_begin_pos = rowCountPerPage * (pageNum - 1);
			if (li_begin_pos > count - 1) {
				break;
			}
			// 截止行号
			int li_end_pos = li_begin_pos + rowCountPerPage - 1;
			if (li_end_pos >= count - 1) {
				li_end_pos = count - 1;
			}
			
			String sql = "select * from (select t.*,rownum rNum from ("+querySql+") t where rownum<="+(li_end_pos+1)+") where rNum>="+(li_begin_pos+1) ;
			
	        Object[][] objValues = getExportData(sql);
	        
	        logger.info("开始生成EXCEL文件......");
	        String fileName = ExportUtil.getNewFileName("download",preFileName+pageNum,"xls");
	        new ExcelGenerateService(fileName, titles, types, widths,objValues);
	        fileList.add(fileName);
	        logger.info("导出文件["+fileName+"]生成成功");
		}
		String zipFileName = ExportUtil.getNewFileName("download",preFileName,"zip");
		FileUtil.ZipAndDelFile(fileList, zipFileName);
		logger.info("生成打包文件  " + zipFileName);
		File zipFile = new File(zipFileName);
		
		return zipFile.getName();
	}
	
	
	/**
	 * 获取导出数据
	 * @param qeurySql
	 * @return
	 * @throws Exception
	 */
	private Object[][] getExportData(String qeurySql) throws Exception{
		Object[][] obj_values = null;
		CommDMO dmo = new CommDMO();
		
    	HashVO[] vos = dmo.getHashVoArrayByDSUnlimitRows(datasource, qeurySql );
    	obj_values = new Object[vos.length][itemMap.size()];
		for (int i=0;i<vos.length;i++)
    	{
			HashVO vo = vos[i];
			Set<String> keys = itemMap.keySet();
			for (String key : keys) {
				ExcelItemVo item = itemMap.get(key);
				//根据映射关系，直接赋值 
				obj_values[i][item.getSeq()] = vo.getStringValueNotNull(key);
			}
    	}
    	
    	return obj_values;
	}
	
	private Object[][] getExportData(String qeurySql,String[] extCols,HashMap<String, HashMap<String, String>> detailValueMap,String xlsViewPk) throws Exception{
		Object[][] obj_values = null;
		CommDMO dmo = new CommDMO();
		
    	HashVO[] vos = dmo.getHashVoArrayByDSUnlimitRows(datasource, qeurySql );
    	obj_values = new Object[vos.length][itemMap.size()];
		for (int i=0;i<vos.length;i++)
    	{
			HashVO vo = vos[i];
			Set<String> keys = itemMap.keySet();
			String viewPkValue = vo.getStringValueNotNull(xlsViewPk);
			//EXCEL导出的值中，一部分是从SQL中查询出，一部分由detailValueMap映射查出
			HashMap<String, String> valueMap = detailValueMap.get(viewPkValue);
			
			for (String key : keys) {
				ExcelItemVo item = itemMap.get(key);
				
				//根据映射关系，直接赋值 
				obj_values[i][item.getSeq()] = vo.getStringValueNotNull(key);
			}
			//对扩展字段赋值，覆盖原有的值
			for(String extCol : extCols){
				ExcelItemVo item = itemMap.get(extCol);
				if(item == null){
					throw new Exception("比对导出定义中扩展字段["+extCol+"]与EXCEL导入定义中字段不一致！");
				}
				obj_values[i][item.getSeq()] = valueMap.get(extCol);
			}
    	}
    	
    	return obj_values;
	}
	
}
