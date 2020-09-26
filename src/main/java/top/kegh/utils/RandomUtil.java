package top.kegh.utils;

import java.util.Random;

/**
 * @author By--MirKKK
 * @time 2020/9/13$ 18:11$
 * @Version: 1.0
 * @QQ 2641195399
 * @Description Java类作用描述
 */
public class RandomUtil {

    public static long randomSleepTime() {
        return 1000l * 60 * randomInt(10);
    }

    public static long randomSleepTime(int time) {
        return 1000l * 60 * randomInt(time);
    }

    public static int randomInt(int time) {
        Random r = new Random();
        int number = r.nextInt(time);
        return number;
    }
}
