package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.response.CourseView;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    //课程计划查询
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;


    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseId = teachplan.getCourseid();
        String parentId = teachplan.getParentid();
        //如果parentid为空,则将这个课程设置为  根节点
        //再取出其根节点
        if (StringUtils.isEmpty(parentId)) {
            parentId = this.getTeachplanRoot(courseId);
        }
        //----根节点 为新创建的一级 -----
        //接下来  创建一个新的子结点
        Teachplan teachplanNew = new Teachplan();
        //将页面填写的信息,拷贝到teachplanNew中
        Optional<Teachplan> optional = teachplanRepository.findById(parentId);
        Teachplan teachplan1 = optional.get();
        String parentGrade = teachplan1.getGrade();

        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setParentid(parentId);
        teachplanNew.setCourseid(courseId);
        //根据父结点的grade决定子结点的grade
        if (parentGrade.equals("1")) {
            teachplanNew.setGrade("2");
        } else {
            teachplanNew.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询添加的该课程是否有根节点
    private String getTeachplanRoot(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }


        CourseBase courseBase = optional.get();
        List<Teachplan> teachplans = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplans == null || teachplans.size() <= 0) {
            //查询不到时,将传过来的课程设置为根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
        }
        return teachplans.get(0).getId();
    }

    public QueryResponseResult<CourseInfo> findCourseList(String companyId,int currentPage, int pageSize, CourseListRequest courseListRequest) {
        if(courseListRequest==null){
            courseListRequest=new CourseListRequest();
        }
        courseListRequest.setCompanyId(companyId);
        PageHelper.startPage(currentPage, pageSize);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);


