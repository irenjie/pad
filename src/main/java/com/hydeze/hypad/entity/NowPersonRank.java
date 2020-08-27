package com.hydeze.hypad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class NowPersonRank implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id-bigint
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 人员编号
     */
    private String personid;

    /**
     * id-bigint
     */
    private Long rankid;

    /**
     * 升/降职原因
     */
    private String reason;

    /**
     * 职级位置
     */
    private Integer shunxu;

    /**
     * 职级升降时间
     */
    private LocalDateTime cstChange;

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
