package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.Credentials;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="/Employee", urlPatterns = "/Employee")
public class EmployeeServlet extends HttpServlet {

    ReimbursementService reimbursementService = new ReimbursementService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");

        try {
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
