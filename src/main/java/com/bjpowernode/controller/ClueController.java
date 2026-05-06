package com.bjpowernode.controller;

import com.bjpowernode.entity.TClue;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.ClueService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 线索
 */
@RestController
public class ClueController {

    @Resource
    private ClueService clusterService;

    /**
     * 分页查询
     */
    @GetMapping("/api/clue")
    public Result queryClueByPage(Integer current){
        PageInfo<TClue> pageInfo = clusterService.getClueByPage(current);

        return Result.OK(pageInfo);
    }


    /**
     * excel上传
     * 文件上传的参数MultipartFile
     * excelFile与前端upload中的name属性对应
     */
    @PostMapping("api/importExcel")
    public Result importExcel(MultipartFile excelFile) throws IOException {
        clusterService.importExcel(excelFile.getInputStream());
        return Result.OK();
    }

    /**
     * 查询线索明细
     */
    @GetMapping("api/clue/{id}")
    public Result queryClueById(@PathVariable("id") Integer id) {
        TClue tClue = clusterService.getClueById(id);

        return Result.OK(tClue);
    }
}
