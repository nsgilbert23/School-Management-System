package sms.model;

import java.util.List;

public interface DatabaseOperations {

    void add();

    void update();

    void delete();

    List<Student> search(String keyword);
}