package com.changgou.oauth.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class AdminToken {

    /**
     * 发放管理员令牌
     * @return
     */
    public static String getAdminToken() {
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
        tokenMap.put("name", "changgouAdmin");
        tokenMap.put("authorities", new String[]{"admin","oauth"});

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate)); //载荷与盐

        //取出令牌
        String encoded = jwt.getEncoded();
        return encoded;
    }
}
