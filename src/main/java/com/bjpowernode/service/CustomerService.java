package com.bjpowernode.service;

import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.entity.TCustomerEditLog;
import com.bjpowernode.query.CustomerQuery;
import com.github.pagehelper.PageInfo;

import java.io.OutputStream;
import java.util.List;

public interface CustomerService {

  PageInfo<TCustomer> getCustomersByPage(Integer current);

  void exportExcel(List<String> idList, OutputStream outputStream);

  TCustomer queryCustomerByCustomerId(Integer customerId);

  List<TCustomerEditLog> listEditLogs(Integer customerId);

  int addCustomer(CustomerQuery customerQuery);

  int editCustomer(CustomerQuery customerQuery);

  int deleteCustomer(Integer id);
}
