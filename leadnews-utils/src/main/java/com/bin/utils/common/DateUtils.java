package com.bin.utils.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/4 11:20
 */
public class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_TIME_STAMP_FORMAT = "yyyyMMddHHmmss";

    private static final String DATE_STAMP_FORMAT = "yyyyMMdd";

    private static final String DATE_FORMAT_CHINESE = "yyyy年M月d日";

    private static final String DATE_TIME_FORMAT_CHINESE = "yyyy年M月d日 HH:mm:ss";

    private static final Logger logger =  LoggerFactory.getLogger(DateUtils.class);

    /**
     * 获取当前日期的字符串
     * @return
     */
    public static String getDateFormat(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String date = format.format(new Date());
        return date;
    }

    /**
     * 获取当前时间的字符串
     * @return
     */
    public static String getDateTimeStampFormat(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_STAMP_FORMAT);
        String date = format.format(new Date());
        return date;
    }

    /**
     * 获取当前时间的字符串
     * @return
     */
    public static String getDateTimeFormat(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
        String date = format.format(new Date());
        return date;
    }

    /**
     * 获取当前日期的字符串
     * @return
     */
    public static  String getDateStampFormat(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_STAMP_FORMAT);
        String date = format.format(new Date());
        return date;
    }

    /**
     * 获取当前日期的中文格式
     * @return
     */
    public static String getDateFormatChinese(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_CHINESE);
        String date = format.format(new Date());
        return date;
    }

    /**
     * 获取当前时间的中文格式
     * @return
     */
    public static String getDateTimeFormatChinese(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT_CHINESE);
        String date = format.format(new Date());
        return date;
    }

    /**
     * 转换指定日期为字符串格式
     * @param date
     * @return
     */
    public static String getDateToDateTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
        String dateStr = format.format(date);
        return dateStr;
    }

    /**
     * 返回指定格式日期
     * @param dateFormat
     * @return
     */
    public static String getCurrentTime(String dateFormat){
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String date = format.format(new Date());
        return date;
    }

    /**
     * 将字符串日期转换成指定日期格式
     * yyyy-MM-dd
     * @param dateTime
     * @return
     */
    public static Date stringTimeToDate(String dateTime){
        if (StringUtils.isEmpty(dateTime)){
            return null;
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            //e.printStackTrace();
            logger.info("日期转换出错！");
            logger.error(e.getMessage(), e);
            date = stringTimeToDate(dateTime, DATE_STAMP_FORMAT);
        }
        return date;
    }

    /**
     * 将字符串转换成日期格式
     * 自定义
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date stringTimeToDate(String dateStr, String dateFormat){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            //e.printStackTrace();
            logger.info("日期转换出错！");
            logger.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * 将日期转换为字符串格式
     * yyyy-MM-dd
     * @param date
     * @return
     */
    private static String dateToStringTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String stringTime = format.format(date);
        return stringTime;
    }

    /**
     * 将日期转换为字符串格式
     * 自定义
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToStringTime(Date date, String dateFormat){
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String StringTime = format.format(date);
        return StringTime;
    }

    /**
     * 获取date日期中的day值
     * @param date
     * @return
     */
    private static int getDayOfDate(Date date){
        int day = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取date日期的month值
     * @param date
     * @return
     */
    private static int getMonthOfDate(Date date){
        int month = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取date日期中的year值
     * @param date
     * @return
     */
    private static int getYearOfDate(Date date){
        int year = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        year = calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取星期几
     * @param date
     * @return
     */
    private static int getWeekOfDate(Date date){
        int week = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }

    /**
     * 获取date日期month的第一天
     * @param date
     * @return
     */
    private static Date getFirstDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取date日期中月份的最后一天
     * @param date
     * @return
     */
    private static Date getLastDayOfMonth(Date date){
        return addDay(getFirstDayOfMonth(addMonth(date, 1)), -1);
    }

    /**
     * 在输入日期上增加或减去天数
     * @param date
     * @param day
     * @return
     */
    private static Date addDay(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 在输入日期上增加或者减去月份
     * @param date
     * @param month
     * @return
     */
    private static Date addMonth(Date date, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 判断是否是闰年
     * @param date
     * @return
     */
    public static boolean isLeadYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        if (year % 4 == 0 && year % 100 != 0 | year %400 ==0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据整型数表示的年月日，生成日期格式
     * @param year
     * @param month
     * @param day
     * @return
     */
    private static Date getDateByMD(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * 获取年周期对应日期
     * @param date
     * @param year
     * @return
     */
    private static Date getYearCycleOfDate(Date date, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取月周期对应的日期
     * @param date
     * @param month
     * @return
     */
    private static Date getMonthCycleOfDate(Date date, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 计算相差多少年
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int getYearMinusDate(Date fromDate, Date toDate){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(fromDate);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(toDate);

        return calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
    }

    /**
     * 计算相差多少个月
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int getMonthMinusDate(Date fromDate, Date toDate){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(fromDate);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(toDate);

        return calendar2.get(Calendar.YEAR) * 12 + calendar2.get(Calendar.MONTH)
                - calendar1.get(Calendar.YEAR) * 12 + calendar1.get(Calendar.MONTH);
    }

    /**
     * 计算相差多少天
     * @param fromDate
     * @param toDate
     * @return
     */
    public static long getDayMinusDate(Object fromDate, Object toDate){
        Date f = DateUtils.chgObject(fromDate);
        Date t = DateUtils.chgObject(toDate);
        long fd = f.getTime();
        long td = t.getTime();
        return (td - fd) / (24L * 60L * 60L * 1000L);
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日日期
     * @param calcDate 要计算的日期点
     * @return
     */
    public static int calcAge(Date birthday, Date calcDate) {
        int cYear = DateUtils.getYearOfDate(calcDate);
        int cMonth = DateUtils.getMonthOfDate(calcDate);
        int cDay = DateUtils.getDayOfDate(calcDate);
        int bYear = DateUtils.getYearOfDate(birthday);
        int bMonth = DateUtils.getMonthOfDate(birthday);
        int bDay = DateUtils.getDayOfDate(birthday);
        if (cMonth > bMonth || (cMonth == bMonth && cDay > bDay)) {
            return cYear - bYear;
        } else {
            return cYear - 1 - bYear;
        }
    }

    /**
     * 从身份证中获取出生日期
     *
     * @param idno 身份证号码
     * @return
     */
    public static String getBirthDayFromIDCard(String idno) {
        Calendar cd = Calendar.getInstance();
        if (idno.length() == 15) {
            cd.set(Calendar.YEAR, Integer.valueOf("19" + idno.substring(6, 8))
                    .intValue());
            cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(8, 10))
                    .intValue() - 1);
            cd.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(10, 12)).intValue());
        } else if (idno.length() == 18) {
            cd.set(Calendar.YEAR, Integer.valueOf(idno.substring(6, 10))
                    .intValue());
            cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(10, 12))
                    .intValue() - 1);
            cd.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(12, 14)).intValue());
        }
        return DateUtils.dateToStringTime(cd.getTime());
    }

    /**
     * 將OBJECT類型轉換為Date
     *
     * @param date
     * @return
     */
    public static Date chgObject(Object date) {
        if (date != null && date instanceof Date) {
            return (Date) date;
        }
        if (date != null && date instanceof String) {
            return DateUtils.stringTimeToDate((String) date);
        }
        return null;

    }

    public static long getAgeByBirthday(String date) {
        Date birthday = stringTimeToDate(date, "yyyy-MM-dd");
        long sec = new Date().getTime() - birthday.getTime();
        long age = sec / (1000 * 60 * 60 * 24) / 365;
        return age;
    }

}
