package com.yan.demo.javademo.service;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.entity.RenameFileAO;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 15:52
 * @Description:
 */
public interface DemoService {

    RResult<Boolean> renameFile(RenameFileAO ao);
}
