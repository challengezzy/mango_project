/**************************************************************************
 * $RCSfile: FrameWorkTBUtil.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/04/23 09:15:35 $
 **************************************************************************/
package smartx.framework.common.vo;

import java.io.*;
import java.util.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.metadata.vo.*;


/**
 * 通用Util工具
 * 它与VO一样,无论UI还是BS都会用到!!!但VO一般都是存放数据对象(载体),这里存放的上工具类,比如字符处理类!!
 * @author user
 *
 */
public class FrameWorkTBUtil implements Serializable {

    private static final long serialVersionUID = 1906433042446054793L;

    public FrameWorkTBUtil() {
    }

    

    

    /**
     * 转换含有表达式的字串
     * @param clenv 客户端环境变量
     * @param exp 需要被转换的表达式
     * @return 转换后的环境变量
     */
    public String convertExpression(NovaClientEnvironment clenv, String exp) {
        String[] keys = StringUtil.getFormulaMacPars(exp);
        String str_newdsn = exp;
        for (int i = 0; i < keys.length; i++) {
        	Object obj = clenv.get(keys[i]); 
            if (obj != null) {
                str_newdsn = StringUtil.replaceAll(str_newdsn, "{" + keys[i] + "}", obj.toString()); //替换
            }
        }
        return str_newdsn;        
    }

    /**
     * 转换公式中宏代码的内容,为客户端缓存或页面上的值!!!
     * @param _inittext
     * @param _env
     * @param _map
     * @return
     * @throws Exception
     */
    public String convertFormulaMacPars(String _oldsql, NovaClientEnvironment _clientenv, HashMap _map) throws
        Exception {
        String str_newformula = _oldsql;
        String[] str_allkeys = StringUtil.getFormulaMacPars(str_newformula); //

        for (int i = 0; i < str_allkeys.length; i++) {
            String str_key = str_allkeys[i]; //key名!!!!

            
            Object obj = (_clientenv==null)?null:_clientenv.get(str_key); 
            if (obj != null) { //如果从客户端缓存取到数了!!
                str_newformula = StringUtil.replaceAll(str_newformula, "{" + str_key + "}", obj.toString()); //替换
            } else { //从页面控件取
                int li_pos = str_key.indexOf("."); //看有没有"点操作"??
                String str_itemvalue = "";
                if (li_pos <= 0) { //如果没有点操作!!则直接取
                    obj = _map.get(str_key);
                    if (obj != null) {
                        if (obj instanceof String) {
                            str_itemvalue = "" + obj; //
                        } else if (obj instanceof ComBoxItemVO) { //如果是下拉框
                            str_itemvalue = ( (ComBoxItemVO) obj).getId(); //
                        } else if (obj instanceof RefItemVO) { //如果是参照!!
                            str_itemvalue = ( (RefItemVO) obj).getId(); //
                        } else {
                            str_itemvalue = "" + obj;
                        }
                    } else {

                    }
                } else { //如果有点操作!!
                    String str_key_prefix = str_key.substring(0, li_pos); //前辍
                    String str_key_subfix = str_key.substring(li_pos + 1, str_key.length()); //后辍

                    obj = _map.get(str_key_prefix); //
                    if (obj != null) {
                        if (obj instanceof String) {
                            str_itemvalue = "" + obj; //
                        } else if (obj instanceof ComBoxItemVO) { //如果是下拉框
                            str_itemvalue = ( (ComBoxItemVO) obj).getItemValue(str_key_subfix); //
                        } else if (obj instanceof RefItemVO) { //如果是参照!!
                            str_itemvalue = ( (RefItemVO) obj).getItemValue(str_key_subfix); //
                        } else {
                            str_itemvalue = "" + obj;
                        }
                    } else {
                    }
                }

                str_newformula = StringUtil.replaceAll(str_newformula, "{" + str_key + "}", str_itemvalue); //用页面控件的值替换!!!
            }
        } //end for 循环
        return str_newformula; //
    }

    

    /**
     * 取得一个字符串的unicode的长度!!
     * @param s
     * @return
     */
    public int getStrUnicodeLength(String s) { //这个方法是指送入一个字符串,算出其字节长度,如果字符串中有个中文字符,那么他的长度就算2
        char c;
        int j = 0;
        boolean bo_1 = false;
        if (s == null || s.length() == 0) {
            return 0;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= 0x100) {
                bo_1 = true;
                j = j + 2;
            } else {
                j = j + 1;
            }
        }
        if (bo_1) {
            j = j - 1;
        }
        return j;
    }

}
/**************************************************************************
 * $RCSfile: FrameWorkTBUtil.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/04/23 09:15:35 $
 *
 * $Log: FrameWorkTBUtil.java,v $
 * Revision 1.2.8.2  2009/04/23 09:15:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:47  qilin
 * no message
 *
 * Revision 1.2  2007/03/21 03:05:50  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/07 02:01:56  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/27 05:15:54  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:47:51  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
