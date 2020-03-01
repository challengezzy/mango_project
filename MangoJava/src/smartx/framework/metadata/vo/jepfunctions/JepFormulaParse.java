/**************************************************************************
 * $RCSfile: JepFormulaParse.java,v $  $Revision: 1.2.8.5 $  $Date: 2010/05/05 14:07:07 $
 **************************************************************************/
package smartx.framework.metadata.vo.jepfunctions;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.jepfunctions.*;

//TODO 本公式对象每次计算的时候都会被初始化，应该处理成单例类，用getInstance()方法。
/*
 * 由于本类已经在多个地方使用，每次使用都会进行初始化
 */

/**
 * 公式引擎
 * @author Administrator
 *
 */
public class JepFormulaParse {
    public static int li_ui = 0;
    public static int li_bs = 1;

    private static HashMap _map=new HashMap();
    private JEP parser=null;
	private Vector fundoc = null;
    private Vector ufundoc = null;
    
    /**
     * TODO 需要改造为单例处理
     * @param calltype 调用类型：UI调用、BS调用
     */
    public JepFormulaParse(int calltype) {
        init(calltype);
    }

    /**
     * 初始化公式引擎
     * @param calltype 调用类型：UI调用、BS调用
     */
    private void init(int calltype) {
        //如果已经初始化过，则不用重复初始化，把缓冲的取出即可
    	if(_map.containsKey("parse_"+calltype)){
    		parser=(JEP)_map.get("parse_"+calltype);
    		fundoc=(Vector)_map.get("fundoc_"+calltype);
    		ufundoc=(Vector)_map.get("ufundoc_"+calltype);
    		return;
        }
    	/**
    	 * 没有初始化过，下面执行初始化操作。
    	 */
    	parser=new JEP();    	
    	
    	// 增加所有API内置函数
        parser.addStandardFunctions(); 
        parser.addStandardConstants(); 
        
        // 平台定义函数的说明
        fundoc=getSysFun(parser,calltype);
        
        //注册项目自定义函数
        //ufundoc=getUserFun(parser,calltype);
        
        /**
         * 缓冲，保持唯一实例
         */
        _map.put("parse_"+calltype, parser);
        _map.put("fundoc_"+calltype, fundoc);
        _map.put("ufundoc_"+calltype, ufundoc);
        
    }
    
    /**
     * 按照函数名，函数说明，举例的方式加入Vector
     *
     * @return
     */
    private Vector getSysFun(JEP parse,int calltype) {
    	// 注册平台定义函数
        parser.addFunction("getColValue", new GetColValue(calltype)); // 加入getColValue()函数
        parser.addFunction("getColValueByDS", new GetColValueByDS(calltype)); // 加入getColValue()函数,根据数据源取数!!!!
        parser.addFunction("getFnValue", new GetFnValue(calltype)); // 获得存储函数值
        parser.addFunction("getLoginName", new GetLoginUserName()); // 加入getLoginName()函数
        parser.addFunction("getLoginCode", new GetLoginUserCode()); // 加入getLoginCode()函数
        parser.addFunction("getCurrDBDate", new GetCurrentDBDate(calltype)); // 加入getCurrDate()函数
        parser.addFunction("getCurrDBTime", new GetCurrentDBTime(calltype)); // 加入getCurrDate()函数
        parser.addFunction("getCurrDate", new GetCurrentDate()); // 加入getCurrDate()函数
        parser.addFunction("getCurrDate2", new GetCurrentDate2()); // 加入getCurrDate2()函数
        parser.addFunction("getCurrTime", new GetCurrentTime()); // 加入getCurrTime()函数
        parser.addFunction("toString", new ToString()); // 加入ToString函数
        parser.addFunction("indexOf", new IndexOf()); // 检索给定字符串中给定的字符或字符串
        parser.addFunction("subString", new SubString()); // 截取字符串
        parser.addFunction("replaceall", new ReplaceAll()); // 替换字符串中的内容
        parser.addFunction("numericFormat", new NumericFormat()); // 格式化数值显示
        parser.addFunction("messageBox", new MessageBox()); // 界面提示
    	
    	Vector rt=new Vector();
    	rt.add(new String[] {"getColValue(\"tablename\",\"fieldname\",\"con_field\",\"con_value\")",
                          "根据tablename,con_field,con_value，查询相应的数据，返回fielname的value",
                          "getColValue(\"pub_menu\",\"LOCALNAME\",\"ID\",\"22\") = \"客户查询\""});
    	rt.add(new String[] {
                 "getColValueByDS(\"datasourcename\",\"tablename\",\"fieldname\",\"con_field\",\"con_value\")",
                 "从指定数据源中,根据tablename,con_field,con_value，查询相应的数据，返回fielname的value",
                 "getColValueByDS(\"datasource_default\",\"pub_menu\",\"LOCALNAME\",\"ID\",\"22\") = \"客户查询\""});

    	rt.add(new String[] {"getFnValue(\"fnname\",\"pa_1\",……)", "获得存储函数的执行结果，函数的参数个数不确定，返回结果为String", ""});

    	rt.add(new String[] {"if(1==1,\"aaa\",\"bbb\")", "根据条件返回", "if(1==1,\"aaa\",\"bbb\"),则返回\"aaa\""});

    	rt.add(new String[] {"getLoginName()", "返回当前登录用户", "getLoginName()= \"ADMIN\""});
    	rt.add(new String[] {"getLoginCode()", "返回当前登录用户CODE", "getLoginCode()= \"ADMIN\""});

    	rt.add(new String[] {"getCurrDBDate()", "返回服务器端当前日期", "getCurrDBDate()= \"2007-01-05\""});
    	rt.add(new String[] {"getCurrDBTime()", "返回服务器端当前时间", "getCurrDBTime()= \"2007-01-05 10:05:33\""});

    	rt.add(new String[] {"getCurrDate()", "返回系统当前日期", "getCurrDate()= \"20070105\""});
    	rt.add(new String[] {"getCurrDate2()", "返回系统当前日期", "getCurrDate()= \"2007-01-05\""});
    	rt.add(new String[] {"getCurrTime()", "返回系统当前时间", "getCurrTime()= \"2007-01-05 10:05:33\""});

    	rt.add(new String[] {"toString(Object)", "把传入的参数object转化为String", "toString(123) = \"123\""});
    	rt.add(new String[] {"indexOf(\"string\",\"indexvalue\")", "检索string中indexvalue的位置",
                          "indexOf(\"abcdf\",\"c\") = 2"});
    	rt.add(new String[] {"subString(\"string\",beginindex,endindex)",
                          "截取string的beginindex-endindex之间的字符串", "subString(\"abcdefg\",1,5) = \"bcde\""});
    	rt.add(new String[] {"replaceall(\"string\",\"regexstring\",\"replacestring\")",
                          "将string中所有的regexstring替换为replacestring",
                          "replaceall(\"abcd abdg\",\"ab\",\"11\") = \"11cd 11dg\""});
    	rt.add(new String[] {"numericFormat(\"double\",\"formatstring\")",
                "格式化数值函数，类似0.00，#,##0.00，￥#,##0.00等等",
                "numericFormat(1234567.22822,\"#,##0.00\") = \"1,234,567.22\""});
    	rt.add(new String[] {"messageBox(\"msg\",\"消息类型info/warn/error\",\"返回值\")",
                "提示一个消息，并返回指定的值",
                "messageBox(\"本月值没有填写，不能计算！\", \"warn\", \"0\")=\"0\" "});
        return rt;
    }
    
