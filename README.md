# neotech-test


Функциональные требования :

Написать микросервис для определения страны по телефонному номеру. Пользователь вводит номер телефона, система валидирует его и показывает страну или сообщение об ошибке.
Для кодов стран воспользуйтесь таблицой на странице https://en.wikipedia.org/wiki/List_of_country_calling_codes, необходимо загружать из нее данны каждый раз при запуске сервиса.

Нефункциональные требования :

Backend :

Java 8
Spring Boot
Maven
HTTP, REST-WS с JSON-форматом данных.

Frontend :

HTML
JavaScript

Вспомогательные библиотеки - на Ваше усмотрение.

Замечания :

Приложение должно собиратся и запускатся из командной строки, на 8080 порту. Также должна быть возможность запуска тестов и просмотра отчетов по ним.
Все обращения к приложению делаются черес REST-WS с JSON в качестве формата данных. 
Внешний вид интерфейса неважен, достаточно опрятного HTML. 
Для запросов используйте любой AJAX-capable фреймворк, можно просто JQuery.
Валидация данных, тесты обязательны.


In order to run integration tests you 
running integration tests:

```bash mvn integration-test -Pintegration -Dphantomjs.binary="${PATH_TO_PHANTOMJS_BINARY}"```

where PATH_TO_PHANTOMJS_BINARY could be: /usr/local/Cellar/phantomjs/2.1.1/bin/phantomjs
