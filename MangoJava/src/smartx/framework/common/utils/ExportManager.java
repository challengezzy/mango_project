package smartx.framework.common.utils;

import java.io.*;
import java.util.*;
import org.apache.poi.hssf.usermodel.*;


/**
 * 处理平台数据导出到Excel表格
 *
 * 1、构造对象并通过属性方法设置导出对象，设置导出类型等信息
 * 2、调用输出方法
 *    2.1、判断输出对象【输出对象建立为】是否建立，是2.3，否2.2
 *    2.2、创建导出对象，并校验导出参数
 *    2.3、调用导出对象的输出接口
 * 3、调用结束导出方法
 *
 *
 * @author Administrator
 *
 */
public class ExportManager {
	/**
     * 构造方法
     */
    public ExportManager(){
    }
    /**
     * 构造方法，按导出类型创建
     * @param _file_name
     */
    public  ExportManager(String type) throws Exception{
    	this.setExportType(type);
    }
    
    /**
	 * 获得输出类型
	 * @return
	 */
	public String getExportType() {
		return this.export_type;
	}
	/**
	 * 设置输出类型
	 * @param types
	 * @throws Exception
	 */
	public void setExportType(String type)throws Exception {
		if(type==null||type.trim().equals("")){
    		throw new Exception("类型不能设置为空！");
    	}
    	type=type.toLowerCase();
	    if(!EXPORT_TYPES.containsValue(type)){
	    	throw new Exception("导出类型不匹配！");	
    	}
    	this.export_type=type;
	}
    
	/**
	 * 设置输出参数
	 * @param key
	 * @param value
	 */
	public void addExportParam(String key,Object value){
		if(key==null||key.trim().equals("")||value==null){
			return;
		}
		this.export_params.put(key,value);			
	}
	/**
	 * 获得设置的输出参数
	 * @param key
	 * @return
	 */
	public Object getExportParam(String key){
		return this.export_params.get(key);
	}
    
	/**
	 * 导出数据处理
	 * @param objs
	 * @throws Exception
	 */
    public void exportData(Object[][] objs) throws Exception{
    	if(this.export_type==null||this.export_type.trim().equals("")){
    		throw new Exception("导出类型未正确设置！");
    	}
    	if(this.export_processor==null){
    		try{
    			this.export_processor=(ExportProcessorIFC)Class.forName((String)EXPORT_PROCESSORS.get(this.export_type)).newInstance();
    		}catch(Exception e){
    			throw new Exception("不能正确创建导出处理器！处理器实现类："+EXPORT_PROCESSORS.get(this.export_type));
    		}    		
    		this.export_processor.validParamaters(this.export_params);
    	}
    	this.export_processor.exportData(objs);
    	
    }
    
    /**
     * 导出数据结束
     * @throws Exception
     */
    public void stopExport()throws Exception{    	
    	this.export_processor.stopExport();
    	this.export_processor=null;
    	//JOptionPane.showMessageDialog(null, "数据导出到Excel出现异常!");
    	//JOptionPane.showMessageDialog(null, "数据导出到Excel成功，但关闭导出流出现异常!");
    }
    
    
    
	
	public static String EXPORT_DATATYPE_STRING="string";
	public static String EXPORT_DATATYPE_NUMERIC="numeric";
	public static String EXPORT_DATATYPE_DATE="date";
	public static String EXPORT_DATATYPE_DATETIME="datetime";
	
