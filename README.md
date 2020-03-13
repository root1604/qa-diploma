### Проект автоматизации тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка
Задача - автоматизировать сценарии (как позитивные, так и негативные) покупки тура.  

Порядок работ:  
1. [Планирование автоматизации тестирования](documentation/Plan.md)
2. Настройка тестового окружения
3. Написание и отладка автотестов
4. Прогон тестов и описание багов
5. [Отчёт по автоматизации](documentation/Summary.md)
6. [Отчёт по итогам тестирования](documentation/Report.md)
  
Запуск тестового окружения в ОС Linux (при работе над проектом использовался дистрибутив Centos 7)
1. Проверить, что сервис docker запущен и у текущего пользователя есть права на выполнение команды sudo для работы с docker (используется в скриптах запуска тестового окружения)
2. Для запуска окружения с использованием БД Mysql запустить следующую команду (во избежание конфликтов и для корректного запуска окружения при запуске скрипта сначала удаляются запущенные контейнеры и образы, сохраненные в системе)
```
sh startMysqlEnv.sh
```
3. Для запуска окружения с использованием Postgres запустить следующую команду (во избежание конфликтов и для корректного запуска окружения при запуске скрипта сначала удаляются запущенные контейнеры и образы, сохраненные в системе)
```
sh startPostgresEnv.sh
```
4. Чтобы удалить все контейнеры и образы docker после проведения тестирования запустить следующую команду
```
sh removeAllContainers.sh
```

5. Запуск тестов.
```
./gradlew clean test allureReport
```

6. Просмотр отчета allure.
```
./gradlew allureServe
```
