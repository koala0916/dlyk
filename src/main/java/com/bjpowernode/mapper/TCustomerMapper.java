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

  List<TCustomer> selectByPage(@org.apache.ibatis.annotations.Param("baseQuery") BaseQuery baseQuery,
      @org.apache.ibatis.annotations.Param("search") com.bjpowernode.query.CustomerSearchQuery search,
      @org.apache.ibatis.annotations.Param("idList") List<String> idList);

  TCustomer selectDetailById(Integer customerId);

  List<com.bjpowernode.entity.TTran> selectTransByCustomerId(Integer customerId);

  Integer selectTotalCustomerCount();
}
