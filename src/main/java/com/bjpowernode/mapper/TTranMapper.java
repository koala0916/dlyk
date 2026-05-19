package com.bjpowernode.mapper;

import com.bjpowernode.entity.TTran;
import com.bjpowernode.query.BaseQuery;

import java.math.BigDecimal;
import java.util.List;

public interface TTranMapper {

  int deleteByPrimaryKey(Integer id);

  int insertSelective(TTran record);

  TTran selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(TTran record);

  List<TTran> selectPage(@org.apache.ibatis.annotations.Param("baseQuery") BaseQuery baseQuery,
      @org.apache.ibatis.annotations.Param("search") com.bjpowernode.query.TranSearchQuery search);

  List<TTran> selectPageForExport(BaseQuery baseQuery, List<String> idList);

  TTran selectDetailById(Integer id);

  BigDecimal selectSuccessTranAmount();

  BigDecimal selectTotalTranAmount();

  Integer selectTotalTranCount();

  Integer selectSuccessTotalCount();

  int deleteByCustomerId(Integer customerId);
}
