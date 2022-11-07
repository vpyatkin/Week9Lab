package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Role;
import models.User;
import services.RoleService;
import services.UserService;

public class UserServlet extends HttpServlet {
    boolean edit = false;
    boolean noUsers = false;
    boolean error = false;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UserService us = new UserService();
        RoleService rs = new RoleService();
        List <User> userList;
        List <Role> userRoles;
        String action = request.getParameter("action");
        String email = request.getParameter("userEmail");
        int userID = 0;
        
        
        if (action == null){
            try {
                userList = us.getAll();
                userRoles = rs.getAll(); 

                if (userList.isEmpty()) {
                    noUsers = true;
                }
                else {
                    noUsers = false;
                }
            
                request.setAttribute("userList", userList); 
                request.setAttribute("userRoles", userRoles);          
                request.setAttribute("edit", edit);
                request.setAttribute("noUsers", noUsers);

                edit = false;
                error = false;
                getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
                session.setAttribute("message", "");
            
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(action.equals("edit")){
            edit = true;  

            try {
                email = request.getParameter("userEmail");
                User user = us.get(email);
                userID = user.getRole().getRoleId();
                response.sendRedirect("/");
                
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            session.setAttribute("email", email);
            session.setAttribute("userID", userID);  
        } 
        
        else if(action.equals("delete")){          
            try {               
                us.delete(email);
                response.sendRedirect("/");
            } catch (Exception ex) {               
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();   
        String action = request.getParameter("action");
        UserService us = new UserService();
        RoleService rs = new RoleService();
        List <User> userList;
        List <Role> userRoles;
             

        
        if (action.equals("add")){
            
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            int ID = 0;
            if (role.equals("system admin")) {
                ID = 1;
            }
            else {
                ID = 2;
            }
            try {
                userList = us.getAll();
                for (User user : userList){
                    if (user.getEmail().equals(email)){
                        error = true;
                    }
                }
                if (error == true){
                    session.setAttribute("message", "Please enter a different email address.");
                    response.sendRedirect("/");
                }
                else {
                    Role userRole = new Role(ID, role);
                    User user = new User(email, firstName, lastName, password, userRole);
                    
                    us.insert(user);
                    userList = us.getAll();
                    userRoles = rs.getAll();
                    request.setAttribute("userList", userList); 
                    request.setAttribute("userRoles", userRoles);
                    response.sendRedirect("/");
                    
                }
               
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
        
        else if (action.equals("update")){
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String password = request.getParameter("password");

            try {
                String email = (String) session.getAttribute("email");
                User userRoleID = us.get(email);
                int roleID = userRoleID.getRole().getRoleId();
                Role role = rs.get(roleID);
                session.setAttribute("userRole", role.getRoleId());
                User user = new User(email, firstName, lastName, password, role);
                
                
                if (firstName == "" || lastName == "" || password == ""){    
                    error = true;
                }
                if (error == true){
                    edit = true;
                    session.setAttribute("message", "Please fill out all fields.");
                    response.sendRedirect("/users");
                }
                else {
                    edit = false;
                    us.update(user);
                    //action = "edit";
                    response.sendRedirect("/users");
                }
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }      
    }                  
}
