package com.bjpowernode.service.impl;

import com.alibaba.excel.EasyExcel;
import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TClue;
import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.mapper.TClueMapper;
import com.bjpowernode.mapper.TCustomerMapper;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.CustomerExcel;
import com.bjpowernode.query.CustomerQuery;
import com.bjpowernode.service.CustomerService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private TClueMapper tclueMapper;

    @Resource
    private TCustomerMapper tCustomerMapper;

    /**
     * 线索转换顾客
     * 1.判断该线索是否已经转换为顾客,若转换过，则无需再转换
     * 2.若未转换，则向顾客表中添加数据
     * 3.将线索表中的状态改为-1
     * @param customerQuery
     * @return
     */
    @Transactional
    @Override
    public boolean convertCustomer(CustomerQuery customerQuery) {
        //1.判断该线索是否已经转换为顾客,若转换过，则无需再转换
        TClue tClue = tclueMapper.selectByPrimaryKey(customerQuery.getClueId());
        if (tClue.getState() == -1) {
            throw new RuntimeException("该线索已经转换过，无需再转换");
        }

        //2.若未转换，则向顾客表中添加数据
        TCustomer tCustomer = new TCustomer();
        BeanUtils.copyProperties(customerQuery, tCustomer);
        tCustomer.setCreateTime(new Date());
        tCustomer.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());

        int insertCount = tCustomerMapper.insertSelective(tCustomer);

        //3.将线索表中的状态改为-1
        TClue updateClue = new TClue();
        updateClue.setId(customerQuery.getClueId());
        updateClue.setState(-1);//状态
        int updateCount = tclueMapper.updateByPrimaryKeySelective(updateClue);

        return insertCount > 0 && updateCount > 0;
    }

    @Override
    public PageInfo<TCustomer> getCustomersByPage(Integer current) {

        PageHelper.startPage(current, Constant.PAGE_SIZE);
        List<TCustomer> tCustomerList = tCustomerMapper.selectByPage(new BaseQuery(),null);
        PageInfo<TCustomer> pageInfo = new PageInfo<>(tCustomerList);

        return pageInfo;
    }


    /**
     * 导出excel
     * 1.获取需要导出的顾客数据
     * 2.将内存中的数据映射到CustomerExcel中
     * 3.将CustomerExcel数据以流的方式写入到excel中
     * @param idList
     * @param outputStream
     */
    @Override
    public void exportExcel(List<String> idList, OutputStream outputStream) {
        //1.查询导出的顾客数据
        List<TCustomer> tCustomerList = tCustomerMapper.selectByPage(new BaseQuery(), idList);

        //创建CustomerExcel集合
        List<CustomerExcel> customerExcelList = new ArrayList<>();

        //2.将内存中的数据映射到CustomerExcel中
        for (TCustomer tCustomer : tCustomerList) {
            CustomerExcel customerExcel = new CustomerExcel();

            customerExcel.setOwnerName(ObjectUtils.isEmpty(tCustomer.getOwnerDO()) ? "" : tCustomer.getOwnerDO().getName());
            customerExcel.setActivityName(ObjectUtils.isEmpty(tCustomer.getActivityDO()) ? "" : tCustomer.getActivityDO().getName());
            customerExcel.setFullName(ObjectUtils.isEmpty(tCustomer.getClueDO()) ? "" : tCustomer.getClueDO().getFullName());
            customerExcel.setAppellationName(ObjectUtils.isEmpty(tCustomer.getAppellationDO()) ? "" : tCustomer.getAppellationDO().getTypeValue());
            customerExcel.setPhone(ObjectUtils.isEmpty(tCustomer.getClueDO()) ? "" : tCustomer.getClueDO().getPhone());
            customerExcel.setWeixin(ObjectUtils.isEmpty(tCustomer.getClueDO()) ? "" : tCustomer.getClueDO().getWeixin());
            customerExcel.setNeedLoanName(ObjectUtils.isEmpty(tCustomer.getLoanDO()) ? "" : tCustomer.getLoanDO().getTypeValue());
            customerExcel.setIntentionStateName(ObjectUtils.isEmpty(tCustomer.getIntentionStateDO()) ? "" : tCustomer.getIntentionStateDO().getTypeValue());
            customerExcel.setSourceName(ObjectUtils.isEmpty(tCustomer.getSourceDO()) ? "" : tCustomer.getSourceDO().getTypeValue());
            customerExcel.setIntentionProductName(ObjectUtils.isEmpty(tCustomer.getProductDO()) ? "" : tCustomer.getProductDO().getName());
            customerExcel.setNextContactTime(tCustomer.getNextContactTime());

            //构建好的CustomerExcel对象放入集合中
            customerExcelList.add(customerExcel);
        }

//        3.将CustomerExcel数据以流的方式写入到excel中
        EasyExcel.write(outputStream, CustomerExcel.class).sheet().doWrite(customerExcelList);
    }

    @Override
    public TCustomer queryCustomerByCustomerId(Integer customerId) {
        return tCustomerMapper.selectById(customerId);
    }
}
