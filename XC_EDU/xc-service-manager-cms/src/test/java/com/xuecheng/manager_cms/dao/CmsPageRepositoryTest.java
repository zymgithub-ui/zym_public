package com.xuecheng.manager_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Test
    public void testFindAll(){
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //页面别名模糊查询,自定义字符串的匹配器实现模糊查询
//        ExampleMatcher.GenericPropertyMatchers.contains()  包含
//        ExampleMatcher.GenericPropertyMatchers.startsWith() 开头
        CmsPage cmsPage=new CmsPage();
        //站点id
        cmsPage.setSiteId("5a754adf6abb500ad05688d9");
        //模板id
        cmsPage.setTemplateId("5a962b52b00ffc514038faf7");
        Example<CmsPage> example=Example.of(cmsPage,exampleMatcher);
        Pageable pageable=new PageRequest(0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all);


    }

    @Test
    public void testPageQuery(){
        Pageable pageable = PageRequest.of(1, 10);
        Page<CmsPage> pages = cmsPageRepository.findAll(pageable);
        for (CmsPage page : pages) {
            System.out.println(page);
        }
    }

    //更新操作
    @Test
    public void testUpdate(){
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById("5a754adf6abb500ad05688d9");
        if(optionalCmsPage.isPresent()){
            CmsPage cmsPage = optionalCmsPage.get();
            System.out.println(cmsPage);
            cmsPage.setPageAliase("test001");
            cmsPageRepository.save(cmsPage);
        }
    }
    //自定义查询
    @Test
    public void testQueryByName(){
        CmsPage cmsPage = cmsPageRepository.findByPageNameOrPageAliase("","首页");
        System.out.println(cmsPage);
    }
}
