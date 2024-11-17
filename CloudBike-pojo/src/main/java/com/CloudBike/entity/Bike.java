package com.CloudBike.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自行车表
 * </p>
 *
 * @author unique
 * @since 2024-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bike")
@ApiModel(value="Bike对象", description="自行车表")
public class Bike implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "bike_id", type = IdType.AUTO)
    private Integer bikeId;

    @ApiModelProperty(value = "自行车类型")
    private String bikeType;

    @ApiModelProperty(value = "图片")
    private String bikePic;

    @ApiModelProperty(value = "售价")
    private Integer price;

    @ApiModelProperty(value = "状态（0：正常，1：租赁中，2：已售，3：调整）")
    private Integer status;


}
