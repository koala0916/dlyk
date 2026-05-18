package com.bjpowernode.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户编辑日志 t_customer_edit_log
 */
@Data
public class TCustomerEditLog implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private Integer customerId;
  private Integer editBy;
  private Date editTime;
  private String changeContent;

  private TUser editByDO;
}
