package src.phonebook.contact.serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IdGenerator implements  Serializable {
    private int id = 1;

    private List<Integer>  notUsedId = new ArrayList<>();

    public int newId() {
        if(notUsedId.size() > 0) {
            return notUsedId.remove(0);
        }
        return id++;
    }

    public void removeId(int id) {
        notUsedId.add(id);
    }
}
