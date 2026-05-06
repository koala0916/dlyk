package com.bjpowernode.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public enum DicEnum {

    APPELLATION("appellation"),

    STATE("clueState"),

    SOURCE("source"),

    NEEDLOAN("needLoan"),

    INTENTIONSTATE("intentionState"),

    INTENTIONPRODUCT("intentionProduct");


    @Getter
    @Setter
    private String code;
}
