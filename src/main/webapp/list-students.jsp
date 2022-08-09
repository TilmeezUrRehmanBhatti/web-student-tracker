<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Student Tracker App</title>

    <link type="text/css" rel="stylesheet" href="css/style.css">

</head>



<body>

    <div id="wrapper">
        <div id="header">
          <h2>Student's University</h2>
        </div>
    </div>

    <div id="container">
        <div id="content">

            <!-- put new button: Add Student -->
            <input type="button" value="Add Student"
            onclick="window.location.href='add-student-form.jsp';return false"
            class="add-student-button"/>


            <table>

                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>email</th>
                </tr>

                <c:forEach var="tempStudent" items="${STUDENT_LIST}" >
                        <tr>
                            <td>${tempStudent.firstName}</td>
                            <td>${tempStudent.lastName}</td>
                            <td>${tempStudent.email}</td>
                </c:forEach>

            </table>
            <div style="clear: both"></div>

            <p>
                <a href="StudentControllerServlet">Back to Complete List</a>
            </p>
        </div>
    </div>

</body>

</html>
