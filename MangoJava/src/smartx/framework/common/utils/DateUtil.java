package smartx.framework.common.utils;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author xiaoyuer
 * @version 1.0
 *  Changed.log 2006 add some static method.
 */
public class DateUtil
    extends Date {
  public DateUtil() {
    super();
  }

  /**
   *
   * @param date String
   */
  public DateUtil(String date) {
    super();
    // 20050202
    // 01234567
    int year, month, day;
    if (date.indexOf("-") == -1) {
      year = Integer.parseInt(date.substring(0, 4));
      month = Integer.parseInt(date.substring(4, 6));
      day = Integer.parseInt(date.substring(6, 8));
    }
    // 2004-01-01
    // 0123456789
    else {
      year = Integer.parseInt(date.substring(0, 4));
      month = Integer.parseInt(date.substring(5, 7));
      day = Integer.parseInt(date.substring(8, 10));
    }

    this.setYear(year - 1900);
    this.setMonth(month - 1);
    this.setDate(day);
  }

  /**
   *
   * @param t long
   */
  public DateUtil(long t) {
    super();
    this.setTime(t);
  }

  /**
   *
   * @param dt Date
   */
  public DateUtil(Date dt) {
    super();
    this.setTime(dt.getTime());
  }

  /**
   *
   * @param fmt String
   * @return String
   */
  public String format(String fmt) {
    return new SimpleDateFormat(fmt).format(this);
  }

  /**
   *
   * @param dt Date
   * @param fmt String
   * @return String
   */
  public static String format(Date dt, String fmt) {
    return new SimpleDateFormat(fmt).format(dt);
  }

  /**
   *
   * @param dt Date
   * @return boolean
   */
  public static boolean isRYear(Date dt) {
    return (isRYear(1900 + dt.getYear()));
  }

  /**
   * 是否闰年
   * @return boolean
   */
  public boolean isRYear() {
    return (isRYear(1900 + this.getYear()));
  }

  /**
   *
   * @param y int
   * @return boolean
   */
  public static boolean isRYear(int y) {
    return (y % 400 == 0 || (y % 4 == 0 && y % 100 != 0));
  }

  /**
   * 移动天
   * @param days int
   */
  public void moveDay(int days) {
    long mills = (long) days * 24 * 3600 * 1000;
    this.setTime(getTime() + mills);
  }

  /**
   *移动小时
   * @param hours int
   */
  public void moveHour(int hours) {
    long mills = (long) hours * 3600 * 1000;
    this.setTime(getTime() + mills);
  }

  /**
   * 移动分钟
   * @param minutes int
   */
  public void moveMinute(int minutes) {
    long mills = (long) minutes * 60 * 1000;
    this.setTime(getTime() + mills);
  }

  /**
   * 移动月
   * @param monthes int
   */
  public void moveMonth(int monthes) {
    setMonth(this.getMonth() + monthes);
  }

  /**
   * 移动秒
   * @param seconds int
   */
  public void moveSecond(int seconds) {
    long mills = (long) seconds * 1000;
    this.setTime(this.getTime() + mills);
  }

  /**
   * 移动周
   * @param weeks int
   */
  public void moveWeek(int weeks) {
    long mills = (long) weeks * 7 * 24 * 3600 * 1000;
    this.setTime(this.getTime() + mills);
  }

  /**
   * 移动年
   * @param year int
   */
  public void moveYear(int year) {
    setYear(this.getYear() + year);
  }

  /**
   *获取日期字符串
   * @return String
   */
  public String getDateStr() {
    return new SimpleDateFormat("yyyy-MM-dd").format(this);
  }

  /**
   * 获取一个日期值的日期字符串
   * @param dt Date
   * @return String
   */
  public static String getDateStr(Date dt) {
    return new SimpleDateFormat("yyyy-MM-dd").format(dt);
  }

  /**
   * 获取带时间的日期字符串
   * @return String
   */
  public String getLongDate() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this);
  }

  /**
   * 获取一个日期值的带时间日期字符串
   * @param dt Date
   * @return String
   */
  public static String getLongDate(Date dt) {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
  }

  /**
   * 获取时间字符串
   * @return String
   */
  public String getTimeStr() {
    return new SimpleDateFormat("HH:mm:ss").format(this);
  }

  /**
   * 获取一个日期的时间字符串
   * @param dt Date
   * @return String
   */
  public static String getTimeStr(Date dt) {
    return new SimpleDateFormat("HH:mm:ss").format(dt);
  }

  /**
   * 时间刷新
   * @return DateUtil
   */
  public DateUtil move() {
    this.setTime(new Date().getTime());
    return this;
  }

  /**
   * 时间移动一个字符串表达式，只支持单次运算，可考虑添加多次运算的方式
   * @param move String
   * @return DateUtil
   */
  public DateUtil move(String move) {
    if (move == null || move.length() == 0) {
      return this;
    }

    /**
     * 计算前面的数值
     */
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < move.length(); i++) {
      char c = move.charAt(i);
      if (c == '-' || c >= '0' && c <= '9') {
        buf.append(c);
      }
      else if (c == '+') { // 过滤+
      }
      else {
        // 非数值字符，退出循环
        break;
      }
    }

    /**
     * 获取后面的实际移动单位
     */
    int i = Integer.parseInt(buf.toString());
    int index = move.indexOf(buf.toString());
    if (index >= 0) {
      move = move.substring(index + buf.length());
    }
    move.toLowerCase();
    if (move.equals("year")) {
      this.moveYear(i);
    }
    else if (move.equals("month")) {
      this.moveMonth(i);
    }
    else if (move.equals("day")) {
      this.moveDay(i);
    }
    else if (move.equals("hour")) {
      this.moveHour(i);
    }
    else if (move.equals("minute")) {
      this.moveMinute(i);
    }
    else if (move.equals("second")) {
      this.moveSecond(i);
    }
    else if (move.equals("week")) {
      this.moveWeek(i);
    }

    return this;
  }

  /**
   * 重载toString 方法
   * @return String
   */
  public String toString() {
    return format("yyyy-MM-dd HH:mm:ss");
  }

  /**
   *
   * @param dt Date
   * @return String
   */
  public static String toString(Date dt) {
    return format(dt, "yyyy-MM-dd HH:mm:ss");
  }

  /**
   *
   * @param date Date
   * @return Timestamp
   * added by jiayc
   */
  public static java.sql.Timestamp dateToTimeStamp(java.util.Date date) {
    if (date == null) {
      return null;
    }
    else {
      return new java.sql.Timestamp(date.getTime());
    }
  }

  /**
   *
   * @param args String[]
   */
  public static void main(String args[]) {
    System.out.println(DateUtil.format(new Date(), "yyyy-MM"));
    System.out.println(new DateUtil());
    System.out.println(DateUtil.toString(new Date()));
    System.out.println(new DateUtil(DateUtil.parse("" + new Date())).move(
        "10day"));
  }
}
