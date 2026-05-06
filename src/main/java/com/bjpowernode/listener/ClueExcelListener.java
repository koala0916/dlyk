package com.bjpowernode.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.bjpowernode.mapper.TClueMapper;
import com.bjpowernode.query.ClueExcel;

import java.util.Date;
import java.util.List;

/**
 * 不要交给spring 容器管理
 *
 * ClueExcel 映射的类
 *
 *  将读取的excel中的数据添加到t_clue表中
 */
public class ClueExcelListener implements ReadListener<ClueExcel> {


    //每次读取的数据个数,读取100条数据后向数据库添加
    private static final int BATCH_COUNT = 100;

    /**
     * 创建集合存放数据
     * @param clueExcel
     * @param analysisContext
     */
    private List<ClueExcel> clueExcelList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    private TClueMapper tClueMapper;

    private Integer loginUserId;

    //该类不能交给spring管理，所以采用了构造函数传入
    public ClueExcelListener(TClueMapper tClueMapper, Integer loginUserId) {
        this.tClueMapper = tClueMapper;
        this.loginUserId = loginUserId;
    }


    /**
     * 每解析一行数据都会调用该方法
     * clueExcel 存储了当前读取的excel中的一行数据
     */
    @Override
    public void invoke(ClueExcel clueExcel, AnalysisContext analysisContext) {
        clueExcel.setCreateTime(new Date());
        clueExcel.setCreateBy(loginUserId);

        //将对象放入集合（缓存）中
        clueExcelList.add(clueExcel);

        //当缓存中的数据达到100条时，批量插入数据库
        if(clueExcelList.size() >= BATCH_COUNT){
            //批量插入数据库
            tClueMapper.batchSaveExcel(clueExcelList);

            //清空集合
            clueExcelList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }


    /**
     * excel解析完成之后调用该方法,该方法只调用一次
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //批量插入数据库
        tClueMapper.batchSaveExcel(clueExcelList);
    }
}
