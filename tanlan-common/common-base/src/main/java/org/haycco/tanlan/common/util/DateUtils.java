package org.haycco.tanlan.common.util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author haycco
 **/
public class DateUtils {

    public static final ZoneId chinaZone = ZoneId.systemDefault();
    public static final String CRON_FORMATTER = "ss mm HH dd MM ? yyyy";
    public static final String SIMPLE_FORMATTER_DAY_PATTERN = "yyyy-MM-dd";
    public static final String SIMPLE_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String MSIMPLE_FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMATTER_CN = "yyyy年MM年dd";

    public static final DateTimeFormatter SIMPLE_FORMATTER = DateTimeFormatter.ofPattern(SIMPLE_FORMATTER_PATTERN);

    public static final DateTimeFormatter SIMPLE_FORMATTER_DAY = DateTimeFormatter.ofPattern(SIMPLE_FORMATTER_DAY_PATTERN);

    /**
     * 获取当天日期(YYYY-MM-DD格式)
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    /**
     * 按指定格式获取当天日期
     */
    public static String nowDateTime(String formatter) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter));
    }

    /**
     * 按指定格式获取当天日期
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), chinaZone);
    }

    /**
     * 日期时间对象转换为日期对象
     *
     * @param localDateTime 日期时间对象
     * @return 日期对象
     */
    public static LocalDate dateTime2Date(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();

    }

    /**
     *@Description: 把日期间隔生成一个连续的日期列表
     *  默认为当前日期延后7天
    */
    public static List<String> sequenceDays(String startDate,String endDate) {
        List<String> result = new ArrayList<>();
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().toString();
        }
        result.add(startDate);
        for (int i = 1; i <= 6; i++){
            LocalDate end = DateUtils.str2LocalDate(startDate).plusDays(i);
            if(endDate != null && end.isAfter(DateUtils.str2LocalDate(endDate))){
                break;
            }
            result.add(end.toString());
        }
        return result;
    }

    /**
     * 日期对象转换为日期对象
     *
     * @param localDate 日期对象
     * @return 日期时间对象
     */
    public static LocalDateTime date2DateTIme(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.NOON);
    }

    /**
     * 字符串转换为日期
     *
     * @param strDate 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static LocalDate str2LocalDate(String strDate) {
        return LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
    }

    /**
     * 字符串转换为日期
     *
     * @param strDate 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static LocalDateTime str2LocalDateTime(String strDate, DateTimeFormatter formatter) {
        return LocalDateTime.parse(strDate, formatter);
    }

    /**
     * 字符串转换为日期
     *
     * @param strDate 字符串日期
     * @return 日期对象 yyyy-mm-dd
     */
    public static LocalDate str2LocalDate(String strDate, DateTimeFormatter formatter) {
        return LocalDate.parse(strDate, formatter);
    }

    /**
     * yyyyMMddHHmmss时间转LocalDateTime
     */
    public static LocalDateTime str2LocalDateTime(String strDate) {
        return LocalDateTime.parse(strDate, SIMPLE_FORMATTER);
    }

    /**
     * LocalDateTime转换为Date
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(chinaZone).toInstant());
    }

    /**
     * LocalDateTime转换为Date
     */
    public static Date str2Date(String strDate, String formatter) {
        return localDateTime2Date(str2LocalDateTime(strDate, DateTimeFormatter.ofPattern(formatter)));
    }

    /**
     * yyyyMMddHHmmss时间转Date
     */
    public static Date str2dateWithYMDHMS(String yyyyMMddHHmmssTime) {
        LocalDateTime ldt = LocalDateTime.parse(yyyyMMddHHmmssTime, SIMPLE_FORMATTER);
        return localDateTime2Date(ldt);
    }

    /***
     * 将指定格式的时间字符串转为Date
     * @param time
     * @param formatter
     * @return
     */
    public static Date str2date(String time, String formatter) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime ldt = LocalDateTime.parse(time, dtf);
        return localDateTime2Date(ldt);
    }

    /**
     * 日期对象转换为字符串
     *
     * @param localDate 日期对象
     * @return 日期字符串 yyyy-mm-dd
     */
    public static String date2Str(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(SIMPLE_FORMATTER_DAY_PATTERN));
    }

    /**
     * 日期时间对象转换为字符串
     *
     * @param localDateTime 日期时间对象
     * @param dateTimeFormatter 格式化字符串
     * @return 日期字符串
     */
    public static String dateTime2Str(LocalDateTime localDateTime, String dateTimeFormatter) {
        return localDateTime.format(DateTimeFormatter.ofPattern(dateTimeFormatter));
    }

    /**
     * 日期时间转字符串函数 返回ISO标准的日期字符串
     *
     * @param localDateTime 日期时间对象
     * @return 日期字符串
     */
    public static String dateTime2Str(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(SIMPLE_FORMATTER_PATTERN));
    }

    /**
     * 获取东八时间戳,秒级
     */
    public static long getTimeStrampMiniseconds() {
        //获取秒数
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /***
     * 获取时间戳,毫秒级
     * @return
     */
    public static String getTimeStamp() {
        Date d = new Date();
        //getTime()得到的是微秒， 需要换算成秒
        long timeStamp = d.getTime() / 1000;
        return String.valueOf(timeStamp);
    }

    /**
     * 获取东八时间戳,毫秒级
     */
    public static long getTimeStrampSeconds() {
        //获取秒数
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * date 转string： yyyy-MM-dd HH:mm:ss
     */
    public static String fromDate2Str(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_FORMATTER_PATTERN);
        return simpleDateFormat.format(date);
    }

    /**
     * date 转string： yyyy-MM-dd HH:mm:ss
     */
    public static String fromDate2Str(Date date,String formatter) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
        return simpleDateFormat.format(date);
    }

    /**
     * 计算两个时间之间相差的秒数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     */
    public static long secondsBetween(LocalDateTime date1, LocalDateTime date2) {
        Duration duration = Duration.between(date1, date2);
        return duration.getSeconds();
    }

    /**
     * 计算两个时间之间相差的秒数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     */
    public static long secondsBetween(Date date1, Date date2) {
        Instant instantDateTime1 = date1.toInstant();

        Instant instantDateTime2 = date2.toInstant();
        Duration duration = Duration.between(instantDateTime1, instantDateTime2);
        return duration.getSeconds();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int daysBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getDays();
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int monthsBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getMonths();
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int yearsBetween(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date1, date2);
        return period.getYears();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int daysBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(chinaZone).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(chinaZone).toLocalDate();
        instantDate1.atZone(chinaZone);
        Period period = Period.between(localDate1, localDate2);
        return period.getDays();
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int monthsBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(chinaZone).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(chinaZone).toLocalDate();
        instantDate1.atZone(chinaZone);
        Period period = Period.between(localDate1, localDate2);
        return period.getMonths();
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param date1 起始日期
     * @param date2 结束日期
     */
    public static int yearsBetween(Date date1, Date date2) {
        Instant instantDate1 = date1.toInstant();
        Instant instantDate2 = date2.toInstant();
        LocalDate localDate1 = instantDate1.atZone(chinaZone).toLocalDate();
        LocalDate localDate2 = instantDate2.atZone(chinaZone).toLocalDate();
        instantDate1.atZone(chinaZone);
        Period period = Period.between(localDate1, localDate2);
        return period.getYears();
    }

    /**
     * 获取指定日期对象当前月的起始日
     *
     * @param localDate 指定日期
     */
    public static int getFirstDayInMonth(LocalDate localDate) {
        LocalDate result = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return result.getDayOfMonth();

    }

    /**
     * 获取指定日期对象的当前月的结束日
     *
     * @param localDate 指定日期
     */
    public static int getLastDayInMonth(LocalDate localDate) {
        LocalDate result = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return result.getDayOfMonth();
    }

    /**
     * 获取指定日期对象本月的某周某天的日期
     *
     * @param localDate 日期对象
     * @param weekNumber 周
     * @param dayNumber 日
     */
    public static LocalDate getLocalDateBydayAndWeek(LocalDate localDate, int weekNumber, int dayNumber) {
        return localDate.with(TemporalAdjusters.dayOfWeekInMonth(weekNumber, DayOfWeek.of(dayNumber)));
    }

    public static String getHour(LocalDateTime localDate) {
        return dateTime2Str(localDate, SIMPLE_FORMATTER_PATTERN).substring(8, 10);
    }

    public static String getDay(LocalDateTime localDate) {
        return dateTime2Str(localDate, SIMPLE_FORMATTER_PATTERN).substring(6, 8);
    }

    /**
     * 获取指定秒数后的时间戳
     */
    public static long getDateTimeAfterSeconds(LocalDateTime dateTime, long after) {
        return dateTime.plusSeconds(after).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取指定秒数后的时间
     */
    public static LocalDateTime getLocalDateTimeAfterSeconds(LocalDateTime dateTime, long after) {
        return dateTime.plusSeconds(after);
    }


    /**
     * 转时间戳（千分秒）
     */
    public static long toEpochMilli(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime);
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 时间（千分秒）戳转 LocalDateTime
     */
    public static LocalDateTime epochMilliToLocalDateTime(String epochMilli) {
        long timestamp = Long.parseLong(epochMilli);
        return LocalDateTime.ofEpochSecond(timestamp / 1000, (int) (timestamp % 1000 * 1000_000), ZoneOffset.ofHours(8));
    }


    public static final DateTimeFormatter TODAY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static final DateTimeFormatter BEFORE_YESTERDAY_FORMATTER = DateTimeFormatter.ofPattern("M.d");

    public static final DateTimeFormatter LAST_YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy.M.d");

    /**
     * 时间转显示时间
     *
     * 当天显示具体时间（24h制），非当天显示日期3.1/10.1/4.20，非当年显示年月日2015.3.1/2018.10.1/2019.4.20
     */
    public static String toShowDate(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime);

        LocalDateTime nowDate = LocalDateTime.now();

        long diff_day = ChronoUnit.DAYS.between(localDateTime.toLocalDate(), nowDate.toLocalDate());

        //当天显示具体时间（24h制）
        if (diff_day == 0) {
            return localDateTime.format(TODAY_FORMATTER);
        }

        //非当天显示日期3.1/10.1/4.20
        if (nowDate.getYear() - localDateTime.getYear() == 0) {
            return localDateTime.format(BEFORE_YESTERDAY_FORMATTER);
        } else {
            //非当年显示年月日2015.3.1/2018.10.1/2019.4.20
            return localDateTime.format(LAST_YEAR_FORMATTER);
        }

    }

    /**
     *@Description: 把日期转化为cron表达式
     *@param: [date]
     *@return: java.lang.String
    */
    public static String getCron(Date date){
        return fromDate2Str(date,CRON_FORMATTER);
    }

    public static void main(String[] args) {

        LocalDateTime today = LocalDateTime.parse("2019-03-29 03:02:12", SIMPLE_FORMATTER);

        LocalDateTime yesterday = LocalDateTime.parse("2019-03-28 23:12:12", SIMPLE_FORMATTER);

        LocalDateTime lastDay = LocalDateTime.parse("2019-03-07 23:12:12", SIMPLE_FORMATTER);

        LocalDateTime lastYear = LocalDateTime.parse("2018-03-08 23:12:12", SIMPLE_FORMATTER);

        System.out.println(toShowDate(today));
        System.out.println(toShowDate(yesterday));
        System.out.println(toShowDate(lastDay));
        System.out.println(toShowDate(lastYear));

        System.out.println(fromDate2Str(new Date(),MSIMPLE_FORMATTER_PATTERN));

        System.out.println(getCron(new Date()));

//        System.out.println(DateUtils.fromDate2Str(new Date(1548404064262L)));
//        System.out.println(DateUtils.getDateTimeAfterSeconds(LocalDateTime.now(), 5 * 60));
//
//        System.out.println(DateUtils
//                .str2Date("20180814140659", DateUtils.SIMPLE_FORMATTER));
//
//        System.out.println(DateUtils.nowDateTime(DateUtils.SIMPLE_FORMATTER));
//
//        LocalDateTime now = LocalDateTime.now();
//        Thread.sleep(1000);
//        LocalDateTime now2 = LocalDateTime.now();
//        System.out.println(secondsBetween(now, now2));
//
//        System.out.println(Duration.between(now, now2).toMillis() / 1000);
//
//        System.out.println(getDateTimeAfterSeconds(LocalDateTime.now(), 3600));
//        LocalDateTime date1 = dateToLocalDateTime(new Date());
//        Thread.sleep(3000);
//        System.out.println(secondsBetween(date1, LocalDateTime.now()));
        System.out.println(LocalDate.now().toString());
        System.out.println(str2LocalDate("2019-06-17"));
        System.out.println(str2LocalDate("2019-06-17").toString());
    }

}
