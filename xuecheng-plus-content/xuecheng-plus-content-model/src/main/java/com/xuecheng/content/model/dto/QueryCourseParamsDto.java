package com.xuecheng.content.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.ToString;

/**
 * @author lmy
 * @version 1.0
 * @description 课程查询条件模型类
 * @date 2023/3/15 15:37
 */
@Data
@ToString
@ApiModel(description = "课程查询参数")
public class QueryCourseParamsDto {

    //审核状态
    @ApiModelProperty("审核状态")
    private String auditStatus;
    //课程名称
    @ApiModelProperty("课程名称")
    private String courseName;
    //发布状态
    @ApiModelProperty("发布状态")
    private String publishStatus;

}
