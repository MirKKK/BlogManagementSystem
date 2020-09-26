package top.kegh.auth;

import java.net.PasswordAuthentication;
import java.net.Authenticator;
/**
 * @author By--MirKKK
 * @time 2020/9/12$ 17:43$
 * @Version: 1.0
 * @QQ 2641195399
 * @Description Java类作用描述
 */
public class ProxyAuthenticator extends Authenticator {
    private String authUser, authPwd;

    public ProxyAuthenticator(String authUser, String authPwd) {
        this.authUser = authUser;
        this.authPwd = authPwd;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return (new PasswordAuthentication(authUser, authPwd.toCharArray()));
    }
}