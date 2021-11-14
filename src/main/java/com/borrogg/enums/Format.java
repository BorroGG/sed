package com.borrogg.enums;

import lombok.Getter;

@Getter
public enum Format {
    TXT(1, ".txt"),
    DOC(2, ".doc"),
    DOCX(3, ".docx"),
    PDF(4, ".pdf");

    private final int num;
    private final String format;

    Format(int num, String format) {
        this.num = num;
        this.format = format;
    }
}
