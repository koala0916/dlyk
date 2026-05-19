package com.bjpowernode.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AnalysisFilterOptionsVO {

  private List<String> dimensions;

  /** 各维度可选值：createBy 为 id 字符串，studying 为 0/1 */
  private Map<String, List<NameValueData>> options;
}
