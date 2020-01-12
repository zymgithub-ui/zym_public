package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search/course")
public class EscourseController implements EsCourseControllerApi {
    @Autowired
    private EsCourseService esCourseService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return esCourseService.list(page, size, courseSearchParam);
    }

    @Override
    @GetMapping("/getAll/{id}")
    public Map<String, CoursePub> getAll(@PathVariable("id") String id) {
        return esCourseService.getAll(id);
    }

    @Override
    @GetMapping("/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        String[] teachplanIds = new String[]{teachplanId};
        QueryResponseResult<TeachplanMediaPub> queryResponseResult = esCourseService.getmedia(teachplanIds);
        QueryResult<TeachplanMediaPub> queryResult = queryResponseResult.getQueryResult();
        if (queryResult!=null) {
            List<TeachplanMediaPub> list = queryResult.getList();
            if(list!=null&&list.size()>0){
                return list.get(0);
            }
        }
        return new TeachplanMediaPub();
    }
}
