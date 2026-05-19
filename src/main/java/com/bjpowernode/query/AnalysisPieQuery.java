package com.bjpowernode.query;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AnalysisPieQuery {

  /** customer / clue */
  private String entityType;

  /** 左侧范围：字段 -> 选中值列表 */
  private Map<String, List<String>> scopes;

  /** 右侧维度字段 */
  private String dimension;
}
