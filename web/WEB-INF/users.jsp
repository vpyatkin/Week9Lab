<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
        <title>Users</title>
    </head>
    <body>
        <h1>Manage Users</h1>
        <c:choose>
            <c:when test="${noUsers == false}">
                <table>
                    <th>Email</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Role</th>
                    <th></th>
                    <th></th>
                    <c:forEach var="user" items="${userList}">
                        <tr>
                            <td>${user.email}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <c:choose>
                                <c:when test="${user.role.roleId == 1}">
                                    <td>system admin</td>
                                </c:when>
                                <c:otherwise>
                                    <td>regular user</td>
                                </c:otherwise>
                            </c:choose>
                            </td>
                            <td><a href="<c:url value='/users?action=edit'>
                                       <c:param name='userEmail' value='${user.email}' />
                                       <c:param name='action' value='edit' />
                                   </c:url>">
                                   Edit
                                </a>
                            </td>
                            <td><a href="<c:url value='/users?action=delete'>
                                       <c:param name='userEmail' value='${user.email}' />
                                       <c:param name='action' value='delete' />
                                   </c:url>">
                                   Delete
                                </a> 
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <p><b>No users found. Please add a user.</b></p>
            </c:otherwise>
        </c:choose> 
        <form method="post" action="">
            <c:if test="${edit == false}">
                <h2>Add User</h2>
                Email: <input type="email" name="email" required><br>
                First name: <input type="text" name="firstName" required><br>
                Last name: <input type="text" name="lastName" required><br>
                Password: <input type="password" name="password" required><br>
                Role: <select name="role">
                            <c:forEach var="role" items="${userRoles}" >
                                <c:choose>
                                    <c:when test="${role.roleId == 1}">
                                        <option>system admin</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option>regular user</option>
                                    </c:otherwise>
                                </c:choose>
                          </c:forEach>
                      </select>
                <input type="hidden" name="action" value="add"><br>
                <input type="submit" value="Add User">
                <p>${message}</p>
            </c:if>
            <c:if test="${edit == true}">
                <h2>Edit User</h2>
                Email: ${email}<br>
                First name: <input type="text" name="firstName"><br>
                Last name: <input type="text" name="lastName"><br>
                Password: <input type="password" name="password"><br>
                Role: <select name="role">
                            <c:choose>
                                <c:when test="${userID == 1}">
                                    <option>system admin</option>
                                    <option>regular user</option>                              
                                </c:when>
                                <c:otherwise>
                                    <option>regular user</option>
                                    <option>system admin</option>
                                </c:otherwise>
                            </c:choose>
                    </select><br>
                <input type="hidden" name="action" value="update">
                <input type="submit" value="Update"> 
                <a href="/">
                    <input type="button" value="Cancel">
                </a> 
                <p>${message}</p>
            </c:if>
        </form>
    </body>
</html>
