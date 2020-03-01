/**********************************************************************
 *$RCSfile: JepFormulaUtil.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/09/16 06:12:40 $
 *********************************************************************/ 
package smartx.framework.metadata.vo.jepfunctions;
/**
 * <li>Title: JepFormulaUtil.java</li>
 * <li>Description: 简介</li>
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W Of VASG
 * @version 1.0
 */
public class JepFormulaUtil{
	
	/**
	 * 执行工时处理
	 * @param _exp
	 * @param lie  JepFormulaParse.li_ui 或 JepFormulaParse.li_bs
	 * @return
	 */
	public static String getJepFormulaValue(String _exp,int lie) {
        JepFormulaParse jepParse = new JepFormulaParse(lie); //
        Object obj = jepParse.execFormula(_exp);
        if (obj == null) {
            return "";
        }
        String str_return = "";

        if (obj instanceof Double) {
            Double d_value = (Double) obj;
            double ld_tmp = d_value.doubleValue();
            long ll_tmp = d_value.longValue(); //
            if (ld_tmp == ll_tmp) {
                return "" + ll_tmp;
            } else {
                return "" + ld_tmp;
            }
        } else if (obj instanceof String) {
            str_return = "" + obj;
        }

        return str_return;
    }
	
}

/**********************************************************************
 *$RCSfile: JepFormulaUtil.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/09/16 06:12:40 $
 *
 *$Log: JepFormulaUtil.java,v $
 *Revision 1.1.2.1  2008/09/16 06:12:40  wangqi
 *patch   : 20080916
 *file    : nova_20080128_20080916.jar
 *content : 处理 MR nova20-87,nova20-30；
 *另外，改写了快速查询面板的处理。
 *
 *********************************************************************/