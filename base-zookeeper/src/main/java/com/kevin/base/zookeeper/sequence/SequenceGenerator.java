package com.kevin.base.zookeeper.sequence;

import com.google.common.base.Preconditions;
import com.kevin.base.common.exception.ZKOperationException;
import com.kevin.base.zookeeper.utils.ZkUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Description: 序列号生成器
 * @Author: kevin
 * @Date: 2019/7/4 14:28
 */
public final class SequenceGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceGenerator.class);

    private static final RetryPolicy DEFAULT_RETRYPOLICY = new RetryNTimes(3, RandomUtils.nextInt(200, 500));

    private SequenceGenerator() {
    }

    /**
     * 生成下一个序列号.
     * 每次以1为步长递增.
     * @param sequenceName 序列号名称.
     * @return 下一个序列号
     */
    public static int nextInt(String sequenceName) {
        try {
            DistributedAtomicInteger coder = new DistributedAtomicInteger(ZkUtils.getClient(),
                    "/sequence/" + sequenceName,
                    DEFAULT_RETRYPOLICY);
            AtomicValue<Integer> result = coder.increment();
            if (!result.succeeded()) {
                throw new ZKOperationException("coder increase sequence " + sequenceName + " failed");
            }
            return result.postValue();
        } catch (Exception e) {
            LOGGER.error(String.format("generate next sequence with %s failed", sequenceName), e);
            throw new ZKOperationException(e.getMessage());
        }
    }

    /**
     * 生成下一个编码.
     * 如: nextCode("foo","bar") 且前一个序号值为2，
     * 则返回:bar3
     * @param sequenceName 序列号名称.
     * @param prefix       前缀。为空则忽略.
     * @return 下一个编码
     */
    public static String nextCode(String sequenceName, String prefix) {
        String finalPrefix = StringUtils.isBlank(prefix) ? "" : prefix;
        return finalPrefix + nextInt(sequenceName);
    }

    /**
     * 生成下一个编码.
     * 如: nextCode("myseq",4,"foobar") 且序列myseq的当前序号值为2，
     * 则返回:foobar0003
     * @param sequenceName 序列号名称.
     * @param length       序号长度.序号长度不够时自动补齐到指定长度.
     * @param prefix       前缀。为空则忽略.
     * @return 下一个编码
     */
    public static String nextCode(String sequenceName, int length, String prefix) {
        String finalPrefix = StringUtils.isBlank(prefix) ? "" : prefix;
        String next = String.valueOf(nextInt(sequenceName));
        // 位数补齐
        next = StringUtils.leftPad(next, length, "0");

        return finalPrefix + next;
    }

    /**
     * 按照序列号生成下一个编码,生成过程基于当前时间和指定的时间格式进行格式化。
     * 如：nextCodeWithDateformat("myseq","yyyyMMdd",4,"foobar"),
     * 且myseq的当前序号为1,则返回值为:foobar201806260002
     * @param sequenceName 序列名称
     * @param dateformat   日期格式
     * @param length       序号长度。序号长度不够时自动补齐到指定长度.
     * @param prefix       前缀
     * @return 下一个序列编码
     */
    public static String nextCodeWithDateformat(String sequenceName, String dateformat, int length, String prefix) {
        Preconditions.checkNotNull(dateformat);
        Preconditions.checkArgument(StringUtils.isNotBlank(dateformat));

        try {
            String dateStr = DateFormatUtils.format(new Date(), dateformat);
            String finalPrefix = (StringUtils.isBlank(prefix) ? "" : prefix) + dateStr;

            DistributedAtomicInteger coder = new DistributedAtomicInteger(ZkUtils.getClient(), "/sequence/" + sequenceName + "/" + dateStr,
                    DEFAULT_RETRYPOLICY);
            AtomicValue<Integer> result = coder.increment();

            if (!result.succeeded()) {
                throw new ZKOperationException("coder increase sequence " + sequenceName + " failed");
            }

            String next = String.valueOf(result.postValue());
            // 位数补齐
            next = StringUtils.leftPad(next, length, "0");

            return finalPrefix + next;
        } catch (Exception e) {
            LOGGER.error("generate next with date failed, sequence name:{},date format:{}", sequenceName, dateformat);
            LOGGER.error("generate next with date exception", e);
            throw new ZKOperationException(e.getMessage());
        }
    }
}
