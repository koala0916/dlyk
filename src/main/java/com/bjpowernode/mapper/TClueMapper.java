package com.bjpowernode.mapper;

import com.bjpowernode.annotation.DataScope;
import com.bjpowernode.entity.TClue;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.vo.NameValueData;

import java.util.List;

public interface TClueMapper {

  int deleteByPrimaryKey(Integer id);

  int insertSelective(TClue record);

  TClue selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(TClue record);

  @DataScope(tableAlias = "tc", columnName = "create_by")
  List<TClue> selectByPage(BaseQuery baseQuery);

  @DataScope(tableAlias = "tc", columnName = "create_by")
  List<TClue> selectByPageForExport(BaseQuery baseQuery, List<String> idList);

  TClue selectDetailById(Integer id);

  Integer selectTotalClueCount();

  List<NameValueData> selectClueSourceCount();
}
