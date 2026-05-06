package com.bjpowernode.controller;

import com.bjpowernode.entity.TTran;
import com.bjpowernode.query.TranQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.TranService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 * 交易控制器
 */
@RestController
public class TranController {


    @Resource
    private TranService tranService;
    /**
     * 添加交易
     */
    @PostMapping("api/tran")
    public Result addTran(@RequestBody TranQuery tranQuery) {
        int num = tranService.addTran(tranQuery);

        if (num > 0) {
            return Result.OK();
        } else {
            return Result.FAIL();
        }
    }

    /**
     * 交易分页查询
     */
    @GetMapping("api/trans")
    public Result getTrans(Integer current) {
        return Result.OK(tranService.getTrans(current));
    }

    /**
     * 交易明细查询
     */
    @GetMapping("/api/tran/{id}")
    public Result getTranById(@PathVariable("id") Integer id) {
        TTran tTran = tranService.getTranById(id);
        return Result.OK(tTran);
    }
}
