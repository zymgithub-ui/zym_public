package com.xuecheng.api.filesystem;


import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemControllerApi {
    /**
     * @param multipartFile  上传的文件
     * @param filetag       文件标签
     * @param businesskey     业务标签
     * @param metadata      元信息,json格式
     * @return
     */
    UploadFileResult upLoad(MultipartFile multipartFile,
                            String filetag,
                            String businesskey,
                            String metadata
                            );
}
