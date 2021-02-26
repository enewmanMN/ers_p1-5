package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.Credentials;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
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

@WebServlet(name="/Employee", urlPatterns = "/Employee")
public class UserServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(UserServlet.class);

    private final UserService USER_SERVICE = UserService.getInstance();

    ReimbursementService reimbursementService = new ReimbursementService();

    //register new user or reimbursement
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        User rqstr = (session == null) ? null : (User) request.getSession(false).getAttribute("this-user");

        //admin
        if (rqstr.getUserRole() == 1) {
            try {
                User newUser = mapper.readValue(request.getInputStream(), User.class);
                LOG.info("Adding new user, {}", newUser.toString());
                USER_SERVICE.register(newUser);

                Reimbursement reimbursement = mapper.readValue(request.getInputStream(), Reimbursement.class);

                reimbursementService.save(reimbursement);

                out.write(mapper.writeValueAsString(reimbursement));


                response.setStatus(200);


            }catch (MismatchedInputException e) {
                e.printStackTrace();
                response.setStatus(400);
                out.write("400 error");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        PrintWriter writer = resp.getWriter();
//        String userIdParam = req.getParameter("userId");
//        resp.setContentType("text/html");
//
//        try {
//
//            if (userIdParam != null) {
//                int userId = Integer.parseInt(userIdParam);
//                LOG.info("Attempting to confirm account with id, {}", userId);
//                USER_SERVICE.confirmAccount(userId);
//                LOG.info("Account belonging to user with id, {}, successfully confirmed.", userId);
//                resp.sendRedirect(APP_URL);
//            } else {
//                throw new InvalidRequestException();
//            }
//
//        } catch (NumberFormatException | InvalidRequestException e) {
//            LOG.warn(e.getMessage());
//            resp.setStatus(400);
//            writer.write(errRespFactory.generateErrorResponse(HttpStatus.BAD_REQUEST).toJSON());
//        } catch (ResourceNotFoundException e) {
//            LOG.warn(e.getMessage());
//            resp.setStatus(404);
//            writer.write(errRespFactory.generateErrorResponse(HttpStatus.NOT_FOUND).toJSON());
//        } catch (Exception e) {
//            LOG.error(e.getMessage());
//            resp.setStatus(500);
//            writer.write(errRespFactory.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR).toJSON());
//        }
    }

}
