package com.changgou.oauth;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.core.MultivaluedMap;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:42
 * @Description: com.changgou.token
 *      创建JWT令牌，使用私钥加密
 ****/
public class CreateJwtTest {

    /***
     * 创建令牌测试
     */
    @Test
    public void testCreateToken(){
        //证书文件路径
        String key_location="changgou96.jks";
        //秘钥库密码
        String key_password="changgou96";
        //秘钥密码
        String keypwd = "changgou96";
        //秘钥别名
        String alias = "changgou96";

        //加载证书，read file from classpath
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建秘钥工厂，读取证书数据
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,key_password.toCharArray());

        //读取秘钥对(公钥、私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypwd.toCharArray());

        //获取私钥 -> RSA算法
        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();

        //自定义Payload
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("name", "itheima");
        tokenMap.put("authorities", new String[]{"admin","oauth"});

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate)); //载荷与盐

        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

//    @Test
//    public void test() {
//        MultiValueMap<String,Object> body = new LinkedMultiValueMap();
//        body.add("robod",1);
//        body.add("robod",2);
//        System.out.println(body.toSingleValueMap());
//        Map hashMap = new HashMap();
//        hashMap.
//    }
}