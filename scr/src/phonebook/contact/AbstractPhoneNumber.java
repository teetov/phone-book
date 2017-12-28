package phonebook.contact;

import phonebook.contact.PhoneNumber;

public abstract class AbstractPhoneNumber implements PhoneNumber {

    public String toString() {
        StringBuilder result = new StringBuilder();
        if(!"".equals(getDescription())) {
            result.append(" (");
            result.append(getDescription());
            result.append(") ");
        }
        result.append(getNumber());
        return result.toString();
    }

}
