package com.xuecheng.manager_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.xuecheng.framework.domain.cms")
@ComponentScan("com.xuecheng.manager_cms.service")
@ComponentScan("com.xuecheng.manager_cms.dao")
@ComponentScan("com.xuecheng.framework")//扫描common
public class ManagerCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerCmsApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
