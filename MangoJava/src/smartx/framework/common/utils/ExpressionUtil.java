package smartx.framework.common.utils;

import java.util.Stack;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 *
 * <p>Title: ExpressionUtil</p>
 * <p>Description: 表达式工具类 计算简单的数学表达式或布尔表达式 </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author xiaoyuer
 * @version 1.0
 *  Changed.log
 */
public class ExpressionUtil {
  public static String OPTS = "+-*/%~><][!|&=#";

  /**
   *
   * @param expression
   * @return
   * @throws Exception
   */
  public Object calculate(String expression) throws Exception {
    System.out.println(expression);
    try {
      // 一些关键字符串替换

      expression = StringUtil.replaceAll(expression, ">\\s*=", "]");
      expression = StringUtil.replaceAll(expression, "<\\s*=", "[");
      expression = StringUtil.replaceAll(expression, "=\\s*=", "=");
      expression = StringUtil.replaceAll(expression, "&\\s*&", "&");
      expression = StringUtil.replaceAll(expression, "\\s+and\\s+", "&");
      expression = StringUtil.replaceAll(expression, "\\s+or\\s+", "|");
      expression = StringUtil.replaceAll(expression, "\\s*not\\s+", "~");
      expression = StringUtil.replaceAll(expression, "\\s+true\\s*", "1");
      expression = StringUtil.replaceAll(expression, "\\s*true\\s+", "1");
      expression = StringUtil.replaceAll(expression, "\\s+false\\s*", "0");
      expression = StringUtil.replaceAll(expression, "\\s*false\\s+", "0");

      expression = StringUtil.replaceAll(expression, "\\s+", ""); // 去除所有空格
      System.out.println(expression);
      expression = replaceE(expression); // 首先把科学计数的数值转换

      Stack Opts = new Stack();
      Stack Values = new Stack();

      Opts.setSize(1024); // add by cxy 0913
      Values.setSize(1024); // add by cxy 0913

      String exp = expression + "#";
      int nCount = exp.length(), nIn, nOut, nTemp;
      Opts.push("#");
      String temp = "", optIn = "", value1 = "", value2 = "", opt = "",
          temp1 = "";
      int nFun = 0;
      boolean isFun = false;
      for (int i = 0; i < nCount; ) {
        nTemp = 0;
        opt = exp.substring(i, i + 1);
        isFun = false;
        temp1 = "";
        while (i < nCount) {
          if (!temp1.equals("")) {
            if (opt.equals("(")) {
              nFun++;
              isFun = true;
            }
            else if (opt.equals(")")) {
              nFun--;
            }
          }
          if ( (nFun > 0) || ( (!isFun) && this.isValue(opt))) {
            temp1 += opt;
            nTemp++;
            opt = exp.substring(i + nTemp, i + nTemp + 1);
          }
          else {
            if (isFun) {
              temp1 += opt;
              nTemp++;
            }
            break;
          }
        }
        if (temp1.equals("")) {
          temp = opt;
        }
        else {
          temp = temp1;
        }
        if (nTemp > 0) {
          i = i + nTemp - 1;
        }
        temp = temp.trim();

        if (this.isValue(temp)) {
          temp = this.getValue(temp);
          Values.push(temp);
          i++;
        }
        else {
          optIn = Opts.pop().toString();
          nIn = this.getOptPriorityIn(optIn);
          nOut = this.getOptPriorityOut(temp);
          if (nIn == nOut) {
            i++;
          }
          else if (nIn > nOut) {
            String ret = "";
            Object v = Values.pop();
            if (v == null) {
              value1 = null;
            }
            else {
              value1 = v.toString();
            }

            Object o = Values.pop();
            if (o == null) {
              value2 = null;
            }
            else {
              value2 = o.toString();
            }

            ret = String.valueOf(this.calValue(value2, optIn, value1));
            Values.push(ret);
          }
          else if (nIn < nOut) {
            Opts.push(optIn);
            Opts.push(temp);
            i++;
          }
        }
      }
      return Values.pop();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Exception("表达式 " + expression + "出错 " + e.getMessage());
    }
  }

