package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageQuery implements Serializable {
    private Integer page;           // 页码
    private Integer pageSize;       // 分页大小
}
