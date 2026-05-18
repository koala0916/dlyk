package com.bjpowernode.mapper;

import com.bjpowernode.entity.TClueEditLog;

import java.util.List;

public interface TClueEditLogMapper {

  int insert(TClueEditLog record);

  List<TClueEditLog> selectByClueId(Integer clueId);

  int deleteByClueId(Integer clueId);
}