//        Long total = courseMapper.findCount();
//        QueryResult queryResult = new QueryResult(result, total);
        long total = courseListPage.getTotal();
        List<CourseInfo> result = courseListPage.getResult();
        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<>();
        courseInfoQueryResult.setTotal(total);
        courseInfoQueryResult.setList(result);
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, courseInfoQueryResult);
    }

    public ResponseResult addPic(CoursePic coursePic) {
        if (coursePic.getCourseid() != null && coursePic.getPic() != null) {
            try {
                CoursePic save = coursePicRepository.save(coursePic);
                return new ResponseResult(CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public CoursePic findCoursePicList(String courseId) {

        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            CoursePic coursePic = optional.get();
            return coursePic;
        }
        return null;
    }

    //课程视图,包括基本信息,图片,营销,课程计划
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();

        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            courseView.setCourseBase(courseBaseOptional.get());
        }
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()) {
            courseView.setCoursePic(picOptional.get());
        }
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if (marketOptional.isPresent()) {
            courseView.setCourseMarket(marketOptional.get());
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        if (teachplanNode != null) {
            courseView.setTeachplanNode(teachplanNode);
        }
        return courseView;
    }

    public CoursePreviewResult previewPage(String courseId) {
        //获取到课程的基本信息
        CourseBase courseBase = this.findCourseBaseById(courseId);

        //先进行保存页面
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);

        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePreviewResult(CommonCode.FAIL,null);
        }
        //准备拿出url
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        return new CoursePreviewResult(CommonCode.SUCCESS,previewUrl+pageId);
    }

    private CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOEAISTS);
        return null;
    }

    //删除图片
    @Transactional
    public ResponseResult delCoursePicList(String courseId) {
        //判断是否删除成功
        long rows=coursePicRepository.deleteByCourseid(courseId);
        if(rows>0){
        return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOEAISTS);
        return null;
    }

    @Transactional
    public ResponseResult updateCourseBase(CourseBase courseBase) {

        Optional<CourseBase> optional = courseBaseRepository.findById(courseBase.getId());

        if(optional.isPresent()){
            CourseBase base= optional.get();
            if(courseBase.getCompanyId()!=null){
                base.setCompanyId(courseBase.getCompanyId());
            }
            if(courseBase.getUserId()!=null){
                base.setUserId(courseBase.getUserId());
            }
            if(courseBase.getName()!=null){
                base.setName(courseBase.getName());
            }
            if(courseBase.getUsers()!=null){
                base.setUsers(courseBase.getUsers());
            }
            if(courseBase.getMt()!=null){
                base.setMt(courseBase.getMt());
            }
            if(courseBase.getSt()!=null){
                base.setSt(courseBase.getSt());
            }
            if(courseBase.getGrade()!=null){
                base.setGrade(courseBase.getGrade());
            }
            if(courseBase.getStudymodel()!=null){
                base.setStudymodel(courseBase.getStudymodel());
            }
            if(courseBase.getTeachmode()!=null){
                base.setTeachmode(courseBase.getTeachmode());
            }
            if(courseBase.getDescription()!=null){
                base.setDescription(courseBase.getDescription());
            }
            if(courseBase.getStatus()!=null){
                base.setStatus(courseBase.getStatus());
            }
            CourseBase save = courseBaseRepository.save(base);
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            courseBaseRepository.save(courseBase);
        }
        return new ResponseResult(CommonCode.FAIL);

    }

    public CourseMarket getCourseMarketById(String courseId) {

        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    public ResponseResult updateCourseMarket(CourseMarket courseMarket) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseMarket.getId());
        if(optional.isPresent()){
            CourseMarket market= optional.get();
            if(courseMarket.getCharge()!=null){
                market.setCharge(courseMarket.getCharge());
            }
            if(courseMarket.getEndTime()!=null){
                market.setEndTime(courseMarket.getEndTime());
            }
            if(courseMarket.getExpires()!=null){
                market.setEndTime(courseMarket.getExpires());
            }
            if(courseMarket.getPrice()!=null){
                market.setPrice(courseMarket.getPrice());
            }
            if(courseMarket.getPrice_old()!=null){
                market.setPrice_old(courseMarket.getPrice_old());
            }
            if(courseMarket.getValid()!=null){
                market.setValid(courseMarket.getValid());
            }
            if(courseMarket.getStartTime()!=null){
                market.setStartTime(courseMarket.getStartTime());
            }

            CourseMarket save = courseMarketRepository.save(market);
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            courseMarketRepository.save(courseMarket);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //课程发布
    @Transactional
    public CoursePublishResult publish(String id) {
        CourseBase courseBase = findCourseBaseById(id);
        //准备页面信息
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id + ".html");
        cmsPage.setDataUrl(publish_dataUrlPre + id);
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);

        //一键发布
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);

        //保存课程状态为已经发布
        if(!cmsPostPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CourseBase saveCoursePubState = this.saveCoursePubState(id);
        if(saveCoursePubState==null){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //创建课程索引对象
        //保存课程索引信息
        CoursePub coursePub = createCoursePub(id);
        CoursePub saveCoursePub = saveCoursePub(id, coursePub);
        if(saveCoursePub==null){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CREATE_INDEX_ERROR);
        }

        //缓存课程索引信息
        String pageUrl = cmsPostPageResult.getPageUrl();
        //向teachplanMediaPub中保存课程媒资信息
        saveTeachplanMediaPub(id);
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }
    private CoursePub saveCoursePub(String id,CoursePub coursePub){
        if(StringUtils.isEmpty(id)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePub coursePubNew=null;
        Optional<CoursePub> optional = coursePubRepository.findById(id);
        if(optional.isPresent()){
            coursePubNew= optional.get();
        }else{
            coursePubNew= new CoursePub();
        }
        //将传过来的coursePub属性给 数据库中的,
        BeanUtils.copyProperties(coursePub,coursePubNew);
        //设置主键
        coursePubNew.setId(id);
        //更新时间戳为最新时间
        coursePub.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePub.setPubTime(date);
        coursePubRepository.save(coursePub);
        return coursePubNew;

    }
    //为了方便将mysql数据采集到es,将几张表(course_base,course_pic)整合在一张表中(coursePub)
    private CoursePub createCoursePub(String courseId){
        CoursePub coursePub = new CoursePub();
        //根据courseId查询course_base,课程基本信息
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if(baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }
        //根据courseId查询course_pic,课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }
        //根据courseId查询course_market,课程营销
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        if(marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        //课程计划,以json串的形式
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        String jsonString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(jsonString);
        return coursePub;

    }

    //更改课程状态
    private CourseBase saveCoursePubState(String courseId){
        CourseBase courseBase = this.findCourseBaseById(courseId);
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }

    //保存媒资信息
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if(teachplanMedia == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();

        //查询课程计划
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_ISNULL);
        }
        Teachplan teachplan = optional.get();
        //只允许为叶子结点课程计划选择视频
        String grade = teachplan.getGrade();
        if(StringUtils.isEmpty(grade) || !grade.equals("3")){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        TeachplanMedia one = null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        if(!teachplanMediaOptional.isPresent()){
            one = new TeachplanMedia();
        }else{
            one = teachplanMediaOptional.get();
        }
        //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //向teachplanpub中保存课程媒资信息
    private void saveTeachplanMediaPub(String courseId){
        //先删后加
        long deleteCount = teachplanMediaPubRepository.deleteByCourseId(courseId);
        //将teachplanmedia中的数据复制过来
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubs = new ArrayList<>();
        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubRepository.save(teachplanMediaPub);
        }
    }
}
