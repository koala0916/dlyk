package com.bjpowernode.query;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerExcel {

    @ExcelProperty("负责人")
    private String ownerName;

    @ExcelProperty("所属活动")
    private String activityName;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    private String fullName;

    /**
     * 称呼
     */
    @ExcelProperty(value = "称呼")
    private String appellationName;

    /**
     * 手机
     */
    @ExcelProperty(value = "手机")
    private String phone;

    /**
     * 微信
     */
    @ExcelProperty(value = "微信")
    private String weixin;

    /**
     * 是否贷款
     */
    @ExcelProperty(value = "是否贷款")
    private String needLoanName;

    /**
     * 意向状态
     */
    @ExcelProperty(value = "意向状态")
    private String intentionStateName;

    /**
     * 客户来源
     */
    @ExcelProperty(value = "客户来源")
    private String sourceName;

    /**
     * 意向产品
     */
    @ExcelProperty(value = "意向产品")
    private String intentionProductName;

    /**
     * 下次跟踪时间
     */
    @ExcelProperty(value = "下次跟踪时间")
    private Date nextContactTime;
}
