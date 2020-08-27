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
public class PadKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关键词id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关键词
     */
    private String keyname;

    /**
     * 关键词描述
     */
    private String keydes;

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
