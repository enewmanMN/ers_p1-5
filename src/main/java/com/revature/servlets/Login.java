package com.revature.servlets;

import com.revature.models.User;
import com.revature.services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class Login extends HttpServlet {

    // Method to handle GET method request.
    // Carries request parameters in appended url string so less secure
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Login</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <form method=\"post\" action=\"login\">\n" +
                "                Email ID:<input type=\"text\" name=\"username\" /><br/>\n" +
                "                Password:<input type=\"text\" name=\"password\" /><br/>\n" +
                "        <input type=\"submit\" value=\"login\" />\n" +
                "        </form>\n" +
                "    </body>\n" +
                "</html>" +
                "");

    }

    // Method to handle POST method request. - more secure way of sending data from client to server
    // -Carries request paramaters in message body
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserService userService = new UserService();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.authenticate(username, password);
        if (userService.isUserValid(user)) {
            out.write("<h1>/Login credentials valid</h1>");
        }
        else
        {
            out.println("Username or Password incorrect");
            RequestDispatcher rs = request.getRequestDispatcher("index.html");
            rs.include(request, response);
        }
    }
}
