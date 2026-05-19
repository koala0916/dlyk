package com.bjpowernode.service;

import com.bjpowernode.query.AnalysisPieQuery;
import com.bjpowernode.query.ChartFunnelQuery;
import com.bjpowernode.vo.AnalysisFilterOptionsVO;
import com.bjpowernode.vo.LineChartVO;
import com.bjpowernode.vo.NameValueData;

import java.util.List;

public interface ChartStatisticsService {

  LineChartVO getPerformanceLineChart();

  List<NameValueData> getPerformancePie(String periodType, Integer year, Integer month);

  List<NameValueData> getPerformanceFunnel(ChartFunnelQuery query);

  AnalysisFilterOptionsVO getCustomerFilterOptions();

  AnalysisFilterOptionsVO getClueFilterOptions();

  List<NameValueData> getAnalysisPie(AnalysisPieQuery query);
}
