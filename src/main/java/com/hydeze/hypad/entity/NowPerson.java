package com.hydeze.hypad.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ma
 * @since 2020-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"isDelete","cstCreate","cstModified"})
public class NowPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员编号
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 入职时间(中国国家标准时间)
     */
    private LocalDateTime cstEntry;


    /*
     * 历史职级数量
     */
    private Integer ranknum;

    /**
     * 离职时间
     */
    private LocalDateTime cstResign;

    /**
     * 离职原因
     */
    private String resignReason;

    /**
     * 部门
     */
    private String department;

    /**
     * 岗位
     */
    private String post;

    /**
     * 学校
     */
    private String school;

    /**
     * 专业
     */
    private String major;

    /**
     * 学历
     */
    private String qualification;

    /**
     * 毕业时间
     */
    private LocalDateTime cstGraduation;

    /**
     * 电话
     */
    private String phone;

    /**
     * 备注
     */
    private String more;

    /**
     * 逻辑删除,1删除，0未删
     */
    private Integer isDelete;

    /**
     * create时间
     */
    private LocalDateTime cstCreate;

    /**
     * modified时间
     */
    private LocalDateTime cstModified;


}
