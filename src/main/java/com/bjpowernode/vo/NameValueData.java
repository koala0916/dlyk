package com.bjpowernode.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 数据图表使用的类
 */
@Data
@Builder
public class NameValueData {

    private String name;
    private Object value;
}
