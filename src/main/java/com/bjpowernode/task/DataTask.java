package com.bjpowernode.task;

import com.bjpowernode.DlykServerApplication;
import com.bjpowernode.constant.DicEnum;
import com.bjpowernode.entity.TDicType;
import com.bjpowernode.entity.TProduct;
import com.bjpowernode.service.DicTypeService;
import com.bjpowernode.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 定时任务 读取数据字典的数据
 */

//@EnableScheduling  控制台总打印日志信息，先注释
@Component
public class DataTask {

    @Resource
    private DicTypeService dicTypeService;

    @Resource
    private ProductService productService;




    /**
     * 上线后可以每天执行一次 凌晨2点
     */
    @Scheduled(cron="0/3 * * * * ?")
    public void loadDicType(){
        //从数据库中查询数据字典
        List<TDicType> dicTypes = dicTypeService.getDicTypes();

        //将type中的typeCode作为key，dicValue集合作为value
        for(TDicType dicType : dicTypes){
            DlykServerApplication.cacheMap
                    .put(dicType.getTypeCode(), dicType.getDicValues());
        }


        //查询所有的汽车产品
        List<TProduct> products = productService.getProducts();
        DlykServerApplication.cacheMap.put(DicEnum.INTENTIONPRODUCT.getCode(), products);
    }
}
