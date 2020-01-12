package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //创建jwt令牌
    @Test
    public void testRedis(){
        //定义key
        String key = "user_token:f493f98d-f64c-4fe4-9cfa-ae1033e2bbcb";
        //定义value
        Map<String,String> value = new HashMap<>();
        value.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3NzcxMDU0MCwianRpIjoiZjQ5M2Y5OGQtZjY0Yy00ZmU0LTljZmEtYWUxMDMzZTJiYmNiIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.g17gZqs0J4Bc8RPBQWfCBskWbdpjUxgyGGpJkfoBYqhXCO5DWgLQkk4gHupc_-m7mcQl-648Q4D_rfoXtj6RIrXz9Dnp6VHMoXIwtPQrRl1OYrbRelYssay_0Ey3IeJz80VoCdxfWgJsiIdU3F3VxMzGeBm1kRsHT9qZ1tOYw4ECgBnVdoMWiuAcpnLbXDKRHaW-DkAeLEWzkmmjKCxnxPliICZeXOyNXlg1YdpWjBPUKs9_C-PicK4SDDy0V92wHA-4FNU2X8kPd-bTGGCZ3rTHBBeiiqIeH_qEt6DwoPEk9fOVv1MG61fo-Jyfw4LCm6oaElLeicWj1UyfpX0vKg");
        value.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiJmNDkzZjk4ZC1mNjRjLTRmZTQtOWNmYS1hZTEwMzNlMmJiY2IiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3NzcxMDU0MCwianRpIjoiMTQ1ZjRmMzItMzhkMS00NWQ4LTg4ZDQtOWZjYzAxOGVlZTQyIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.khGCCnfAMWKjThRlhVCdHFgD6nPbEBulVy8Gdk4XAcqGz--4LuCx4z_No8qzwgj2d8OpvmQv0GDYwXLJFIFSg8yBRTlJ-yGXcjdKDKUn9sMNyq1h_B2ZqrIdC5qUEFKikZvzRhgd3bNrxdL-iFf8ZBBNNZryQI4pFOUNfy6ULVNEJhcAiviBUOdaSL4VKNCjf_LgYwZxJbBA54gbaKFks6TxnalLNihzQpu2JqtZO8nW--vZUImHvMg-L7TL65-edmH3r9EOFbqQwO4h5rdjXofs6zLHGq1TltC9gzqC1Mi5RSnlDufo5sKCfGUegzmAin3kPiuuMdcfDNsjXLp2KQ");
        String jsonString = JSON.toJSONString(value);
        //校验key是否存在，如果不存在则返回-2
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(expire);
        //存储数据
        stringRedisTemplate.boundValueOps(key).set(jsonString,30, TimeUnit.SECONDS);
        //获取数据
        String string = stringRedisTemplate.opsForValue().get(key);
        System.out.println(string);
    }
}
