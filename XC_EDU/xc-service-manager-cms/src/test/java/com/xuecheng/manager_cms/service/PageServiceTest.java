package com.xuecheng.manager_cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    private PageService pageService;

    @Test
    public void getPageHtmlTest(){

        String pageHtml = pageService.getPageHtml("5ded0badb35b8724c0be5cf6");
        System.out.println(pageHtml);
    }
//    //获取数据模型测试
//    @Test
//    public void getDataModelTest(){
//        Map map = pageService.getModelByPageId("5ded0badb35b8724c0be5cf6");
//        System.out.println(map);
//    }


//    //获取模板测试
//    @Test
//    public void getTemplateTest(){
//
//        String pageHtml = pageService.getTemplateByPageId("5ded0badb35b8724c0be5cf6");
//        System.out.println(pageHtml);
//    }

}
