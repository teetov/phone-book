package src.phonebook.contact.sqldb;

class Validator {
    //Проверка строки пред вставкой в бд
    static String validate(String input, int maxSize) {
        input = input == null ? "" : input;

        if(input.length() <= maxSize)
            return input;
        return input.substring(0, maxSize);
    }
}
