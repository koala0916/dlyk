package com.bjpowernode.controller;

import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.query.CustomerQuery;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.CustomerService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 顾客控制器
 */
@RestController
public class CustomerController {

    @Resource
    private CustomerService customerService;

    /**
     * 线索转顾客
     */
    @PostMapping("/api/customer")
    public Result convertCustomer(@RequestBody CustomerQuery customerQuery){

        boolean flag = customerService.convertCustomer(customerQuery);

        return flag ? Result.OK() : Result.FAIL();
    }


    /**
     * 分页查询顾客信息
     */
    @GetMapping("/api/customers")
    public Result getCustomers(Integer current){

        PageInfo<TCustomer> pageInfo = customerService.getCustomersByPage(current);

        return Result.OK(pageInfo);
    }


    /**
     * 导出
     *
     * ids 2,5,6
     */
    @GetMapping("/api/exportExcel")
    public void exportExcel(String ids, HttpServletResponse response) throws IOException {

        List<String> idList = null;
        //  处理ids，将其转为集合
        if (StringUtils.hasText(ids)){
            idList = Arrays.asList(ids.split(","));
        }

        //设置响应 告诉前端响应的数据是excel文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        //告诉前端返回的是一个文件
        response.setHeader("Content-disposition",
                "attachment;filename*=utf-8''" + System.currentTimeMillis() + ".xlsx");

        //将数据以流的方式输出到前端
        OutputStream outputStream = response.getOutputStream();

        customerService.exportExcel(idList, outputStream);
    }


    /**
     * 查询顾客明细
     */
    @GetMapping("api/customer/{id}")
    public Result queryCustomerById(@PathVariable("id") Integer customerId) {
        TCustomer tCustomer = customerService.queryCustomerByCustomerId(customerId);
        return Result.OK(tCustomer);
    }
}
