package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 线索转客户（含创建交易）参数
 */
@Data
public class ClueConvertQuery {

  private Integer clueId;
  private String name;
  private String phone;
  private Integer age;
  private String courseType;
  private Integer remainingLessons;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date courseExpireTime;
  private String remark;
  private String source;
  private Integer studying;
  /** 客户创建人（通常取线索创建人） */
  private Integer createBy;
  private BigDecimal tranMoney;
  private String tranRemark;
}
