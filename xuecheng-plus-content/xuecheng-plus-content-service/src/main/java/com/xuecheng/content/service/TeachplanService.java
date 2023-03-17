package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author lmy
 * @version 1.0
 * @description 课程计划管理相关接口
 * @date 2023/3/14 12:10
 */
public interface TeachplanService {
    /**
     * 根据课程id查询课程计划
     *
     * @param courseId 课程计划
     * @return
     */
    List<TeachplanDto> findTeachplanTree(Long courseId);

    /**
     * 新增/修改/保存课程计划
     *
     * @param saveTeachplanDto
     */
    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    /**
     * 课程计划删除
     *
     * @param teachplanId
     */
    void deleteTeachplan(Long teachplanId);

    /**
     * 课程计划排序移动
     *
     * @param moveType
     * @param teachplanId
     */
    void orderByTeachplan(String moveType, Long teachplanId);
}
