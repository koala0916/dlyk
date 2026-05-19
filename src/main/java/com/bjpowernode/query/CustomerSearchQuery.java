package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CustomerSearchQuery {

  private String name;
  private String phone;
  private String courseType;
  private String source;
  private String remark;
  private Integer studying;
  private Integer createBy;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimePoint;
  /** before=之前 after=之后 */
  private String createTimePointType;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date courseExpireTimeStart;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date courseExpireTimeEnd;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date courseExpireTimePoint;
  private String courseExpireTimePointType;

  private String ageOp;
  private Integer ageValue;
  private Integer ageMin;
  private Integer ageMax;

  private String remainingLessonsOp;
  private Integer remainingLessonsValue;
  private Integer remainingLessonsMin;
  private Integer remainingLessonsMax;
}
