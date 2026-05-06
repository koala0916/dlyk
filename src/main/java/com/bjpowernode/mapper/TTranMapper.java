package com.bjpowernode.mapper;

import com.bjpowernode.annotation.DataScope;
import com.bjpowernode.entity.TTran;
import com.bjpowernode.query.BaseQuery;

import java.math.BigDecimal;
import java.util.List;

public interface TTranMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TTran record);

    int insertSelective(TTran record);

    TTran selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TTran record);

    int updateByPrimaryKey(TTran record);


    @DataScope(tableAlias = "tcl", columnName = "owner_id")
    List<TTran> selectPage(BaseQuery baseQuery);

    TTran selectTranById(Integer id);

    BigDecimal selectSuccessTranAmount();

    BigDecimal selectTotalTranAmount();

    Integer selectTotalTranCount();

    Integer selectSuccessTotalCount();
}