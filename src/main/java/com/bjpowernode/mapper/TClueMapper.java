package com.bjpowernode.mapper;

import com.bjpowernode.annotation.DataScope;
import com.bjpowernode.entity.TClue;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.ClueExcel;
import com.bjpowernode.vo.NameValueData;

import java.util.List;

public interface TClueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClue record);

    int insertSelective(TClue record);

    TClue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClue record);

    int updateByPrimaryKey(TClue record);

    @DataScope(tableAlias = "tc",  columnName = "owner_id")
    List<TClue> selectByPage(BaseQuery baseQuery);

    int batchSaveExcel(List<ClueExcel> clueExcelList);

    TClue selectClueDetailById(Integer id);

    Integer selectTotalClueCount();

    List<NameValueData> selectClueSourceCount();

}