package com.tilmeez.web.jdbc;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDbUtil {

    private DataSource dataSource;

    public StudentDbUtil(DataSource theDataSource) {
        dataSource = theDataSource;
    }

    public List<Student> getStudent() throws SQLException {

        List<Student> students = new ArrayList<>();

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {

            // get a connection
            myConn = dataSource.getConnection();

            // create sql statement
            String sql = "SELECT * FROM student ORDER BY last_name";

            myStmt = myConn.createStatement();

            // execute query
            myRs = myStmt.executeQuery(sql);

            // process resul set
            while (myRs.next()) {

                // retrieve data from result set row
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // create new student object
                Student tempStudent = new Student(id, firstName, lastName, email);

                // add it to the list of students
                students.add(tempStudent);

            }

            return students;

        } finally {
            // close JDBC object
            close(myConn, myStmt, myRs);

        }
    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

        try {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close(); // doesn't really close it ... just puts back in connection pool
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStudent(Student theStudent) throws SQLException {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            // get bd connection
            myConn = dataSource.getConnection();

            // create sql for insert
            String sql = "INSERT INTO student" +
                    "(first_name, last_name, email)" +
                    "VALUES (?,?,?)";

            myStmt = myConn.prepareStatement(sql);

            // set the param values for the student
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());

            // execute sql insert
            myStmt.execute();

        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }
    }

    public Student getStudent(String theStudentId) throws Exception {

        Student theStudent = null;

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int studentId;

        try {
            // convert student id to int
            studentId = Integer.parseInt(theStudentId);

            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT * FROM student WHERE id=?";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, studentId);

            // execute statement
            myRs = myStmt.executeQuery();

            // retrieve data from result set row
            if (myRs.next()) {
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // use the studentId during construction
                theStudent = new Student(studentId, firstName, lastName, email);
            } else {
                throw new Exception(("Could not find student id:" + studentId));
            }

            return theStudent;
        } finally {
            // clean up JDBC object
            close(myConn, myStmt, myRs);
        }
    }

    public void updateStudent(Student theStudent) throws SQLException {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            // get db connection
            myConn = dataSource.getConnection();
            // create sql update statement
            String sql = "UPDATE student " +
                    "SET first_name = ?, last_name = ?,email = ? " +
                    "WHERE id = ?";

            // prepare statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.setInt(4, theStudent.getId());

            // execute sql statement
            myStmt.executeUpdate();

        }
        finally{
            // clean up JDBC object
            close(myConn, myStmt, null);
        }
    }
}
