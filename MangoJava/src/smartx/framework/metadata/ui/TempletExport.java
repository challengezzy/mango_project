package smartx.framework.metadata.ui;


import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.NovaRemoteException;

/**
 * 模板导出工具
 * @author 
 *
 */
public class TempletExport {

	private static Logger logger=NovaLogger.getLogger(TempletExport.class);

	/**
	 * 模板多重导出接口
	 * @param _cmp _cmp 调用所在的组件 
	 * @param _sqlf 导出的模板sql文件名
	 * @param _temp 模板的编码
	 * @return 导出是否成功
	 */
	public boolean exportTemplets(Component _cmp,String _sqlf, String _temp){
		return exportFile(_cmp, _sqlf, _temp, true);
	}
	
	/**
	 * 单模板导出接口
	 * @param _cmp 调用所在的组件 
	 * @param _sqlf 导出的模板sql文件名
	 * @param _temp 模板的编码
	 * @return 导出是否成功
	 */
	public boolean exportTemplet(Component _cmp,String _sqlf, String _temp){
		return exportFile(_cmp, _sqlf, _temp, false);
	}
	
	
	/**
     * 模板批量导出接口。
     * 把选择的模板的构造SQL语句写入文件，包括一个总起文件All.sql
     * @param _cmp 调用所在的组件 
     * @param _sqlf 导出的模板sql文件名
     * @param _temp 模板的编码
     * @param isMulti 是否多重导出，true-生成All.sql，false-不生成All.sql
     * @return 导出是否成功
     */
    private boolean exportFile(Component _cmp, String _sqlf, String _temp, boolean isMulti) {
        if (_sqlf == null || _sqlf.equals("")) {
            JOptionPane.showMessageDialog(_cmp, "请输入正确的文件名称！");
            
        }
        File out_file = new File(_sqlf);
        if (!out_file.exists()) {
            if(isMulti && !createAllFile(_cmp,_sqlf)){
            	return false;
            }
            try {
                out_file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(_cmp, "创建" + _sqlf + ".sql出错！");
                logger.error("创建" + _sqlf + ".sql出错！",e);
                return false;
            }
        }
        FileWriter resultFile=null;
        PrintWriter myFile=null;
        try {
            resultFile = new FileWriter(out_file);
            myFile = new PrintWriter(resultFile);

            HashVO[] hv_data = null;
            try {
                hv_data = UIUtil.getHashVoArrayByDS(null,"Select * From PUB_TEMPLET_1 Where templetcode ='" + _temp + "'");
            }catch (Exception e) {
            	logger.error("检索模板发生错误！",e);
            	return false;
            }
            if (hv_data.length == 0) {
            	logger.error("没有检索到模板！");
                return false;
            }
            HashVO[] hv_item = null;
            try {
                hv_item = UIUtil.getHashVoArrayByDS(null, "Select * From PUB_TEMPLET_1_ITEM Where PK_PUB_TEMPLET_1 in " +
                        "(select pk_pub_templet_1 from pub_templet_1 where templetcode = '" + _temp + "')");
            } catch (Exception e) {
                logger.error("检索模板明细发生错误！",e);   
                return false;
            }
            
            
            String[] str_keys = hv_data[0].getKeys();//所有字段名
            
            myFile.println("-- 删除模板明细数据");
            myFile.println("delete pub_templet_1_item where pk_pub_templet_1 in " +
                    "(select pk_pub_templet_1 from pub_templet_1 where templetcode = '" + _temp + "');");
            myFile.println("-- 判断是否已经存在模板");
            myFile.println("DECLARE TEMPL_COUNT NUMBER(6);");
            myFile.println("BEGIN");
            myFile.println("  SELECT COUNT(*) INTO TEMPL_COUNT FROM pub_templet_1 where templetcode ='" + _temp + "';");
            myFile.println("  IF TEMPL_COUNT > 0 THEN");
                myFile.println("    -- 更新老的模板，ID没有变化");
                myFile.print("    UPDATE PUB_TEMPLET_1 SET ");
                String upd = "";
	            for (int j = 1; j < str_keys.length; j++) {
	            	upd +=", "+str_keys[j]+"="+getUpdateValue(hv_data[0].getStringValue(str_keys[j])) ;
                    
	            }
	            upd+=" WHERE templetcode = '" + _temp + "';";
	            myFile.println(upd.substring(1));	                       
            myFile.println("  ELSE");
                myFile.println("    -- 插入新模板，新生成ID");
	            myFile.print("    Insert Into PUB_TEMPLET_1 (");
	            String str_temp = "";
	            String str_value = "s_pub_templet_1.nextval";
	            for (int j = 0; j < str_keys.length; j++) {
	                str_temp = str_temp + str_keys[j] + ", ";
	                if (j > 0) {
	                    str_value = str_value + getInsertValue(hv_data[0].getStringValue(str_keys[j]));
	                }
	            }
	            myFile.print(str_temp.substring(0, str_temp.length() - 2) + ")");
	            myFile.print(" values (");
	            myFile.println(str_value + ");");
	            myFile.println("");
            myFile.println("  END IF;");
            
            myFile.println("  -- 插入模板明细，根据模板名称检索模板ID");
            for (int k = 0; k < hv_item.length; k++) {
            	myFile.print("  Insert Into pub_templet_1_item (");
                str_keys = hv_item[k].getKeys();
                str_temp = "";
                str_value = "s_pub_templet_1_item.nextval,";
                str_value = str_value + "(select PK_PUB_TEMPLET_1  From pub_templet_1 Where TEMPLETCODE = '" + _temp +
                    "')";
                for (int j = 0; j < str_keys.length; j++) {
                    str_temp = str_temp + str_keys[j] + ", ";
                    if (j > 1) {
                        str_value = str_value + getInsertValue(hv_item[k].getStringValue(str_keys[j]));
                    }
                }
                myFile.print(str_temp.substring(0, str_temp.length() - 2) + ")");
                myFile.print(" values (");
                myFile.println(str_value + ");");
            }
            myFile.println("  commit;");
            myFile.println("END;");
            myFile.println("/");
            
            return true;
        } catch (IOException e) {
            logger.error("输出模板sql发生错误！",e);   
            return false;
        }finally{
        	if(myFile!=null){
        		try { myFile.close(); } catch (Exception e) { }
        	}
        	if(resultFile!=null){
        		try { resultFile.flush(); } catch (Exception e) { }
        		try { resultFile.close(); } catch (Exception e) { }
        	}
        }
    }
	
