package smartx.framework.common.utils;

/**
 ****    ParserUtil 解析工具类
 ****    ParserUtil class build 2006/04/28 10:00
 ****    CopyRight 2005-2006 Xiaoyuer
 ****    Author : ChenXiaoyuer
 ****    Email  : windflowers1976@msn.com
 ****    命令格式:
 ****    运行范例:
 ****    修改日志:  20070305 添加一些函数注释，添加一个通过XPATH 添加XML元素方法
 * ***             20070309 添加静态函数，抽取、检查带变量列表的正则表达式中的变量列表
 */

import java.io.*;
import java.util.*;
import org.apache.regexp.*;
import org.dom4j.*;

/**
 * 
 * <p>
 * Title: ParserUtil
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: xiaoyuer studio
 * </p>
 * 
 * @author Xiaoyuer Chen
 * @version 1.0
 */
public class ParserUtil {
	public final static int SUBSTR = 1000;
	public final static int GETFILED = 1001;
	public final static int MATCH = 1002;
	public final static int XMLATTR = 1003;
	public final static int DATETIME = 1004;
	public final static int DATETIMEMATH = 1005;
	public final static int QUESTION = 1006;
	public final static int DEFAULT = 1007;
	public final static int SPLIT = 1008;
	public final static int ARRAY = 1009;
	public final static int BEANSHELL = 1010;
	public final static int LIST = 1011;
	public final static int REGEXP = 1012;
	public final static int MAP = 1013;
	public final static int XML = 1014;

	private HashMap hm = new HashMap(1024);

	// /////////////////////////////////////////////////////////////////////////
	private String source = ""; // 预设源串
	private String fmt = ""; // 预设格式串
	protected String result = ""; // 生成结果串

	// /////////////////////////////////////////////////////////////////////////
	// 2005/11/10 加入如下优化代码
	private RE r = new RE();
	private RECompiler compiler = new RECompiler();

	// /////////////////////////////////////////////////////////////////////////
	// 2006/04/07 加入BEANSHELL使用 2008/11/15移动到方法变量里

	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 拷贝构造函数
	 * 
	 * @param parser
	 *            ParserUtil
	 */
	public ParserUtil(ParserUtil parser) {
		if (parser != null) {
			this.setSource(parser.getSource());
			this.setFmt(parser.getFmt());
			this.setHm(parser.getHm());
		}
	}

	/**
	 * 默认构造函数
	 */
	public ParserUtil() {
		addDefault("leftcdata", "<![CDATA["); // CDATA
		addDefault("rightcdata", "]]>"); // CDATA
		addDefault("leftmark", "${"); // ${ 左定界符号
		addDefault("rightmark", "}"); // } 右定界符号
	}

	/**
	 * 清除缓存的所有信息
	 */
	public void clear() {
		hm.clear(); // 清除所有变量
		addDefault("leftcdata", "<![CDATA["); // CDATA
		addDefault("rightcdata", "]]>"); // CDATA
		addDefault("leftmark", "${"); // ${ 左定界符号
		addDefault("rightmark", "}"); // } 右定界符号

		source = "";
		fmt = "";
		result = "";
	}

	// ///////////////////////////////////////////////////////////////////

	/**
	 * @return Returns the hm.
	 */
	public HashMap getHm() {
		return hm;
	}

	/**
	 * @param hm
	 *            The hm to set.
	 */
	public void setHm(HashMap hm) {
		this.hm = hm;
	}

	/**
	 * @return Returns the source.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            The source to set.
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return Returns the result.
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return Returns the fmt.
	 */
	public String getFmt() {
		return fmt;
	}

	/**
	 * @param fmt
	 *            The fmt to set.
	 */
	public void setFmt(String fmt) {
		this.fmt = fmt;
		result = ""; // 200508016
	}

	// ///////////////////////////////////////////////////////////////
	/**
	 * 20070307 添加另一个parserutil 的变量
	 * 
	 * @param parser
	 *            ParserUtil
	 * @return boolean
	 */
	public boolean addParser(ParserUtil parser) {
		if (parser == null) {
			return false;
		}
		Map m = parser.getHm();
		// this.hm.putAll(m); 不用此方法，以能删除旧元素
		Set s = m.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			String n = (String) me.getKey();
			String v = (String) me.getValue();
			if (hm.containsKey(n)) {
				hm.remove(n);
			}
			hm.put(n, v);
		}

