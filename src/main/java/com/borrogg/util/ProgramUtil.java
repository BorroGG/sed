package com.borrogg.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProgramUtil {
    public static final String MENU = "\n Что вы хотите сделать? Введите число из предложенных: \n" +
            "1. Вывести информацию о конкретном пользователе. \n" +
            "2. Вывести информацию обо всех пользователях. \n" +
            "3. Вывести все должности. \n" +
            "4. Создать пользователя. \n" +
            "5. Удалить пользователя. \n" +
            "6. Редактировать пользователя. \n" +
            "7. Найти пользователя по ФИО. \n" +
            "8. Найти пользователя по телефону. \n" +
            "9. Выйти. \n";
    public static final String INCORRECT_INPUT = "Неверный ввод, попробуйте еще раз.";
    public static final String ADD_NEW_USER_SUCCESS = "Новый пользователь успешно добавлен!";
    public static final String INPUT_FIO = "Введите ФИО пользователя: ";
    public static final String INPUT_DATE_OF_BIRTH = "Введите дату рождения в формате dd-MM-yyyy(Например 08-06-2000): ";
    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final String INCORRECT_DATE = "Дата не соответствует паттерну! Попробуйте еще раз.";
    public static final String CHOOSE_POSITION = "Введите номер роли сотрудника: \n" +
            "1. Пользователь \n" +
            "2. Начальник \n" +
            "3. Админ";
    public static final String INPUT_NUM_DEPARTMENT = "Ввведите номер отдела для сотрудника: ";
    public static final String INPUT_PHONE = "Введите номер телефона в любом формате";
    public static final String BOSS_IN_DEPARTMENT_EXIST = "Отдел уже имеет начальника, выберете другую должность";
    public static final String SELECT_ID_CLIENT_TO_SEE_INFO = "Выберете ID о каком пользователе вы хотите получить информацию: ";
    public static final String SELECT_USER = "Выберете пользователя";
    public static final String INFO_ABOUT_ALL_CLIENTS = "Информация обо всех пользователях: ";
    public static final String INFO_ABOUT_ALL_POSITIONS = "Все доступные должности: \n";
    public static final String REMOVE_USER_SUCCESS = "Удаление прошло успешно";
    public static final String UPDATE_USER_SUCCESS = "Обновление пользователя прошло успешно";
    public static final String INPUT_QUERY_FOR_SEARCH = "Введите строку для поиска: ";
    public static final String QUERY_RESULT = "Вот что нам удалось найти: ";
    public static final String NOTHING_FOUND = "Ничего не найдено";
    public static final String CURRENT_USER_NAME = "Текущее имя пользователя: ";
    public static final String CURRENT_USER_DOB = "Текущяя дата рождения пользователя: ";
    public static final String CURRENT_USER_DEPARTMENT = "Текущий отдел пользователя: ";
    public static final String CURRENT_USER_POSITION = "Текущяя должность пользователя: ";
    public static final String CURRENT_USER_PHONE = "Текущий телефон пользователя: ";
    public static final String INPUT_OT_ENTER = "Для изменения введите строку, для пропуска нажмите enter";
    public static final String CLIENT_INFO = "Пользователь с Id = %d. \n" +
            "ФИО = %s \n" +
            "Дата рождения = %s \n" +
            "Принадлежит к департаменту = %s \n" +
            "Должность = %s \n" +
            "Телефон = %s \n";

}
