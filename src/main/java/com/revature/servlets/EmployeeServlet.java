package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.User;
import com.revature.repositories.ReimbursementsRepository;
import com.revature.repositories.UserRepository;
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
import java.util.List;

@WebServlet(name="/Employee", urlPatterns = "/Employee")
public class EmployeeServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(UserServlet.class);

    private final UserService USER_SERVICE = UserService.getInstance();
    private final UserRepository USER_REPO = UserRepository.getInstance();

    private final ReimbursementsRepository reimbRepo = ReimbursementsRepository.getInstance();
    private final ReimbursementService reimbService = ReimbursementService.getInstance();

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


        try {
            if (rqstr.getUserRole() == 3) {
                Reimbursement reimbursement = mapper.readValue(request.getInputStream(), Reimbursement.class);
                LOG.info("Adding new reimbursement, {}", reimbursement.toString());

                reimbService.save(reimbursement);


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

                int id = rqstr.getUserId();
                List<Reimbursement> reimbursement = reimbService.getReimbByUserId(id);

                for(Reimbursement r : reimbursement){
                    if(r.getReimbursementStatus() == ReimbursementStatus.PENDING) {

                        Reimbursement getReimb = mapper.readValue(request.getInputStream(), Reimbursement.class);
                        LOG.info("Getting reimbursement, {}", getReimb.toString());

                        // TODO: 2/26/2021 might update all pending reimbursement so will have to filter 
                        reimbService.updateEMP(getReimb);
                    }
                }



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
            if (rqstr.getUserRole() == 3) {
                int id = rqstr.getUserId();
                List<Reimbursement> reimbursement = reimbService.getReimbByUserId(id);


                out.write(mapper.writeValueAsString(reimbursement));


                response.setStatus(200);
            }

        }catch (MismatchedInputException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("400 error");
        }

    }

}
