package com.xuecheng.manage_course.dao;


import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeachplanMapper{
    //课程计划查询
    TeachplanNode selectList(String courseId);
}