	public static String EXPORT_TYPE_XLS="xls";  //Excel类型，需要设置标题、列数、列标题、列类型
	public static String EXPORT_TYPE_CSV="csv";  //TODO CSV类型。需要设置字段分界符。
	public static String EXPORT_TYPE_TXT="txt";  //TODO 文本类型。需要设置字段分界符。
	public static String EXPORT_TYPE_DBF="dbf";  //TODO 文本类型。需要设置字段分界符。
	public static String EXPORT_TYPE_SQL="sql";  //TODO sql脚本。需要设置sql模板，如insert into xxx(...) vlaues({line}); 其中{line}为需要替换部分
	public static String EXPORT_TYPE_DB="db";    //TODO 数据库导出。需要设置导出表名、列名、列类型
	public static String EXPORT_TYPE_DOC="doc";  //TODO Word文档类型。指明模板文档
	private static HashMap EXPORT_TYPES=null;
	private static HashMap EXPORT_PROCESSORS=null;
	//TODO 这里的EXPORT_PROCESSORS最好由配置文件设置
	static{
		EXPORT_TYPES=new HashMap();
		EXPORT_PROCESSORS=new HashMap();
		EXPORT_TYPES.put("EXPORT_TYPE_XLS",EXPORT_TYPE_XLS);
		EXPORT_PROCESSORS.put(EXPORT_TYPE_XLS,"smartx.framework.common.utils.ExcelExportProcessor");
		EXPORT_TYPES.put("EXPORT_TYPE_CSV",EXPORT_TYPE_CSV);
		EXPORT_PROCESSORS.put(EXPORT_TYPE_CSV,"smartx.framework.common.utils.CsvExportProcessor");//TODO CSV类型。需要设置字段分界符。
		EXPORT_TYPES.put("EXPORT_TYPE_TXT",EXPORT_TYPE_TXT);
		EXPORT_PROCESSORS.put(EXPORT_TYPE_TXT,"smartx.framework.common.utils.TxtExportProcessor");//TODO 文本类型。需要设置字段分界符。
		EXPORT_TYPES.put("EXPORT_TYPE_DBF",EXPORT_TYPE_DBF);
		EXPORT_PROCESSORS.put(EXPORT_TYPE_DBF,"smartx.framework.common.utils.DbfExportProcessor");//TODO DBF类型。需要设置字段分界符。
		EXPORT_TYPES.put("EXPORT_TYPE_SQL",EXPORT_TYPE_SQL);
		EXPORT_PROCESSORS.put(EXPORT_TYPE_SQL,"smartx.framework.common.utils.SqlExportProcessor");//TODO sql脚本。需要设置sql模板，如insert into xxx(...) vlaues({line}); 其中{line}为需要替换部分
		EXPORT_TYPES.put("EXPORT_TYPE_DB",EXPORT_TYPE_DB);
		EXPORT_PROCESSORS.put(EXPORT_TYPE_DB,"smartx.framework.common.utils.DbExportProcessor");//TODO 数据库导出。需要设置导出表名、列名、列类型
		EXPORT_TYPES.put("EXPORT_TYPE_DOC",EXPORT_TYPE_DOC);
		EXPORT_PROCESSORS.put(EXPORT_TYPE_DOC,"smartx.framework.common.utils.DocExportProcessor");//TODO Word文档类型。指明模板文档
	}
	
	
	private String export_type=null;
	private ExportProcessorIFC export_processor=null;
	private HashMap export_params=new HashMap();
	
	
	
	
    
    public static void main(String[] args)throws Exception{
    	CreateCells.main(null);
    }
    
    
    
}





class CreateCells {

	public static void main(String[] args)throws IOException{
		HSSFWorkbook wb = new HSSFWorkbook();// 建立新HSSFWorkbook对象
		HSSFSheet sheet = wb.createSheet("new sheet");// 建立新的sheet对象
		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow((short) 0);// 建立新行
		// Create a cell and put a value in it.
		row.createCell((short) 0).setCellValue(1);// 设置cell的整数类型的值
		row.createCell((short) 1).setCellValue(1.2);// 设置cell浮点类型的值
		row.createCell((short) 2).setCellValue("test");// 设置cell字符类型的值
		row.createCell((short) 3).setCellValue(true);// 设置cell布尔类型的值
		HSSFCellStyle cellStyle = wb.createCellStyle();// 建立新的cell样式
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));// 设置cell样式为定制的日期格式
		HSSFCell dCell = row.createCell((short) 4);
		dCell.setCellValue(new Date());// 设置cell为日期类型的值
		dCell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
		HSSFCell csCell = row.createCell((short) 5);
		//csCell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell编码解决中文高位字节截断
		csCell.setCellValue("中文测试_Chinese Words Test");// 设置中西文结合字符串
		row.createCell((short) 6).setCellType(HSSFCell.CELL_TYPE_ERROR);// 建立错误cell
		
		for(int i=0;i<65535;i++){
			System.out.println(i);
			// Create a row and put some cells in it. Rows are 0 based.
			row = sheet.createRow(i);// 建立新行			
			// Create a cell and put a value in it.
			row.createCell((short) 0).setCellValue(1);// 设置cell的整数类型的值
			row.createCell((short) 1).setCellValue(1.2);// 设置cell浮点类型的值
			row.createCell((short) 2).setCellValue("test");// 设置cell字符类型的值
			row.createCell((short) 3).setCellValue(true);// 设置cell布尔类型的值			
			dCell = row.createCell((short) 4);
			dCell.setCellValue(new Date());// 设置cell为日期类型的值
			dCell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
			csCell = row.createCell((short) 5);
			//csCell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell编码解决中文高位字节截断
			csCell.setCellValue("中文测试_Chinese Words Test");// 设置中西文结合字符串
			row.createCell((short) 6).setCellType(HSSFCell.CELL_TYPE_ERROR);// 建立错误cell
		}
		
		
		
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("c:/EditTemp/workbook.xls");
		wb.write(fileOut);
		fileOut.close();
	}

}

/**
 * 数据导出接口
 * @author James.W
 *
 */
interface ExportProcessorIFC{
	/**
	 * 校验导出配置参数
	 * @param params
	 * @throws Exception 由于特殊错误（message）不符合导出数据要求
	 */
	public void validParamaters(HashMap params)throws Exception;
	
	/**
	 * 导出数据
	 * 第一次导出时初始化，但不关闭导出文件，等待继续导出，如果是数据库DB类型，则每次导出作为一个事务
	 * @param values
	 * @throws Exception
	 */
	public void exportData(Object[][] objs) throws Exception;
	
	/**
	 * 导出停止
	 * 关闭文件、终止连接。
	 * @throws Exception
	 */
	public void stopExport() throws Exception;
	
}





