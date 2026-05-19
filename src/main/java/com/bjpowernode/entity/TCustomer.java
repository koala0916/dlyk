package com.bjpowernode.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户表（武术学员）
 * t_customer
 */
@Data
public class TCustomer implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 创建人信息（展示用） */
  private TUser createByDO;

  private Integer id;
  private String name;
  private String phone;
  private Integer age;
  /** 课程类型 */
  private String courseType;
  /** 剩余课时 */
  private Integer remainingLessons;
  /** 课程到期时间 */
  private Date courseExpireTime;
  private String remark;
  private Integer createBy;
  private Date createTime;
  /** 来源 */
  private String source;
  /** 是否正在学习 0否 1是 */
  private Integer studying;
  /** 来源线索ID */
  private Integer clueId;
  /** 详情：关联交易列表（非表字段） */
  private List<TTran> tranList;
}
