package com.bjpowernode.service.impl;

import com.bjpowernode.entity.TDicValue;
import com.bjpowernode.mapper.TDicValueMapper;
import com.bjpowernode.service.DicValueService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import com.bjpowernode.service.DicValueService;

import java.util.List;

@Service
public class DicValueServiceImpl implements DicValueService {

    @Resource
    private TDicValueMapper tdicValueMapper;

    @Override
    public List<TDicValue> getDicValueByType(String dicCode) {
        return tdicValueMapper.selectDicValueByType(dicCode);
    }
}
