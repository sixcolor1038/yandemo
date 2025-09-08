package com.yan.demo.tools.service;

import com.yan.demo.tools.entity.WdPointsTally;
import com.yan.demo.tools.entity.req.WdPointsTallyReq;

/**
 * @Author: sixcolor
 * @Date: 2024-09-25
 * @Description:
 */
public interface WdService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WdPointsTally queryById(Integer id);

    /**
     * 新增数据
     *
     * @param wdPointsTally 实例对象
     * @return 实例对象
     */
    WdPointsTally insert(WdPointsTallyReq wdPointsTally);

    /**
     * 更新数据
     *
     * @param wdPointsTally 实例对象
     * @return 实例对象
     */
    WdPointsTally update(WdPointsTally wdPointsTally);
}
