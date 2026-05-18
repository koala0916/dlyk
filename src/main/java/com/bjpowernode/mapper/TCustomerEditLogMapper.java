package com.bjpowernode.mapper;

import com.bjpowernode.entity.TCustomerEditLog;

import java.util.List;

public interface TCustomerEditLogMapper {

  int insert(TCustomerEditLog record);

  List<TCustomerEditLog> selectByCustomerId(Integer customerId);

  int deleteByCustomerId(Integer customerId);
}