    /**
     * 从数据库取出项目自定义公式并注册
     * @param parse
     * @param calltype 调用类型：UI调用、BS调用
     * @return
     */
    private Vector getUserFun(JEP parse,int calltype) {
    	/**
    	 * 如果NovaClientCache中存在自定义函数定义，则直接去此处的定义
    	 */
        Object[] obj_temp = NovaClientCache.getInstance().getRowValue("USER_FUNCTION");
        if (obj_temp != null && obj_temp[1].equals("JepFormulaParse")) {
            return (Vector) obj_temp[2];            
        }

        /**
         * 从数据库中取出定义
         */
        String[][] str_fun = null;
        String str_sql = "Select * From pub_formulafunctions";
        if (calltype == li_bs) {
            try {
                str_fun = new CommDMO().getStringArrayByDS(null, str_sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                str_fun = UIUtil.getStringArrayByDS(null, str_sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        Vector rt=new Vector();
        for (int i = 0; i < str_fun.length; i++) {
            String fn_name = str_fun[i][1]; // 应从数据库取!!
            String str_className = str_fun[i][3]; // 应从数据库取!!
            try {
                PostfixMathCommand formula = (PostfixMathCommand) Class.forName(str_className).newInstance(); //
                parser.addFunction(fn_name, formula); // 替换字符串中的内容
                rt.add(new String[] {fn_name, str_fun[i][5], str_fun[i][6]});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rt;
    }

    
    
    /**
     * 获得系统函数定义
     * @return
     */
    public Vector getSysFunDoc(){
    	return fundoc;
    }
    
    /**
     * 获得自定义函数定义
     * @return
     */
    public Vector getUserFunDoc() {
    	return ufundoc;
    }

    /**
     * 执行加载公式...
     *
     * @param _expr
     * @return
     */
    public Object execFormula(final String _expr) {
        try {
            parser.parseExpression(_expr); //执行公式
            return parser.getValueAsObject(); //
        } catch (Throwable ex) {
            return _expr;
        }
    }

    public static void main(String[] args) {
        JepFormulaParse parse = new JepFormulaParse(li_ui); //
        // String str_exp = "toString( \"aa\" +
        // getColValue(\"n1_menu\",\"localname\",\"id\",\"{parentmenuid}\")";
        // ////..
        //String str_exp = "if(12>15,\"yy\",\"bb\")";
        String str_exp = "getCurrDate2()";

        System.out.println(str_exp); //
        Object obj = parse.execFormula(str_exp); //
        System.out.println(obj); //
    }

}
/**************************************************************************
 * $RCSfile: JepFormulaParse.java,v $  $Revision: 1.2.8.5 $  $Date: 2010/05/05 14:07:07 $
 *
 * $Log: JepFormulaParse.java,v $
 * Revision 1.2.8.5  2010/05/05 14:07:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.4  2010/01/19 06:27:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.3  2010/01/18 10:22:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.2  2010/01/04 08:45:58  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2009/04/20 06:31:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:31  qilin
 * no message
 *
 * Revision 1.6  2007/03/30 08:10:55  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/22 04:40:15  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/09 09:52:23  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/09 09:36:32  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/07 02:47:23  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/07 02:25:01  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/07 02:04:19  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/02 05:02:50  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/01 02:04:29  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/01/31 09:09:22  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 07:52:10  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
