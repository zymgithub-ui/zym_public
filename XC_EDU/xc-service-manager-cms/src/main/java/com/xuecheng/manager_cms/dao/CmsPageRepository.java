package com.xuecheng.manager_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面名称查询
CmsPage findByPageNameOrPageAliase(String pageName,String PageAliase);
//根据页面名称,站点id,页面pageWebPath查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
