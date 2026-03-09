package sms.model;
// Student extends Person abstract class and implements DatabaseOperations - BelyseU
import sms.dao.DatabaseConnection;
import java.sql.*;

public class Student extends Person implements DatabaseOperations {

    private int id;
    private String course;
    private double marks;

    public Student() {
        super("", "", "");
    }

    public Student(int id, String firstName, String lastName, String email, String course, double marks) {
        super(firstName, lastName, email);
        this.id = id;
        this.course = course;
        this.marks = marks;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }


    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }


    @Override
    public void displayInfo() {

        System.out.println(
                "ID: " + id +
                " | First Name: " + firstName +
                " | Last Name: " + lastName +
                " | Email: " + email +
                " | Course: " + course +
                " | Marks: " + marks
        );

    }

    @Override
    public void add() {

        String sql = "INSERT INTO students(firstName,lastName,email,course,marks) VALUES(?,?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, course);
            ps.setDouble(5, marks);

            ps.executeUpdate();

            System.out.println("Student added successfully.");

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    @Override
    public void delete() {

        String sql = "DELETE FROM students WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Student deleted successfully.");

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    @Override
    public void update() {

        String sql = "UPDATE students SET firstName=?, lastName=?, email=?, course=?, marks=? WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, course);
            ps.setDouble(5, marks);
            ps.setInt(6, id);

            ps.executeUpdate();

            System.out.println("Student updated successfully.");

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    @Override
    public void search(String keyword) {

        String sql = "SELECT * FROM students WHERE firstName LIKE ? OR lastName LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        "ID: " + rs.getInt("id") +
                        " | First Name: " + rs.getString("firstName") +
                        " | Last Name: " + rs.getString("lastName") +
                        " | Email: " + rs.getString("email") +
                        " | Course: " + rs.getString("course") +
                        " | Marks: " + rs.getDouble("marks")
                );

            }

        } catch (SQLException e) {
            System.out.println("Error searching student: " + e.getMessage());
        }
    }

}