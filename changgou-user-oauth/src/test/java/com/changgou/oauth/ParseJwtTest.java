package com.changgou.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.CLGW07KxCcMqnWbqVG-swzce9_DIoPNVLdEs_7WRQdORwtb0YeiM6yqIV_UM-YAigNkfFbJdv2ji3IBf1Fb40MwnawCWtWY7DVGUZRygltzdyhf2TNQ64oVreExKM1oTMchMugQ3k637ujV12vUyK5pLL4K-wxGiLFxYoZ2NU4BslDQWf_vrVDX3wrNEbyj52OY1xP6A79HY8GGsPQzoWnlLFaP5HCpBxtCFcTVp9IyTO3g8NtXEIAZHkrZMG3-lIJZga6kSf8moEcCyYDwX59OqosBPz4D_t0jkBFsTWRW5gCKWcsxivj3xTymWWnEPZCyGtQGz5RoN-UwR1yPXUA";
        //公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArtzit2lYR9/WjDkKkXxGQgdFUoOpRjNXAe3wO5sHVA6KdEi7k8Ywmtno2DmgRnenraEJZqmRyqP/YDV9toSE5fuPGrq+84qw48YiBNo4wmW338SxIx30EXEUomEdLeZWsd2Sp/v7L8YBoaoTvMwbkULEztYmtZMl2JZfO/wKxQwLV55gMb4MxtFrz1mWyNnI1xBQJA+tHyCpWNBNOJ92Z0Zou9hIt1Ux5tLlbAo2SoviArQnOssvgBEo5g2gwhK8QeMiHI1SrLlMnRnPXCWh+BD9hiyTD4LnLzahHGXtTuzodG5rKb8MM5f4rUozg/tvK1HNXj01Jchs8PkO+S0W/wIDAQAB-----END PUBLIC KEY-----";
        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        //获取Jwt原始内容 载荷
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
