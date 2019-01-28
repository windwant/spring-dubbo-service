package org.windwant.spring.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


public class DateUtil {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String MONTHS = "months";

    public static final String DAYS = "days";

    public static final String DAY = "day";

    public static final String HOURS = "hours";

    public static final String MINUTES = "minutes";

    public static final String SECONDS = "seconds";

    private static final String TIME_PATTERN_T = "yyyyMMddHHmmss";

    private static final Pattern DATE_PATTERN_REGEX = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");

    //中国时区
    private static final ZoneOffset zoneOffset = ZoneOffset.ofHours(8);

    private DateUtil() {
    }
    
    public static void main(String[] args) {
//    	System.out.println(getDateWithChinese("2016-07-27 23:11:25"));

//        System.out.println(dateDiff("2016-09-19 19:10:00", "2016-09-20 17:48:11", "seconds"));
        System.out.println(getDayWithChineses("2017-07-05"));
    }

    public static String getUnixTimestamp() {
    	return String.valueOf(System.currentTimeMillis() / 1000);
    }
    
    /**
     * 获取中文的日期
     * @param dateStr
     * @return
     */
    public static String getDayWithChineses(String dateStr) {
    	dateStr = dateStr + " 00:00:00";
    	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
    	Calendar cal = Calendar.getInstance();
        cal.setTime(strToDateLong(dateStr));
    	return df.format(cal.getTime());
    }

    /**
     * 获取本月第一天
     * @return
     */
    public static String getThisMonthFirstDay() {
    	Calendar cale = Calendar.getInstance();  
    	cale.add(Calendar.MONTH, 0);  
        cale.set(Calendar.DAY_OF_MONTH, 1);
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00"); 
    	return format.format(cale.getTime()); 
    }

    /**
     * 获取指定*分钟前日期
     * @param timeAgo
     * @return
     */
    public static String getDateStr(int timeAgo) {
    	 long time = System.currentTimeMillis() - timeAgo * 60 * 1000;
         Date date = new Date(time);
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         return formatter.format(date);
    }
    
