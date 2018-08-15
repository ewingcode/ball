package com.ewing.order.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tanson lam 2014年9月3日
 *
 */
public class DateUtil {
    
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT1 = "yyyyMMddHHmmss";
    public static final String DAY_FORMAT = "yyyy-MM-dd";
    public static final String SHORT_DAY_FORMAT = "yyyyMMdd";
    public static final String MONTH_FORMAT = "yyyy-MM";
    public static final String SHORT_MONTH_FORMAT = "yyyyMM";
    public static final DateFormat DATATIME_FORMAT_2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private static final String INVALID_TIME = "0000-00-00 00:00:00";
    static final DateFormat DATATIME_FORMAT = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
    public static final SimpleDateFormat DF_TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT);
    public static final SimpleDateFormat DF_DAY_FORMAT = new SimpleDateFormat(DAY_FORMAT);
    public static final SimpleDateFormat DF_SHORT_DAY_FORMAT = new SimpleDateFormat(
            SHORT_DAY_FORMAT);

    /**
     * 获取前N个月数组
     *
     * @param n 月数
     * @return List<String> 返回N个月日期队列
     */
    public static List<String> getPreMonths(int n) {
        if (n <= 0) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            result.add(DateUtil.format(getPreMoth(i), SHORT_MONTH_FORMAT));
        }

        return result;
    }

    /**
     * 获取前N月数据
     *
     * @param pre 前N个月
     * @return String 返回前N个月数据
     */
    public static Date getPreMoth(int pre) {
        if (pre < 0) {
            return null;
        }
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.MONTH, begin.get(Calendar.MONTH) - pre + 1);
        begin.set(Calendar.DAY_OF_MONTH, 0);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        return begin.getTime();
    }
    
    /**
     * 获取当前天
     *
     * @return String
     */
    public static String getCurDayStr() {
        return new SimpleDateFormat(DAY_FORMAT).format(new Date());
    }

    /**
     * 获取前一个月
     *
     * @param date 当前时间
     * @return Date 前一个月时间
     */
    public static Date getPreviousMonth(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.set(Calendar.MONTH, cd.get(Calendar.MONTH) - 1);
        return cd.getTime();
    }
    
    /**
     * 获取前一个月
     *
     * @param date 当前时间
     * @return Date 前一个月时间
     */
    public static Date getNextMonth(Date date, int next) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.set(Calendar.MONTH, cd.get(Calendar.MONTH) + next);
        return cd.getTime();
    }

    /**
     * 获取前一天
     *
     * @return Date
     */
    public static Date getPreDay(int pre) {
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH) - pre);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        return begin.getTime();
    }
    
    /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayBefore(String specifiedDay) {
        // 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        /*
         * String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c .getTime());
         */
        return c.getTime();
        // return dayBefore;
    }

    /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayBefore(Date specifiedDay) {
        // 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        /*
         * String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c .getTime());
         */
        return c.getTime();
        // return dayBefore;
    }

    /**
     * 获得指定日期的前一周
     * 
     * @param specified
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedWeekBefore(String specified) {
        // 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specified);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        c.setTime(date);
        int day = c.get(Calendar.WEDNESDAY);
        c.set(Calendar.WEDNESDAY, day - 1);

        return c.getTime();
    }

    /**
     * 获得指定日期的前一周
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedWeekBefore(Date specifiedDay) {
        // 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day = c.get(Calendar.WEDNESDAY);
        c.set(Calendar.WEDNESDAY, day - 1);

        return c.getTime();
    }

    /**
     * 获取当前月第一天
     *
     * @return Date 当前月第一天
     */
    public static Date getFirstDay() {
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.DAY_OF_MONTH, 1);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        return begin.getTime();
    }

    public static Date getDate(String df) {
        Date date = new Date();
        String dateStr = format(date, df);
        return parse(dateStr, df);
    }

    /**
     * 获取下N天
     *
     * @param date 当前时间
     * @param n 下N天
     * @return Date 下N天日期
     */
    public static Date getNextDate(Date date, int n) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(date);
        begin.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH) + n);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        return begin.getTime();
    }

    /**
     * 时间累加
     *
     * @param date
     * @param second
     * @return Date
     */
    public static Date addDate(Date date, int second) {
        if (null == date) {
            return null;
        }
        return new Date(date.getTime() + second * 1000);
    }

    /**
     * 获取年度第一个月
     *
     * @param date 当前时间
     * @return String 当前时间的年度第一个月
     */
    public static String getFirstMonth(Date date) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(date);
        begin.set(Calendar.MONTH, 0);
        return format(begin.getTime(), MONTH_FORMAT);
    }

    public static Date formatDate(String dateStr) throws ParseException {
        if (INVALID_TIME.contains(dateStr))
            return new Date();
        if (dateStr.length() == TIME_FORMAT.length()) {
            return new SimpleDateFormat(TIME_FORMAT).parse(dateStr);
        } else if (dateStr.length() == DAY_FORMAT.length()) {
            return new SimpleDateFormat(DAY_FORMAT).parse(dateStr);
        } else if (dateStr.length() == MONTH_FORMAT.length()) {
            return new SimpleDateFormat(MONTH_FORMAT).parse(dateStr);
        } else {
            return new Date();
        }

    }

    public static long getNowDaytime() {
        try {
            return DF_DAY_FORMAT.parse(DF_DAY_FORMAT.format(new Date())).getTime();
        } catch (Exception e) {
        }
        return 0l;
    }

    public static long getTime(String dateStr) throws ParseException {
        return formatDate(dateStr).getTime();
    }

    public static String toDatetime(Long l) {
        if (l == null)
            return "";
        return DATATIME_FORMAT.format(new Date(l));
    }

    public static String toDatetime2(Long l) {
        return DATATIME_FORMAT_2.format(new Date(l));
    }

    public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static Date parse(String dateStr, String df) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(df, Locale.ENGLISH);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return new Date();
        }
        return date;
    }

    public static String getCurrentDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static Date getNowUtilDate() {
        return new Date();
    }

    public static Date getNowSqlDate() {
        return new Date(System.currentTimeMillis());
    }

    public static Timestamp getNowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 格式化日期, 根据用户传进来的格式
     *
     * @param date 时间对象
     * @param sdf 日期格式字符串
     * @return 格式化字符串
     */
    public static String format(Date date, String sdf) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(sdf).format(date);
    }

    public static String format(Long time, String df) {
        if (time == null || StringUtils.isEmpty(df)) {
            return StringUtils.EMPTY;
        }

        return new SimpleDateFormat(df).format(new Date(time));
    }

    /**
     * 获取前一天
     *
     * @return Date
     */
    public static Date getPreviousDate() {
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH) - 1);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        return begin.getTime();
    }

    public static Date now() {
        return new Date();
    }

}
