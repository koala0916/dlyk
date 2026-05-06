package com.bjpowernode.service;

import com.bjpowernode.vo.NameValueData;
import com.bjpowernode.vo.SummaryData;

import java.util.List;

public interface StatisticsService {
    SummaryData getSummaryData();

    List<NameValueData> getSaleFunnelData();

    List<NameValueData> getCluePieData();

}
