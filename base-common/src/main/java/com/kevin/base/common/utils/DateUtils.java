package com.kevin.base.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.TimeZone;

/**
 * @Description: 日期工具类
 * @Author: kevin
 * @Date: 2019/7/2 11:35
 */
public final class DateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String COMMON_FULLDATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
    }

    /**
     * 当前时刻的date.
     * @return 当前时刻.
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 将指定毫秒数转化为相应日期并格式化为完整日期,包含时分秒.
     * @param millisecond
     * @return
     */
    public static String normalDateFormat(long millisecond) {
        if (millisecond == 0L) {
            return null;
        }
        try {
            return DateFormatUtils.format(new Date(millisecond), COMMON_FULLDATE_PATTERN, TimeZone.getTimeZone("GMT"));
        } catch (Exception e) {
            LOGGER.error("formart date exception with value:" + millisecond, e);
            return null;
        }
    }

    /**
     * 将指定的日期转换为完整日期格式，包含时分秒
     * @param date
     * @return
     */
    public static String normalDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, COMMON_FULLDATE_PATTERN);
    }

    /**
     * 按照指定的格式进行日期的格式化.
     * @param date 日期,支持null值。
     * @param formatter 格式字串
     * @return 格式化后的字符串
     */
    public static String formatWithNullSafe(Date date, String formatter) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, formatter);
    }
}
