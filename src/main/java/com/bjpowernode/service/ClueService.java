package com.bjpowernode.service;

import com.bjpowernode.entity.TClue;
import com.bjpowernode.entity.TClueEditLog;
import com.bjpowernode.query.ClueConvertQuery;
import com.bjpowernode.query.ClueQuery;
import com.github.pagehelper.PageInfo;

import java.io.OutputStream;
import java.util.List;

public interface ClueService {

  PageInfo<TClue> getClueByPage(Integer current, com.bjpowernode.query.ClueSearchQuery search);

  TClue getClueById(Integer id);

  List<TClueEditLog> listEditLogs(Integer clueId);

  int addClue(ClueQuery clueQuery);

  int editClue(ClueQuery clueQuery);

  int deleteClue(Integer id);

  void exportExcel(List<String> idList, OutputStream outputStream);

  void convertToCustomer(ClueConvertQuery query);
}
