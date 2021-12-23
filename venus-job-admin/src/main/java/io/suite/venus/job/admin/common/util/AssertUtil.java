package io.suite.venus.job.admin.common.util;

public class AssertUtil {


    public static void isTrue(boolean ok, String msg) {
        if (!ok) {
            throw new RuntimeException(msg);
        }
    }

    public static void isFalse(boolean ok, String msg) {
        if (ok) {
            throw new RuntimeException(msg);
        }
    }


    public static void isNotNull(boolean ok, String msg) {
        if (!ok) {
            throw new RuntimeException(msg);
        }
    }

}
