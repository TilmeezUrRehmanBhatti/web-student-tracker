package com.tilmeez.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
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
            // list the student ... in MVC fashion
            listStudent(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{

        // get student from dbUtil
        List<Student> students = studentDbUtil.getStudent();

        // add student to the request
        request.setAttribute("STUDENT_LIST", students);

        // sent to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }

}
