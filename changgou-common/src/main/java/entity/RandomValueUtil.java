package entity;

public class RandomValueUtil {
    public static String base;
    private static String firstName;
    private static String girl;
    public static String boy;
    public static final String[] email_suffix;
    private static String[] telFirst;
    public static String name_sex;

    public static int getNum(final int start, final int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String getEmail(final int lMin, final int lMax) {
        final int length = getNum(lMin, lMax);
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            final int number = (int) (Math.random() * RandomValueUtil.base.length());
            sb.append(RandomValueUtil.base.charAt(number));
        }
        sb.append(RandomValueUtil.email_suffix[(int) (Math.random() * RandomValueUtil.email_suffix.length)]);
        return sb.toString();
    }

    public static String getTelephone() {
        final int index = getNum(0, RandomValueUtil.telFirst.length - 1);
        final String first = RandomValueUtil.telFirst[index];
        final String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        final String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    public static String getLandline() {
        final int index = getNum(0, RandomValueUtil.telFirst.length - 1);
        final String first = RandomValueUtil.telFirst[index];
        final String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        final String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    public static String getChineseName() {
        int index = getNum(0, RandomValueUtil.firstName.length() - 1);
        final String first = RandomValueUtil.firstName.substring(index, index + 1);
        final int sex = getNum(0, 1);
        String str = RandomValueUtil.boy;
        int length = RandomValueUtil.boy.length();
        if (sex == 0) {
            str = RandomValueUtil.girl;
            length = RandomValueUtil.girl.length();
            RandomValueUtil.name_sex = "\u5973";
        } else {
            RandomValueUtil.name_sex = "\u7537";
        }
        index = getNum(0, length - 1);
        final String second = str.substring(index, index + 1);
        final int hasThird = getNum(0, 1);
        String third = "";
        if (hasThird == 1) {
            index = getNum(0, length - 1);
            third = str.substring(index, index + 1);
        }
        return first + second + third;
    }

    static {
        RandomValueUtil.base = "abcdefghijklmnopqrstuvwxyz0123456789";
        RandomValueUtil.firstName = "\u8d75\u94b1\u5b59\u674e\u5468\u5434\u90d1\u738b\u51af\u9648\u891a\u536b\u848b" +
                "\u6c88\u97e9\u6768\u6731\u79e6\u5c24\u8bb8\u4f55\u5415\u65bd\u5f20\u5b54\u66f9\u4e25\u534e\u91d1" +
                "\u9b4f\u9676\u59dc\u621a\u8c22\u90b9\u55bb\u67cf\u6c34\u7aa6\u7ae0\u4e91\u82cf\u6f58\u845b\u595a" +
                "\u8303\u5f6d\u90ce\u9c81\u97e6\u660c\u9a6c\u82d7\u51e4\u82b1\u65b9\u4fde\u4efb\u8881\u67f3\u9146" +
                "\u9c8d\u53f2\u5510\u8d39\u5ec9\u5c91\u859b\u96f7\u8d3a\u502a\u6c64\u6ed5\u6bb7\u7f57\u6bd5\u90dd" +
                "\u90ac\u5b89\u5e38\u4e50\u4e8e\u65f6\u5085\u76ae\u535e\u9f50\u5eb7\u4f0d\u4f59\u5143\u535c\u987e" +
                "\u5b5f\u5e73\u9ec4\u548c\u7a46\u8427\u5c39\u59da\u90b5\u6e5b\u6c6a\u7941\u6bdb\u79b9\u72c4\u7c73" +
                "\u8d1d\u660e\u81e7\u8ba1\u4f0f\u6210\u6234\u8c08\u5b8b\u8305\u5e9e\u718a\u7eaa\u8212\u5c48\u9879" +
                "\u795d\u8463\u6881\u675c\u962e\u84dd\u95f5\u5e2d\u5b63\u9ebb\u5f3a\u8d3e\u8def\u5a04\u5371\u6c5f" +
                "\u7ae5\u989c\u90ed\u6885\u76db\u6797\u5201\u949f\u5f90\u90b1\u9a86\u9ad8\u590f\u8521\u7530\u6a0a" +
                "\u80e1\u51cc\u970d\u865e\u4e07\u652f\u67ef\u548e\u7ba1\u5362\u83ab\u7ecf\u623f\u88d8\u7f2a\u5e72" +
                "\u89e3\u5e94\u5b97\u5ba3\u4e01\u8d32\u9093\u90c1\u5355\u676d\u6d2a\u5305\u8bf8\u5de6\u77f3\u5d14" +
                "\u5409\u94ae\u9f9a\u7a0b\u5d47\u90a2\u6ed1\u88f4\u9646\u8363\u7fc1\u8340\u7f8a\u65bc\u60e0\u7504" +
                "\u9b4f\u52a0\u5c01\u82ae\u7fbf\u50a8\u9773\u6c72\u90b4\u7cdc\u677e\u4e95\u6bb5\u5bcc\u5deb\u4e4c" +
                "\u7126\u5df4\u5f13\u7267\u9697\u5c71\u8c37\u8f66\u4faf\u5b93\u84ec\u5168\u90d7\u73ed\u4ef0\u79cb" +
                "\u4ef2\u4f0a\u5bab\u5b81\u4ec7\u683e\u66b4\u7518\u94ad\u5389\u620e\u7956\u6b66\u7b26\u5218\u59dc" +
                "\u8a79\u675f\u9f99\u53f6\u5e78\u53f8\u97f6\u90dc\u9ece\u84df\u8584\u5370\u5bbf\u767d\u6000\u84b2" +
                "\u53f0\u4ece\u9102\u7d22\u54b8\u7c4d\u8d56\u5353\u853a\u5c60\u8499\u6c60\u4e54\u9634\u90c1\u80e5" +
                "\u80fd\u82cd\u53cc\u95fb\u8398\u515a\u7fdf\u8c2d\u8d21\u52b3\u9004\u59ec\u7533\u6276\u5835\u5189" +
                "\u5bb0\u90e6\u96cd\u5374\u74a9\u6851\u6842\u6fee\u725b\u5bff\u901a\u8fb9\u6248\u71d5\u5180\u90cf" +
                "\u6d66\u5c1a\u519c\u6e29\u522b\u5e84\u664f\u67f4\u77bf\u960e\u5145\u6155\u8fde\u8339\u4e60\u5ba6" +
                "\u827e\u9c7c\u5bb9\u5411\u53e4\u6613\u614e\u6208\u5ed6\u5e9a\u7ec8\u66a8\u5c45\u8861\u6b65\u90fd" +
                "\u803f\u6ee1\u5f18\u5321\u56fd\u6587\u5bc7\u5e7f\u7984\u9619\u4e1c\u6bb4\u6bb3\u6c83\u5229\u851a" +
                "\u8d8a\u5914\u9686\u5e08\u5de9\u538d\u8042\u6641\u52fe\u6556\u878d\u51b7\u8a3e\u8f9b\u961a\u90a3" +
                "\u7b80\u9976\u7a7a\u66fe\u6bcb\u6c99\u4e5c\u517b\u97a0\u987b\u4e30\u5de2\u5173\u84af\u76f8\u67e5" +
                "\u540e\u6c5f\u7ea2\u6e38\u7afa\u6743\u902f\u76d6\u76ca\u6853\u516c\u4e07\u4fdf\u53f8\u9a6c\u4e0a" +
                "\u5b98\u6b27\u9633\u590f\u4faf\u8bf8\u845b\u95fb\u4eba\u4e1c\u65b9\u8d6b\u8fde\u7687\u752b\u5c09" +
                "\u8fdf\u516c\u7f8a\u6fb9\u53f0\u516c\u51b6\u5b97\u653f\u6fee\u9633\u6df3\u4e8e\u4ef2\u5b59\u592a" +
                "\u53d4\u7533\u5c60\u516c\u5b59\u4e50\u6b63\u8f69\u8f95\u4ee4\u72d0\u949f\u79bb\u95fe\u4e18\u957f" +
                "\u5b59\u6155\u5bb9\u9c9c\u4e8e\u5b87\u6587\u53f8\u5f92\u53f8\u7a7a\u4e93\u5b98\u53f8\u5bc7\u4ec9" +
                "\u7763\u5b50\u8f66\u989b\u5b59\u7aef\u6728\u5deb\u9a6c\u516c\u897f\u6f06\u96d5\u4e50\u6b63\u58e4" +
                "\u9a77\u516c\u826f\u62d3\u62d4\u5939\u8c37\u5bb0\u7236\u8c37\u7cb1\u664b\u695a\u960e\u6cd5\u6c5d" +
                "\u9122\u6d82\u94a6\u6bb5\u5e72\u767e\u91cc\u4e1c\u90ed\u5357\u95e8\u547c\u5ef6\u5f52\u6d77\u7f8a" +
                "\u820c\u5fae\u751f\u5cb3\u5e05\u7f11\u4ea2\u51b5\u540e\u6709\u7434\u6881\u4e18\u5de6\u4e18\u4e1c" +
                "\u95e8\u897f\u95e8\u5546\u725f\u4f58\u4f74\u4f2f\u8d4f\u5357\u5bab\u58a8\u54c8\u8c2f\u7b2a\u5e74" +
                "\u7231\u9633\u4f5f\u7b2c\u4e94\u8a00\u798f\u767e\u5bb6\u59d3\u7eed";
        RandomValueUtil.girl = "\u79c0\u5a1f\u82f1\u534e\u6167\u5de7\u7f8e\u5a1c\u9759\u6dd1\u60e0\u73e0\u7fe0\u96c5" +
                "\u829d\u7389\u840d\u7ea2\u5a25\u73b2\u82ac\u82b3\u71d5\u5f69\u6625\u83ca\u5170\u51e4\u6d01\u6885" +
                "\u7433\u7d20\u4e91\u83b2\u771f\u73af\u96ea\u8363\u7231\u59b9\u971e\u9999\u6708\u83ba\u5a9b\u8273" +
                "\u745e\u51e1\u4f73\u5609\u743c\u52e4\u73cd\u8d1e\u8389\u6842\u5a23\u53f6\u74a7\u7490\u5a05\u7426" +
                "\u6676\u598d\u831c\u79cb\u73ca\u838e\u9526\u9edb\u9752\u5029\u5a77\u59e3\u5a49\u5a34\u747e\u9896" +
                "\u9732\u7476\u6021\u5a75\u96c1\u84d3\u7ea8\u4eea\u8377\u4e39\u84c9\u7709\u541b\u7434\u854a\u8587" +
                "\u83c1\u68a6\u5c9a\u82d1\u5a55\u99a8\u7457\u7430\u97f5\u878d\u56ed\u827a\u548f\u537f\u806a\u6f9c" +
                "\u7eaf\u6bd3\u60a6\u662d\u51b0\u723d\u742c\u8317\u7fbd\u5e0c\u5b81\u6b23\u98d8\u80b2\u6ee2\u99a5" +
                "\u7b60\u67d4\u7af9\u972d\u51dd\u6653\u6b22\u9704\u67ab\u82b8\u83f2\u5bd2\u4f0a\u4e9a\u5b9c\u53ef" +
                "\u59ec\u8212\u5f71\u8354\u679d\u601d\u4e3d ";
        RandomValueUtil.boy = "\u4f1f\u521a\u52c7\u6bc5\u4fca\u5cf0\u5f3a\u519b\u5e73\u4fdd\u4e1c\u6587\u8f89\u529b" +
                "\u660e\u6c38\u5065\u4e16\u5e7f\u5fd7\u4e49\u5174\u826f\u6d77\u5c71\u4ec1\u6ce2\u5b81\u8d35\u798f" +
                "\u751f\u9f99\u5143\u5168\u56fd\u80dc\u5b66\u7965\u624d\u53d1\u6b66\u65b0\u5229\u6e05\u98de\u5f6c" +
                "\u5bcc\u987a\u4fe1\u5b50\u6770\u6d9b\u660c\u6210\u5eb7\u661f\u5149\u5929\u8fbe\u5b89\u5ca9\u4e2d" +
                "\u8302\u8fdb\u6797\u6709\u575a\u548c\u5f6a\u535a\u8bda\u5148\u656c\u9707\u632f\u58ee\u4f1a\u601d" +
                "\u7fa4\u8c6a\u5fc3\u90a6\u627f\u4e50\u7ecd\u529f\u677e\u5584\u539a\u5e86\u78ca\u6c11\u53cb\u88d5" +
                "\u6cb3\u54f2\u6c5f\u8d85\u6d69\u4eae\u653f\u8c26\u4ea8\u5947\u56fa\u4e4b\u8f6e\u7ff0\u6717\u4f2f" +
                "\u5b8f\u8a00\u82e5\u9e23\u670b\u658c\u6881\u680b\u7ef4\u542f\u514b\u4f26\u7fd4\u65ed\u9e4f\u6cfd" +
                "\u6668\u8fb0\u58eb\u4ee5\u5efa\u5bb6\u81f4\u6811\u708e\u5fb7\u884c\u65f6\u6cf0\u76db\u96c4\u741b" +
                "\u94a7\u51a0\u7b56\u817e\u6960\u6995\u98ce\u822a\u5f18";
        email_suffix = ("@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net," +
                "@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo" +
                ".com.cn").split(",");
        RandomValueUtil.telFirst =
                "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        RandomValueUtil.name_sex = "";
    }
}