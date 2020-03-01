package smartx.framework.common.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.apache.regexp.RESyntaxException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


/**
 * 通用Util工具
 * 它与VO一样,无论UI还是BS都会用到!!!但VO一般都是存放数据对象(载体),这里存放的上工具类,比如字符处理类!!
 * @author user
 *
 */
public class StringUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(StringUtil.class.getName());
	
    private StringUtil() {
    }

    /**
     * 替换字符串
     * @param str_par 字符串
     * @param old_item 比较值
     * @param new_item 新值
     * @return
     */
    public static String replaceAll(String str_par, String old_item, String new_item) {
        String str_return = "";
        String str_remain = str_par;
        int li_pos=-1;
        while ((li_pos = str_remain.indexOf(old_item))>=0) {
            String str_prefix = str_remain.substring(0, li_pos);
            str_return = str_return + str_prefix + new_item; // 将结果字符串加上原来前辍
            str_remain = str_remain.substring(li_pos + old_item.length(), str_remain.length());
        }
        str_return = str_return + str_remain; // 将剩余的加上
        return str_return;
    }

    /**
     * 取得一个字符串中以{}包括的所有宏代码参数集
     * 比如"{aa} and  {bb}" 返回 ['aa','bb']
     * @param str 待查的字串
     * @return
     * @throws Exception
     */
    public static String[] getFormulaMacPars(String str){
    	return getFormulaMacParamsWest(str);
    }
    
    /**
     * 参数截取器（西方格式，即字母或者下划线开头，字母数字下划线结尾的名称）。
     * 取得一个字符串中以{}包括的所有宏代码参数集
     * 比如"{aa} and  {bb}" 返回 ['aa','bb']
     * @param str 待查的字串
     * @return
     * @throws Exception
     */
    public static String[] getFormulaMacParamsWest(String str){
    	return getFormulaMacParamsEast(str);
    }

    /**
     * 参数截取器。（东方格式，任意字符组成）
     * 取得一个字符串中以{}包括的所有宏代码参数集
     * 比如"{aa} and  {bb}" 返回 ['aa','bb']
     * @param str 待查的字串
     * @return
     * @throws Exception
     */
    public static String[] getFormulaMacParamsEast(String str){
    	Matcher mt=Pattern.compile("\\{([a-z,A-Z,.,_,\u4e00-\u9fa5,\\#]+[0-9]*\\(*\\)*)\\}").matcher(str);//    	
    	LinkedHashMap map=new LinkedHashMap();
    	
    	while(mt.find()){
    		map.put(mt.group(1),mt.group(1));
    	}
    	if(map.size()==0){return new String[0];}
    	String[] rt=(String[])map.keySet().toArray(new String[0]);
    	return (rt==null)?(new String[0]):rt;  	
    }
    
    /**
     * 解析参数字符创，根据制定的开始和结束符号
     * @param str
     * @param start 参数开始符号
     * @param end  参数结束符号
     * @return
     */
    public static String[] getFormulaMacParams(String str,String start,String end){
    	Matcher mt=Pattern.compile("\\"+start+"([a-z,A-Z,.,_,\u4e00-\u9fa5,\\#]+[0-9]*\\(*\\)*)\\" + end).matcher(str);//    	
    	LinkedHashMap map=new LinkedHashMap();
    	
    	while(mt.find()){
    		map.put(mt.group(1),mt.group(1));
    	}
    	if(map.size()==0){return new String[0];}
    	String[] rt=(String[])map.keySet().toArray(new String[0]);
    	return (rt==null)?(new String[0]):rt;  	
    }
    
    /**
     * 拼合数组。逗号隔开，前后空格
     * @param ary
     * @return
     */
    public static String getSelectCols(String[] ary) {
    	StringBuffer sbf = new StringBuffer(1024); 
    	//一开始有个空格
    	sbf.append(" ");
        for (int i = 0; i < ary.length; i++) {
            if (i > 0) {
            	sbf.append(",");              
            }
            sbf.append(ary[i]);
        }
        sbf.append(" ");//最后再加个空格
        return sbf.toString();
    }
    
    /**
	 * 运算带有参数的字符串。参数为start和end包围的部分
	 * 1)参数没有或者空串：返回"null"
	 * @param map 参数表
	 * @param exp 表达式，包含{表达式}
	 * @return 参数被替换了的表达式
	 */
	public static String buildExpression(HashMap<String,Object> map,String exp,String start,String end){
		if(exp==null)return null;
		String[] keys=getFormulaMacParams(exp,start,end);
		String rt=exp;
		for(int i=0;i<keys.length;i++){
			if(!map.containsKey(keys[i])){
				continue;
			}
			String v=String.valueOf(map.get(keys[i]));
			rt=replaceAll(rt,start+keys[i]+end,v);
		}
		return rt;
	}

    /**
	 * 运算带有参数的字符串。参数为大括号包围的部分，如表达式abc"{def}"{xyz}dd的参数有两个：def和xyz
	 * 1)参数没有或者空串：返回"null"
	 * @param map 参数表
	 * @param exp 表达式，包含{表达式}
	 * @return 参数被替换了的表达式
	 */
	public static String buildExpression(Map<String,String> map,String exp){
		if(exp==null)
			return null;
		if(map==null)
			return exp;
		
		String[] keys=getFormulaMacParamsWest(exp);
		String rt=exp;
		for(int i=0;i<keys.length;i++){
			if(!map.containsKey(keys[i])){
				continue;
			}
			String v=String.valueOf(map.get(keys[i])==null?"":map.get(keys[i]));
			//如果对应null，则解析为空字符串
			if(v == null)
				v = "";
			
			rt=replaceAll(rt,"{"+keys[i]+"}",v);
		}
		return rt;
	}
	
	/**
	 * 运算带有参数的字符串。参数为大括号包围的部分，如表达式abc"{def}"{xyz}dd的参数有两个：def和xyz
	 * 1)参数没有或者空串：返回"null"
	 * @param map 参数表，内部的每个参数都是String[]
	 * @param exp 表达式，包含{表达式}
	 * @param idx 参数值对应的位置
	 * @return 参数被替换了的表达式
	 */
	public static String buildExpressionArray(HashMap map,String exp,int idx){
		if(exp==null)return null;
		String[] keys=getFormulaMacParamsWest(exp);
		String rt=exp;
		for(int i=0;i<keys.length;i++){
			if(!map.containsKey(keys[i])){
				continue;
			}
			Object obj=map.get(keys[i]);
			//如果是数组，则按序号取值，否则直接转换为字符串
			String v=(obj instanceof String[])?((String[])obj)[idx]:String.valueOf(obj);
			rt=replaceAll(rt,"{"+keys[i]+"}",v);
		}
		return rt;
	}
    
    
    /**
     * 清除指定边界框的包含子句，如字串"abc『def』hij"清除"『"和"』"之间的东西得到"abchij"。
     */
    public static String clearSubstring(final String src,final String start,final String end){
    	int istart=src.indexOf(start);
    	if(istart==-1){
    		return src;
    	}
    	int iend=src.indexOf(end,istart+start.length());
    	if(iend==-1){
    		return src;
    	}
    	String tmp=src.substring(0,istart)+src.substring(iend+end.length());
    	//递归一下，看看还有没有
    	return clearSubstring(tmp,start,end);    	
    }

    /**
     * 解析多赋值语句。
     * 分号连接多个赋值语句，每个赋值语句用等号区别变量和取值。
     * @param _ref
     * @return
     */
    public static HashMap parseParamsUpper(String _ref){
    	//解析命令参数
    	String[] str_items = _ref.split(";");
        HashMap pmap=new HashMap();
        for (int i = 0; i < str_items.length; i++) {
            String item = str_items[i];
            if (item.equals("")||item.indexOf("=")<0) {
                continue;
            }
            String[] kvs=item.split("=");                       
            pmap.put(kvs[0].toUpperCase(), kvs[1]);//参数值            
        }
        return pmap;
    }
    
    /**
     * 获得对象的int值
     * @param obj
     * @param value 空或者不能转换时取得的默认值
     * @return
     */
    public static int parseIntHex(String hex,int value){
    	if(hex==null) return value;
    	try{
    		return Integer.parseInt(hex,16);    	
    	}catch(NumberFormatException e){
    		return value;
    	}
    }
    
    /**
     * 获得对象的int值
     * @param obj
     * @param value 空或者不能转换时取得的默认值
     * @return
     */
    public static int parseInt(Object obj,int value){
    	if(obj==null) return value;
    	if(obj instanceof Number){
    		return ((Number)obj).intValue();
    	}
    	String tmp=obj.toString();
    	try{
    		return Double.valueOf(tmp).intValue();    	
    	}catch(NumberFormatException e){
    		return value;
    	}
    }
    /**
     * 获得对象的String值
     * @param obj
     * @param value 空或者不能转换时取得的默认值
     * @return
     */
    public static String parseString(Object obj,String value){
    	if(obj==null) return value;
    	return obj.toString();    	
    }
    
    /**
     * 短日期转换文本
     * @param dt
     * @return
     */
    public static String shortDateToString(final Date dt) {
        if (null != dt) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(dt);
        }
        return null;
    }
    /**
     * 文本转换短日期
     * @param s
     * @return
     */
    public static Date shortStringToDate(final String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");            
            return sdf.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 连接字符串数组，半角逗号分隔
     * @param ary
     * @return
     */
    public static String[] splitString2Array(String str){
    	if(str==null||str.equals("")){
    		return new String[0];
    	}
    	return str.split(",");
    }
    
    /**
     * 连接字符串数组，半角逗号分隔
     * @param ary
     * @return
     */
    public static String joinArray2String(String[] ary){
    	if(ary==null||ary.length==0){
    		return "";
    	}
    	StringBuffer sbf=new StringBuffer(1024);
    	for(int i=0,count=ary.length;i<count;i++){
    		sbf.append(",").append((ary[i]==null?"":ary[i]));
    	}
    	return sbf.toString().substring(1);
    }
    
    /**
     * 连接字符串数组。分隔符和包围符号都可以设置
     * @param ary 字符串数组
     * @param split 分隔符
     * @param srd 包围字符串的符号
     * @return
     */
    public static String joinArray2String(String[] ary, String split, String srd){
    	if(ary==null||ary.length==0){
    		return "";
    	}
    	StringBuffer sbf=new StringBuffer(2048);
    	for(int i=0,count=ary.length;i<count;i++){
    		sbf.append(",").append(srd).append((ary[i]==null?"":ary[i])).append(srd);
    	}
    	return sbf.toString().substring(1);
    }
    
    /**
     * xml字符串格式化
     * @param str
     * @return
     * @throws Exception
     */
	public static String xmlFormat(String str) throws Exception {
		// System.out.println(" str :  " + str);
		Document doc = DocumentHelper.parseText(str);
		// 创建输出格式
		OutputFormat formater = OutputFormat.createPrettyPrint();
		// 设置xml的输出编码
		formater.setEncoding("gbk");
		// 创建输出(目标)
		StringWriter out = new StringWriter();
		// 创建输出流
		XMLWriter writer = new XMLWriter(out, formater);
		// 输出格式化的串到目标中，执行后。格式化后的串保存在out中。
		writer.write(doc);

		//System.out.println(out.toString());
		// 返回我们格式化后的结果
		return out.toString();
	}
	
	/**
     * Document转换成String输出
     * @param str
     * @return
     * @throws Exception
     */
	public static String doc4String(Document doc) throws Exception {
		// 创建输出格式
		OutputFormat formater = OutputFormat.createPrettyPrint();
		// 设置xml的输出编码
		formater.setEncoding("gbk");
		// 创建输出(目标)
		StringWriter out = new StringWriter();
		// 创建输出流
		XMLWriter writer = new XMLWriter(out, formater);
		// 输出格式化的串到目标中，执行后。格式化后的串保存在out中。
		writer.write(doc);

		//System.out.println(out.toString());
		// 返回我们格式化后的结果
		return out.toString();
	}
	
	//add by zhangzy 20111128 添加部分字符中处理方法
	/**
	 * 分割参数
	 * 
	 * @param paraSrc
	 *            String
	 * @param sepa
	 *            String
	 * @return Map sample : "a=b,c=d,..."
	 */
	public static Map splitPara(String paraSrc, String sepa) {
		if (paraSrc == null || paraSrc.trim().length() == 0) {
			return null;
		}

		LinkedHashMap paraMap = new LinkedHashMap();
		if (sepa == null || sepa.equals("")) { // 默认分割参数的分隔符为“,”
			sepa = ",";
		}

		/**
		 * 
		 */
		String[] paras = paraSrc.split(sepa);
		for (int i = 0, j = 0; i < paras.length; i++) {
			String tmpResult[] = paras[i].split("=");
			if (tmpResult.length >= 2) { // 2 a=b
				paraMap.put(tmpResult[0].trim(), tmpResult[1]);
			} else if (tmpResult.length == 1) {
				if (paras[i].indexOf("=") >= 0) { // 1 a=
					paraMap.put(tmpResult[0].trim(), "");
				} else { // 0 a
					paraMap.put("TEXT." + j, paras[i]);
					j++;
				}
			}
		}

		return paraMap;
	}
	
	/**
	 * @param srcStr 为输入源字符串，此处为";aa;;"
	 * @param split 为分隔字符串，此处为";"
	 * @param flag 表示：当最后的那个分隔符后面是null时，是否也将他作为一个元素返回
	 * @return String[]
	 */
	public static String[] splitString2Array(String srcStr, String split, boolean flag) {
		ArrayList<String> list = new ArrayList<String>();
        int m = 0 - split.length();
        int n = srcStr.indexOf(split);
        if (n == -1) {
            return (String[])list.toArray(new String[0]);
        }
        while (true) {
            if (n == -1) {
                break;
            }
            String str = srcStr.substring(m + split.length(), n);
            list.add(str);
            m = n;
            n = srcStr.indexOf(split, m + split.length());
        }
        String ss = srcStr.substring(m + split.length());
        if (ss.length() != 0 || flag) {
            list.add(ss);
        }
        
        return (String[])list.toArray(new String[0]);
	}
     

	/**
	 * 半角字符转全角字符
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
//	public static String toDBCS(String str) {
//		if (str == null) {
//			return "";
//		}
//
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0, n = str.length(); i < n; i++) {
//			int c = str.charAt(i);
//			if ((c >= 'a') && (c <= 'z')) {
//				c = (c + 'ａ') - 'a';
//			} else if ((c >= 'A') && (c <= 'Z')) {
//				c = (c + 'Ａ') - 'A';
//			} else if ((c >= '0') && (c <= '9')) {
//				c = (c + '０') - '0';
//			}
//
//			sb.append((char) c);
//		}
//
//		return sb.toString();
//	}

	/**
	 * 全角字符转半角字符
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
//	public static String toSBCS(String str) {
//		if (str == null) {
//			return "";
//		}
//
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0, n = str.length(); i < n; i++) {
//			int c = str.charAt(i);
//			if ((c >= 'Ａ') && (c <= 'Ｚ')) { // A ..
//				c = (c + 'A') - 'Ａ';
//			} else if ((c >= 'ａ') && (c <= 'ｚ')) { // a ..
//				c = (c + 'a') - 'ａ';
//			} else if ((c >= '０') && (c <= '９')) { // 0 ..
//				c = (c + '0') - '０';
//			} else if (c == '”') { // 双引号
//				c = '"';
//			} else if (c == '“') {
//				c = '"';
//			} else if (c == '＜') { // 小于号
//				c = '<';
//			} else if (c == '＞') {
//				c = '>';
//			} else if (c == '’') { // 单引号
//				c = '\'';
//			} else if (c == '‘') {
//				c = '\'';
//			} else if (c == '，') { // 逗号
//				c = ',';
//			} else if (c == '；') { // 分号
//				c = ';';
//			} else if (c == '。') {
//				c = '.';
//			} else if (c == '＆') {
//				c = '&';
//			}
//
//			sb.append((char) c);
//		}
//
//		return sb.toString();
//	}

	/**
	 * 计算布尔表达式
	 * 
	 * @param boolexpr
	 *            String
	 * @return boolean
	 */
	public static boolean boolExpression(String boolexpr) {
		if (boolexpr == null || boolexpr.trim().length() == 0) {
			return true;
		}

		try {
			String s = (String) new ExpressionUtil().calculate(boolexpr);
			return s.equals("1") || s.equals("true");
		} catch (Exception ex) {
			System.out.println("boolExpression:" + ex.getMessage());
			return false;
		}
	}

//	/**
//	 * 计算口语化的布尔表达式 支持 gt lt ge le eq and or not 1 0
//	 * 
//	 * @param boolexpr
//	 *            String
//	 * @return boolean
//	 */
//	public static boolean boolExpr(String boolexpr) {
//		if (boolexpr == null || boolexpr.trim().length() == 0) {
//			return true;
//		}
//
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*gt\\s*", ">");
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*lt\\s*", "<");
//
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*ge\\s*", ">=");
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*le\\s*", "<=");
//
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*eq\\s*", "==");
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*ne\\s*", "!=");
//
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*not\\s*", "!");
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*and\\s*", "&&");
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*or\\s*", "||");
//
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*1\\s*", " true ");
//		boolexpr = StringUtil.replaceAll(boolexpr, "\\s*0\\s*", " false ");
//
//		Object b = StringUtil.bshEval(boolexpr);
//		return b.toString().equals("true");
//	}

	/**
	 * 返回某字符串中所有符合正则表达式的子字符串，以字符串数组形式返回
	 * 
	 * @param src
	 *            String
	 * @param pattern
	 *            String
	 * @return String[]
	 */
	public static String[] findAll(String src, String pattern) {
		if (src == null) {
			return new String[0];
		}
		if (pattern == null) {
			return new String[0];
		}

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(src);
		Collection l = new ArrayList();
		while (m.find()) {
			l.add(m.group());
		}

		return (String[]) l.toArray(new String[] {});
	}

	/**
	 * 替换符合正则表达式的所有子字符串为新字符串
	 * 
	 * @param src
	 *            String
	 * @param pattern
	 *            String
	 * @param to
	 *            String
	 * @return String
	 */
	public static String replaceAll2(String src, String pattern, String to) {
		if (src == null) {
			return null;
		}
		if (pattern == null) {
			return src;
		}

		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(src);

		int i = 1;
		while (m.find()) {
			// System.out.println("找到第" + i + "个匹配:" + m.group() +
			// " 位置为:" + m.start() + "-" + (m.end() - 1));
			m.appendReplacement(sb, to);
			i++;
		}
		m.appendTail(sb);
		// System.out.println("替换后的结果字符串为:" + sb);
		return sb.toString();
	}

	/**
	 * 组织数组为SQL查询中的In子句: (x,y,z) 如果数组是字符串，那么组织为 ('x','y','z')
	 * 
	 * @param conditions
	 *            String[]
	 * @param isString
	 *            boolean
	 * @return String
	 */
	public static final String getQryCondtion(String[] conditions,
			boolean isString) {
		if (conditions == null || conditions.length <= 0) {
			return null;
		}
		StringBuffer cond = new StringBuffer("(");
		for (int i = 0; i < conditions.length; i++) {
			if (isString) {
				cond.append("'").append(conditions[i]).append("'");
			} else {
				cond.append(conditions[i]);
			}
			cond.append(",");
		}
		cond.replace(cond.length() - 1, cond.length(), ")");

		return cond.toString();
	}

	/**
	 * 
	 * @param c
	 *            Collection
	 * @return String
	 */
	public static String loadFromCollection(Collection c) {
		StringBuffer sb = new StringBuffer();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			sb.append(it.next().toString());
		}
		return sb.toString();
	}

	/**
	 * 从TEXT FILE 加载资源, 对英文资源。
	 * 
	 * @param filename
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return String
	 */
	public static String loadFromFile(String filename)
			throws FileNotFoundException, IOException {
		StringBuffer sb = new StringBuffer();
		FileInputStream in = new FileInputStream(filename);
		byte[] buf = new byte[1024];

		try {
			while (true) {
				int n = in.read(buf);
				sb.append(new String(buf, 0, n));
				if (n < 1024) {
					break;
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			in.close();
		}

		return sb.toString();
	}

	/**
	 * 从TEXT FILE 加载资源，根据文件的具体编码方式
	 * 
	 * @param filename
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return String
	 */
	public static String loadFromFile(String filename, String encoding)
			throws FileNotFoundException, IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader b = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), encoding));

		try {
			while (true) {
				String s = b.readLine();
				if (s != null) {
					sb.append(s + "\r\n");
				} else {
					break;
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			b.close();
		}

		return sb.toString();
	}

	/**
	 * 从URL 加载资源
	 * 
	 * @param url
	 *            String
	 * @throws MalformedURLException
	 * @throws IOException
	 * @return String
	 */
	public static String loadFromUrl(String url) throws MalformedURLException,
			IOException {
		StringBuffer sb = new StringBuffer();
		URL u = new URL(url);
		InputStream in = u.openStream();
		byte[] buf = new byte[1024];

		try {
			while (true) {
				int n = in.read(buf);
				sb.append(new String(buf, 0, n));
				if (n < 1024) {
					break;
				}
			}
		} catch (IOException ex) {
			in.close();
			throw ex;
		} finally {
			in.close();
		}

		return sb.toString();
	}

	/**
	 * 字符串替换：如（"aa_a","a_","A"）即将字符串"aa_a"中的"a_"替换为"A" 得到aAa
	 * 
	 * @param source
	 * @param oldString
	 * @param newString
	 * @return 替换后的字符串
	 * @deprecated Replaced by
	 *             <code>StringUtil.repalce(String, String,String)</code>
	 */
	public static String Replace(String source, String oldString,
			String newString) {
		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length();
		int lengthOfOld = oldString.length();
		int posStart;
		int pos;
		for (posStart = 0; (pos = source.indexOf(oldString, posStart)) >= 0; posStart = pos
				+ lengthOfOld) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
		}

		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	/**
	 * 字符串替换：如（"aa_a","a_","A"）即将字符串"aa_a"中的"a_"替换为"A" 得到aAa
	 * 
	 * @param src
	 * @param oldStr
	 * @param newStr
	 * @return 替换后的字符串
	 */
	public static String replace(String src, String oldStr, String newStr) {
		return Replace(src, oldStr, newStr);
	}

	/**
	 * return String basename
	 * 
	 * @param name
	 *            String
	 * @param split
	 *            String
	 * @return String com.ztesoft.ispp.ne --> ne
	 */
	public static String pathname(String name, String split) {
		if (name == null || name.equals("")) {
			return "";
		}
		if (split == null || split.equals("")) {
			split = ".";
		}

		int index = name.lastIndexOf(split);
		if (index >= 0) {
			return name.substring(0, index);
		}

		return name;
	}

	/**
	 * return String basename
	 * 
	 * @param name
	 *            String
	 * @param split
	 *            String
	 * @return String com.ztesoft.ispp.ne --> ne
	 */
	public static String basename(String name, String split) {
		if (name == null || name.equals("")) {
			return "";
		}
		if (split == null || split.equals("")) {
			split = ".";
		}

		int index = name.lastIndexOf(split);
		if (index >= 0) {
			return name.substring(index + split.length());
		}

		return "";
	}

	/**
	 * 判断字符串是否匹配指定的正则表达式
	 * 
	 * @param src
	 * @param regexp
	 * @return boolean
	 * @throws RESyntaxException 
	 */
	public static boolean isMatch(String src, String regexp) throws RESyntaxException {
		RE r = new RE();
		RECompiler compiler = new RECompiler();
		r.setMatchFlags(RE.MATCH_SINGLELINE);
		r.setProgram(compiler.compile(regexp));
		return (r.match(src));
	}

	/**
	 * 
	 * @param src
	 *            String
	 * @return String
	 */
	public static String firstCharToUpperCase(String src) {
		if (src == null) {
			return null;
		}

		if (src.length() > 0) {
			src = src.substring(0, 1).toUpperCase() + src.substring(1);
		}

		return src;
	}

	/**
	 * 字符串数组中是否包含指定的字符串。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param string
	 *            字符串
	 * @param caseSensitive
	 *            是否大小写敏感
	 * @return 包含时返回true，否则返回false
	 */
	public static boolean contains(String[] strings, String string,
			boolean caseSensitive) {
		for (int i = 0; i < strings.length; i++) {
			if (caseSensitive == true) {
				if (strings[i].equals(string)) {
					return true;
				}
			} else {
				if (strings[i].equalsIgnoreCase(string)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String formatUnicode(String str){
		 String regex1 = "&#x";
		 String regex2 = ";";
		if(str.indexOf(regex1)!=-1){
			str = str.replaceAll(regex1, "");
			if(str.indexOf(regex2)!=0){
				str = str.replaceAll(regex2, "");
			}
			byte[] temp = ByteUtil.hex2bytes(str);
			try {
				str = new String(temp,"UNICODE");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return str;
	}

	public static String transCharSet(String str,String oldCharEncoding) throws Exception{
		if(str == null)
			return null;
		
		String s = new String(str.getBytes(oldCharEncoding));
		return s;
	}
	
	public static String transCharSet(String str,String oldCharEncoding,String newCharEncoding) throws Exception{
		if(str == null)
			return null;
		
		String s = new String(str.getBytes(oldCharEncoding),newCharEncoding);
		return s;
	}
	
	//判断是否为空
	public static boolean isEmpty(String str){
		boolean flag = false;
		if(str == null || str.trim().equals("")){
			flag = true;
		}
		return flag;
	}
	
	public static boolean isNumber(String str){
		boolean flag = false;
		try{
			Integer.parseInt(str);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 去除字符串首尾的空格
	 * @param s 字符串
	 * @return
	 */
	public static String trim(String s) {
		int i = s.length();
		int j = 0;
		int k = 0;
		char[] arrayOfChar = s.toCharArray();
		while ((j < i) && (arrayOfChar[(k + j)] <= ' '))
			++j;
		while ((j < i) && (arrayOfChar[(k + i - 1)] <= ' '))
			--i;
		return (((j > 0) || (i < s.length())) ? s.substring(j, i) : s);
	}
	
	/**
	 * 得到当前时间戳(年月日时分秒)
	 * @return
	 */
	public static String getTimestamp(){
		Calendar date = new GregorianCalendar();
		StringBuilder timestamp = new StringBuilder();
		timestamp.append(date.get(Calendar.YEAR)).append(addZero(date.get(Calendar.MONTH)+1,2)).append(addZero(date.get(Calendar.DATE),2))
		.append(addZero(date.get(Calendar.HOUR_OF_DAY),2)).append(addZero(date.get(Calendar.MINUTE),2)).append(addZero(date.get(Calendar.SECOND),2));
		return timestamp.toString();
	}
	
	/**
	 * 得到当前时间戳(年月日时分秒毫秒)
	 * @return
	 */
	public static synchronized String getTimestampMill(){
		Calendar date = new GregorianCalendar();
		StringBuilder timestamp = new StringBuilder();
		timestamp.append(date.get(Calendar.YEAR)).append(addZero(date.get(Calendar.MONTH)+1,2)).append(addZero(date.get(Calendar.DATE),2))
		.append(addZero(date.get(Calendar.HOUR_OF_DAY),2)).append(addZero(date.get(Calendar.MINUTE),2)).append(addZero(date.get(Calendar.SECOND),2))
		.append(addZero(date.get(Calendar.MILLISECOND),3));
		return timestamp.toString();
	}
	
	/**
	 * 位数不够前面补零
	 * @param len
	 * @param num
	 * @return
	 */
	private static String addZero(int num,int len){
		StringBuilder str = new StringBuilder();
		str.append(num);
		while(str.length() < len){
			str.insert(0, "0");
		}
		return str.toString();
	}
    
    /**
     * 主方法测试用!!
     * @param args
     */
    public static void main(String[] args)throws Exception {
        //		FrameWorkTBUtil util = new FrameWorkTBUtil();
        //		try {
        //			String[] str_aa = util.getFormulaMacPars("select aa from bb where '{tt}' and '{pp}' = kk and {rr} 止");
        //			for (int i = 0; i < str_aa.length; i++) {
        //				System.out.println(str_aa[i]); //
        //			}
        //
        //		} catch (Exception e) {
        //			e.printStackTrace();
        //		} //
    	
    	
    	//替换字符串
    	//System.out.println("[f{sa}d{sa}fd{sa}fdsa] ："+StringUtil.replaceAll("f{sa}d{sa}fd{sa}fdsa", "{sa}", "/james/"));
    	//替换的另一实现，这个正则式主要用来提取用
    	//System.out.println("[f{sa}d{sb}fd{sc}fdsd] ："+"f{sa}d{sb}fd{sc}fdsd".replaceAll("\\{[a-z,A-Z,_]+[0-9]*\\}", "/james/"));
    	//提取表达式字符串
    	//System.out.println("[f{sa}d{sb}fd{sc}fdsd] ："+Arrays.toString(StringUtil.getFormulaMacPars("f{sa}d{sb}fd{sc}fdsd_f{sa}d{sb}fd{sc}fdsd")));

    	String xml = "<root><a>ss'[@t11]'tt</a><b>2t{getdate#)}2</b></root>";
    	String[] test = getFormulaMacPars(xml);
    	
    	for(int i=0;i<test.length;i++)
    		System.out.println(test[i]);
    }

}
