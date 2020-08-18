package com.hydeze.hypad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ma
 * @since 2020-08-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PadPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关键词id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 人员姓名
     */
    private String name;

    /**
     * 人员性别
     */
    private Integer gender;

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
     * 工作年限
     */
    private Integer workexp;

    /**
     * 目前薪资
     */
    private String salarynow;

    /**
     * 期望薪资
     */
    private String salaryexpect;

    /**
     * 简历日期
     */
    private LocalDateTime induction;

    /**
     * 招聘渠道
     */
    private String employfrom;

    /**
     * 毕业时间
     */
    private LocalDateTime gmtReport;

    /**
     * 亮点公司
     */
    private String lightcomp;

    /**
     * 是否面试,1是,0否
     */
    private Integer isinterview;

    /**
     * 面试结果,1通过,0未通过
     */
    private Integer interviewres;

    /**
     * 作品链接及其它说明
     */
    private String workset;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 逻辑删除,1删除，0未删
     */
    private Integer isDelete;

    /**
     * create时间
     */
    private LocalDateTime gmtCreate;

    /**
     * modified时间
     */
    private LocalDateTime gmtModified;


}
