package com.yafnds;

import org.junit.Test;

/**
 * 类描述：测试字符串切割
 * <p>创建时间：2022/10/11/011 16:05
 *
 * @author yafnds
 * @version 1.0
 */

public class MyTestStringSplit {

    String source = "http://rjbibpv28.hn-bkt.clouddn.com/2022/10/11/d8a85afc-ac86-4400-9cb2-202fd6db8b88.jpg";
    String substring = "http://rjbibpv28.hn-bkt.clouddn.com/";

    @Test
    public void testStringBuildSplit() {

        StringBuilder sourceSB = new StringBuilder(source);
        StringBuilder substringSB = new StringBuilder(substring);

        long startTime = System.nanoTime();

        int first = sourceSB.indexOf(substring);
        String result = sourceSB.substring(first + substringSB.length());

        long endTime = System.nanoTime();

        System.out.println("result = " + result);
        System.out.println("转换成 SpringBuild 后 程序耗时："+(endTime - startTime)+" ns");
    }

    /**
     * string 切割
     */
    @Test
    public void testStringSplit() {

        long startTime = System.nanoTime();

        int first = source.indexOf(substring);
        String result = source.substring(first + substring.length());

        long endTime = System.nanoTime();

        System.out.println("result = " + result);
        System.out.println("纯字符串切割 程序耗时："+(endTime - startTime)+" ns");
    }

    /**
     * stringBuild 和 string 切割效率对比
     */
    @Test
    public void testOverall() {
        testStringBuildSplit();
        System.out.println("------------");
        testStringSplit();
    }
}
