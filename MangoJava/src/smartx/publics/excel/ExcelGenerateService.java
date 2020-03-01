package smartx.publics.excel;


import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;

import smartx.framework.common.vo.NovaLogger;

/**
 * Object[][]数据导出到Excel表格
 *
 * @author Administrator
 *
 */
public class ExcelGenerateService {
	private Logger logger = NovaLogger.getLogger(this.getClass());

    private String str_path = null;

    private HSSFWorkbook hwb_excel = null; // 建立新HSSFWorkbook对象

    private HSSFSheet hs_sheet = null; // 建立新的sheet对象

    private String[] str_title = null;

    private String[] str_types = null;

    private int[] li_cols = null;

    private int[] li_widths = null;

    private HSSFCellStyle[] hcs_style = null;

    private FileOutputStream fos_stream = null;

    private int li_index = 0;

    private int li_cellindex = 0;

    private Object[][] obj_values = null;

    private boolean bo_ifinvokeExcel = false;

    private ExcelGenerateService() {
    }
    
    /**
     * 
     * @param _file_name 创建的文件名实际路径
     * @param _names 列名
     * @param _types 列类型
     * @param _widths 列宽度
     * @param values 实际值
     */
    public ExcelGenerateService(String _file_name, String[] _names, String[] _types, int[] _widths,Object[][] values) {
        this.str_path = _file_name; // 文件全路径名
        this.str_title = _names;
        this.str_types = _types;
        this.li_widths = _widths;
        this.obj_values = values;

        initExcel();
    }


