package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
//将页面的查询条件封装为 对象,
public class QueryPageRequest extends RequestData {
    //接受页面的查询条件
    //站点id
    @ApiModelProperty("站点id")
    private String siteId;
    //页面id
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模板id
    private String templateId;
    //...
}
