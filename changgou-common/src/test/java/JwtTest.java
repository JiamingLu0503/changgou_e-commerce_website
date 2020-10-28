import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    /****
     * 创建Jwt令牌
     */
    @Test
    public void testCreateJwt() {
        JwtBuilder builder = Jwts.builder()
                .setId("888")             //设置唯一编号
                .setSubject("小白")       //设置主题  可以是JSON数据
                .setIssuer("ITHEIMA")     //设置颁发者
                .setIssuedAt(new Date())  //设置签发日期
                .setExpiration(new Date(System.currentTimeMillis() + 60000))  //设置过期日期
                .signWith(SignatureAlgorithm.HS256, "itcast");//设置签名 使用HS256算法，并设置密钥(盐)

        //自定义载荷信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("address", "Royal Dr");
        userInfo.put("phone", "882211");
        userInfo.put("salary", 12000);
        builder.addClaims(userInfo);

        //构建 并返回一个字符串
        System.out.println(builder.compact());
    }

    /***
     * 解析Jwt令牌数据
     */
    @Test
    public void testParseJwt() {
        String compactJwt = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpc3MiOiJJVEhFSU1BIiwiaWF0IjoxNjAyNTY4NDY3LCJleHAiOjE2MDI1Njg1MjcsImFkZHJlc3MiOiJSb3lhbCBEciIsInBob25lIjoiODgyMjExIiwic2FsYXJ5IjoxMjAwMH0.jb9MUSSpQU95UtdMMcvikbzf1TuTlgrgZfc3b-aF7sY";
        Claims claims = Jwts.parser()
                .setSigningKey("itcast")   //密钥(盐
                .parseClaimsJws(compactJwt)
                .getBody();
        System.out.println(claims);
    }


}
