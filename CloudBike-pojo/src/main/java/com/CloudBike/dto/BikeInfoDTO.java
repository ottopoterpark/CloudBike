package com.CloudBike.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 单车基本信息DTO
 * @author unique
 */
@Data
public class BikeInfoDTO implements Serializable {
    /**
     * 单车主键
     */
    Integer id;
    /**
     * 单车编号
     */
    String number;
    /**
     * 单车名称
     */
    String name;
    /**
     * 自行车类型（0：公路车，1：旅行车，2：折叠车，3：死飞，4：山地车，5：其他）
     */
    Integer type;
    /**
     * 车身大小
     */
    Integer size;
    /**
     * 车龄
     */
    Integer age;
    /**
     * 售价
     */
    Integer price;
    /**
     * 日租金
     */
    Integer daily;
    /**
     * 月租金
     */
    Integer monthly;
    /**
     * 描述
     */
    String description;
    /**
     * 图片路径集合
     */
    List<String> images;
}
