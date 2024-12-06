package com.CloudBike.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bike")
public class Bike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 自行车类型
     */
    private String type;

    /**
     * 图片
     */
    private String pic;

    /**
     * 售价
     */
    private Integer price;

    /**
     * 状态（0：正常，1：租赁中，2：已售，3：待归还）
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
