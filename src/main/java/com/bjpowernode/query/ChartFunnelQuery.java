package com.bjpowernode.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ChartFunnelQuery {

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endTime;

  /** 为空或 0 表示全公司 */
  private Integer userId;
}
