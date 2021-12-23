package io.suite.venus.job.admin.core.util;

import java.util.Random;

public class RandomUtil {

    public  static String getRandomCode() {
        Random r = new Random(System.currentTimeMillis());
        return String.format("%d%d%d%d", r.nextInt(100) % 9,
                r.nextInt(100) % 9,
                r.nextInt(100) % 9,
                r.nextInt(100) % 9, r.nextInt(100) % 9);

    }


}
