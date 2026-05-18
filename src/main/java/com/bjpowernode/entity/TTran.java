package com.bjpowernode.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易表 t_tran
 */
@Data
public class TTran implements Serializable {

  private static final long serialVersionUID = 1L;

  private TUser createByDO;

  private Integer id;
  private String tranNo;
  private Integer customerId;
  /** 学员姓名 */
  private String studentName;
  private BigDecimal money;
  private Integer createBy;
  private Date dealTime;
  private String courseType;
  private String tranRemark;
  private Date createTime;
}
