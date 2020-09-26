package top.kegh.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
import top.kegh.utils.HttpUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author By--MirKKK
 * @time 2020/9/12$ 20:08$
 * @Version: 1.0
 * @QQ 2641195399
 * @Description Java类作用描述
 */
public class GetProxyInfo {

    /**
     * @author By--MirKKK
     * @time 2020/9/12 20:12
     * @Version: 1.0
     * @QQ 2641195399
     * @Params * @Param null:
     * @Return * @return: null
     * @Description 我是在网上找的 芝麻http代理，地址：http://h.zhimaruanjian.com/getapi/
     * 不同的代理商接口有差异，但是应该差别不大，不同代理商，根据我这个模仿一个即可
     * 如果嫌弃代理麻烦在配置文件中关闭代理即可
     **/
    public static List<MyProxyInfo> listProxyInfoFromZhiMa(String url, String param) throws IOException {
        List<MyProxyInfo> list = new ArrayList<>();
        String path = url + param;
        String json = HttpUtil.doGet(path, null);
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (CollectionUtils.isEmpty(jsonArray)) {
            System.out.println("接口存在问题");
        } else {
            for (Object object : jsonArray) {
                JSONObject ipDetail = (JSONObject) object;
                String ip = ipDetail.getString("ip");
                Integer port = ipDetail.getInteger("port");
                MyProxyInfo myProxyInfo = new MyProxyInfo();
                myProxyInfo.setAddress(ip);
                myProxyInfo.setPort(port);
                list.add(myProxyInfo);
            }
        }
        return list;
    }


    public static MyProxyInfo getProxyInfoFromZhiMa(String url, String param) throws Exception {
        MyProxyInfo myProxyInfo = new MyProxyInfo();

        String path = url + param;
        String json = HttpUtil.doGet(path, null);
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject.getString("msg").contains("您的余额不足")) {
            throw new Exception("今天免费的代理的余额不足");
        }
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (CollectionUtils.isEmpty(jsonArray)) {
            throw new Exception("接口存在问题,json" + JSON.toJSONString(jsonObject));
        } else {
            for (Object object : jsonArray) {
                JSONObject ipDetail = (JSONObject) object;
                String ip = ipDetail.getString("ip");
                String expireTime = ipDetail.getString("expire_time");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Integer port = ipDetail.getInteger("port");
                myProxyInfo.setAddress(ip);
                myProxyInfo.setPort(port);
                myProxyInfo.setExpireTime(dateFormat.parse(expireTime).getTime());
                return myProxyInfo;
            }
        }
        return myProxyInfo;
    }

}
