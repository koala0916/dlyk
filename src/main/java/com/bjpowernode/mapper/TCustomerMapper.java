package com.bjpowernode.mapper;

import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.query.BaseQuery;

import java.util.List;

public interface TCustomerMapper {

  int deleteByPrimaryKey(Integer id);

  int insert(TCustomer record);

  int insertSelective(TCustomer record);

  TCustomer selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(TCustomer record);

  List<TCustomer> selectByPage(BaseQuery baseQuery, List<String> idList);

  TCustomer selectDetailById(Integer customerId);

  Integer selectTotalCustomerCount();
}
