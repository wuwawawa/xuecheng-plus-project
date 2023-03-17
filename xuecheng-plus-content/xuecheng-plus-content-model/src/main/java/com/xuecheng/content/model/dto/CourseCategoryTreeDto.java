package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * @author lmy
 * @version 1.0
 * @description TODO
 * @date 2023/3/12 11:51
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable {

    //子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;

}
