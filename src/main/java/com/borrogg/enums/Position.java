package com.borrogg.enums;

import lombok.Getter;

@Getter
public enum Position {
    USER("Пользователь"), BOSS("Начальник"), ADMIN("Администратор");

    private final String value;
    Position(String value) {
        this.value = value;
    }
}
