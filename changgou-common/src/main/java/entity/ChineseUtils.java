package entity;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class ChineseUtils {
    private static Random random;

    private static Random getRandomInstance() {
        if (ChineseUtils.random == null) {
            ChineseUtils.random = new Random(System.currentTimeMillis());
        }
        return ChineseUtils.random;
    }

    public static String getChinese() {
        String str = null;
        final Random random = getRandomInstance();
        final int highPos = 176 + Math.abs(random.nextInt(39));
        final int lowPos = 161 + Math.abs(random.nextInt(93));
        final byte[] b = {new Integer(highPos).byteValue(), new Integer(lowPos).byteValue()};
        try {
            str = new String(b, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getFixedLengthChinese(final int length) {
        String str = "";
        for (int i = length; i > 0; --i) {
            str += getChinese();
        }
        return str;
    }

    public static String getRandomLengthChiness(final int start, final int end) {
        String str = "";
        final int length = new Random().nextInt(end + 1);
        if (length < start) {
            str = getRandomLengthChiness(start, end);
        } else {
            for (int i = 0; i < length; ++i) {
                str += getChinese();
            }
        }
        return str;
    }

    public static void main(final String[] args) {
        System.out.println(getChinese());
        System.out.println(getFixedLengthChinese(20));
        System.out.println(getRandomLengthChiness(2, 5));
    }

    static {
        ChineseUtils.random = null;
    }
}
