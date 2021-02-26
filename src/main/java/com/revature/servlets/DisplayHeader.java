package com.revature.servlets;

import com.revature.models.User;
import com.revature.services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name="/Login", urlPatterns = "/Login")
public class DisplayHeader extends HttpServlet {
    // Method to handle GET method request.
    // Carries request parameters in appended url string so less secure
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserService userService = new UserService();
        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.authenticate(username, password);

        if (userService.isUserValid(user)) {
            out.write("<h1>/Login credentials valid</h1>");
            HttpSession session = request.getSession();
            session.setAttribute( "id", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("name", user.getFirstname());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getUserRole());

            out.println("<h1> Welcome " + session.getAttribute( "name"));
        }
        else
        {
            out.println("Username or Password incorrect");
            RequestDispatcher rs = request.getRequestDispatcher("/Login");
            rs.include(request, response);
        }

    }
}