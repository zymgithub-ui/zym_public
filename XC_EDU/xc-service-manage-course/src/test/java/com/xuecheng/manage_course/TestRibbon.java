package com.xuecheng.manage_course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired
    CmsPageClient cmsPageClient;
    @Autowired
    RestTemplate restTemplate;
    @Test
    public void testRibbon(){
        //服务名
        String serviceId="XC-SERVICE-MANAGE-CMS";
        for (int i = 0; i < 10; i++) {
            ResponseEntity<CmsPage> forEntity = restTemplate.getForEntity("http://" + serviceId + "/cms/page/get/5ded0badb35b8724c0be5cf6", CmsPage.class);
            CmsPage body = forEntity.getBody();
            System.out.println(body);
        }
    }
    @Test
    public void testFeign(){
        CmsPage cmsPage = cmsPageClient.findCmsPageById("5ded0badb35b8724c0be5cf6");
        System.out.println(cmsPage);

    }
}

