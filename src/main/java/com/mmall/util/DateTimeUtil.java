package com.mmall.util;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by sww_6 on 2018/9/7.
 */
public class DateTimeUtil {

  private static final String DATE_FORMART = "yyyy-MM-dd HH:mm:ss";

  /**
   * 日字符串转日期
   */
  public static Date strToDate(String dateTimeStr) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMART);
    DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
    return dateTime.toDate();
  }

  public static Date strToDate(String dateTimeStr, String forMatstr) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(forMatstr);
    DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
    return dateTime.toDate();
  }

  /**
   * 日期转字符串
   */
  public static String dateToStr(Date date) {
    if (date == null) {
      return StringUtils.EMPTY;
    }
    DateTime dateTime = new DateTime(date);
    return dateTime.toString(DATE_FORMART);
  }


  public static String dateToStr(Date date, String forMatstr) {
    if (date == null) {
      return StringUtils.EMPTY;
    }
    DateTime dateTime = new DateTime(date);
    return dateTime.toString(forMatstr);
  }

  public static void main(String[] args) {
    System.out.println(strToDate("2010-04-14 12:11:22", "yyyy-MM-dd HH:mm:ss"));
    System.out.println(dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
  }
}
