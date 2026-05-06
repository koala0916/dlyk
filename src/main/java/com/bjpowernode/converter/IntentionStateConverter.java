package com.bjpowernode.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bjpowernode.DlykServerApplication;
import com.bjpowernode.constant.DicEnum;
import com.bjpowernode.entity.TDicValue;

import java.util.List;

/**
 * 意向状态converter
 */
public class IntentionStateConverter implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //获取excel中的值 有意向 无意向 意向不明
        String excelValue = cellData.getStringValue();

        //获取缓存中的意向状态的type value
        List<TDicValue> tDicValueList = (List<TDicValue>)DlykServerApplication.cacheMap.get(DicEnum.INTENTIONSTATE.getCode());

        for (TDicValue tDicValue : tDicValueList) {
            Integer id = tDicValue.getId();//46
            String typeValue = tDicValue.getTypeValue();//有意向
            //判断缓存中的意向状态的type value是否与excel中的值一致
            if (excelValue.equals(typeValue)) {
                return id;
            }
        }

        return tDicValueList.get(0).getId();
    }
}
