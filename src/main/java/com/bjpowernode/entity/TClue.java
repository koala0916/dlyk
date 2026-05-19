package com.bjpowernode.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 线索表 t_clue
 */
@Data
public class TClue implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String STATUS_UNCONTACTED = "未联系";
  public static final String STATUS_CONTACTED = "已联系";
  public static final String STATUS_CONVERTED = "已转客户";

  private TUser createByDO;

  private Integer id;
  private String name;
  private Integer age;
  private String phone;
  /** 意向课程 */
  private String intentionCourse;
  /** 意向强度 1-10 */
  private Integer intentionStrength;
  private String source;
  private String remark;
  private Date trialClassTime;
  /** 线索状态：未联系、已联系、已转客户 */
  private String clueStatus;
  private Integer createBy;
  private Date createTime;
  /** 转客户后关联的客户ID（列表展示用） */
  private Integer customerId;
}
