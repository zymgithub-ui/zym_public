package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    //页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
    QueryResponseResult findList(int currentPage, int pageSize, QueryPageRequest queryPageRequest);
    //新增页面
    @ApiOperation("新增页面")
    CmsPageResult add(CmsPage cmsPage);
    //编辑时的回显,根据id得到数据
    @ApiOperation("根据id后去CmsPage")
    CmsPage findById(String id);
    //编辑时的保存
    @ApiOperation("编辑")
    CmsPageResult edit(String id,CmsPage cmsPage);
    //删除
    @ApiOperation("删除")
    ResponseResult del(String id);
    //页面发布接口
    @ApiOperation("页面发布")
    ResponseResult post(String pageId);
    //保存或者更新
    @ApiOperation("保存+更新页面")
    CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("一键发布")
    CmsPostPageResult postPageQuick(CmsPage cmsPage);




}
