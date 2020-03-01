package smartx.publics.databatch.ui;


import java.io.*;
import java.util.ArrayList;

import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.publics.databatch.bs.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;



/**
 * <p>Title:客户端:读取excel文件和xml文件并解析</p>
 * <p>Description: NOVA </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author Yang Huan
 * @version 2.0
 */
public class DataBatchParse {
	private ArrayList data_sheet = new ArrayList();
	
	private DataBatchDealIFC ifc = null;
	
	private DataBatchDefine databatchdef = null;
	
	public  DataBatchParse(){
		
	}

	/**
	 * 根据excel文件路径解析成为ArrayList
	 * @param in_excelStream		待解析的Excel文件数据流
	 * @param in_define				导入约束,根据该约束来解析Excel文件
	 * @return
	 * @throws Exception
	 */
	public ArrayList excelParse(InputStream in_excelStream, DataBatchDefine in_define) throws Exception{
		
		try{
			//构建Workbook对象, 只读Workbook对象
		    Workbook wb = null;
			try
			{
				wb = Workbook.getWorkbook(in_excelStream);
			} catch (BiffException e1)
			{
				throw new Exception(DataBatchConstant.EXCEL_FILE + DataBatchConstant.PARSE_FIELD + e1.getMessage());
			} catch (IOException e1)
			{
				throw new Exception(DataBatchConstant.EXCEL_FILE + DataBatchConstant.PARSE_FIELD +e1.getMessage());
			}
			Sheet[] sheet = wb.getSheets();
			//System.out.println("......sheet.length="+sheet.length);
			for (int sheetnum = 0; sheetnum < sheet.length; sheetnum++)
			{
				Sheet currentsheet = sheet[sheetnum];
				int i = 0;
				//System.out.println("......databatchdef.hasTitle()="+databatchdef.hasTitle());
				if (in_define.hasTitle())
				{
					i = 1;
				}
				//将excel表中的数据保存到List
				if(sheet[sheetnum].getRows() != 0 || sheet[sheetnum]!=null){
					for (; i < sheet[sheetnum].getRows(); i++) 
					{
						//取出行
						ArrayList data_row = new ArrayList();
						Cell[] data = currentsheet.getRow(i);
						int colCount = in_define.getColCount();
						int[] columnNum = in_define.getColumnNum();
						for (int j = 0; j < colCount; j++)
						{
							int colNum = columnNum[j]-1;
							if(colNum != -1){
								data_row.add(data[colNum].getContents().toString());
								//System.out.println("...deal...data["+colNum+"].getContents()="+data[colNum].getContents());
							}
						}
						data_sheet.add(data_row);
					}
				}
			}
		    wb.close();
		}catch (Exception e){
			 System.out.println(DataBatchConstant.EXCEL_FILE + DataBatchConstant.PARSE_FIELD);
	         e.printStackTrace();
	         throw new Exception(DataBatchConstant.EXCEL_FILE + DataBatchConstant.PARSE_FIELD + e.getMessage());
		}
		if(data_sheet.size() == 0){
			throw new Exception("Excel文件为空");
		}
		return data_sheet;
	}
	
	
	/**
	 * 读取xml文件并解析到DataBatchDef
	 * @param in_xmlFile				Xml配置文件
	 * @param in_tableName				数据导入目标表的名称
	 * @return							导入约束
	 * @throws Exception
	 */
	public DataBatchDefine xmlParse(File in_xmlFile, String in_tableName)throws Exception{
	
		try{
			if(ifc == null){
				ifc =(DataBatchDealIFC) NovaRemoteServiceFactory.getInstance().lookUpService(DataBatchDealIFC.class);
			}
			this.databatchdef = ifc.parseXml(in_xmlFile, in_tableName);
		} catch (Exception e) {		
            e.printStackTrace();
            throw new Exception(DataBatchConstant.CONFIG_FILE + DataBatchConstant.PARSE_FIELD + e.getMessage());
        }
		return this.databatchdef;
	}
	
	/**
	 * 根据表名解析Xml配置文件
	 * @param in_tableName				数据导入目标表的名称
	 * @return							导入约束
	 * @throws Exception
	 */
	public DataBatchDefine xmlParse(String in_tableName)throws Exception{
	
		try{
			if(ifc == null){
				ifc =(DataBatchDealIFC) NovaRemoteServiceFactory.getInstance().lookUpService(DataBatchDealIFC.class);
			}
			this.databatchdef = ifc.parseXml(in_tableName);
		
		} catch (Exception e) {		
            e.printStackTrace();
            throw new Exception(DataBatchConstant.CONFIG_FILE + DataBatchConstant.PARSE_FIELD + e.getMessage());
        } 
		return this.databatchdef;
	}
	
}
