package sms.model;
import sms.dao.StudentDAO;
import java.util.List;

public class Student extends Person implements DatabaseOperations {
    private int id;
    private String firstName;
    private String lastName;
    private String course;
    private double marks;

    public Student(int id, String firstName, String lastName, String email, String course, double marks) {
        super(firstName + " " + lastName, email);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
        this.marks = marks;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    @Override
    public void displayInfo() {
        System.out.printf(
            "Student ID: %d, Name: %s %s, Email: %s, Course: %s, Marks: %.2f%n",
            id, firstName, lastName, email, course, marks
        );
    }

    @Override
    public void add() { StudentDAO.addStudent(this); }

    @Override
    public void delete() { StudentDAO.deleteStudent(this.id); }

    @Override
    public void update() { StudentDAO.updateStudent(this); }

    @Override
    public List<Student> search(String keyword) {
        return StudentDAO.searchStudents(keyword);
    }
}