package com.bjpowernode.query;

import lombok.Data;

import java.util.Date;

/**
 * 接受线索转客户的数据
 */
@Data
public class CustomerQuery {

    private Integer clueId;
    private Integer product;
    private String description;
    private Date nextContactTime;
}
