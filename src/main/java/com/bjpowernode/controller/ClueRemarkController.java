package com.bjpowernode.controller;

import com.bjpowernode.query.ClueRemarkQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.ClueRemarkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线索备注
 */
@RestController
public class ClueRemarkController {

    @Resource
    private ClueRemarkService clueRemarkService;

    @PostMapping("/api/clue/remark")
    public Result addClueRemark(@RequestBody ClueRemarkQuery clueRemarkQuery) {
        int flag = clueRemarkService.addClueRemark(clueRemarkQuery);

        return flag > 0 ? Result.OK() : Result.FAIL();
    }
}