    private void initExcel() {
        
        try {
            if (!str_path.toLowerCase().endsWith(".xls") && !str_path.toLowerCase().endsWith(".csv")) {
                str_path = str_path + ".xls";
            }
            fos_stream = new FileOutputStream(str_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.info("导出到Excel时，创建" + str_path + "文件出错！");
            return;
        }
        
        hwb_excel = new HSSFWorkbook(); // 建立新HSSFWorkbook对象        
        hs_sheet = hwb_excel.createSheet();
        hs_sheet.setGridsPrinted(true);
        
        setColumnTitles(str_title);
        generateCellStyle(str_types);
        if(obj_values!=null){
	        printValues(obj_values);
	        endPrintValue();//关闭文件
        }
    }

    public void addTitleItem(String _title, short _color, int _width) {
        if (_title == null) {
            return;
        }
        HSSFRow row = hs_sheet.getRow(0);
        if (row == null) {
            row = hs_sheet.createRow(0);
            li_index++;
        }

        HSSFCellStyle cellStyle = hwb_excel.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFFont hf_font = hwb_excel.createFont();
        hf_font.setFontName("宋体");
        hf_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hf_font.setFontHeightInPoints( (short) 12);
        hf_font.setColor(_color);

        HSSFCell cell = row.createCell( (short) li_cellindex);
        cellStyle.setFont(hf_font);
        cell.setCellStyle(cellStyle);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(_title);
        if (_width != 0) {
            hs_sheet.setColumnWidth( (short) li_cellindex, (short) (_width * 8 / ( (double) 1 / 20)));
        }
        li_cellindex++;
    }

    /**
     * 设置列标题
     *
     * @param _title
     */
    public void setColumnTitles(String[] _title) {
        if (_title == null) {
            return;
        }
        for (int i = 0; i < _title.length; i++) {
            addTitleItem(_title[i], HSSFColor.BLACK.index, li_widths[i] / 5);
        }
    }

    /**
     * 设置列宽度
     *
     * @param _widths
     */
    public void setColumnWidths(int[] _widths) {
        if (_widths == null) {
            return;
        }
        for (int i = 0; i < _widths.length; i++) {
            hs_sheet.setColumnWidth( (short) i, (short) (_widths[i] * 8 / ( (double) 1 / 20)));
        }
    }

    /**
     * 设置列宽度
     *
     * @param _widths
     */
    public void setColumnWidths(int[] _cols, int[] _widths) {
        if (_widths == null || _cols == null) {
            return;
        }
        for (int i = 0; i < _cols.length; i++) {
            hs_sheet.setColumnWidth( (short) _cols[i], (short) (_widths[i] * 8 / ( (double) 1 / 20)));
        }
    }

    /**
     * 向Excel表格中填充数据
     *
     * @param _obj
     * @throws Exception
     */
    public void printValues(Object[][] _obj) {
        if (_obj == null) {
            return;
        }

        for (int i = 0; i < _obj.length; i++) {
        	//System.out.println("创建Excel第"+li_index+"行");
            HSSFRow row = hs_sheet.createRow(li_index); // 建立新行
            int real_cols=(_obj[i].length==this.str_title.length?_obj[i].length:(_obj[i].length - 1));
            for (int j = 0; j <real_cols ; j++) {
                HSSFCellStyle cellStyle = getCellStyle(j);
                HSSFCell dCell = row.createCell((short)j);

                if (str_types[j].equals("文本框")) {
                    dCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                } else if (str_types[j].equals("数字框")) {
                    dCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                } else {
                    dCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                }

                //dCell.setEncoding(HSSFCell.ENCODING_UTF_16);

                if (_obj[i][j] != null) {
                    dCell.setCellValue(_obj[i][j].toString()); //
                } else {
                    dCell.setCellValue(""); //
                }
                if (cellStyle != null) {
                    // dCell.setCellStyle(cellStyle); //
                }
            }
            li_index++;
        }
    }
    
    /**
     * 结束Excel文件编辑
     */
    public void endPrintValue(){
    	try {
            hwb_excel.write(fos_stream);
            logger.info( "数据导出到Excel成功!");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info( "数据导出到Excel出现异常!");
        } finally {
            try {
                fos_stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info( "数据导出到Excel成功，但关闭导出流出现异常!");
            }
        }
    }
    

    /**
     * 根据参数来创建指定列的显示格式
     *
     * @param _cols:要采用指定格式的列
     * @param _fomart
     * @param _aligment
     */
    public void generateCellStyle(String[] _types) {
        li_cols = new int[_types.length];
        hcs_style = new HSSFCellStyle[li_cols.length];

        for (int i = 0; i < _types.length; i++) {
            li_cols[i] = i;
            HSSFDataFormat format = hwb_excel.createDataFormat();
            hcs_style[i] = hwb_excel.createCellStyle();
            if (_types[i] == null) {
                continue;
            }

            if (_types[i].equals("数字框")) {
                hcs_style[i].setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            } else if (_types[i].equals("时间")) {
                hcs_style[i].setDataFormat(format.getFormat("yyyy-mm-dd hh:mm:ss"));
            } else if (_types[i].equals("日历")) {
                hcs_style[i].setDataFormat(format.getFormat("yyyy-mm-dd"));
            }
        }
    }

    /**
     * 获得指定列的CellStyle
     *
     * @param _col
     * @return
     */
    private HSSFCellStyle getCellStyle(int _col) {
        if (li_cols == null || hcs_style == null) {
            return null;
        }
        for (int i = 0; i < li_cols.length; i++) {
            if (_col == li_cols[i]) {
                return hcs_style[i];
            }
        }
        return null;
    }

    /**
     * 向Excel表格中填充一行
     *
     * @param _row
     */
    public void printRow(Object[] _row) {

    }

    /**
     * 向Excel表格中填充一行
     *
     * @param _vec
     */
    public void printRow(Vector _vec) {

    }

    /**
     * 获得Excel表格中所有的数据
     *
     * @return
     */
    public Object[][] getExcelValues() {
        return null;
    }

    /**
     * 获得Excel表中各指定行的数据
     *
     * @param _row
     * @return
     */
    public Object[] getRowValues(int _row) {
        return null;
    }

    /**
     * 获得Excel表格中指定列的数据
     *
     * @param _col
     * @return
     */
    public Object[] getColumnValues(int _col) {
        return null;
    }

    /**
     * 获得Excel表格中每列的列名
     *
     * @return
     */
    public Object[] getTitleValues() {
        return null;
    }

    /**
     * 获得Excel表格中指定区域的数据
     *
     * @param _beginrow
     * @param _endrow
     * @param _begincol
     * @param _endcol
     * @return
     */
    public Object[][] getSpecifyValues(int _beginrow, int _endrow, int _begincol, int _endcol) {
        return null;
    }

//    public void invokeExcel() {        
//        try {
//            String str_path = WindowsCOM.getExcelExePath();
//            if (str_path != null) {
//                String str_command = str_path + " " + "\"" + this.str_path + "\""; //
//                System.out.println(str_command); //
//                Runtime.getRuntime().exec(str_command); // 调用Excel
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            JOptionPane.showMessageDialog(this.con_parent, "直接调用Excel来查看导出数据出现异常！");
//        } //
//
//    }

    public static void main(String[] argv) {
    }
}