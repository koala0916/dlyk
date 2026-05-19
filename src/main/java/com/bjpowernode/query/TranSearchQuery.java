package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TranSearchQuery {

  private String tranNo;
  private String studentName;
  private String courseType;
  private String tranRemark;
  private Integer createBy;
  /** 按客户筛选（从客户详情跳转） */
  private Integer customerId;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date dealTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date dealTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date dealTimePoint;
  private String dealTimePointType;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimePoint;
  private String createTimePointType;

  private String moneyOp;
  private BigDecimal moneyValue;
  private BigDecimal moneyMin;
  private BigDecimal moneyMax;
}
