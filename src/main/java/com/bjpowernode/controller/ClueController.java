package com.bjpowernode.controller;

import com.bjpowernode.entity.TClue;
import com.bjpowernode.entity.TClueEditLog;
import com.bjpowernode.query.ClueConvertQuery;
import com.bjpowernode.query.ClueQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.ClueService;
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
public class ClueController {

  @Resource
  private ClueService clueService;

  @GetMapping("/api/clues")
  public Result clues(Integer current) {
    PageInfo<TClue> pageInfo = clueService.getClueByPage(current);
    return Result.OK(pageInfo);
  }

  @GetMapping("api/clue/{id}")
  public Result getClueById(@PathVariable("id") Integer id) {
    return Result.OK(clueService.getClueById(id));
  }

  @GetMapping("api/clue/{id}/edit-logs")
  public Result editLogs(@PathVariable("id") Integer clueId) {
    List<TClueEditLog> logs = clueService.listEditLogs(clueId);
    return Result.OK(logs);
  }

  @PostMapping("/api/clue/add")
  public Result addClue(ClueQuery clueQuery) {
    try {
      int rows = clueService.addClue(clueQuery);
      return rows > 0 ? Result.OK("添加成功") : Result.FAIL("添加失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @PutMapping("/api/clue")
  public Result editClue(ClueQuery clueQuery) {
    try {
      int rows = clueService.editClue(clueQuery);
      return rows > 0 ? Result.OK("保存成功") : Result.FAIL("保存失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @DeleteMapping("/api/clue/{id}")
  public Result deleteClue(@PathVariable("id") Integer id) {
    try {
      int rows = clueService.deleteClue(id);
      return rows > 0 ? Result.OK("删除成功") : Result.FAIL("删除失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @PostMapping("/api/clue/convert")
  public Result convertToCustomer(@RequestBody ClueConvertQuery query) {
    try {
      clueService.convertToCustomer(query);
      return Result.OK("转客户成功，已同步创建交易");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @GetMapping("/api/clue/exportExcel")
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
    clueService.exportExcel(idList, outputStream);
  }
}
