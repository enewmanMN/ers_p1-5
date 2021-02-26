package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.Credentials;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import com.sun.xml.bind.v2.TODO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="/User", urlPatterns = "/User")
public class UserServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(UserServlet.class);

    private final UserService USER_SERVICE = UserService.getInstance();
    private final UserRepository USER_REPO = UserRepository.getInstance();

    /**
     * Method for posting a new user when an admin hits this endpoint and calls a post
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        User rqstr = (session == null) ? null : (User) request.getSession(false).getAttribute("this-user");

        //admin
        try {
            out.write("<p>im in creating post</p>");
            out.write("Role here with name: " + rqstr.getUsername() +" "+ rqstr.getUserRole());
            if (rqstr.getUserRole() == 1) {
                out.write("<p>im here</p>");

                User newUser = mapper.readValue(request.getInputStream(), User.class);

                out.write(newUser.toString());

                LOG.info("Adding new user, {}", newUser.toString());
                USER_SERVICE.register(newUser);

                response.setStatus(200);
            }

        }catch (MismatchedInputException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("400 error");
        }

    }

    /**
     * Method for putting a updated a user when an admin hits this endpoint and calls a put
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json");

            HttpSession session = request.getSession(false);
            User rqstr = (session == null) ? null : (User) request.getSession(false).getAttribute("this-user");

            try {
                if (rqstr.getUserRole() == 1) {
                    User updateUser = mapper.readValue(request.getInputStream(), User.class);
                    LOG.info("Updating user, {}", updateUser.toString());

                    UserService.getInstance().update(updateUser);

                    response.setStatus(200);
                }

            }catch (MismatchedInputException e) {
                e.printStackTrace();
                response.setStatus(400);
                out.write("400 error");
            }

    }

    /**
     * Method for deleting user when an admin hits this endpoint and calls a delete
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        User rqstr = (session == null) ? null : (User) request.getSession(false).getAttribute("this-user");

        try {
            if (rqstr.getUserRole() == 1) {
                User deleteUser = mapper.readValue(request.getInputStream(), User.class);
                LOG.info("Deleting user, {}", deleteUser.toString());

                int deletedUserId = deleteUser.getUserId();

                USER_SERVICE.deleteUserById(deletedUserId);

                response.setStatus(200);
            }

        }catch (MismatchedInputException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("400 error");
        }

    }

    /**
     * Method for getting user by their username when an admin hits this endpoint and calls a get
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        User rqstr = (session == null) ? null : (User) request.getSession(false).getAttribute("this-user");

            try {
                if (rqstr.getUserRole() == 1) {

                    // TODO: 2/26/2021  add a get all user

                    User getUser = mapper.readValue(request.getInputStream(), User.class);
                    LOG.info("Getting user, {}", getUser.toString());



                    User user = UserService.getInstance().getByUsername(getUser.getUsername());

                    out.write(mapper.writeValueAsString(user));


                    response.setStatus(200);
                }

            }catch (MismatchedInputException e) {
                e.printStackTrace();
                response.setStatus(400);
                out.write("400 error");
            }

    }

}