  /**
   *
   * @param opt
   * @return
   * @throws Exception
   */
  protected int getOptPriorityOut(String opt) throws Exception {
    if (opt.equals("+")) {
      return 1;
    }
    else if (opt.equals("-")) {
      return 2;
    }
    else if (opt.equals("*")) {
      return 5;
    }
    else if (opt.equals("/")) {
      return 6;
    }
    else if (opt.equals("%")) {
      return 7;
    }
    else if (opt.equals("~")) {
      return 11;
    }
    else if (opt.equals(">")) {
      return 12;
    }
    else if (opt.equals("<")) {
      return 13;
    }
    else if (opt.equals("]")) {
      return 14;
    }
    else if (opt.equals("[")) {
      return 15;
    }
    else if (opt.equals("!")) {
      return 16;
    }
    else if (opt.equals("|")) {
      return 17;
    }
    else if (opt.equals("&")) {
      return 24;
    }
    else if (opt.equals("=")) {
      return 26;
    }
    else if (opt.equals("#")) {
      return 0;
    }
    else if (opt.equals("(")) {
      return 1000;
    }
    else if (opt.equals(")")) {
      return -1000;
    }
    throw new Exception("运算符" + opt + "非法!");
  }

  /**
   *
   * @param opt
   * @return
   * @throws Exception
   */
  protected int getOptPriorityIn(String opt) throws Exception {
    if (opt.equals("+")) {
      return 3;
    }
    else if (opt.equals("-")) {
      return 4;
    }
    else if (opt.equals("*")) {
      return 8;
    }
    else if (opt.equals("/")) {
      return 9;
    }
    else if (opt.equals("%")) {
      return 10;
    }
    else if (opt.equals("~")) {
      return 11;
    }
    else if (opt.equals(">")) {
      return 18;
    }
    else if (opt.equals("<")) {
      return 19;
    }
    else if (opt.equals("]")) {
      return 20;
    }
    else if (opt.equals("[")) {
      return 21;
    }
    else if (opt.equals("!")) {
      return 22;
    }
    else if (opt.equals("|")) {
      return 23;
    }
    else if (opt.equals("&")) {
      return 25;
    }
    else if (opt.equals("=")) {
      return 27;
    }
    else if (opt.equals("(")) {
      return -1000;
    }
    else if (opt.equals(")")) {
      return 1000;
    }
    else if (opt.equals("#")) {
      return 0;
    }
    throw new Exception("运算符" + opt + "非法！");
  }

  /**
   *
   * @return
   */
  protected String getOPTS() {
    return OPTS;
  }

  /**
   * 是一个纯值
   * @param cValue
   * @return
   */
  protected boolean isValue(String cValue) {
    String notValue = this.getOPTS() + "()";
    return notValue.indexOf(cValue) == -1;
  }

  /**
   * 是一个计算符
   * @param value
   * @return
   */
  protected boolean isOpt(String value) {
    return this.getOPTS().indexOf(value) >= 0;
  }

