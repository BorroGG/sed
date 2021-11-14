package com.borrogg.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchEntity {
    private int id;
    private String fio;
    private String format;


    public SearchEntity(Object[] columns) {
        this.id = (columns[0] != null)? (Integer) columns[0]:0;
        this.fio = (String) columns[1];
        this.format = (String) columns[2];
    }
}
