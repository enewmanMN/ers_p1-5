//package com.revature.servlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.exc.MismatchedInputException;
//import com.revature.dtos.Credentials;
//import com.revature.models.User;
//import com.revature.services.UserService;
//import org.hibernate.Session;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Enumeration;
//import java.util.logging.LogManager;
//
//@WebServlet(name="/Login", urlPatterns = "/Login")
//public class DisplayHeader extends HttpServlet {
//
//    public final UserService userService = new UserService();
//
//    // Method to handle GET method request.
//    // Carries request parameters in appended url string so less secure
//    public void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        // Set response content type
//        PrintWriter out = response.getWriter();
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json");
//
//        try {
//            Credentials credentials = mapper.readValue(request.getInputStream(), Credentials.class);
//
//            User authenticateUser = userService.authenticate(credentials.getUsername(), credentials.getPassword());
//
//            out.write(mapper.writeValueAsString(authenticateUser));
//
//            request.getSession().setAttribute("this-user", authenticateUser);
//
//            if(authenticateUser.getUserRole().equals(3)) {
//                out.write("<p> im inside employee</p>");
//            }
//
//            response.setStatus(200);
//
//
//        }catch (MismatchedInputException e) {
//            e.printStackTrace();
//            response.setStatus(400);
//            out.write("400 error");
//        }
//
////        HttpSession session = request.getSession();
////        UserService userService = new UserService();
////
////        User user = mapper.readValue(request.getInputStream(), User.class);
////        userService.authenticate(user.getUsername(), user.getPassword());
////        String userJSON = mapper.writeValueAsString(user);
////        out.write(userJSON);
////        response.setStatus(201);
////
////        String username = request.getParameter("username");
////        String password = request.getParameter("password");
////
////        User authenticateUser = userService.authenticate(user.getUsername(), user.getPassword());
////
////        if (!authenticateUser.equals(null)) {
////            out.write("<h1>/Login credentials valid</h1>");
////            HttpSession session = request.getSession();
////            session.setAttribute( "id", user.getUserId());
////            session.setAttribute("username", user.getUsername());
////            session.setAttribute("name", user.getFirstname());
////            session.setAttribute("email", user.getEmail());
////            session.setAttribute("role", user.getUserRole());
////
////            out.println("<h1> Welcome " + session.getAttribute( "name") + session.getAttribute("role"));
////
////
////        }
////        else
////        {
////            out.println("Username or Password incorrect");
////            RequestDispatcher rs = request.getRequestDispatcher("/Login");
////            rs.include(request, response);
////        }
//
//    }
//}
