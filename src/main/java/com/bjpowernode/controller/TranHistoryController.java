package com.bjpowernode.controller;

import com.bjpowernode.query.TranHistoryQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.TranHistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易历史
 */
@RestController
public class TranHistoryController {

    @Resource
    private TranHistoryService tranHistoryService;

    /**
     * 添加交易历史
     * @return
     */
    @PostMapping("/api/tran/stage")
    public Result modifyTranStage(@RequestBody TranHistoryQuery tranHistoryQuery) {
        boolean flag = tranHistoryService.modifyTranStage(tranHistoryQuery);

        if (flag) {
            return Result.OK();
        } else {
            return Result.FAIL();
        }

    }

}
