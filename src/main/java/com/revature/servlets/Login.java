package com.revature.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.Credentials;

import com.revature.dtos.HttpStatus;
import com.revature.exceptions.AuthenticationException;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.ErrorResponseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class Login extends HttpServlet {

    private final ErrorResponseFactory errRespFactory = ErrorResponseFactory.getInstance();
    public final UserService userService = UserService.getInstance();
    private static final Logger LOG = LogManager.getLogger(UserServlet.class);

    // Method to handle GET method request.
    // Carries request parameters in appended url string so less secure
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response content type
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");

        try {

            Credentials credentials = mapper.readValue(request.getInputStream(), Credentials.class);

            User authenticateUser = userService.authenticate(credentials.getUsername(), credentials.getPassword());

            out.write(mapper.writeValueAsString(authenticateUser));

            LOG.info("Establishing a session for user, {}", credentials.getUsername());
            request.getSession().setAttribute("this-user", authenticateUser);

            if(authenticateUser.getUserRole().equals(3)) {
                out.write("<p> im inside employee</p>");
            }

            response.setStatus(200);
        } catch (MismatchedInputException e) {
            e.printStackTrace();
            LOG.warn(e.getMessage());
            response.setStatus(400);
            out.write(errRespFactory.generateErrorResponse(HttpStatus.BAD_REQUEST).toJSON());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            response.setStatus(401);
            out.write(errRespFactory.generateErrorResponse(401, e.getMessage()).toJSON());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            response.setStatus(500);
            out.write(errRespFactory.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR).toJSON());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session != null) {
            String username = ((User) session.getAttribute("this-user")).getUsername();
            LOG.info("Invalidating session for user, {}", username);
            req.getSession().invalidate();
        }

    }

}
