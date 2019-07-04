package com.kevin.base.zookeeper.sequence;

import com.kevin.base.zookeeper.utils.ZkUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @Description: 序列号生成器测试类
 * @Author: kevin
 * @Date: 2019/7/4 14:52
 */
public class SequenceGeneratorTest {

    private String seqName = "foobar";

    @Before
    public void prepare() throws Exception {

        List<String> children = ZkUtils.getClient().getChildren().forPath("/");
        for (String child : children) {
            ZkUtils.getClient().delete().deletingChildrenIfNeeded().forPath("/" + child);
        }
    }

    @Test
    public void testNextInt() {
        int num = SequenceGenerator.nextInt(seqName);
        Assert.assertEquals(1, num);
        num = SequenceGenerator.nextInt(seqName);
        Assert.assertEquals(2, num);
    }

    @Test
    public void testNextCodeStringString() {
        String code = SequenceGenerator.nextCode(seqName, "foobar");
        Assert.assertEquals("foobar1", code);
        code = SequenceGenerator.nextCode(seqName, "foobar");
        Assert.assertEquals("foobar2", code);
    }

    @Test
    public void testNextCodeStringIntString() {
        String code = SequenceGenerator.nextCode(seqName, 4, "foobar");
        Assert.assertEquals("foobar0001", code);
        code = SequenceGenerator.nextCode(seqName, 4, "foobar");
        Assert.assertEquals("foobar0002", code);
    }

    @Test
    public void testNextCodeWithDateformat() {
        String dateStr = DateFormatUtils.format(new Date(), "yyyyMMdd");

        String code = SequenceGenerator.nextCodeWithDateformat(seqName, "yyyyMMdd", 4, "foobar");
        Assert.assertEquals("foobar" + dateStr + "0001", code);
        code = SequenceGenerator.nextCodeWithDateformat(seqName, "yyyyMMdd", 4, "foobar");
        Assert.assertEquals("foobar" + dateStr + "0002", code);
    }
}
