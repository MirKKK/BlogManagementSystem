package top.kegh.proxy;

/**
 * @author By--MirKKK
 * @time 2020/9/12$ 16:00$
 * @Version: 1.0
 * @QQ 2641195399
 * @Description Java类作用描述
 */
public class MyProxyInfo {

    private String address;

    private Integer port;

    private Long expireTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return address + ":" + port;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}