package com.bjpowernode.controller;

import com.bjpowernode.entity.TDicValue;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.DicValueService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据字典controller
 */
@RestController
public class DicController {

    @Resource
    private DicValueService dicValueService;

    /**
     * 方案1.在缓存中启动类的cacheMap属性里面已经有了数据字典的数据，直接返回即可
     * 方案2.从数据库重新查询
     * @return
     */
    @GetMapping("/api/dic/{dicCode}")
    public Result getDic(@PathVariable("dicCode") String dicCode){
        List<TDicValue> tDicValueList = dicValueService.getDicValueByType(dicCode);
        return Result.OK(tDicValueList);
    }
}
