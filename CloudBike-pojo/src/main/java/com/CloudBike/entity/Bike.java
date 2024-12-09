package com.CloudBike.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自行车表
 * </p>
 *
 * @author unique
 * @since 2024-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bike")
public class Bike implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 单车编号
     */
    private String number;

    /**
     * 单车名称
     */
    private String name;

    /**
     * 自行车类型（0：公路车，1：旅行车，2：折叠车，3：死飞，4：山地车，5：其他）
     */
    private Integer type;

    /**
     * 售价
     */
    private Integer price;

    /**
     * 图片路径
     */
    private String image;

    /**
     * 日租金
     */
    private Integer daily;

    /**
     * 月租金
     */
    private Integer monthly;

    /**
     * 状态（0：正常，1：待提车，2：租赁中，3：已售，4：待归还）
     */
    private Integer status;

    /**
     * 车龄
     */
    private Integer age;

    /**
     * 描述
     */
    private String description;

    /**
     * 车身大小（单位：寸）
     */
    private Integer size;


}
