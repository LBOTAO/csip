package com.fsmer.csip.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期时间工具类
 * @author Tracy
 */
public class DateTimeUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

	private static final SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat yyyy_mm = new SimpleDateFormat("yyyy-MM");
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 获取今天剩余毫秒数
	 * @author Tracy
	 * @return
	 */
	public static long getMilliSeconds(){
		Calendar curDate = Calendar.getInstance();
		Calendar tommorowDate = new GregorianCalendar(curDate
				.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
				.get(Calendar.DATE) + 1, 0, 0, 0);
		return (tommorowDate.getTimeInMillis() - curDate .getTimeInMillis());
	}

	/**
	 * long 转 Timestamp----------------------
	 * @param time
	 * @return
	 */
	public static Timestamp longToTimestamp(Long time){
		try{
			return new Timestamp(time);
		}catch (Exception e){
			e.printStackTrace();
		}
		return  null;
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static Timestamp toDay(){
		try{
		return new Timestamp(new Date().getTime());
		}catch (Exception e){
			e.printStackTrace();
		}
		return  null;
	}

	/**
	 * -1 昨天 0 今天 1明天
	 * @return
	 */
	public  static String oneDay(int type){
		Calendar cal= Calendar.getInstance();
		cal.add(Calendar.DATE,type);
		Date d=cal.getTime();
		SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
		return sp.format(d);
	}


	public static  String  YYYY_MM_DD_HH_MM_SS(String dateTime){
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (dateTime.contains("Z")) {
				dateTime = dateTime.replace("Z", " UTC");
			}
			   logger.info("--->>"+dateTime.length());
				if (dateTime!=null&&dateTime.length()<=10){
					dateTime+=" 00:00:00";
					return  dateTime;
				}
				return defaultFormat.format(format.parse(dateTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateTime;
	}



   //2019-05-01T16:00:00.000Z 转换成 0000-00-00 00:00:00格式
	public static String dealDateFormat(String oldDate) {
		Date date1 = null;
		DateFormat df2 = null;
		try {
			oldDate= oldDate.replace("Z", " UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			Date date = df.parse(oldDate);
			SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
			date1 = df1.parse(date.toString());
			df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df2.format(date1);
	}

}
