package com.bjpowernode.service;

import com.bjpowernode.entity.TTran;
import com.bjpowernode.query.TranQuery;
import com.github.pagehelper.PageInfo;

public interface TranService {
    int addTran(TranQuery tranQuery);

    PageInfo<TTran> getTrans(Integer current);

    TTran getTranById(Integer id);
}
