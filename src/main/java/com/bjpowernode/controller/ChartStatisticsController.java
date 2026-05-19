package com.bjpowernode.controller;

import com.bjpowernode.query.AnalysisPieQuery;
import com.bjpowernode.query.ChartFunnelQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.ChartStatisticsService;
import com.bjpowernode.vo.AnalysisFilterOptionsVO;
import com.bjpowernode.vo.LineChartVO;
import com.bjpowernode.vo.NameValueData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChartStatisticsController {

  @Resource
  private ChartStatisticsService chartStatisticsService;

  @GetMapping("/api/chart/performance/line")
  public Result performanceLine() {
    return Result.OK(chartStatisticsService.getPerformanceLineChart());
  }

  @GetMapping("/api/chart/performance/pie")
  public Result performancePie(@RequestParam(defaultValue = "month") String periodType,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month) {
    return Result.OK(chartStatisticsService.getPerformancePie(periodType, year, month));
  }

  @GetMapping("/api/chart/performance/funnel")
  public Result performanceFunnel(ChartFunnelQuery query) {
    return Result.OK(chartStatisticsService.getPerformanceFunnel(query));
  }

  @GetMapping("/api/chart/analysis/customer/options")
  public Result customerOptions() {
    return Result.OK(chartStatisticsService.getCustomerFilterOptions());
  }

  @GetMapping("/api/chart/analysis/clue/options")
  public Result clueOptions() {
    return Result.OK(chartStatisticsService.getClueFilterOptions());
  }

  @PostMapping("/api/chart/analysis/pie")
  public Result analysisPie(@RequestBody AnalysisPieQuery query) {
    return Result.OK(chartStatisticsService.getAnalysisPie(query));
  }
}
