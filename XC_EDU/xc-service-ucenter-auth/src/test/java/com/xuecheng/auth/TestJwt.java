package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJwt {

    @Test
    public void testCreateJwt(){
        //密钥库文件
        String keystore="xc.keystore";
        //密钥库密码
        String keystore_password="xuechengkeystore";
        //密钥库文件路径
        //密钥别名
        String alias="xckey";
        //秘钥访问密码
        String key_password="xuecheng";
        //秘钥工厂
        ClassPathResource classPathResource = new ClassPathResource(keystore);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keystore_password.toCharArray());
        //秘钥对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());

        //获取私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey)keyPair.getPrivate();
        //jwt令牌的内容
        Map<String,String> body = new HashMap<>();
        body.put("name","itcast");
        String bodyString = JSON.toJSONString(body);
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(aPrivate));
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
    @Test
    public void testVerify(){
        String publickey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAns0yoWluNtnPa8zjSjFAE0yXOe1VWOXjIjRRPa8mNgsH5myOc8ye55FDQd9neuIKd+zrw1ILLcOUPfKb3jRWOz/6hAZbMPMM+4UxKT0H/lAanOtocSfk2e+yabEOSyDNigV7D+38qVYJu0hwTkM1HCoC6etB8qdU/KGwQKFvmkfl6Qfa4MG82skveQC6RlyymDgleP6aGpphc4Rqs3EkmOFx0DohoLCwqZkz8GcYRZw0MSPfBP2GIKD/lm8HymU3alITmZRQYZrwZXv9E2r6Z8M6VtmvGDxTQqb338J/u7EE+/sH9FqZ4+jGYUjQgWLhkuomYIzEYqCVpAcnsPZ2zQIDAQAB-----END PUBLIC KEY-----";
        String jwtString="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6InFxcSIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjoicXFxIiwidXR5cGUiOiIxMDEwMDEiLCJpZCI6IjUzIiwiZXhwIjoxNTc4MDY0NzIzLCJqdGkiOiJkMWU1YjAxNi00YWEzLTRlZmYtODNmNi0wMjFhZjcwNzcyMjkiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.iPC1OnBdy4c9o3WveSsLIQSoY2ma35ayPVi4sD74jbl7BuAYKLO7aeCstiwUKrYoEp6K9KxaGec_UXEWcIDOdOeUwDLvVnPuu4-U6uTEUhp2B2GuFOT8w5F52FryK1dbC8SHhjiNjXzdZZ3Da1U7AArg9ineYck6r_rhf_tJSH8vicSjhU8IfwAE8I8kvwb4Dqy-1jAH8p3O1zUjmXTeZ2oFR_lJ-WNbE8r9gnZDojpQey2AX7uAIx3PHH6bqdIv6MVKHc5CE3Rz24eX8k14ezpH4oio7376GE5R_g9jaZRsR0yfkDZ4lhV30MnFhkRomeHrEqVHdjsXSDtdD5irQQ";
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        String claims = jwt.getClaims();
        System.out.println(claims);


    }
}
