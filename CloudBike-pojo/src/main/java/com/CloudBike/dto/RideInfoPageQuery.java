package com.CloudBike.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 骑行团信息分页查询DTO
 * @author unique
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RideInfoPageQuery extends PageQuery implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 骑行团名称
     */
    private String name;
    /**
     * 审核状态
     */
    private Integer status;
}
