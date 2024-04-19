package com.yan.demo.javademo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: sixcolor
 * @Date: 2024-04-12 17:28
 * @Description:
 */
public abstract class AbstractController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public void sou() {
        log.info("aaa");
    }


}
