package phonebook.contact.serializable_list;

import phonebook.contact.IdGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IdGeneratorImpl implements IdGenerator, Serializable {
    private int id = 1;

    private List<Integer>  notUsedId = new ArrayList<>();
    @Override
    public int newId() {
        if(notUsedId.size() > 0) {
            return notUsedId.remove(0);
        }
        return id++;
    }

    @Override
    public void removeId(int id) {
        notUsedId.add(id);
    }
}
