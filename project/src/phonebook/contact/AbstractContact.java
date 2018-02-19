package src.phonebook.contact;

public abstract class AbstractContact implements Contact {

    @Override
    public int lengthOfNumbersList() {
        return getNumbers().size();
    }

    @Override
    public void removeNumber(int id) {
        removeNumber(getNumber(id));
    }

    @Override
    public void setDefoultNumber(int id) {
        setDefaultNumber(getNumber((id)));
    }

    @Override
    public PhoneNumber addNumber(String phoneNumber) {
        return addNumber(phoneNumber, "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Имя: ");
        sb.append(getName());
        String address = getAddress();
        if(!"".equals(address))
        sb.append("\r\nАдрес: ");
        sb.append(address);
        sb.append("\r\n");
        sb.append("Номер по умолчанию: ");
        if(getDefaultNumber() == null) {
            sb.append("Отсутствует");
        } else {
            sb.append(getDefaultNumber().toString());
        }
        sb.append("\r\n");
        for(PhoneNumber pn : getNumbers()) {
            if(!pn.equals(getDefaultNumber()))
                sb.append("\t" + pn.toString() + "\r\n");
        }
        return sb.toString();
    }

}
