package top.kegh.schedule;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.kegh.auth.ProxyAuthenticator;
import top.kegh.proxy.GetProxyInfo;
import top.kegh.proxy.MyProxyInfo;
import top.kegh.utils.HttpUtil;
import top.kegh.utils.RandomUtil;
import top.kegh.utils.RegularUtil;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author By--MirKKK
 * @time 2020/9/12$ 17:10$
 * @Version: 1.0
 * @QQ 2641195399
 * @Description Java类作用描述
 */
@Component
public class BlogTrafficSchedule {
    //日志
    private static Logger logger = LoggerFactory.getLogger(BlogTrafficSchedule.class);

    //定时线程
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    //配置文件中拉取ID
    @Value("${userId:}")
    String userId;

    //时间间隔
    @Value("${timeInterval:}")
    String timeInterval;

    //是否开启 mock proxy配置
    @Value("${proxyStatus:}")
    String proxyStatus;

    // 代理服务器url
    @Value("${proxyUrl:}")
    String proxyUrl;
    // 代理服务器url
    @Value("${proxyParam:}")
    String proxyParam;

    // 代理验证
    @Value("${proxyUser:}")
    String proxyUser;
    @Value("${proxyPwd:}")
    String proxyPwd;


    //用户信息
    List<String> users = new ArrayList<>();

    //代理对象
    Proxy proxy = null;
    MyProxyInfo myProxyInfo;

    //一些常量
    final static String PREFIX_URL = "https://blog.csdn.net/";  //CSDN地址
    final static String LIST_URI = "/article/list/";//列表的URI
    // http代理: Proxy.Type.HTTP, socks代理: Proxy.Type.SOCKS
    Proxy.Type proxyType = Proxy.Type.HTTP;

    /**
     * @author By--MirKKK
     * @time 2020/9/12 19:38
     * @Version: 1.0
     * @QQ 2641195399
     * @Params * @Param null:
     * @Return * @return: null
     * @Description 主业务处理方法
     **/
    void accessBlog(String oneUser) throws Exception {
        //随机休眠
        Thread.sleep(RandomUtil.randomSleepTime(Integer.parseInt(timeInterval)));

        logger.info("-----【" + oneUser + "】的博客:正式开始博客访问调度逻辑-----");

        //获取CSDN这个用户的博客列表的URL
        String homeUrl = PREFIX_URL + oneUser + LIST_URI;

        //获取可读博客的urlSet
        Set<String> urls = new HashSet<String>();

        int totalPage = 0;

        String pageStr;
        StringBuilder curUrl = null;

        for (int i = 1; i < 100; i++) {
            curUrl = new StringBuilder(homeUrl);
            curUrl.append(i);
            logger.info("地址：" + curUrl);

            pageStr = HttpUtil.doGet(curUrl.toString(), proxy);

            List<String> list = RegularUtil.getMatherSubStrs(pageStr, "(?<=href=\")https://blog.csdn.net/" + userId + "/article/details/[0-9]{8,9}(?=\")");
            urls.addAll(list);

            totalPage = i;
            if (pageStr.lastIndexOf("空空如也") != -1) {
                logger.info("第" + i + "页是最后一页");
                break;
            } else {
                logger.info("第" + i + "页拉取成功~");
            }
        }
        logger.info("总页数为: " + totalPage);

        logger.info("开始遍历所有可读博客链接");
        int index = 0;
        for (String url : urls) {
            // 访问目标网页
            HttpUtil.doGet(url, proxy);
            logger.info("成功访问第" + (++index) + "个链接,共" + urls.size() + "个:" + url);
        }
        logger.info("运行完毕，成功增加访问数：" + urls.size());
    }


    /**
     * @author By--MirKKK
     * @time 2020/9/12 17:19
     * @Version: 1.0
     * @QQ 2641195399
     * @Params
     * @Return * @return: void
     * @Description 启动时会调用这个类
     **/
    @PostConstruct
    void init() {
        //获取多个用户信息
        if (StringUtils.isEmpty(userId)) {
            logger.error("-----用户信息缺失-----");
        } else {
            users = Arrays.asList(userId.split(","));
            logger.info("-----开始【"+users+"】的博客访问-----");
        }
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("-----开始定时调度-----");
                    if ("open".equals(proxyStatus)) {//代理开启，获取对象信息
                        logger.info("❤❤❤当前已打开代理的配置，检测代理情况❤❤❤");
                        Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPwd));
                        // 创建代理服务器
                        if (myProxyInfo == null) {
                            myProxyInfo = GetProxyInfo.getProxyInfoFromZhiMa(proxyUrl, proxyParam);
                        } else if (myProxyInfo != null && myProxyInfo.getExpireTime() != null && myProxyInfo.getExpireTime() > new Date().getTime()) {
                            logger.info("未达到过期时间，之前代理还可以使用");
                        } else {
                            myProxyInfo = GetProxyInfo.getProxyInfoFromZhiMa(proxyUrl, proxyParam);
                            logger.info("已达到过期时间，创建新的代理");
                        }
                        logger.info("❤❤❤能正常获取代理信息，代理信息❤❤❤：");
                        logger.info("❤代理IP：" + myProxyInfo.getAddress());
                        logger.info("❤代理Port：" + myProxyInfo.getPort());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        logger.info("❤过期时间：" + dateFormat.format(myProxyInfo.getExpireTime()));
                        InetSocketAddress addr = new InetSocketAddress(myProxyInfo.getAddress(), myProxyInfo.getPort());
                        proxy = new Proxy(proxyType, addr);
                    }
                    //执行主逻辑
                    for (String oneUser : users) {
                        accessBlog(oneUser);
                    }

                } catch (Exception e) {
                    logger.error("×××××定时调度出问题×××××");
                    logger.error("e：", e);
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
}
