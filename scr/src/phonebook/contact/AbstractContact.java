package src.phonebook.contact;

public abstract class AbstractContact implements Contact {

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
        if(getDefoultNumber() == null) {
            sb.append("Отсутствует");
        } else {
            sb.append(getDefoultNumber().toString());
        }
        sb.append("\r\n");
        for(PhoneNumber pn : getNumbers()) {
            if(!pn.equals(getDefoultNumber()))
                sb.append("\t" + pn.toString() + "\r\n");
        }
        return sb.toString();
    }

}
