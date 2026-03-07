package sms.model;

import java.util.List;

public interface DatabaseOperations {

    void add();

    void update();

    void delete();
    
    void search(String keyword);
}