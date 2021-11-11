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
            "9. Удалить файл. \n" +
            "10. Добавить новый файл. \n" +
            "11. Выгрузить файл. \n" +
            "12. Выгрузить все файлы. \n" +
            "13. Найти файл. \n" +
            "14. Отчет по варианту. \n" +
            "15. Выйти. \n";
    public static final String UNIQUE_QUERY = "SELECT d.name as \"Отдел\", SUM(f.size_kb) " +
            "as \"Размер всех файлов сотрудников\", AVG(Count) as \"Среднее количество файлов сотрудников\"\n" +
            "FROM (\n" +
            "SELECT COUNT(c.client_id) as Count, c.department_id FROM file\n" +
            "JOIN client c on file.client_id = c.client_id\n" +
            "JOIN department d on d.department_id = c.department_id\n" +
            "group by c.department_id, c.client_id) as avg\n" +
            "JOIN department d on avg.department_id = d.department_id\n" +
            "JOIN client c on d.department_id = c.department_id\n" +
            "JOIN file f on c.client_id = f.client_id\n" +
            "GROUP BY d.name";
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
    public static final String INPUT_OR_ENTER = "Для изменения введите строку, для пропуска нажмите enter";
    public static final String INPUT_FILENAME = "Введите полный путь c названием файла для его загрузки: ";
    public static final String INCORRECT_FILE_FORMAT = "Неверный формат файла!";
    public static final String INCORRECT_FILE_SIZE = "Размер файла превышает максимальный размер в 10 МБ!";
    public static final String INCORRECT_FILES_COUNT = "Превышен лимит количества файлов!";
    public static final String ERROR_ON_READ_FILE = "Неудалось прочитать файл, попробуйте еще раз";
    public static final String ERROR_ON_CREATE_FILE = "Ошибка при создании файла, что-то не так";
    public static final String ERROR_ON_WRITE_FILE = "Ошибка при записи файла, что-то не так";
    public static final String ERROR_ON_RETURN_CHANGES = "Не удалось откатить изменения после ошибки!";
    public static final String CREATE_FILE_COMPLETE = "Файл успешно создан!";
    public static final String SELECT_FILE_TO_DOWNLOAD = "Выберете номер файла для загрузки: ";
    public static final String SELECT_FILE_TO_DELETE = "Выберете номер файла для удаления: ";
    public static final String INPUT_PATH_TO_DOWNLOAD = "Введите путь для сохранения файла";
    public static final String DOWNLOAD_FILE_COMPLETE = "Файл загружен успешно!";
    public static final String DELETE_FILE_COMPLETE = "Файл успешно удален!";
    public static final String ERROR_ON_DELETE_FILE = "Удаление завершилось неудачно!";
    public static final String DOWNLOAD_ALL_FILE_COMPLETE = "Все файлы загружены успешно!";
    public static final String INPUT_RANGE_SIZE_FILE = "Введите диапазон размера файла в кб через тире (например 100-200, 2000-5000): ";
    public static final String CLIENT_INFO = "Пользователь с Id = %d. \n" +
            "ФИО = %s \n" +
            "Дата рождения = %s \n" +
            "Принадлежит к департаменту = %s \n" +
            "Должность = %s \n" +
            "Телефон = %s \n" +
            "Файлы пользователя: \n%s";
    public static final String FILE_INFO = "Файл с Id = %d. \n" +
            "Название = %s \n" +
            "Формат = %s \n" +
            "Дата загрузки = %s \n" +
            "Размер в кб = %s \n" +
            "Уникальный код = %s \n" +
            "Принадлежит: %s \n";
    public static final String SELECT_FORMAT_FOR_SEARCH = "Выберете формат изображения для поиска: \n" +
            "1. .png \n" +
            "2. .jpeg \n" +
            "3. .bmp \n" +
            "4. .gif \n";

}
