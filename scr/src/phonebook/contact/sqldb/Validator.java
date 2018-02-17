package src.phonebook.contact.sqldb;

class Validator {
    static String validate(String input, int constraint) {
        input = input == null ? "" : input;

        if(input.length() <= constraint)
            return input;
        return input.substring(0, constraint);
    }
}