    /**
     * 获取指定时间的日期
     * @param dateStr
     * @return
     */
    public static String getDate(String dateStr) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	try {
			df.parse(dateStr);
		} catch (ParseException e1) {
			return null;
		}
    	return dateStr.substring(0, "yyyy-MM-dd".length());
    }
    
    /**
     * 获取中文日期
     * @param dateStr
     * @return
     */
    public static String getDateWithChinese(String dateStr) {
    	SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");
    	Calendar cal = Calendar.getInstance();
        cal.setTime(strToDateLong(dateStr));
    	return df.format(cal.getTime());
    }
    
    /**
     * 获取指定时间的小时
     * @param dateStr
     * @return
     */
    public static int getHour(String dateStr) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			return -1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    
    /**
     * 获取指定时间戳
     * @param dateStr
     * @return
     */
    public static long getTimeStamps(String dateStr) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			return 0l;
		}
        return date.getTime();
    }

    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 获取指定时间戳
     * @param dateStr
     * @return
     */
    public static Long getTimeStamps(String dateStr, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
        return date.getTime();
    }
    
    /**
     * 字符串转化成星期几
     * @param dt
     * @return
     */
    public static String getWeekOfDate(String dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(strToDateLong(dt));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    
    /**
     * 将字符串转化成date格式
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	ParsePosition pos = new ParsePosition(0);
    	Date strtodate = formatter.parse(strDate, pos);
    	return strtodate;
    }
    
    
    public static String getCurrentTimeStr() {
    	Date currentTime = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return formatter.format(currentTime);
    }
    
    public static String getCurrentTimeStrWithoutBlank() {
    	Date currentTime = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    	return formatter.format(currentTime);
    }

    public static String getCurrentTimeStrToDayWithoutBlank() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(currentTime);
    }

    public static String getCurrentTimeStrSimpleYearToDay() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        return formatter.format(currentTime);
    }

    public static long dateDiff (Date startDate, Date endDate, String type) {
        long startLong = startDate.getTime();
        long endLong;
        if (endDate == null) {
            endLong = System.currentTimeMillis();
        } else {
            endLong = endDate.getTime();
        }
        long diff = (endLong - startLong) / 1000L;

        switch (type) {
            case MONTHS :
                return diff/60/60/24/30;
            case DAYS :
                return diff/60/60/24;
            case DAY :
                return diff/60/60/24;
            case HOURS :
                return diff/60/60;
            case MINUTES :
                return diff/ 60;
            case SECONDS :
                return diff;
        }

        return -1l;
    }

    /**
     * 时间比较
     * @tip ParsePosition每次需要重新创建
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    public static long dateDiff (String startDate, String endDate, String type) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startLong = formatter.parse(startDate, new ParsePosition(0)).getTime();
        long endLong;
        if (endDate == null) {
    		endLong = System.currentTimeMillis();
    	} else {
            endLong = formatter.parse(endDate, new ParsePosition(0)).getTime();
        }
        long diff = (endLong - startLong) / 1000L;

        switch (type) {
    		case MONTHS :
    			return diff/60/60/24/30;
    		case DAYS :
    			return diff/60/60/24;
    		case DAY :
    			return diff/60/60/24;
    		case HOURS :
    			return diff/60/60;
    		case MINUTES :
    			return diff/ 60;
    		case SECONDS :
    			return diff;
    	}
    	
    	return -1l;
    }

    /**
     * 计算两个日期字符串的间隔天数
     *
     * @param firstDate 格式：yyyy-MM-dd
     * @param lastDate  格式：yyyy-MM-dd
     */
    public static int daysBetween(String firstDate, String lastDate) {
        LocalDateTime localDate1 = LocalDateTime.parse(firstDate + " 00:00:00", DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime localDate2 = LocalDateTime.parse(lastDate + " 00:00:00", DateTimeFormatter.ofPattern(TIME_PATTERN));
        return (int) Duration.between(localDate1, localDate2).toDays();
    }

    /**
     * 时间间隔
     * @param startDate
     * @param endDate
     * @param timeUnit 0 day 1 hours 2 minutes 3 seconds 4 milliseconds 5 nanoseconds
     * @param abs
     * @return
     */
    public static Long timeIntervalBetween(String startDate, String endDate, int timeUnit, boolean abs){
        LocalDateTime startLocalDateTime = parseLocalDateTime(startDate);
        LocalDateTime endLocalDateTime = parseLocalDateTime(endDate);
        return timeIntervalBetween(startLocalDateTime, endLocalDateTime, timeUnit, abs);
    }

    /**
     * 时间间隔
     * @param startLocalDateTime
     * @param endLocalDateTime
     * @param timeUnit
     * @param abs
     * @return
     */
    public static Long timeIntervalBetween(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime, int timeUnit, boolean abs){
        Duration duration = Duration.between(startLocalDateTime, endLocalDateTime);
        Long interval = 0L;
        switch (timeUnit){
            case 0: interval = duration.toDays(); break;
            case 1: interval = duration.toHours(); break;
            case 2: interval = duration.toMinutes(); break;
            case 3: interval = duration.toMillis()/1000; break;
            case 4: interval = duration.toMillis(); break;
            case 5: interval = duration.toNanos(); break;
            default: interval = duration.toDays(); break;
        }
        return abs? Math.abs(interval): interval;
    }

    /**
     * transfer
     * @param date
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String date){
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    /**
     * 计算两个日期字符串的间隔天数
     *
     * @param firstDate 格式：yyyy-MM-dd
     * @param lastDate  格式：yyyy-MM-dd
     */
    public static int daysBetweenOfDateTime(String firstDate, String lastDate) {
        LocalDateTime localDate1 = LocalDateTime.parse(firstDate, DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime localDate2 = LocalDateTime.parse(lastDate, DateTimeFormatter.ofPattern(TIME_PATTERN));
        return (int) Duration.between(localDate1, localDate2).toDays();
    }

    /**
     * 获取当前系统时间，返回格式 yyyy-MM-dd
     */
    public static String getCurrentTime() {
        LocalDate localDate = LocalDate.now();
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * 解析日期字符串
     *
     * @param date 日期字符串 格式yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime parseDateTime(String date) throws ParseException {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    /**
     * 解析日期字符串
     *
     * @param date    日期字符串
     * @param pattern 字符串格式
     */
    public static LocalDateTime parseDateTime(String date, String pattern) throws ParseException {
        if (StringUtils.isEmpty(pattern)) {
            return parseDateTime(date);
        }
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期字符串
     *
     * @param date 日期字符串 格式yyyy-MM-dd
     */
    public static LocalDate parseDate(String date) throws ParseException {
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

    /**
     * 计算当前日期的long时间戳 (单位:秒)
     *
     * @param localDateTime 格式：yyyy-MM-dd HH:mm:ss
     */
    public static long getSeconds(LocalDateTime localDateTime) {
        return between(localDateTime).getSeconds();
    }

    /**
     * 计算当前日期的long时间戳 (单位：毫秒)
     *
     * @param localDateTime 格式：yyyy-MM-dd HH:mm:ss
     */
    public static long getMilliseconds(LocalDateTime localDateTime) {
        return between(localDateTime).toMillis();
    }

    /**
     * @deprecated 判断字符串是否是日期
     */
    @Deprecated
    public static boolean isDate(String date) {
        return DATE_PATTERN_REGEX.matcher(date).find();
    }

    /**
     * 转日期字符串
     *
     * @param time long类型时间戳(单位:秒)
     */
    public static String formatDateFromLong(Long time) {
        //ZoneOffset.UTC +8 = china
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(time, 0, zoneOffset);
        return localDateTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN_T));
    }

    /**
     * 转日期字符串
     *
     * @param time long类型时间戳(单位:秒)
     */
    public static String formatDateFromLong(Long time, String format) {
        //ZoneOffset.UTC +8 = china
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(time, 0, zoneOffset);
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    private static Duration between(LocalDateTime localDateTime) {
        return Duration.between(LocalDateTime.ofEpochSecond(0, 0, zoneOffset), localDateTime);
    }

    public static boolean checkDate(String date){
        try{
            parseDate(date);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static String dateFormatToString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static Date stringParseToDate(String string){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
