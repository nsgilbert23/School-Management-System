package sms.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import sms.dao.StudentDAO;
import sms.model.Student;

public class MainFrameDesign extends javax.swing.JFrame {

    private DefaultTableModel tableModel;

    public MainFrameDesign() {
        initComponents();

        setupTable();
        setSize(950, 720);

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setRowHeight(25);
        jTable1.getTableHeader().setReorderingAllowed(false);
        
        btnAdd.addActionListener(this::btnAddActionPerformed);
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        addFieldListeners();
        addTableListener();

        refreshTable(StudentDAO.getAllStudents());
        getContentPane().setLayout(new BorderLayout());
        
        

        sliderMarks.addChangeListener(e -> applyFiltersAndSort());

        chkMathOnly.addActionListener(e -> applyFiltersAndSort());
        jCheckBox1.addActionListener(e -> applyFiltersAndSort());
        jCheckBox2.addActionListener(e -> applyFiltersAndSort());
        jCheckBox3.addActionListener(e -> applyFiltersAndSort());

        rbSortName.addActionListener(e -> applyFiltersAndSort());
        rbSortMarks.addActionListener(e -> applyFiltersAndSort());
        // After setting lblStatus, start a timer to clear it
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> lblStatus.setText(""));
        timer.setRepeats(false);
        timer.start();
    }

    // ==========================
    // TABLE SETUP
    // ==========================
    private void setupTable() {

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "First Name", "Last Name", "Email", "Course", "Marks"}, 0
        );

        jTable1.setModel(tableModel);
        
        jTable1.setRowHeight(28);
jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
jTable1.setGridColor(new java.awt.Color(200,200,200));
jTable1.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
jTable1.getTableHeader().setReorderingAllowed(false);
    }

    // ==========================
    // ADD
    // ==========================
    private void addStudent() {

    if (!validateInputs()) {
        JOptionPane.showMessageDialog(this,"Fill all fields correctly");
        return;
    }

    Student s = getStudentFromFields(0);

    StudentDAO.addStudent(s);

    JOptionPane.showMessageDialog(this,"Student added successfully!");

    refreshTable(StudentDAO.getAllStudents());

    clearFields();
}

    // ==========================
    // UPDATE
    // ==========================
    private void updateStudent() {

    int row = jTable1.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a student first!");
        return;
    }

    int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

    Student s = getStudentFromFields(id);

    StudentDAO.updateStudent(s);

    refreshTable(StudentDAO.getAllStudents());

    clearFields();
}

    // ==========================
    // DELETE
    // ==========================
  private void deleteStudent() {
    int row = jTable1.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a student first!");
        return;
    }

    // Get student info for display (optional, but nicer)
    String firstName = tableModel.getValueAt(row, 1).toString();
    String lastName = tableModel.getValueAt(row, 2).toString();
    int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete " + firstName + " " + lastName + "?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (confirm == JOptionPane.YES_OPTION) {
        StudentDAO.deleteStudent(id);
        refreshTable(StudentDAO.getAllStudents());
        clearFields();
        // Optional: show a status message
        lblStatus.setText("Student deleted successfully.");
    }
}
    // ==========================
    // SEARCH
    // ==========================
    private void searchStudent() {

        String keyword = jTextField1.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter name to search");
            return;
        }

        refreshTable(StudentDAO.searchStudents(keyword));
    }

    // ==========================
    // SHOW ALL
    // ==========================
    private void showAllStudents() {
        refreshTable(StudentDAO.getAllStudents());
    }

    // ==========================
    // STRING OPERATIONS
    // ==========================
    private void stringOperations() {

        String name = jTextField1.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter first name first!");
            return;
        }

        String result =
                "Uppercase: " + name.toUpperCase() +
                "\nLowercase: " + name.toLowerCase() +
                "\nLength: " + name.length();

        JOptionPane.showMessageDialog(this, result);
    }

    // ==========================
    // REFRESH TABLE
    // ==========================
    private void refreshTable(List<Student> students) {

    tableModel.setRowCount(0); // clear table

    for (Student s : students) {

        Object[] row = {
            s.getId(),
            s.getFirstName(),
            s.getLastName(),
            s.getEmail(),
            s.getCourse(),
            s.getMarks()
        };

        tableModel.addRow(row);
    }
}

    // ==========================
    // VALIDATION
    // ==========================
    private boolean validateInputs() {

        if (jTextField1.getText().trim().isEmpty()) return false;
        if (jTextField3.getText().trim().isEmpty()) return false;
        if (jTextField4.getText().trim().isEmpty()) return false;
        if (jTextField6.getText().trim().isEmpty()) return false;

        try {
            Double.parseDouble(jTextField6.getText());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // ==========================
    // GET STUDENT OBJECT
    // ==========================
    private Student getStudentFromFields(int id) {

    String first = jTextField1.getText().trim();
    String last = jTextField3.getText().trim();
    String email = jTextField4.getText().trim();

    String course = (String) jComboBox1.getSelectedItem();

    double marks = Double.parseDouble(jTextField6.getText().trim());

    return new Student(id, first, last, email, course, marks);
}

    // ==========================
    // CLEAR FIELDS
    // ==========================
    private void clearFields() {

    jTextField1.setText("");
    jTextField3.setText("");
    jTextField4.setText("");
    jTextField6.setText("");

    jComboBox1.setSelectedIndex(0);

    jTable1.clearSelection();

    btnUpdate.setEnabled(false);
    btnDelete.setEnabled(false);
}

    // ==========================
    // FIELD LISTENERS
    // ==========================
    private void addFieldListeners() {

        DocumentListener listener = new DocumentListener() {

            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }

            private void update() {
                btnAdd.setEnabled(validateInputs());
            }
        };

        jTextField1.getDocument().addDocumentListener(listener);
        jTextField3.getDocument().addDocumentListener(listener);
        jTextField4.getDocument().addDocumentListener(listener);
        jTextField6.getDocument().addDocumentListener(listener);

        btnAdd.setEnabled(false);
    }

    // ==========================
    // TABLE LISTENER
    // ==========================
    private void addTableListener() {

    jTable1.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {

            int row = jTable1.getSelectedRow();

            if (row == -1) {
                return;
            }

            Object firstName = tableModel.getValueAt(row, 1);
            Object lastName  = tableModel.getValueAt(row, 2);
            Object email     = tableModel.getValueAt(row, 3);
            Object course    = tableModel.getValueAt(row, 4);
            Object marks     = tableModel.getValueAt(row, 5);

            jTextField1.setText(firstName == null ? "" : firstName.toString());
            jTextField3.setText(lastName == null ? "" : lastName.toString());
            jTextField4.setText(email == null ? "" : email.toString());

            if (course != null) {
                jComboBox1.setSelectedItem(course.toString());
            }

            jTextField6.setText(marks == null ? "" : marks.toString());

            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    });
}

    // ==========================
    // FILTER + SORT
    // ==========================
    private void applyFiltersAndSort() {

        List<Student> students = StudentDAO.getAllStudents();
        List<Student> filtered = new ArrayList<>();

        int minMarks = sliderMarks.getValue();

        for (Student s : students) {

            if (s.getMarks() < minMarks) continue;

            boolean courseMatch = true;

            if (chkMathOnly.isSelected())
                courseMatch &= s.getCourse().equalsIgnoreCase("Mathematics");

            if (jCheckBox1.isSelected())
                courseMatch &= s.getCourse().equalsIgnoreCase("Operating System");

            if (jCheckBox2.isSelected())
                courseMatch &= s.getCourse().equalsIgnoreCase("Networking");

            if (jCheckBox3.isSelected())
                courseMatch &= s.getCourse().equalsIgnoreCase("Java OOP");

            if (courseMatch)
                filtered.add(s);
        }

        if (rbSortName.isSelected()) {

            filtered.sort((a, b) ->
                    a.getFirstName().compareToIgnoreCase(b.getFirstName()));

        } else if (rbSortMarks.isSelected()) {

            filtered.sort((a, b) ->
                    Double.compare(b.getMarks(), a.getMarks()));
        }

        refreshTable(filtered);
    }

    // ==========================
    // ABOUT
    // ==========================
    private void showAboutDialog() {

        JOptionPane.showMessageDialog(
                this,
                "Student Management System\nVersion 1.0\nJava Swing Project",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
    addStudent();
}

private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
    updateStudent();
}

