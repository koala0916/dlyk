package com.bjpowernode.service.impl;


import com.bjpowernode.entity.TProduct;
import com.bjpowernode.mapper.TProductMapper;
import com.bjpowernode.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private TProductMapper productMapper;

    @Override
    public List<TProduct> getProducts() {
        return productMapper.selectProducts();
    }
}
