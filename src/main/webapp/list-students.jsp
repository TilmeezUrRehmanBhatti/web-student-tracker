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
        </div>
    </div>

</body>

</html>