    /**
     * 创建all.sql文件
     * @param _filepath
     * @return
     */
    private boolean createAllFile(Component _cmp,String _sqlf) {
        String all_name = "";
        StringTokenizer token_file = new StringTokenizer(_sqlf, "\\");

        int index = 0;
        int count = token_file.countTokens();
        while (token_file.hasMoreTokens()) {
            all_name = all_name + token_file.nextToken() + "\\";
            index++;
            if (index == count - 1) {
                break;
            }
        }
        String file_name = token_file.nextToken();
        all_name = all_name + "All.sql";

        File out_file = new File(all_name);
        try {
            FileWriter resultFile = new FileWriter(out_file, true);
            PrintWriter myFile = new PrintWriter(resultFile);
            myFile.println("@" + file_name);
            myFile.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(_cmp, file_name + "写入All.sql出错！");
            logger.error("将【"+file_name + "】写入All.sql出错！",e);
            return false;
        }
        return true;
    }
    
    
    /**
     * 根据_value来确定要更新的值
     * @param _value
     * @return
     */
    private String getUpdateValue(String _value) {
        String str_value = null;
        if (_value == null || _value.equals("")) {
            str_value = "null";
        } else {
            str_value = "'" + convert(_value) + "'";
        }
        return str_value;
    }
    /**
     * 根据_value来确定要插入的值
     * @param _value
     * @return
     */
    private String getInsertValue(String _value) {
        String str_value = null;
        if (_value == null || _value.equals("")) {
            str_value = ", null";
        } else {
            str_value = ", '" + convert(_value) + "'";
        }
        return str_value;
    }
    
    private String convert(String _str) {
        if (_str == null) {
            return "";
        }
        return _str.replaceAll("'", "''");
    }
	
}
