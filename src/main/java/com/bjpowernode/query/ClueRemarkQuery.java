package com.bjpowernode.query;


import lombok.Data;

/**
 * 接受线索备注参数
 */
@Data
public class ClueRemarkQuery {
    private Integer clueId;
    private String noteContent;
    private Integer noteWay;
}
