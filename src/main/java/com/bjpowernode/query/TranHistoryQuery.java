package com.bjpowernode.query;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 更新交易阶段
 */
@Data
public class TranHistoryQuery {

    private Integer tranId;
    private Integer stage;
    private BigDecimal money;
    private Date expectedDate;
}
