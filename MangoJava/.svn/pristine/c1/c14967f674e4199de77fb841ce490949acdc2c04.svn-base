package smartx.framework.common.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;


/**
 * Excel文件处理器
 * 本类不应该被直接调用，而应该有ExportManager处理
 * @author James.W
 *
 */

public class ExcelExportProcessor implements ExportProcessorIFC {
	
	/**
	 * 校验输出控制参数
	 * @param param 输出参数。结构为<key,value>，key-参数名，value=参数值
	 *       path      ：String【必选】文件输出路径，包括文件名
	 *       title     ：String【可选】文档标题
	 *       sheetname ：String【可选】sheet标题
	 *       titles    ：String[][]【可选】列标题 允许多行标题、融合（rowspa、colspan、bothspan【被完全包围的】）
	 *       types     ：String[]【可选】列类型 string、numeric、date、datetime、... 见ExportManager.EXPORT_DATATYPE_XXXX
	 *       widths    ：int[]【可选】列宽度
	 *       cols      ：int【可选】输出列数量
	 *       objs      ：Object[][]【可选】输出数据列表
	 */
	public void validParamaters(HashMap params) throws Exception {
		this.isvalid=false;
		if(params==null||params.size()==0){
			throw new Exception("输出参数不能为空。");
		}
		
		//校验Excel输出所需要的参数
		//path
		this.path=(String)params.get("path");
		if(this.path==null||this.path.trim().equals("")){
			throw new Exception("没有设置正确的文件路径！");
		}else{
			if (!this.path.toLowerCase().endsWith(".xls")) {
				this.path += ".xls";
            }
		}
		//title
		this.title=(String)params.get("title");
		//sheetName
		this.sheetName=(String)params.get("sheetname");
		//titles
		this.titles=(String[][])params.get("titles");
		//types
		this.types=(String[])params.get("types");
		//widths
		this.widths=(int[])params.get("widths");
		//cols
		Integer cs=(Integer)params.get("cols");
		this.cols=(cs!=null)?cs.intValue():-1;
		if(this.titles!=null&&this.titles[0].length!=this.cols){
			if(this.cols==-1){
				this.cols=this.titles[0].length;
			}else{
				throw new Exception("设置的列数与列标题中的列数不一致！");
			}
		}
		if(this.types!=null&&this.types.length!=this.cols){
			if(this.cols==-1){
				this.cols=this.types.length;
			}else{
				throw new Exception("设置的列数与列类型中的列数不一致！");
			}
		}
		if(this.widths!=null&&this.widths.length!=this.cols){
			if(this.cols==-1){
				this.cols=this.widths.length;
			}else{
				throw new Exception("设置的列数与列宽度中的列数不一致！");
			}
		}		
		
		this.isvalid=true;
	}	
	
	
	
	
	
	
	/**
	 * 导出数据
	 * 
	 * @param objs 导出数据
	 * @throws Exception
	 */
	public void exportData(Object[][] objs) throws Exception {
		if(!this.isvalid){
			throw new Exception("请先执行验证！");
		}
		
		if(this.excel==null) this.initExcel();
		
		
		for(int i=0;i<objs.length;i++){
			this.exportRowData(objs[i]);			
		}
	}

	/**
	 * 结束输出，生成物理文件（在本方法之前还是在内存中）
	 */
	//TODO 寻找一种实现方法，最好出现边输出边生成物理文件，这样更加合理
	public void stopExport() throws Exception {
		try {
            this.excel.write(new FileOutputStream(this.path));
            this.excel=null;
            this.isvalid=false;
        } catch (IOException e) {
            throw new Exception("输出生成物理文件时出现错误！",e);
            
        }
	}



	//参数
	private boolean isvalid=false;                  // 参数是否校验
	private String path=null;                       // 导出文件路径
	private String title=null;                      // 文档标题
	private String sheetName=null;                  // sheetname
	private String[][] titles = null;               // 列标题
    private String[] types = null;                  // 列类型
    private int[] widths = null;                    // 列宽度
    private int cols=-1;                            // 列数量
    
