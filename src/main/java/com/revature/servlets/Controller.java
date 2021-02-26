package com.revature.servlets;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@WebServlet(name="/Controller", urlPatterns = "/Controller")
public class Controller extends HttpServlet {

    // Method to handle GET method request.
    // Carries request parameters in appended url string so less secure
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        UserService userService = new UserService();
//        ReimbursementService reimbursementService = new ReimbursementService();
//        HttpSession curSession = request.getSession();
//        int userId = (int) curSession.getAttribute("id");
        HttpSession curSession = request.getSession();
        int userRole = (int) curSession.getAttribute("role");
        PrintWriter out = response.getWriter();

        out.write("<p>Your role id in Controller is : " + userRole + "</p>");

//        switch (userRole) {
//            case 1:
////                if (paramMap.containsKey("action")) {
////                    String[] adminAction = paramMap.get("action");
////                    if (adminAction[0] == "add") {
////                        User userToAdd = new User();
////                        userToAdd.setUserId(userId);
////                    }
////                }
//                out.write("<p> got to admin action in controller </p>");
//                break;
//            case 2:
////                    List<Reimbursement> reimbursementList;
////                    reimbursementList =
//
//                break;
//            case 3:
//                //we are a employee
//                System.out.println("Wednesday");
//                break;
//            case 4:
//                // we are deleted ?
//                System.out.println("Thursday");
//                break;
//            default:
//                //employee ?
//                System.out.println("Friday");
//                break;
//        }



    }


}
