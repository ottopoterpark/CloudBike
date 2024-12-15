package com.CloudBike.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult implements Serializable {
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 当前页面数据集合
     */
    private List records;
}
