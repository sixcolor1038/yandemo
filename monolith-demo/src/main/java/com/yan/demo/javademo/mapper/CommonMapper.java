package com.yan.demo.javademo.mapper;

import com.yan.demo.javademo.entity.CommonRec;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: sixcolor
 * @Date: 2024-04-21 10:32
 * @Description:
 */
@Mapper
public interface CommonMapper {

    void addCommonRec(CommonRec rec);

    void updateCommonRec(CommonRec rec);

}
