package com.borrogg.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SearchEntity {
    private String name;
    private int sizeKb;
    private Date dateCreate;
    private String fioOwner;

    public SearchEntity(Object[] columns) {
        this.name = (String) columns[0];
        this.sizeKb = (columns[1] != null)? (Integer) columns[1]:0;
        this.dateCreate = (columns[2] != null)?((Date)columns[2]): null;
        this.fioOwner = (String) columns[3];
    }
}
