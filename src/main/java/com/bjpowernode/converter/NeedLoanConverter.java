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
 * 是否带宽转换器
 */
public class NeedLoanConverter implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        //获取excel中的数据 excel中的称呼
        String excelValue = cellData.getStringValue();

        //获取称呼的type value
        List<TDicValue> tDicValueList = (List<TDicValue>) DlykServerApplication.cacheMap.get(DicEnum.NEEDLOAN.getCode());

        for (TDicValue tDicValue : tDicValueList) {
            Integer id = tDicValue.getId();
            String typeValue = tDicValue.getTypeValue();
            if (excelValue.equals(typeValue)) {
                //这里会return给ClueExcel中的appellation属性
                return id;
            }
        }


        //上面循环没有匹配，返回集合的第一条数据  excel中的数据没有与数据字典匹配的时候会执行到这里
        return tDicValueList.get(0).getId();
    }
}
