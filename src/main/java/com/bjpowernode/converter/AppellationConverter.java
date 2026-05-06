package com.bjpowernode.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bjpowernode.DlykServerApplication;
import com.bjpowernode.constant.DicEnum;
import com.bjpowernode.entity.TDicType;
import com.bjpowernode.entity.TDicValue;

import java.util.List;

/**
 * 称呼转换器
 */
public class AppellationConverter implements Converter<Integer> {

    /**
     * 根据excel中的内容找到数据字典中对应的id
     */
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //获取excel中的数据 excel中的称呼  先生 女士
        String excelValue = cellData.getStringValue();

        //获取称呼的type value
        List<TDicValue> tDicValueList = (List<TDicValue>)DlykServerApplication.cacheMap.get(DicEnum.APPELLATION.getCode());

        for (TDicValue tDicValue : tDicValueList) {
            Integer id = tDicValue.getId(); //18
            String typeValue = tDicValue.getTypeValue();//先生
            if (excelValue.equals(typeValue)) {
                //这里会return给ClueExcel中的appellation属性
                return id;
            }
        }


        //上面循环没有匹配，返回集合的第一条数据  excel中的数据没有与数据字典匹配的时候会执行到这里
        return tDicValueList.get(0).getId();
    }
}
