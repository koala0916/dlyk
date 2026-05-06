package com.bjpowernode.query;

import lombok.Data;

@Data
public class BaseQuery {

    //拼接的sql 语句   tu.id=2
    private String filterSQL;
}
