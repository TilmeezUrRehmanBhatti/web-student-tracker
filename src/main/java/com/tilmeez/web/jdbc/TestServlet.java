package com.tilmeez.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "TestServlet", value = "/TestServlet")
public class TestServlet extends HttpServlet {

    // Define datasource / connection pool for resource Injection
    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Step 1: Set up the printwriter
        PrintWriter out = response.getWriter();
        response.setContentType("text/plan");
        out.println("Email: ");

        // Step 2 : Get a connection to a database
        Connection myConn;
        Statement myStmt;
        ResultSet myRs;

        try {
            myConn = dataSource.getConnection();

            // Step 3: Create a SQL statement
            String sql = "SELECT * FROM student";
            myStmt = myConn.createStatement();

            // Step 4: Execute SQL query
            myRs = myStmt.executeQuery(sql);

            // Step 5: Process the result set
            while (myRs.next()) {
                String email = myRs.getString("email");
                out.println("Email: ");
                out.println(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e);
        }


    }

}
