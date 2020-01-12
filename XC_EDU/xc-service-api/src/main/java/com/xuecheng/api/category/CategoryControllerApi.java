package com.xuecheng.api.category;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "课程分类的相关信息")
public interface CategoryControllerApi {
    @ApiOperation(value = "获取课程分类列表")
    List<CategoryNode> findCategoryList();



}
