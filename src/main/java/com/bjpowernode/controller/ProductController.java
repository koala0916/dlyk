package com.bjpowernode.controller;

import com.bjpowernode.entity.TProduct;
import com.bjpowernode.result.Result;
import com.bjpowernode.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 产品控制器
 */
@RestController
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 获取产品信息
     * 方案1：从缓存中获取
     * 方案2：从数据库中获取
     * @return
     */
    @GetMapping("/api/product")
    public Result getProducts(){
        List<TProduct> products = productService.getProducts();
        return Result.OK(products);
    }
}
