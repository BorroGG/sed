# Вариант 13
## Чтобы корректно работало на вашей машине:
1. Склонить этот проект к себе в idea через File -> New -> Project from Version Control.
2. Создать бд с названием sed в postgresql.
3. В idea подключиться к этой базе и проверить, что там ничего нет, если что то есть, удалить все.
4. Зайти в application.yml и поменять 6-7 и 11-12 строку на ваши данные для подключения к бд.
5. Зайти в liquibase.properties и поменять 3-4 строку на ваши данные для подключения к бд.
6. Найти класс src/main/java/com/borrogg/util/ProgramUtil.java и поменять знаечение строки 7 на полный путь к папке files, которая лежит в корне проекта.
7. Найти класс src/main/java/com/borrogg/SEDApplication.java и запустить программу, нажав зелёную кнопочку.
8. Готово, можно порадоваться. Если проект все равно не запускается, вам не повезло :(.