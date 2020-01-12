package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.management.resource.ResourceRequest;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "媒资管理接口",description = "媒资管理街口")
public interface MediaUploadControllerApi {
//文件上传前的准备工作,,校验文件是否存在
    @ApiOperation("文件注册")
    ResponseResult register(String fileMd5,
                                    String fileName,
                                    Long fileSize,
                                    String mimeType,
                                    String fileExt);
    @ApiOperation("校验文件是否存在")
    CheckChunkResult checkChunk(String fileMd5,
                                Integer chunk,
                                Integer chunkSize);
    @ApiOperation("上传分块")
    ResponseResult upLoadChunk(MultipartFile file,
                               String fileMd5,
                               Integer chunk
                               );
    @ApiOperation("文件合并")
    ResponseResult mergeChunks(String fileMd5,
                               String fileName,
                               Long fileSize,
                               String mimeType,
                               String fileExt);
   }
