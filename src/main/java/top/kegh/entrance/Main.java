package top.kegh.entrance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/*
 * --------------------CSDN博客访问量程序--------------------
 * 设计思路：
 * 1、需要爬取CSDN文章的列表
 * 2、随机访问：
 *      模拟不同ip代理
 *      模拟不同时刻的随机访问
 *      模拟不同文章的随机访问
 * 3、定时调用
 * 4、结果显示
 *
 *
 *
 * 将要刷访问量的博客id填写入24行的变量userId中，点击运行
 * 本程序访问该博主【用户ID】名下所有博客链接
 * 仅供学习测试使用，不要真的用于刷访问量~
 */
public class Main {
    public static void main(String args[]) throws IOException, InterruptedException {
        //获取上下文对象ApplicationContext  classpath*:
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
    }
}
