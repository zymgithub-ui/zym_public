package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublicResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.response.CourseView;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.Response;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.context.annotation.ApplicationScope;

@Api(value = "课程管理接口", description = "课程管理接口的增删改查")
public interface CourseControlllerApi {
    @ApiOperation("课程列表")
    TeachplanNode findTeachPlanList(String courseId);
    @ApiOperation("添加课程计划")
    ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    QueryResponseResult findCourseList(int currentPage, int pageSize,CourseListRequest courseListRequest);

    @ApiOperation("根据id获取课程信息")
    CourseBase getCourseBaseById(String courseId);

    @ApiOperation("更新课程信息")
    ResponseResult updateCourseBase(CourseBase courseBase);

    @ApiOperation("更新课程信息")
    ResponseResult updateCourseMarket(CourseMarket courseMarket);

    @ApiOperation("课程营销")
    CourseMarket getCourseMarket(String courseId);

    @ApiOperation("添加图片到mysql")
    ResponseResult addPic(CoursePic coursePic);

    @ApiOperation("查找课程图片")
    CoursePic findCoursePicList(String courseId);

    @ApiOperation("删除课程图片")
    ResponseResult delCoursePicList(String courseId);


    @ApiOperation("课程视图查询")
    CourseView courseview(String id);

    //预览页面
    @ApiOperation("课程预览")
    CoursePreviewResult previewPage(String id);

    //预览课程
    @ApiOperation("课程发布")
    CoursePublishResult publish(String id);

    @ApiOperation("保存课程计划与妹子文件关联")
    ResponseResult saveMedia(TeachplanMedia teachplanMedia);

}
