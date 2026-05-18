package com.bjpowernode.controller;

import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.entity.TCustomerEditLog;
import com.bjpowernode.query.CustomerQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.CustomerService;
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
public class CustomerController {

  @Resource
  private CustomerService customerService;

  /** 新增客户（学员） */
  @PostMapping("/api/customer/add")
  public Result addCustomer(CustomerQuery customerQuery) {
    try {
      int flag = customerService.addCustomer(customerQuery);
      return flag > 0 ? Result.OK("添加成功") : Result.FAIL("添加失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  /** 编辑客户 */
  @PutMapping("/api/customer")
  public Result editCustomer(CustomerQuery customerQuery) {
    try {
      int flag = customerService.editCustomer(customerQuery);
      return flag > 0 ? Result.OK("保存成功") : Result.FAIL("保存失败");
    } catch (RuntimeException e) {
      return Result.FAIL(e.getMessage());
    }
  }

  @DeleteMapping("/api/customer/{id}")
  public Result deleteCustomer(@PathVariable("id") Integer id) {
    int flag = customerService.deleteCustomer(id);
    return flag > 0 ? Result.OK("删除成功") : Result.FAIL("删除失败");
  }

  @GetMapping("/api/customers")
  public Result getCustomers(Integer current) {
    PageInfo<TCustomer> pageInfo = customerService.getCustomersByPage(current);
    return Result.OK(pageInfo);
  }

  @GetMapping("/api/exportExcel")
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
    customerService.exportExcel(idList, outputStream);
  }

  @GetMapping("api/customer/{id}")
  public Result queryCustomerById(@PathVariable("id") Integer customerId) {
    TCustomer tCustomer = customerService.queryCustomerByCustomerId(customerId);
    return Result.OK(tCustomer);
  }

  @GetMapping("api/customer/{id}/edit-logs")
  public Result editLogs(@PathVariable("id") Integer customerId) {
    List<TCustomerEditLog> logs = customerService.listEditLogs(customerId);
    return Result.OK(logs);
  }
}