		return true;
	}

	/**
	 * 添加一个MAP到缓存中，不需要加入前缀名 20070309 添加
	 * 
	 * @param name
	 *            String
	 * @param m
	 *            Map
	 * @return boolean
	 */
	public boolean addMap(Map m) {
		if (m == null) {
			return false;
		}

		Set s = m.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			String n = (String) me.getKey();
			String v = (String) me.getValue();
			if (hm.containsKey(n)) {
				hm.remove(n);
			}
			hm.put(n, v);
		}

		return true;
	}

	/**
	 * 添加一个MAP到缓存中
	 * 
	 * @param name
	 *            String
	 * @param m
	 *            Map
	 * @return boolean
	 */
	public boolean addMap(String name, Map m) {
		if (name == null || m == null) {
			return false;
		}

		Set s = m.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			String n = name + "." + me.getKey();
			String v = (String) me.getValue();
			if (hm.containsKey(n)) {
				hm.remove(n);
			}
			hm.put(n, v);
		}

		return true;
	}

	// * @see Set
	// * @see List
	// * @see Map
	// * @see SortedSet
	// * @see SortedMap
	// * @see HashSet
	// * @see TreeSet
	// * @see ArrayList
	// * @see LinkedList
	// * @see Vector
	// * @see Collections
	// * @see Arrays
	// * @see AbstractCollection
	/**
	 * 添加Collection 到缓存
	 * 
	 * @param name
	 *            String
	 * @param col
	 *            Collection
	 * @return boolean
	 */
	public boolean addCollection(String name, Collection col) {
		if (name == null || col == null) {
			return false;
		}

		int i = 0;
		Iterator it = col.iterator();
		while (it.hasNext()) {
			String n = name + "." + i;
			String s = (String) it.next();
			if (hm.containsKey(n)) {
				hm.remove(n);
			}
			hm.put(n, s);
			i++;
		}

		return true;
	}

	/**
	 * 添加数组变量到内存
	 * 
	 * @param name
	 *            String
	 * @param array
	 *            Object[]
	 * @return boolean
	 */
	public boolean addArray(String name, Object[] array) {
		if (name == null || array == null) {
			return false;
		}

		for (int i = 0; i < array.length; i++) {
			String n = name + "." + i;
			String s = (String) array[i];
			if (hm.containsKey(n)) {
				hm.remove(n);
			}
			hm.put(n, s);
		}

		return true;
	}

	/**
	 * 添加一个LIST到缓存中
	 * 
	 * @param name
	 *            String
	 * @param l
	 *            List
	 * @return boolean
	 */
	public boolean addList(String name, List l) {
		if (name == null || l == null) {
			return false;
		}

		int i = 0;
		Iterator it = l.iterator();
		while (it.hasNext()) {
			String n = name + "." + i;
			String s = (String) it.next();
			if (hm.containsKey(n)) {
				hm.remove(n);
			}
			hm.put(n, s);
			i++;
		}

		return true;
	}

	/**
	 * 添加字符计算，比如+- > < != 等等
	 * 
	 * @param name
	 *            String
	 * @param opt
	 *            String
	 * @param leftValue
	 *            String
	 * @param rightValue
	 *            String
	 * @return boolean
	 */
	public boolean addStrQuestion(String name, String opt, String leftValue,
			String rightValue) {
		if (name == null || opt == null || leftValue == null
				|| rightValue == null) {
			return false;
		}

		boolean flag = true;
		int compare = leftValue.compareTo(rightValue);

		if (hm.containsKey(name)) {
			hm.remove(name);
		}
		if (opt.equals("==") || opt.equals("=")) {
			flag = leftValue.equals(rightValue);
			leftValue = "true";
			rightValue = "false";
		} else if (opt.equals(">=")) {
			flag = compare >= 0;
			leftValue = "true";
			rightValue = "false";
		} else if (opt.equals("<=")) {
			flag = compare <= 0;
			leftValue = "true";
			rightValue = "false";
		} else if (opt.equals(">")) {
			flag = compare > 0;
			leftValue = "true";
			rightValue = "false";
		} else if (opt.equals("<")) {
			flag = compare < 0;
			leftValue = "true";
			rightValue = "false";
		} else if (opt.equals("<>") || opt.equals("!=")) {
			flag = compare != 0;
			leftValue = "true";
			rightValue = "false";
		} else if (opt.equals("+")) {
			leftValue += rightValue;
		} else if (opt.equals("-")) {
			int i = 0;
			if ((i = leftValue.indexOf(rightValue)) != -1) {
				leftValue = leftValue.substring(0, i)
						+ leftValue.substring(i + rightValue.length());
			}
		}

		if (flag) {
			hm.put(name, leftValue);
		} else {
			hm.put(name, rightValue);
		}

		return true;
	}

	/**
	 * 获取正则表达式抽取的变量，循环抽取直到无法匹配； 添加的变量名为 name.i.j
	 * 
	 * @param name
	 *            String
	 * @param regexp
	 *            String
	 * @return boolean
	 */
	public boolean addRegexpGroup(String name, String regexp) {
		return addRegexpGroup(source, name, regexp);
	}

	/**
	 * 获取正则表达式抽取的变量，循环抽取直到无法匹配； 添加的变量名为 name.i.j
	 * 
	 * @param src
	 *            String
	 * @param name
	 *            String
	 * @param regexp
	 *            String
	 * @return boolean
	 */
	public boolean addRegexpGroup(String src, String name, String regexp) {
		if (src == null || name == null || regexp == null) {
			return false;
		}

		// 2005/11/10 修改成如下代码
		r.setMatchFlags(RE.MATCH_SINGLELINE);
		r.setProgram((compiler.compile(regexp)));

		int row = 0;
		while (r.match(src)) {
			for (int col = 0; col < r.getParenCount(); col++) {
				String n = name + "." + row + "." + col;
				String value = r.getParen(col);

				if (hm.containsKey(n)) {
					hm.remove(n);
				}

				// logger.debug(n + ":" + value.trim());
				// 2006/02/14 发现中间有或时，返回NULL
				if (value == null) {
					hm.put(n, "");
				} else {
					hm.put(n, value.trim());
				}
			}

			row++;
			String pos = r.getParen(0);
			src = src.substring(src.indexOf(pos) + pos.length());
		}

		return true;
	}

	/**
	 * 是否字符串匹配
	 * 
	 * @param src
	 *            String
	 * @param regexpTemplate
	 *            String
	 * @return boolean
	 */
	public boolean isMatch(String src, String regexp) {
		r.setMatchFlags(RE.MATCH_SINGLELINE);
		r.setProgram(compiler.compile(this.result(regexp))); // 20070622 cxy
		// 优化
		// new ParserUtil().result(regexp)));
		return (r.match(src));
	}

	/**
	 * 是否字符串匹配，带模版的匹配串
	 * 
	 * @param src
	 *            String
	 * @param regexpTemplate
	 *            String
	 * @return boolean
	 */
	public boolean isMatchTemplate(String src, String regexpTemplate) {
		r.setMatchFlags(RE.MATCH_SINGLELINE);
		r.setProgram(compiler.compile(this.result(regexpTemplate))); // 20070622
		// cxy
		// 优化
		// new ParserUtil().result(regexpTemplate)));
		return (r.match(src));
	}

	/**
	 * 添加带模版信息的正则表达式组抽取
	 * 
	 * @param src
	 *            String
	 * @param regexpTemplate
	 *            String
	 * @return boolean
	 */
	public boolean addRegexpTemplateGroup(String src, String regexpTemplate) {
		return true;
	}

	/**
	 * 添加带模版信息的正则表达式抽取
	 * 
	 * @param src
	 *            String
	 * @param regexpTemplate
	 *            String
	 * @return boolean
	 */
	public boolean addRegexpTemplate(String src, String regexpTemplate) {
		if (src == null || regexpTemplate == null) {
			return false;
		}

		List l = getTemplateName(regexpTemplate);
		r.setMatchFlags(RE.MATCH_SINGLELINE);
		r.setProgram(compiler.compile(this.result(regexpTemplate))); // 20070622
		// cxy
		// 优化
		// new ParserUtil().result(regexpTemplate)));
		if (r.match(src)) {
			for (int i = 1; i < r.getParenCount(); i++) {
				String n = "var_" + i;
				if (l.size() > i - 1) {
					n = (String) l.get(i - 1);
				}
				String value = r.getParen(i);

				if (hm.containsKey(n)) {
					hm.remove(n);
				}
				if (value == null) {
					hm.put(n, "");
				} else {
					hm.put(n, value.trim());
				}
			}
		}

		return true;
	}

	/**
	 * 添加带模版信息的正则表达式抽取
	 * 
	 * @param regexpTemplate
	 *            String
	 * @return boolean
	 */
	public boolean addRegexpTemplate(String regexpTemplate) {
		return addRegexpTemplate(source, regexpTemplate);
	}

	/**
	 * 检查模版是否与LIST匹配
	 * 
	 * @param template
	 *            String
	 * @param namelist
	 *            List
	 * @return boolean
	 */
	public static boolean checkTemplateName(String template, List namelist) {
		List templist = getTemplateName(template);

		List copyOfName = new ArrayList(namelist);
		Collections.sort(copyOfName);

		List copyOfTemp = new ArrayList(templist);
		Collections.sort(copyOfTemp);
		if (copyOfName.size() != copyOfTemp.size()) {
			return false;
		}

		Iterator it = copyOfName.iterator();
		while (it.hasNext()) {
			String s = (String) it.next();
			if (!copyOfTemp.contains(s)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 获取模版参数名LIST
	 * 
	 * @param template
	 *            String
	 * @return List
	 */
	public static List getTemplateName(String NeResultParserSA) {
		LinkedList l = new LinkedList();

		// / 格式串为空，设置为源串
		if (NeResultParserSA == null || NeResultParserSA.length() == 0) {
			return l;
		}

		// / 格式串不含格式符号
		if (NeResultParserSA.indexOf("${") < 0
				&& NeResultParserSA.indexOf("}") < 0) {
			return l;
		}

		final int INIT = 0;
		final int LEFT = 1;
		final int RIGHT = 2;
		int index = 0;
		int flag = INIT;
		String tmp = NeResultParserSA;
		String tail = null;

		for (;;) {
			if (flag != LEFT && (index = tmp.indexOf("${")) >= 0) { // 已发现 "${"
				// 则当前不再找"${"，否则首先寻找"${"标志，并设置标志为LEFT
				flag = LEFT;
			} else if (flag != RIGHT && (index = tmp.indexOf("}")) >= 0) { // 已发现
				// "}"
				// 则当前不再找"}"，否则当前应寻找"}" 标志，并设置标志为RIGHT
				flag = RIGHT;
			} else { // 替换完成
				break;
			}

			switch (flag) {
			case LEFT: // "${"
				tmp = tmp.substring(index + 2);
				break;
			case RIGHT: // "}"
				String variable = tmp.substring(0, index);
				l.add(variable);
				tmp = tmp.substring(index + 1);
				break;
			}
		}

		return l;
	}

	/**
	 * 获取正则表达式抽取的变量
	 * 
	 * @param name
	 *            String
	 * @param regexp
	 *            String
	 * @return boolean
	 */
	public boolean addRegexp(String name, String regexp) {
		return addRegexp(source, name, regexp);
	}

	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 获取正则表达式抽取的变量
	 * 
	 * @param src
	 *            String
	 * @param name
	 *            String
	 * @param regexp
	 *            String
	 * @return boolean
	 */
	public boolean addRegexp(String src, String name, String regexp) {
		if (src == null || name == null || regexp == null) {
			return false;
		}

		r.setMatchFlags(RE.MATCH_SINGLELINE);
		r.setProgram((compiler.compile(regexp)));
		if (r.match(src)) {
			for (int i = 0; i < r.getParenCount(); i++) {
				String n = name + "." + i;
				String value = r.getParen(i);

				if (hm.containsKey(n)) {
					hm.remove(n);
				}
				if (value == null) {
					hm.put(n, "");
				} else {
					hm.put(n, value.trim());
				}
			}
			return true;
		}

		return false;
	}

	/**
	 * 添加默认值
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            String
	 * @return boolean
	 */
	public boolean addDefault(String name, String value) {
		if (name == null || value == null) {
			return false;
		}

		if (hm.containsKey(name)) {
			hm.remove(name);

		}
		hm.put(name, value);
		return true;
	}

	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 获取某字符串为分隔符分割的某字段
	 * 
	 * @param src
	 *            String
	 * @param sepa
	 *            String
	 * @param col
	 *            int
	 * @param startbytes
	 *            int
	 * @return String
	 */
	private String getField(String src, String sepa, int col, int startbytes) {
		String tmp = src.substring(startbytes);

		int count = 0;
		int index = 0;
		for (;;) {
			if (col == count) { // 找到指定COL
				int n = tmp.indexOf(sepa);
				if (n >= 0) { // 有下一分隔符
					return new String(tmp.substring(0, n)); // 返回正确切割的字段
				} else {
					return new String(tmp);
				}
			}
			if ((index = tmp.indexOf(sepa)) >= 0) { // 移动到下一分隔符
				count++;
				tmp = tmp.substring(index + sepa.length()); // 临时String
				// tmp变量当前位置
			} else {
				break; // 找不到分隔符，退出
			}
		}

		return null;
	}

	/**
	 * 添加以某字符串分隔符分割的变量
	 * 
	 * @param src
	 *            String
	 * @param name
	 *            String
	 * @param sepa
	 *            String
	 * @param col
	 *            int
	 * @param startbytes
	 *            int
	 * @return boolean
	 */
	public boolean addField(String src, String name, String sepa, int col,
			int startbytes) {
		if (src == null || name == null) {
			return false;
		}

		String field = getField(src, sepa, col, startbytes);
		if (field == null) {
			field = "";
		}
		if (hm.containsKey(name)) {
			hm.remove(name);

		}
		hm.put(name, field);
		return true;
	}

	/**
	 * 添加以某字符串分隔符分割的变量
	 * 
	 * @param name
	 *            String
	 * @param sepa
	 *            String
	 * @param col
	 *            int
	 * @param startbytes
	 *            int
	 * @return boolean
	 */
	public boolean addField(String name, String sepa, int col, int startbytes) {
		return addField(source, name, sepa, col, startbytes);
	}

	/**
	 * 添加日期型变量到缓存
	 * 
	 * @param name
	 *            String
	 * @param fmt
	 *            String
	 * @param move
	 *            String
	 * @return boolean
	 */
	public boolean addDateTime(String name, String fmt, String move) {
		if (name == null || fmt == null) {
			return false;
		}

		DateUtil sd = new DateUtil();
		sd.move(move);

		if (hm.containsKey(name)) {
			hm.remove(name);

		}
		hm.put(name, sd.format(fmt));
		return true;
	}

	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 添加SUBSTR 变量。
	 * 
	 * @param src
	 *            String
	 * @param name
	 *            String
	 * @param start
	 *            int
	 * @param nbytes
	 *            int
	 * @return boolean
	 */
	public boolean addSubStr(String src, String name, int start, int nbytes) {
		if (src == null || name == null) {
			return false;
		}

		if (hm.containsKey(name)) {
			hm.remove(name);
		}

		int end = start + nbytes;
		if (end > src.length()) {
			end = src.length();
		}

		hm.put(name, src.substring(start, end));
		return true;
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @param start
	 *            int
	 * @param nbytes
	 *            int
	 * @return boolean
	 */
	public boolean addSubStr(String name, int start, int nbytes) {
		return addSubStr(source, name, start, nbytes);
	}

	/**
	 * flag ? left : right function. 添加布尔变量，通过表达式计算添加。
	 * 
	 * @param name
	 *            value name
	 * @param flag
	 *            question flag
	 * @param leftValue
	 *            left value
	 * @param rightValue
	 *            right value
	 * @return boolean
	 */
	public boolean addQuestion(String name, boolean flag, String leftValue,
			String rightValue) {
		if (name == null || leftValue == null || rightValue == null) {
			return false;
		}

		if (hm.containsKey(name)) {
			hm.remove(name);
		}

		if (flag) {
			hm.put(name, leftValue);
		} else {
			hm.put(name, rightValue);
		}

		return true;
	}

	/**
	 * 添加字符串分割的变量
	 * 
	 * @param src
	 *            String
	 * @param prefixName
	 *            String
	 * @param sepa
	 *            String
	 * @return boolean
	 */
	public boolean addSplits(String src, String prefixName, String sepa) {
		if (src == null || prefixName == null || src.length() == 0
				|| prefixName.length() == 0) {
			return false;
		}

		/**
		 * 添加前部值
		 */
		int count = 0;
		int n = 0;
		String v = null;
		String name = null;
		for (;;) {
			n = src.indexOf(sepa);
			if (n >= 0) {
				v = src.substring(0, n); // 获取当前分隔值
				name = prefixName + "." + count;
				if (hm.containsKey(name)) {
					hm.remove(name);
				}
				hm.put(name, v);
				count++;
				src = src.substring(n + sepa.length());
			} else {
				break; // 找不到分隔符，退出
			}
		}

		/**
		 * 加上尾部最后一个值
		 */
		if (src.length() > 0) {
			v = src;
			name = prefixName + "." + count;
			if (hm.containsKey(name)) {
				hm.remove(name);
			}
			hm.put(name, v);
		}

		return true;
	}

	/**
	 * 添加简单XML元素
	 * 
	 * @param xmlElement
	 *            String
	 * @return boolean
	 */
	public boolean addXmlElement(String xmlElement) {
		try {
			Document doc = Dom4jUtil.createDOM4jDocument(xmlElement, false,
					null);
			Element root = doc.getRootElement();
			List l = root.attributes();
			Iterator i = l.iterator();
			String name = root.getName();
			while (i.hasNext()) {
				Attribute a = (Attribute) i.next();
				if (hm.containsKey(a.getName())) {
					hm.remove(a.getName());
				}
				hm.put(name + "." + a.getName(), a.getValue());
			}

			l = root.elements();
			i = l.iterator();
			while (i.hasNext()) {
				Element e = (Element) i.next();
				if (hm.containsKey(e.getName())) {
					hm.remove(e.getName());
				}
				if (e.isTextOnly()) {
					hm.put(name + "." + e.getName(), e.getText());
				} else {
					hm.put(name + "." + e.getName(), e.asXML());
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 添加XML 元素，通过描述XPATH
	 * 
	 * @param xmlElement
	 *            String
	 * @param name
	 *            String
	 * @param path
	 *            String
	 * @return boolean
	 */
	public boolean addXmlElementByPath(Element xmlElement, String name,
			String path) {
		if (xmlElement == null || name == null) {
			return false;
		}

		List l = xmlElement.selectNodes(path);
		int n = 0;
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Object o = iter.next();
			if (o instanceof Attribute) { // 查找到的是一个属性
				if (n == 0) {
					if (hm.containsKey(name)) {
						hm.remove(name);
					}
					hm.put(name, ((Attribute) o).getValue());
				} else {
					if (hm.containsKey(name)) {
						hm.remove(name);
					}
					hm.put(name + "." + n, ((Attribute) o).getValue());
				}
				continue;
			} else { // 查找到的是一个Element (or text node)
				Element elementTmp = (Element) iter.next();
				if (n == 0) {
					try {
						if (elementTmp.isTextOnly()) {
							if (hm.containsKey(name)) {
								hm.remove(name);
							}
							hm.put(name, elementTmp.getTextTrim());
						} else {
							if (hm.containsKey(name)) {
								hm.remove(name);
							}
							hm.put(name, Dom4jUtil.GenerateXMLStringBuffer(
									elementTmp, "").toString().trim());
						}
					} catch (IOException ex) {
					}
				} else {
					try {
						if (elementTmp.isTextOnly()) {
							if (hm.containsKey(name)) {
								hm.remove(name);
							}
							hm.put(name + "." + n, elementTmp.getTextTrim());
						} else {
							if (hm.containsKey(name)) {
								hm.remove(name);
							}
							hm.put(name + "." + n, Dom4jUtil
									.GenerateXMLStringBuffer(elementTmp, "")
									.toString().trim());
						}
					} catch (IOException ex1) {
					}
				}
			}

			n++;
		} // end of for

		return true;
	}

	/**
	 * 添加XML元素中指定XPATH的变量
	 * 
	 * @param xmlElement
	 *            String
	 * @param name
	 *            String
	 * @param path
	 *            String
	 * @return boolean
	 */
	public boolean addXmlElementByPath(String xmlElement, String name,
			String path) {
		if (xmlElement == null || name == null) {
			return false;
		}

		try {
			Document doc = Dom4jUtil.createDOM4jDocument(xmlElement, false, "");
			return addXmlElementByPath(doc.getRootElement(), name, path);
		} catch (Exception ex) {
			return false;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	/**
	 * 删除某缓存变量
	 * 
	 * @param name
	 *            String
	 */
	public void del(String name) {
		if (name != null) {
			hm.remove(name);
		}
	}

	/**
	 * 获取缓存变量
	 * 
	 * @param name
	 *            String
	 * @return String
	 */
	public String get(String name) {
		return (String) hm.get(name);
	}

	/**
	 * 根据设置的格式化模板输出结果
	 * 
	 * @return String
	 */
	public String result() {
		return result(this.getFmt());
	}

	/**
	 * result 根据设置的格式化模板输出结果
	 * 
	 * @param format
	 *            String
	 * @return String
	 */
	public String result(String format) {
		// / 格式串为空，设置为源串
		if (format == null || format.length() == 0) {
			result = ""; // source; 2005/09/13
			return result;
		}

		// / 格式串不含格式符号，直接设置为格式串
		if (format.indexOf("${") < 0 && format.indexOf("}") < 0) {
			result = format;
			return result;
		}

		final int INIT = 0;
		final int LEFT = 1;
		final int RIGHT = 2;
		int index = 0;
		int flag = INIT;
		String tmp = format;
		String tail = null;

		result = ""; // 09/13
		for (;;) {
			if (flag != LEFT && (index = tmp.indexOf("${")) >= 0) { // 已发现 "${"
				// 则当前不再找"${"，否则首先寻找"${"标志，并设置标志为LEFT
				flag = LEFT;
			} else if (flag != RIGHT && (index = tmp.indexOf("}")) >= 0) { // 已发现
				// "}"
				// 则当前不再找"}"，否则当前应寻找"}" 标志，并设置标志为RIGHT
				flag = RIGHT;
			} else { // 替换完成
				break;
			}

			switch (flag) {
			case LEFT: // "${"
				result = (result == null ? "" : result)
						+ tmp.substring(0, index);
				tmp = tmp.substring(index + 2);
				break;
			case RIGHT: // "}"
				String variable = tmp.substring(0, index);
				tail = tmp.substring(index + 1); // 保留末尾字符
				String tran = this.get(variable); // 变量替换
				if (tran == null) {
					tran = "";
				}
				result = (result == null ? "" : result) + tran;
				tmp = tmp.substring(index + 1);
				break;
			}
		}

		if (tail != null) {
			result += tail;
		}

		return result;
	}

	/**
	 * 返回嵌套格式的结果
	 * 
	 * @return String
	 */
	public String resultNested() {
		return resultNested(this.getFmt());
	}

	/**
	 * 20060609 添加 可支持嵌套定义的结果 resultNested("dddf${abc${1}}ddd")
	 * 以反向搜索分界符号来完成整个算法的实现。
	 * 
	 * @param format
	 *            String
	 * @return String
	 */
	public String resultNested(String format) {
		// / 格式串为空，设置为源串
		if (format == null || format.length() == 0) {
			result = "";
			return result;
		}

		// 格式串不含格式符号，直接设置为格式串
		if (format.indexOf("${") < 0 || format.indexOf("}") < 0) {
			result = format;
			return result;
		}

		final int INIT = 0;
		final int LEFT = 1; // ${ 标志
		final int RIGHT = 2; // } 标志
		int index = 0;
		int rightIndex = 0;
		int flag = INIT;
		String tmp = format;
		result = "";
		for (;;) {
			// 标志不为RIGHT 则搜索 "}"
			if (flag != RIGHT && (index = tmp.indexOf("}")) >= 0) {
				flag = RIGHT;
			}
			// 标志不为LEFT 则搜索 "${"
			else if (flag != LEFT
					&& (index = tmp.substring(0, index).lastIndexOf("${")) >= 0) {
				flag = LEFT;
			}
			// 否则则退出搜索，可能有不匹配情况。
			else {
				break;
			}

			switch (flag) {
			case RIGHT: // "}"
				rightIndex = index;
				break;
			case LEFT: // 当前为"${" 则开始把"${var}" 之间的变量进行替换
				String variable = tmp.substring(index + 2, rightIndex);
				String tran = this.get(variable); // 变量替换
				if (tran == null) {
					tran = "";
				}
				tmp = tmp.substring(0, index) + tran
						+ tmp.substring(rightIndex + 1);
				break;
			}
		}

		result = tmp;
		return result;
	}

	/**
	 * 默认toString 的方法为返回格式替换后的结果
	 * 
	 * @return String
	 */
	public String toString() {
		return result();
	}

	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args) {
		System.out.println("into ParserUtil...");
		ParserUtil util = new ParserUtil();

		util
				.addXmlElement("<DataBase test=\"true\">	                                    "
						+ "	<url>jdbc:oracle:thin:@134.128.5.200:1521:IPMS</url>"
						+ "	<driver>oracle.jdbc.driver.OracleDriver</driver>    "
						+ "	<user>ipms</user>                                   "
						+ "	<pswd>123456</pswd>                                 "
						+ "</DataBase>                                          ");

		util
				.addXmlElementByPath(
						"<DataBase test=\"true\">	                                    "
								+ "	<url>1jdbc:oracle:thin:@134.128.5.200:1521:IPMS</url>"
								+ "	<driver>oracle.jdbc.driver.OracleDriver</driver>    "
								+ "	<user>ipms</user>                                   "
								+ "	<pswd>123456</pswd>                                 "
								+ "</DataBase>                                          ",
						"test", "/DataBase/@test");

		String ascii = "255";
		int c = Integer.parseInt(ascii);
		char[] buf = new char[1 + 1];
		buf[0] = (char) c;
		String value = new String(buf, 0, 1);
		util.addDefault("mm", value);
		util.addDefault("mmmm", "mm");
		String a = util.result("${mm} ff");
		System.out.println("a:" + a);
		String b = util.resultNested("${${mmmm}} ${mm${hello}} ff");
		System.out.println("b:" + b);

		util.setSource("   OP CFGSTAT SM 4 FIRST RECORD");
		util.addRegexp("sm-no", "\\s+OP CFGSTAT SM (\\d+).*RECORD");

		util.setSource("<" + "M  ORIGINATING COMMAND # = 005640.0018"
				+ "OP CLK COMPLETED " + "05-09-08 10:39+");
		util.addRegexp("datetime", "");
		util.addRegexp("op-clk", "M  ORIGINATING COMMAND # = 005593.0007"
				+ "OP CLK COMPLETED" + " 05-09-07 15:29",
				"OP CLK COMPLETED.*?(\\d+):(\\d+)");
		util.setFmt("${op-clk.0} oho ${op-clk.1} ${op-clk.2}");
		System.out.println(util.result());
		ParserUtil snew = new ParserUtil(util);
		snew.setFmt("gg ${op-clk.0} oho ${op-clk.1} ${op-clk.2}");
		System.out.println(snew.result());

		util.addSplits("ggg|ddd|eee|ff", "split", "|");
		System.out.println("split parser:" + util.get("split.0")
				+ util.get("split.1") + util.get("split.2")
				+ util.get("split.3"));

		util.addQuestion("man", true, "power", "spare");
		System.out.println("man parser:" + util.get("man"));
		util.addDefault("gege", "小强");
		util.addDefault("dd", "小明");
		System.out.println("gege parser:" + util.get("gege"));

		util.addSubStr("风吹石头跑", "stone", 4, 4);
		System.out.println("stone parser:" + util.get("stone"));

		util.addField("风|吹|石|跑", "wind", "|", 1, 0);
		System.out.println("wind parser:" + util.get("wind"));

		util.setFmt("gg's name is $ { } ${gege}, dd's name is ${dd}, haha!");
		System.out.println(util.result());

		util.addDefault("gg", "晓辉");
		util.setFmt("${gg}");
		System.out.println("do : " + util.result());
		util.setFmt("${gg}");
		System.out.println("do : " + util.result());
		System.out.println("do baba: " + util.result("${gg} ${dd}"));

		util.addDateTime("today", "yyyy-MM-dd", null);
		System.out.println("today parser:" + util.get("today"));

		String t = "TYPE    GSM  SET  MEM  CLASS"
				+ "c7natl  1    1    0    MTP2 "
				+ "c7natl  1    1    1    MTP2 "
				+ "c7natl  1    1    2    MTP2 "
				+ "c7natl  1    1    3    MTP2 "
				+ "c7natl  1    1    4    MTP2 "
				+ "c7natl  1    1    5    MTP2 "
				+ "c7natl  1    1    6    MTP2 "
				+ "c7natl  1    1    7    MTP2 "
				+ "c7natl  1    2    0    MTP2 "
				+ "c7natl  1    2    1    MTP2 ";
		util.addRegexpGroup(t, "c7natl",
				"c7natl\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+?MTP2");
		System.out.println("kakaka " + util.get("c7natl.9.3"));

		String text = "<op-oddsm:1&&25;"
				+ "006061.0018 IP - COMMAND QUEUED, CHECK CMDLOG FOR ACKNOWLEDGMENT."
				+ "<"
				+ "M  ORIGINATING COMMAND # = 006061.0018"
				+ "OP ODD                                                       PAGE 1 OF 6"
				+ "SM      RODD    USED=1045       AVAIL=1690      PCT USED=38"
				+ "NRODD       PCT         UODD        PCT"
				+ "USED    AVAIL   USED    USED    AVAIL   USED    UNALLOC SABM"
				+ "SM 1          1099   597     64      35561   28374   55      2       203360"
				+ "SM 2          5826   2654    68      32170   29061   52      2       101420"
				+ "SM 3          9974   4394    69      33008   29855   52      2       93900"
				+ "SM 4          5878   2585    69      30209   30527   49      2       101932"
				+ "M  ORIGINATING COMMAND # = 006061.0018"
				+ "OP ODD                                                       PAGE 2 OF 6"
				+ "                NRODD       PCT         UODD        PCT"
				+ "            USED    AVAIL   USED    USED    AVAIL   USED    UNALLOC SABM"
				+ "SM 5          16931  7468    69      34445   27058   56      0       191560"
				+ "SM 6          7096   3064    69      31508   29467   51      2       99996"
				+ "SM 7          3898   1781    68      32276   29691   52      2       103484"
				+ "SM 8          8602   3765    69      31217   29694   51      2       97852"
				+ ""
				+ "M  ORIGINATING COMMAND # = 006061.0018"
				+ "   OP ODD                                                       PAGE 3 OF 6"
				+ "                    NRODD       PCT         UODD        PCT"
				+ "                USED    AVAIL   USED    USED    AVAIL   USED    UNALLOC SABM"
				+ "   SM 9          8012   3492    69      31156   29788   51      2       98684"
				+ "   SM 10         6181   2714    69      32124   29971   51      2       100140"
				+ "   SM 11         9832   4263    69      30997   28394   52      2       97644"
				+ "   SM 12         6797   3154    68      30892   28163   52      2       102124"
				+ ""
				+ "M  ORIGINATING COMMAND # = 006061.0018"
				+ "   OP ODD                                                       PAGE 3 OF 6"
				+ "                    NRODD       PCT         UODD        PCT"
				+ "                USED    AVAIL   USED    USED    AVAIL   USED    UNALLOC SABM"
				+ "   SM 9          8012   3492    69      31156   29788   51      2       98684"
				+ "   SM 10         6181   2714    69      32124   29971   51      2       100140"
				+ "   SM 11         9832   4263    69      30997   28394   52      2       97644"
				+ "   SM 12         6797   3154    68      30892   28163   52      2       102124gggg";
		util.addRegexp(text, "kaka", ".*?gggg");
		util.addStrQuestion("hahaha", "-", "ggddgg", "dd");
		util.addStrQuestion("hahaha", "=", "", "");
		util.addStrQuestion("hahaha", "!=", "ggddgg", "dd");
		util.addStrQuestion("gugugu", "<", "05-09-15 16:21:32",
				"05-08-20 00:00:00");
		DateUtil sd = new DateUtil("20050119");
		sd.move(); // 时间归到当前时间
		sd.move("-9hour"); // 时间移动配置的偏移量
		sd.moveYear(0);
		sd.moveDay(0);
		sd.moveMonth(0);
		sd.moveWeek(0);
		sd.moveHour(10);
		sd.moveMinute(-1);

		int i = Integer.parseInt("-30");
		System.out.println(i);

		HashMap h = new HashMap();
		h.put("ga", "gaga");
		util.addMap("g", h);
		System.out.println(util.get("g.ga"));
		util.addSplits("ACK:LOCK CRBT GROUP:", "mml", ":");
		util.setFmt("${mml.0} ${mml.1} ${mml.2}");
		System.out.println(util);

		// /////////////////////////////////////////////////////////////
		List l = ParserUtil.getTemplateName("${ff} ${bb} ${cc} ${dd}");
		System.out.println(l);
		System.out.println(ParserUtil.checkTemplateName(
				"${ff} ${bb} ${dd} ${cc}", l));
		util.addRegexpTemplate("2007-01-01",
				"(${year}\\d+)-(${month}\\d+)-(${day}\\d+)");

		util.setFmt("fffff}");
		System.out.println("{ -> " + util);

	}
}
