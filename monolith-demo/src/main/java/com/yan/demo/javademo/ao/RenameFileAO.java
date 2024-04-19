package com.yan.demo.javademo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 16:52
 * @Description:
 */
@ApiModel("修改文件名AO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameFileAO {
    @ApiModelProperty("文件路径")
    private String path;
    @ApiModelProperty("需去除前缀")
    private List<String> prefixes;
    @ApiModelProperty("操作类型")
    private String type;
    @ApiModelProperty("是否包含子目录")
    private boolean includeSubdirectories = false;
}