  /**
   *
   * @param value1
   * @param opt
   * @param value2
   * @return
   * @throws Exception
   */
  protected Object calValue(String value1, String opt, String value2) throws
      Exception {
    System.out.println(value1 + opt + value2);
    boolean isString = false; // 判断是否字符串之间的运算
    if ( (value1 != null && value1.matches("\\s*\".*\"\\s*"))
        || value2 != null && value2.matches("\\s*\".*\"\\s*")) {
      isString = true;
    }

    /**
     *
     */
    try {
      long lg = 0;
      double dbValue1 = 0;
      double dbValue2 = 0;
      if (!isString) { // 非字符串，则进行数值计算
        if (value1 != null && value1.trim().length() > 0) {
          dbValue1 = Double.valueOf(value1).doubleValue();
        }
        if (value2 != null && value2.trim().length() > 0) {
          dbValue2 = Double.valueOf(value2).doubleValue();
        }
      }

      if (opt.equals("+")) {
        if (!isString) {
          return new Double(dbValue1 + dbValue2);
        }
        return value1 + value2;
      }
      else if (opt.equals("-")) {
        if (!isString) {
          return new Double(dbValue1 - dbValue2);
        }
      }
      else if (opt.equals("*")) {
        if (!isString) {
          return new Double(dbValue1 * dbValue2);
        }
      }
      else if (opt.equals("/")) {
        if (!isString) {
          return new Double(dbValue1 / dbValue2);
        }
      }
      else if (opt.equals("%")) {
        if (!isString) {
          lg = (long) (dbValue1 / dbValue2);
          return new Double(dbValue1 - lg * dbValue2);
        }
      }
      else if (opt.equals(">")) {
        if (!isString) {
          if (dbValue1 > dbValue2) {
            return new Integer(1);
          }
          else {
            return new Integer(0);
          }
        }
        return new Integer(value1.compareTo(value2) > 0 ? 1 : 0);
      }
      else if (opt.equals("<")) {
        if (!isString) {
          if (dbValue1 < dbValue2) {
            return new Integer(1);
          }
          else {
            return new Integer(0);
          }
        }
        return new Integer(value1.compareTo(value2) < 0 ? 1 : 0);
      }
      else if (opt.equals("]")) {
        if (!isString) {
          if (dbValue1 >= dbValue2) {
            return new Integer(1);
          }
          else {
            return new Integer(0);
          }
        }
        return new Integer(value1.compareTo(value2) >= 0 ? 1 : 0);
      }
      else if (opt.equals("[")) {
        if (!isString) {
          if (dbValue1 <= dbValue2) {
            return new Integer(1);
          }
          else {
            return new Integer(0);
          }
        }
        return new Integer(value1.compareTo(value2) <= 0 ? 1 : 0);
      }
      else if (opt.equals("!")) {
        if (!isString) {
          if (value1 != null) {
            if (dbValue1 != dbValue2) {
              return new Integer(1);
            }
            else {
              return new Integer(0);
            }
          }
          else {
            return new Integer(value2.equals("1") ? 0 : 1);
          }
        }
        return new Integer(value1.compareTo(value2) != 0 ? 1 : 0);
      }
      else if (opt.equals("|")) {
        if (!isString) {
          if (dbValue1 > 0 || dbValue2 > 0) {
            return new Integer(1);
          }
          else {
            return new Integer(0);
          }
        }
      }
      else if (opt.equals("&")) {
        if (!isString) {
          if ( (value1 == null || dbValue1 > 0) && dbValue2 > 0) {
            return new Integer(1);
          }
          else {
            return new Integer(0);
          }
        }
      }
      else if (opt.equals("=")) {
        if (!isString) {
          if (dbValue1 == dbValue2) {
            return new Integer(1);
          }
          else {
            return new Integer(0);
          }
        }
        return new Integer(value1.compareTo(value2) == 0 ? 1 : 0);
      }
      else if (opt.equals("~")) {
        if (value2 == null) {
          return new Integer(0);
        }
        return new Integer(value2.equals("1") ? 0 : 1);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println("error " + e.getMessage() + " .");
      throw new Exception("值" + value1 + "或" + value2 + "在进行" + opt + "运算时非法！");
    }

    throw new Exception("运算符" + opt + "非法！");
  }

  /**
   *
   * @param oldValue
   * @return
   * @throws Exception
   */
  protected String getValue(String oldValue) throws Exception {
    String reg = "([\\w+]+)\\(([\\w+\\.\\(\\)/\\*\\+-]+)\\)"; // "([\\w+]+)\\(([\\w+\\.\\(\\)\\+-]+)\\)";
    if (this.isFunctionCal(oldValue)) { // 函数调用
      RE r = null;
      try {
        r = new RE(reg, RE.MATCH_SINGLELINE);
      }
      catch (RESyntaxException ex) {
        return oldValue;
      }

      if (r.match(oldValue)) {
        return calFunction(r.getParen(1), r.getParen(2));
      }
    }

    return oldValue;
  }

  /**
   *
   * @param value
   * @return
   */
  protected boolean isFunctionCal(String value) {
    String reg = "([\\w+]+)\\(([\\w+\\.\\(\\)/\\*\\+-]+)\\)"; // "([\\w+]+)\\(([\\w+\\.\\(\\)\\+-]+)\\)";
    RE r = null;
    try {
      r = new RE(reg, RE.MATCH_SINGLELINE);
    }
    catch (RESyntaxException ex) {
      return false;
    }

    return r.match(value);
  }

  /**
   *
   * @param function
   * @param value
   * @return
   * @throws Exception
   */
  protected String calFunction(String function, String value) throws
      Exception {
    /**
     * 若传入的值是表达式，首先递归计算出表达式的值 add by cxy 20050913
     */
    // 1.611111111111111E-4
    String regvalue = "^\\-?[0-9]*\\.?[0-9]*[Ee]*-*\\d*$"; // "^\\-?[0-9]*\\.?[0-9]*$";
    RE r = null;
    try {
      r = new RE(regvalue); // , RE.MATCH_SINGLELINE);
    }
    catch (RESyntaxException ex) {
    }

    if (!r.match(value)) { // 已经是计算出的数值了
      value = (String) calculate(value);
      /**
       * 若传入的值是表达式，首先递归计算出表达式的值 add by cxy 20050913
       */
    }
    String lowerFun = function.toLowerCase();
    double db = 0;
    try {
      db = Double.valueOf(this.getValue(value)).doubleValue();
      if (lowerFun.equals("log")) {
        return String.valueOf(Math.log(db));
      }
      else if (lowerFun.equals("square")) {
        return String.valueOf(Math.pow(db, 2));
      }
      else if (lowerFun.equals("sqrt")) {
        return String.valueOf(Math.sqrt(db));
      }
      else if (lowerFun.equals("sin")) {
        return String.valueOf(Math.sin(db));
      }
      else if (lowerFun.equals("asin")) {
        return String.valueOf(Math.asin(db));
      }
      else if (lowerFun.equals("cos")) {
        return String.valueOf(Math.cos(db));
      }
      else if (lowerFun.equals("tan")) {
        return String.valueOf(Math.tan(db));
      }
      else if (lowerFun.equals("atan")) {
        return String.valueOf(Math.atan(db));
      }
      else if (lowerFun.equals("ceil")) {
        return String.valueOf(Math.ceil(db));
      }
      else if (lowerFun.equals("exp")) {
        return String.valueOf(Math.exp(db));
      }
    }
    catch (Exception e) {
      throw new Exception("函数" + function + "值" + value + "非法!");
    }

    throw new Exception("函数" + function + "不支持！");
  }

  /**
   * @param exp
   * @return
   */
  protected String replaceE(String exp) {
    RE r = null;
    String regexp = "\\d+\\.\\d+([Ee])(-?\\d+)";

    try {
      r = new RE(regexp, RE.MATCH_SINGLELINE);
    }
    catch (RESyntaxException re) {
      return exp;
    }

    while (r.match(exp)) {
      // 333+1.5e 5
      // 333+1.5e- 5 --> (1.5/100000)
      // 333+ipos ie in
      String pos = r.getParen(0);
      String e = r.getParen(1);
      String num = r.getParen(2);

      int ip = exp.indexOf(pos);
      int in = ip + pos.indexOf(num);
      int ieofPos = pos.indexOf(e);

      String s = "";
      int n = Integer.parseInt(num);
      if (n < 0) { // 负数除
        n = -n;
        s = "/1";
      }
      else {
        s = "*1"; // 正数乘
      }
      for (int i = 0; i < n; i++) {
        s += "0";
      }

      String s1 = exp.substring(0, ip);
      String s2 = "(" + pos.substring(0, ieofPos) + s + ")";
      String s3 = exp.substring(in + num.length());

      exp = s1 + s2 + s3;
    }

    return exp;
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    ExpressionUtil expr = new ExpressionUtil();
    try {
      //System.out.println(be.calculate("(\"c3\" >= \"d0\") > 1 < 9"));
      //System.out.println(be.calculate("1 and 0"));
      //System.out.println(be.calculate("0 or 1"));
      System.out.println(expr.calculate("(3-2)==1"));
    }
    catch (Exception eE) {
      System.out.println(eE.getMessage());
    }
  }
}
