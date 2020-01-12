package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControlllerApi;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.response.CourseView;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControlllerApi {
    @Autowired
    private CourseService courseService;

    @PreAuthorize("hasAuthority('xc_teachmanager_course_add')")
    @Override
    @GetMapping("/teachPlan/list/{courseId}")
    public TeachplanNode findTeachPlanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }
    @PreAuthorize("hasAuthority('xc_teachmanager_course_add')")
    @Override
    @PostMapping("/teachPlan/add")
    public ResponseResult addTeachPlan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/courseBase/list/{currentPage}/{pageSize}")
    public QueryResponseResult findCourseList(@PathVariable("currentPage") int currentPage,@PathVariable("pageSize") int pageSize,CourseListRequest courseListRequest) {
        //当前用户companyId
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        String companyId=userJwt.getCompanyId();

        return courseService.findCourseList(companyId,currentPage,pageSize,courseListRequest);
    }

    @Override
    @GetMapping("/courseBase/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) {

        return courseService.getCourseBaseById(courseId);
    }

    @Override
    @PostMapping("/updateCourseBase")
    public ResponseResult updateCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.updateCourseBase(courseBase);
    }

    @Override
    @PostMapping("/updateCourseMarket")
    public ResponseResult updateCourseMarket(@RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseMarket);
    }

    @Override
    @GetMapping("/courseMarket/{courseId}")
    public CourseMarket getCourseMarket(@PathVariable("courseId") String courseId) {

        return courseService.getCourseMarketById(courseId);
    }

    @Override
    @GetMapping("/coursePic/add")
    public ResponseResult addPic(CoursePic coursePic) {
        return courseService.addPic(coursePic);
    }

    @Override
    @GetMapping("/coursePic/list/{courseId}")
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePicList(courseId);
    }

    @Override
    @DeleteMapping("/coursePic/delete")
    public ResponseResult delCoursePicList(@RequestParam("courseId") String courseId) {

        return courseService.delCoursePicList(courseId);
    }

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id) {


        return courseService.getCourseView(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePreviewResult previewPage(@PathVariable("id") String id) {
        return courseService.previewPage(id);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id) {

        return courseService.publish(id);
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult saveMedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }
}
