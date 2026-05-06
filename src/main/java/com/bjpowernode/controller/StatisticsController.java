package com.bjpowernode.controller;

import com.bjpowernode.result.Result;
import com.bjpowernode.service.StatisticsService;
import com.bjpowernode.vo.NameValueData;
import com.bjpowernode.vo.SummaryData;
import jakarta.annotation.Resource;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据统计控制器
 */
@RestController
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 查询第一行数据
     */
    @GetMapping("/api/summary/data")
    public Result summaryData() {
        SummaryData summaryData = statisticsService.getSummaryData();

        return Result.OK(summaryData);
    }

    /**
     * 查询销售漏斗数据
     */
    @GetMapping("/api/sale/funnel")
    public Result funnelData() {

        List<NameValueData> nameValueDataList =  statisticsService.getSaleFunnelData();

        return Result.OK(nameValueDataList);
    }

    /**
     * 线索来源饼图数据
     */
    @GetMapping("/api/clue/pie")
    public Result cluePieData() {
        List<NameValueData> nameValueDataList = statisticsService.getCluePieData();
        return Result.OK(nameValueDataList);
    }
}
