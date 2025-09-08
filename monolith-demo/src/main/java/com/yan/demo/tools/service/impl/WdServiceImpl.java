package com.yan.demo.tools.service.impl;

import com.yan.demo.tools.entity.WdPointsTally;
import com.yan.demo.tools.entity.req.WdPointsTallyReq;
import com.yan.demo.tools.mapper.WdMapper;
import com.yan.demo.tools.service.WdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

/**
 * @Author: sixcolor
 * @Date: 2024-09-25
 * @Description:
 */
@Service
public class WdServiceImpl implements WdService {

    @Autowired
    private WdMapper wdMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    public WdPointsTally queryById(Integer id) {
        return wdMapper.queryById(id);
    }

    @Override
    public WdPointsTally insert(WdPointsTallyReq wdPointsTally) {
        return null;
    }

    /**
     * 新增数据
     *
     * @param wdPointsTally 实例对象
     * @return 实例对象
     */
    public WdPointsTally insert(WdPointsTally wdPointsTally) {
        // 获取当前日期
        LocalDate now = LocalDate.now();
        int year1 = now.getYear();
        int weekOfYear = now.get(WeekFields.ISO.weekOfWeekBasedYear());
        wdPointsTally.setId(year1 + weekOfYear);
        wdMapper.insert(wdPointsTally);
        return wdPointsTally;
    }

    /**
     * 更新数据
     *
     * @param wdPointsTally 实例对象
     * @return 实例对象
     */
    public WdPointsTally update(WdPointsTally wdPointsTally) {
        wdMapper.update(wdPointsTally);
        return queryById(wdPointsTally.getId());
    }
}
