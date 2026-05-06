package com.bjpowernode.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bjpowernode.DlykServerApplication;
import com.bjpowernode.constant.DicEnum;
import com.bjpowernode.entity.TProduct;

import java.util.List;

/**
 * 意向产品转换器
 */
public class IntentionProductConverter implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //获取excel中的数据
        String excelValue = cellData.getStringValue();

        //从缓存中获取意向产品
        List<TProduct> tProductList =(List<TProduct>)DlykServerApplication.cacheMap.get(DicEnum.INTENTIONPRODUCT.getCode());
        for (TProduct tProduct : tProductList) {
            Integer id = tProduct.getId();//产品id
            String name = tProduct.getName();//产品名称

            if (excelValue.equals(name)) {
                return id;
            }
        }

        return tProductList.get(0).getId();
    }
}
