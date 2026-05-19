package com.bjpowernode.vo;

import lombok.Data;

import java.util.List;

@Data
public class LineSeriesVO {
  private String name;
  /** 使用 Double 便于 Jackson 输出标准 JSON 数字，避免前端折线图无法识别 BigDecimal */
  private List<Double> data;
}
