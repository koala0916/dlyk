package com.bjpowernode.service;

import com.bjpowernode.entity.TClue;
import com.github.pagehelper.PageInfo;

import java.io.InputStream;

public interface ClueService {
    PageInfo<TClue> getClueByPage(Integer current);

    void importExcel(InputStream inputStream);

    TClue getClueById(Integer id);
}
