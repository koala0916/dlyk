package com.bjpowernode.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 数据统计vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryData {

    //正在进行中的活动数量
    private Integer goingActivityCount;

    //活动总数
    private Integer totalActivityCount;

    //线索总数
    private Integer totalClueCount;

    //客户总数
    private Integer totalCustomerCount;

    //成功交易额
    private BigDecimal successTranAmount;

    //总交易额
    private BigDecimal totalTranAmount;



}
