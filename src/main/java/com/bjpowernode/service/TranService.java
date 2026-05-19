package com.bjpowernode.service;

import com.bjpowernode.entity.TTran;
import com.bjpowernode.query.TranQuery;
import com.github.pagehelper.PageInfo;

import java.io.OutputStream;
import java.util.List;

public interface TranService {

  PageInfo<TTran> getTrans(Integer current, com.bjpowernode.query.TranSearchQuery search);

  TTran getTranById(Integer id);

  int addTran(TranQuery tranQuery);

  int editTran(TranQuery tranQuery);

  int deleteTran(Integer id);

  void exportExcel(List<String> idList, OutputStream outputStream);
}
