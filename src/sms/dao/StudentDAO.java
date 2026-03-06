package sms.dao;

import sms.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class StudentDAO {

    // JDBC connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/sms"; // replace with your DB
    private static final String USER = "root"; // replace with your DB user
    private static final String PASS = "";     // replace with your DB password

    // ==========================
    // GET CONNECTION
    // ==========================
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ==========================
    // ADD STUDENT
    // ==========================
    public static void addStudent(Student s) {
        String sql = "INSERT INTO students (firstName, lastName, email, course, marks) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, s.getFirstName());
            pst.setString(2, s.getLastName());
            pst.setString(3, s.getEmail());
            pst.setString(4, s.getCourse());
            pst.setDouble(5, s.getMarks());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        s.setId(rs.getInt(1)); // set generated ID
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding student: " + e.getMessage());
        }
    }

    // ==========================
    // UPDATE STUDENT
    // ==========================
    public static void updateStudent(Student s) {
        String sql = "UPDATE students SET firstName=?, lastName=?, email=?, course=?, marks=? WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, s.getFirstName());
            pst.setString(2, s.getLastName());
            pst.setString(3, s.getEmail());
            pst.setString(4, s.getCourse());
            pst.setDouble(5, s.getMarks());
            pst.setInt(6, s.getId());

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating student: " + e.getMessage());
        }
    }

    // ==========================
    // DELETE STUDENT
    // ==========================
    public static void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting student: " + e.getMessage());
        }
    }

    // ==========================
    // GET ALL STUDENTS
    // ==========================
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("email"),
                    rs.getString("course"),
                    rs.getDouble("marks")
                );
                students.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching students: " + e.getMessage());
        }

        return students;
    }

    // ==========================
    // SEARCH STUDENTS BY NAME
    // ==========================
    public static List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE firstName LIKE ? OR lastName LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            pst.setString(1, kw);
            pst.setString(2, kw);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getDouble("marks")
                    );
                    students.add(s);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error searching students: " + e.getMessage());
        }

        return students;
    }
}