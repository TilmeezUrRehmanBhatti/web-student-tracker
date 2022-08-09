package com.tilmeez.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "StudentControllerServlet", value = "/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

    private StudentDbUtil studentDbUtil;

    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    // this method is called by the JAVAEE server or by Tomcat when this servlet is first loaded
    @Override
    public void init() throws ServletException {
        super.init();

        // create our student db util ... and pass in the connection pool/datasource
        try {
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // read the "command" parameter
            String theCommand = request.getParameter("command");

            // if the command is missing, then default to list students
            if (theCommand == null) {
                theCommand = "LIST";
            }

            // route to the appropriate method
            switch (theCommand) {

                case "LIST":
                    listStudents(request, response);
                    break;

                case "LOAD":
                    loadStudent(request, response);
                    break;
                case "UPDATE":
                    updateStudent(request, response);
                    break;
                case "DELETE":
                    deleteStudent(request, response);
                    break;
                case "SEARCH":
                    searchStudent(request, response);
                    break;

                default:
                    listStudents(request, response);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void searchStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        // read search name from data
        String theSearchName = request.getParameter("theSearchName");

        // search student from db util
        List<Student> students = studentDbUtil.searchStudents(theSearchName);

        // add students to the request
        request.setAttribute("STUDENT_LIST", students);

        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{
            // read the "command" parameter
            String theCommand = request.getParameter("command");

            // route to the appropriate method
            switch (theCommand) {
                case "ADD":
                    addStudent(request, response);
                    break;
                default:
                    listStudents(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read student info from form data
        String theStudentId = request.getParameter("studentId");

        // delete student from database
        studentDbUtil.deleteStudent(theStudentId);

        // send them back to "lists students" page
        listStudents(request, response);
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read student info from form data
        int id = Integer.parseInt(request.getParameter("studentId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // create a new student object
        Student theStudent = new Student(id, firstName, lastName, email);

        // perform update on database
        studentDbUtil.updateStudent(theStudent);

        // send then back to the "list students" page
        listStudents(request, response);

    }

    private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read student id from form data
        String theStudentId = request.getParameter("studentId");

        // get student from database (db util)
        Student theStudent = studentDbUtil.getStudent(theStudentId);

        // place student in the request attribute
        request.setAttribute("THE_STUDENT", theStudent);

        // send to jsp page : update-student-form.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read student info from form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // create a new student object
        Student theStudent = new Student(firstName, lastName, email);

        // add the student to the database
        studentDbUtil.addStudent(theStudent);

        // send back to main page (the Student list)
        // SEND AS REDIRECT to avoid multiple-browser reload issue
//        listStudents(request, response);
        response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{

        // get student from dbUtil
        List<Student> students = studentDbUtil.getStudent();

        // add student to the request
        request.setAttribute("STUDENT_LIST", students);

        // sent to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }

}
