package com.CloudBike.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 运营团队分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeInfoPageQuery extends PageQuery implements Serializable {
    /**
     * 用户名
     */
    private String username;
}
