package com.borrogg.util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class SearchEntity {
    private String department;
    private int sizeSum;
    private int avgCountFiles;

    public SearchEntity(Object[] columns) {
        this.department = (String) columns[0];
        this.sizeSum = (columns[1] != null)?((BigInteger)columns[1]).intValue():0;
        this.avgCountFiles = (columns[2] != null)?((BigDecimal)columns[2]).intValue():0;
    }
}
