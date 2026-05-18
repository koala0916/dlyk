package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TranQuery {

  private Integer id;
  private Integer customerId;
  private String studentName;
  private BigDecimal money;
  private String courseType;
  private String tranRemark;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date dealTime;
}