private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
    deleteStudent();
}

private void btnShowAllActionPerformed(java.awt.event.ActionEvent evt) {
    showAllStudents();
}

private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
    searchStudent();
}

private void btnStringOpsActionPerformed(java.awt.event.ActionEvent evt) {
    stringOperations();
}

private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {
    applyFiltersAndSort();
}

private void rbSortMarksActionPerformed(java.awt.event.ActionEvent evt) {
    applyFiltersAndSort();
}

private void showStringOperations() {
    stringOperations();
}

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit",
            JOptionPane.YES_NO_OPTION
    );

    if(confirm == JOptionPane.YES_OPTION){
        System.exit(0);
    }
}

/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgSort = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        pnlDisplay = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        plnInput = new javax.swing.JPanel();
        txtFName = new javax.swing.JLabel();
        txtLName = new javax.swing.JLabel();
        txtEmail = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        txtMarks = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        cmbCourse = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnShowAll = new javax.swing.JButton();
        btnStringOps = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        sliderMarks = new javax.swing.JSlider();
        chkMathOnly = new javax.swing.JCheckBox();
        rbSortName = new javax.swing.JRadioButton();
        rbSortMarks = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "First Name", "Last Name", "Email", "Course", "Marks"
            }
        ));
        jTable1.setToolTipText("");
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout pnlDisplayLayout = new javax.swing.GroupLayout(pnlDisplay);
        pnlDisplay.setLayout(pnlDisplayLayout);
        pnlDisplayLayout.setHorizontalGroup(
            pnlDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDisplayLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 891, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 90, Short.MAX_VALUE))
        );
        pnlDisplayLayout.setVerticalGroup(
            pnlDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDisplayLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 87, Short.MAX_VALUE))
        );

        plnInput.setMaximumSize(new java.awt.Dimension(10000, 10000));

        txtFName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtFName.setText("First Name:");

        txtLName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtLName.setText("Last Name:");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtEmail.setText("Email");

        btnSearch.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnUpdate.setBackground(new java.awt.Color(0, 0, 255));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnUpdate.setText("Update");

        btnDelete.setBackground(new java.awt.Color(255, 0, 0));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnDelete.setText("Delete");

        btnAdd.setBackground(new java.awt.Color(0, 255, 0));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnAdd.setText("Add");

        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        txtMarks.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtMarks.setText("Marks:");

        jTextField6.addActionListener(this::jTextField6ActionPerformed);

        cmbCourse.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cmbCourse.setText("Course:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mathematics", "Networking", "Java OOP", "Operating System" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        btnShowAll.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnShowAll.setText("Show All");
        btnShowAll.addActionListener(this::btnShowAllActionPerformed);

        btnStringOps.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnStringOps.setText("String Ops");
        btnStringOps.addActionListener(this::btnStringOpsActionPerformed);

        javax.swing.GroupLayout plnInputLayout = new javax.swing.GroupLayout(plnInput);
        plnInput.setLayout(plnInputLayout);
        plnInputLayout.setHorizontalGroup(
            plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plnInputLayout.createSequentialGroup()
                .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plnInputLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(plnInputLayout.createSequentialGroup()
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(plnInputLayout.createSequentialGroup()
                                .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtLName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtFName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(plnInputLayout.createSequentialGroup()
                                .addComponent(txtMarks, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(plnInputLayout.createSequentialGroup()
                                .addComponent(cmbCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(plnInputLayout.createSequentialGroup()
                        .addGap(233, 233, 233)
                        .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSearch)
                            .addComponent(btnAdd))
                        .addGap(65, 65, 65)
                        .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnShowAll)
                            .addComponent(btnUpdate))
                        .addGap(58, 58, 58)
                        .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnStringOps)
                            .addComponent(btnDelete))))
                .addGap(0, 160, Short.MAX_VALUE))
        );
        plnInputLayout.setVerticalGroup(
            plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plnInputLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFName)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCourse)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMarks)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(plnInputLayout.createSequentialGroup()
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnDelete)
                            .addComponent(btnUpdate))
                        .addGap(26, 26, 26)
                        .addGroup(plnInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnShowAll)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnStringOps))
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(plnInputLayout.createSequentialGroup()
                        .addComponent(txtEmail)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(plnInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 68, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(plnInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(pnlDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(395, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Manage Students", jPanel1);

        sliderMarks.setMajorTickSpacing(10);
        sliderMarks.setPaintLabels(true);
        sliderMarks.setPaintTicks(true);

        chkMathOnly.setText("Mathematics Only");

        bgSort.add(rbSortName);
        rbSortName.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        rbSortName.setText("Sort by Name");

        bgSort.add(rbSortMarks);
        rbSortMarks.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        rbSortMarks.setText("Sort by Marks");
        rbSortMarks.addActionListener(this::rbSortMarksActionPerformed);

        jCheckBox1.setText("Operating System Only");

        jCheckBox2.setText("Networking Only");

        jCheckBox3.setText("Java OOP Only");
        jCheckBox3.addActionListener(this::jCheckBox3ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkMathOnly, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbSortName)
                            .addComponent(sliderMarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rbSortMarks)))
                    .addComponent(jCheckBox1))
                .addContainerGap(818, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(rbSortName)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rbSortMarks)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBox3)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(chkMathOnly)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(sliderMarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(389, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Filters & Sorting", jPanel2);

        getContentPane().add(jTabbedPane2);
        jTabbedPane2.setBounds(10, 10, 1450, 600);

        jMenu1.setText("File");

        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(this::jMenuItem1ActionPerformed);
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Students");

        jMenuItem2.setText("Add Student");
        jMenuItem2.addActionListener(this::jMenuItem2ActionPerformed);
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Update Student");
        jMenuItem3.addActionListener(this::jMenuItem3ActionPerformed);
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Delete Student");
        jMenuItem4.addActionListener(this::jMenuItem4ActionPerformed);
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Tools");

        jMenuItem5.setText("String Operations");
        jMenuItem5.addActionListener(this::jMenuItem5ActionPerformed);
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Help");

        jMenuItem6.setText("About");
        jMenuItem6.addActionListener(this::jMenuItem6ActionPerformed);
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
 * 
 * @param evt 
 */
    
    /**
    private void rbSortMarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSortMarksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbSortMarksActionPerformed
**/
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        addStudent();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    // optional action when Enter is pressed
}
    
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        updateStudent();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        deleteStudent();
    }//GEN-LAST:event_jMenuItem4ActionPerformed
/**
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed
**/
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        showAboutDialog();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        showStringOperations();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed
/**
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        
    }//GEN-LAST:event_jTextField1ActionPerformed
**/
    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    /**
    private void btnShowAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAllActionPerformed
        showAllStudents();
    }//GEN-LAST:event_btnShowAllActionPerformed

    private void btnStringOpsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStringOpsActionPerformed
        stringOperations();
    }//GEN-LAST:event_btnStringOpsActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

**/
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgSort;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JButton btnStringOps;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox chkMathOnly;
    private javax.swing.JLabel cmbCourse;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel plnInput;
    private javax.swing.JPanel pnlDisplay;
    private javax.swing.JRadioButton rbSortMarks;
    private javax.swing.JRadioButton rbSortName;
    private javax.swing.JSlider sliderMarks;
    private javax.swing.JLabel txtEmail;
    private javax.swing.JLabel txtFName;
    private javax.swing.JLabel txtLName;
    private javax.swing.JLabel txtMarks;
    // End of variables declaration//GEN-END:variables

}