    private HSSFWorkbook excel = null;              // HSSFWorkbook对象
    private HSSFSheet sheet = null;                 // 当前操作的sheet对象
    private HSSFCellStyle[] styles = null;          // 列风格
    
    private int idx = 0;                            // 当前操作的行序号 
    
    
	//初始化创建文件，在输出任何数据前执行
    private void initExcel() throws Exception{
    	// 建立新HSSFWorkbook对象     
        this.excel = new HSSFWorkbook();    
        this.sheet = this.excel.createSheet();
        if(sheetName!=null){
        	this.excel.setSheetName(0,sheetName);
        }
        this.sheet.setGridsPrinted(true);
        
        // 设置标题
        if(this.title!=null) this.setTitle();        
        // 设置列标题
        if(this.titles!=null) this.setTitles();
        // 生成列风格
        if(this.types!=null) this.generateCellStyle();
        // 设置列宽度
        if(this.widths!=null) this.setWidths();
        
    }
    
    /**
     * 建立文档标题，标题永远在第0行
     */
    private void setTitle(){
    	
    	HSSFRow row = this.sheet.getRow(0);
        if (row == null) {
            row = this.sheet.createRow(0);
            this.idx=1;
        }
    	
        HSSFCellStyle cellStyle = this.excel.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFFont font = this.excel.createFont();
        font.setFontName("宋体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)18);
        font.setColor(HSSFColor.BLACK.index);

        HSSFCell cell = row.createCell( (short) 0);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(this.title);
        
        if(this.cols>0){
        	//如果有多列，则单元格融合
        	sheet.addMergedRegion(new Region(0, (short)(0), 0, (short)(this.cols-1)));        	
    	}    	
    }
    
    /**
     * 建立文档标题，标题永远在第0行
     */
    private void setTitles(){
    	for(int i=0;i<this.titles.length;i++){  		
    		this.setSubTitles(this.titles[i]);
    	}
    	//处理融合
    	int[][] merges=this.mergeTitle();
    	int posit=this.idx-this.titles.length;
    	for(int i=0;i<merges.length;i++){
    		sheet.addMergedRegion(new Region(posit+merges[i][0], (short)merges[i][1], posit+merges[i][2], (short)merges[i][3]));
    	}
    }
    private void setSubTitles(String[] ts){    	
    	HSSFRow row = this.sheet.getRow(this.idx);
        if (row == null) {
            row = this.sheet.createRow(this.idx);
            this.idx++;
        }
    	
        
        HSSFCellStyle cellStyle = this.excel.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFFont font = this.excel.createFont();
        font.setFontName("宋体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)10);
        font.setColor(HSSFColor.BLACK.index);
        
        for(int i=0;i<ts.length;i++){        
	        HSSFCell cell = row.createCell( (short) i);
	        cellStyle.setFont(font);
	        cell.setCellStyle(cellStyle);
	        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	        cell.setCellValue((ts[i].toLowerCase().equals("rowspan")||ts[i].toLowerCase().equals("colspan"))?"":ts[i]);//rowspan和colspan
        }            	
    }
    private int[][] mergeTitle(){
    	class Merges{
    		int srow=0;
    		int erow=0;
    		int scol=0;
    		int ecol=0;
    		public Merges(int srow,int scol,int erow,int ecol){
    			this.srow=srow;this.erow=erow;
    			this.scol=scol;this.ecol=ecol;
    		}
    		public boolean isMerge(){
    			return !(erow>srow||ecol>scol||(srow==erow&&scol==ecol));
    		}
    		public int[] getMerge(){
    			return new int[]{srow,scol,erow,ecol};
    		}
    	}
    	Merges[][] ms=new Merges[this.titles.length][this.titles[0].length];
    	for(int i=0;i<this.titles.length;i++){
    		for(int j=0;j<this.titles[0].length;j++){
    			ms[i][j]=new Merges(i,j,i,j);
    		}
    	}
    	
    	for(int i=this.titles.length-1;i>=0;i--){
    		for(int j=this.titles[0].length-1;j>=0;j--){
    			if(this.titles[i][j].toLowerCase().equals("rowspan")){
    				ms[i-1][j].erow=ms[i][j].erow;
    			}
    			if(this.titles[i][j].toLowerCase().equals("colspan")){
    				ms[i-1][j].ecol=ms[i][j].ecol;
    			}
    		}
    	}    	
    	ArrayList ary=new ArrayList();
    	for(int i=0;i<this.titles.length;i++){
    		for(int j=0;j<this.titles[0].length;j++){
    			if(ms[i][j].isMerge()){
    				ary.add(ms[i][j].getMerge());
    			}
    		}
    	}
    	
    	return (int[][])ary.toArray(new int[0][0]);
    }
    
    /**
     * 生成列的显示格式
     */
    private void generateCellStyle() {
        this.styles= new HSSFCellStyle[this.cols];
        
        HSSFFont font = this.excel.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)10);
        font.setColor(HSSFColor.BLACK.index);
        
        for (int i=0; i<this.cols; i++) {
            this.styles[i] = this.excel.createCellStyle();
            this.styles[i].setFont(font);
            if (this.types[i] == null||this.types[i].toLowerCase().equals(ExportManager.EXPORT_DATATYPE_STRING)) {
                continue;
            }
            
            HSSFDataFormat format = excel.createDataFormat();

            if (this.types[i].toLowerCase().equals(ExportManager.EXPORT_DATATYPE_NUMERIC)) {
            	this.styles[i].setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            } else if (this.types[i].toLowerCase().equals(ExportManager.EXPORT_DATATYPE_DATETIME)) {
            	this.styles[i].setDataFormat(format.getFormat("yyyy-mm-dd hh:mm:ss"));
            } else if (this.types[i].toLowerCase().equals(ExportManager.EXPORT_DATATYPE_DATE)) {
            	this.styles[i].setDataFormat(format.getFormat("yyyy-mm-dd"));
            }
            /*
            if (_types[i].equals("数字框")) {
                hcs_style[i].setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            } else if (_types[i].equals("时间")) {
                hcs_style[i].setDataFormat(format.getFormat("yyyy-mm-dd hh:mm:ss"));
            } else if (_types[i].equals("日历")) {
                hcs_style[i].setDataFormat(format.getFormat("yyyy-mm-dd"));
            }
            */
        }
    }
    //导出一行数据
    private void exportRowData(Object[] objs){
    	HSSFRow row = this.sheet.getRow(this.idx);
        if (row == null) {
            row = this.sheet.createRow(this.idx);
            this.idx++;
        }
    	
    	
        
        /*
                HSSFCellStyle cellStyle = getCellStyle(j);
                HSSFCell dCell = row.createCell((short)j);

                if (str_types[j].equals("文本框")) {
                    dCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                } else if (str_types[j].equals("数字框")) {
                    dCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                } else {
                    dCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                }

                dCell.setEncoding(HSSFCell.ENCODING_UTF_16);

                if (_obj[i][j] != null) {
                    dCell.setCellValue(_obj[i][j].toString()); //
                } else {
                    dCell.setCellValue(""); //
                }
                if (cellStyle != null) {
                    // dCell.setCellStyle(cellStyle); //
                }
         * 
         */
        
        for(int i=0;i<(this.cols==-1?objs.length:this.cols);i++){        
	        HSSFCell cell = row.createCell( (short) i);
	        cell.setCellStyle(this.styles[i]);
	        
	        if(this.types==null){
	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        }else if(this.types[i].toLowerCase().equals(ExportManager.EXPORT_DATATYPE_NUMERIC)) {
	        	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
	        }else if(this.types[i].toLowerCase().equals(ExportManager.EXPORT_DATATYPE_DATETIME)) {
	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        }else if(this.types[i].toLowerCase().equals(ExportManager.EXPORT_DATATYPE_DATE)) {
	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        }
	        
	        //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	        
	        
	        if (objs[i]!=null) {	        	
                cell.setCellValue(objs[i].toString());
            } else {
                cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
            }            
        }    
    }
    
    //设置列宽度
    private void setWidths() {
        if (this.widths == null) {
            return;
        }
        for (int i = 0; i < this.widths.length; i++) {
            sheet.setColumnWidth((short) i, (short)(this.widths[i]*8/((double)1/20)));
        }
    }

    
    
    

	
}
