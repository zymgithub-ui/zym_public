package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeachplanRepository extends JpaRepository<Teachplan,String>{
//    @Select("select * from teachplan a where a.courseid=#{courseId} and a.parentid='0'")
    List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}
