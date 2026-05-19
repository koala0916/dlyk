package com.bjpowernode.vo;

import lombok.Data;

import java.util.List;

@Data
public class LineChartVO {
  private List<String> months;
  private List<LineSeriesVO> series;
}
