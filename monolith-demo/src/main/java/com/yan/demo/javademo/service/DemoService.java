package com.yan.demo.javademo.service;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.ao.RenameFileAO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 15:52
 * @Description:
 */
public interface DemoService {

    RResult<Boolean> renameFile(RenameFileAO ao);

    RResult<Boolean> generateBuilderByExcel(MultipartFile file) throws IOException;
}
