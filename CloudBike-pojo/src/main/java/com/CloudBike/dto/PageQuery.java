package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询DTO
 * @author unique
 */
@Data
public class PageQuery implements Serializable {
    /**
     * 页码
     */
    private Integer page = 1;
    /**
     * 分页大小
     */
    private Integer pageSize = 10;
}
