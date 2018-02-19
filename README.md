# phone-book

Учебный проект - телефонная книга.

Долговременное хранение представлено в двух реализациях - через реляционную БД(при написании использовалась PostgreSQL) и XML.
Переключение между реализациями происходит через property файл - "src/phonebook/prop.properties". 
Параметр PHONE_BOOK_VERSION прнимает два занчения: SQLDB и XML.
Доступ к БД на сервере осуществляется через InitialContext, обращающися к файлу web/META-INF/context.xml.
Проюполагается что БД имеет следующую структуру:
    
    schema: pbulic
    tables: notes (id bigint, name varchar, address varchar, date_of_creation timestamp, default_phone_id)
            phoneNumbers (id bigint, noteId bigint, number varchar, description varchar)



Телефонная книга может запускаться как на сервере приложений(Tomcat) так и открываться в командной строке. 
Точкой входа в этом случае является src.phonebook.main.Main.