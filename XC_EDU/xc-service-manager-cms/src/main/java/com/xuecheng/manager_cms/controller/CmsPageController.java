package com.xuecheng.manager_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manager_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;

    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int currentPage, @PathVariable("size") int pageSize, QueryPageRequest queryPageRequest) {
        return pageService.findList(currentPage, pageSize, queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return  pageService.add(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.findById(id);
    }

    @Override
    @PutMapping("/edit/{id}")   //put 表示更新
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.edit(id,cmsPage);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult del(@PathVariable("id") String id) {
        return pageService.delete(id);
    }

    @Override
    @PostMapping("postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }

    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return pageService.save(cmsPage);
    }

    @Override
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {

        return pageService.postPageQuick(cmsPage);
    }

}
