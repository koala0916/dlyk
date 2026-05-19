package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserSearchQuery {

  private String loginAct;
  private String name;
  private String phone;
  private String email;
  private Integer createBy;
  private Integer editBy;
  private Integer accountEnabled;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimePoint;
  private String createTimePointType;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date editTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date editTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date editTimePoint;
  private String editTimePointType;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastLoginTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastLoginTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastLoginTimePoint;
  private String lastLoginTimePointType;
}
