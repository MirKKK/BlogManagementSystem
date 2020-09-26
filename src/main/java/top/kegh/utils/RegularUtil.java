package top.kegh.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author By--MirKKK
 * @time 2020/9/12$ 17:17$
 * @Version: 1.0
 * @QQ 2641195399
 * @Description Java类作用描述
 */
public class RegularUtil {
    // 正则匹配
    public static List<String> getMatherSubStrs(String str, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}
