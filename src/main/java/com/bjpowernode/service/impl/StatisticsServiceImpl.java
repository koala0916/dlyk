package com.bjpowernode.service.impl;


import cn.hutool.core.util.NumberUtil;
import com.bjpowernode.mapper.TActivityMapper;
import com.bjpowernode.mapper.TClueMapper;
import com.bjpowernode.mapper.TCustomerMapper;
import com.bjpowernode.mapper.TTranMapper;
import com.bjpowernode.service.StatisticsService;
import com.bjpowernode.vo.NameValueData;
import com.bjpowernode.vo.SummaryData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private TActivityMapper tActivityMapper;

    @Resource
    private TClueMapper  tClueMapper;


    @Resource
    private TCustomerMapper tCustomerMapper;

    @Resource
    private TTranMapper tTranMapper;

    /**
     * 查询第一行数据
     * @return
     */
    @Override
    public SummaryData getSummaryData() {
        //进行中的活动数
        Integer goingActivityCount = tActivityMapper.selectGoingActivityCount();

        //市场活动总数
        Integer totalActivityCount = tActivityMapper.selectTotalActivityCount();

        //线索总数
        Integer totalClueCount = tClueMapper.selectTotalClueCount();

        //客户总数
        Integer totalCustomerCount = tCustomerMapper.selectTotalCustomerCount();

        //成功交易额
        BigDecimal successTranAmount = tTranMapper.selectSuccessTranAmount();

        //交易总额
        BigDecimal totalTranAmount = tTranMapper.selectTotalTranAmount();


        SummaryData summaryData = SummaryData.builder()
                .goingActivityCount(goingActivityCount)
                .totalActivityCount(totalActivityCount)
                .totalClueCount(totalClueCount)
                .totalCustomerCount(totalCustomerCount)
                .successTranAmount(successTranAmount)
                .totalTranAmount(totalTranAmount)
                .build();

        return summaryData;
    }

    /**
     * 销售漏斗数据
     * @return
     */
    @Override
    public List<NameValueData> getSaleFunnelData() {

        List<NameValueData> nameValueDataList = new ArrayList<>();

        //查询线索总数
        Integer totalClueCount = tClueMapper.selectTotalClueCount();

        /*
            线索总数百分比
            1  100%
         */
        BigDecimal clueNum = NumberUtil.div(new BigDecimal(totalClueCount), new BigDecimal(totalClueCount), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));

        //构建线索数据
        NameValueData clueData = NameValueData.builder().name("线索").value(clueNum).build();
        nameValueDataList.add(clueData);

        //顾客 总数
        Integer totalCustomerCount = tCustomerMapper.selectTotalCustomerCount();

        BigDecimal customerNum = NumberUtil.div(new BigDecimal(totalCustomerCount), new BigDecimal(totalClueCount), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        //构建顾客数据
        NameValueData customerData = NameValueData.builder().name("客户").value(customerNum).build();
        nameValueDataList.add(customerData);


        //交易总数
        Integer totalTranCount = tTranMapper.selectTotalTranCount();

        BigDecimal totalTranNum = NumberUtil.div(new BigDecimal(totalTranCount), new BigDecimal(totalClueCount), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));

        //构建交易数据
        NameValueData tranData = NameValueData.builder().name("交易").value(totalTranNum).build();
        nameValueDataList.add(tranData);

        //成交总数
        Integer successTranCount = tTranMapper.selectSuccessTotalCount();
        BigDecimal successTotalTranNum = NumberUtil.div(new BigDecimal(successTranCount), new BigDecimal(totalClueCount), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));

        //构建成交数据
        NameValueData successTranData = NameValueData.builder().name("成交").value(successTotalTranNum).build();
        nameValueDataList.add(successTranData);

        return nameValueDataList;
    }

    @Override
    public List<NameValueData> getCluePieData() {

        return tClueMapper.selectClueSourceCount();
    }
}
