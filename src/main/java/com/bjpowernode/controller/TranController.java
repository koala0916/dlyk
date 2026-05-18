package com.bjpowernode.controller;

import com.bjpowernode.entity.TTran;
import com.bjpowernode.query.TranQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.TranService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@RestController
public class TranController {

  @Resource
  private TranService tranService;

  @GetMapping("/api/trans")
  public Result trans(Integer current) {
    PageInfo<TTran> pageInfo = tranService.getTrans(current);
    return Result.OK(pageInfo);
  }

  @GetMapping("/api/tran/{id}")
  public Result getTranById(@PathVariable("id") Integer id) {
    return Result.OK(tranService.getTranById(id));
  }

  @PostMapping("/api/tran/add")
  public Result addTran(TranQuery tranQuery) {
    try {
      int rows = tranService.addTran(tranQuery);
      return rows > 0 ? Result.OK("添加成功") : Result.FAIL("添加失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @PutMapping("/api/tran")
  public Result editTran(TranQuery tranQuery) {
    try {
      int rows = tranService.editTran(tranQuery);
      return rows > 0 ? Result.OK("保存成功") : Result.FAIL("保存失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @DeleteMapping("/api/tran/{id}")
  public Result deleteTran(@PathVariable("id") Integer id) {
    try {
      int rows = tranService.deleteTran(id);
      return rows > 0 ? Result.OK("删除成功") : Result.FAIL("删除失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @GetMapping("/api/tran/exportExcel")
  public void exportExcel(String ids, HttpServletResponse response) throws IOException {
    List<String> idList = null;
    if (StringUtils.hasText(ids)) {
      idList = Arrays.asList(ids.split(","));
    }
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Content-disposition",
        "attachment;filename*=utf-8''" + System.currentTimeMillis() + ".xlsx");
    OutputStream outputStream = response.getOutputStream();
    tranService.exportExcel(idList, outputStream);
  }
}
