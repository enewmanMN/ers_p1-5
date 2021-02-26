package com.revature.servlets;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

// Extend HttpServlet class
public class ReadParams extends HttpServlet {

    // Method to handle GET method request.
    // Carries request parameters in appended url string so less secure
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "Reading All Form Parameters";
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType +
                        "<html>\n" +
                        "<head><title>" + title + "</title></head>\n" +
                        "<body bgcolor = \"#f0f0f0\">\n" +
                        "<h1 align = \"center\">" + title + "</h1>\n" +
                        "<table width = \"100%\" border = \"1\" align = \"center\">\n" +
                        "<tr bgcolor = \"#949494\">\n" +
                        "<th>Param Name</th>" +
                        "<th>Param Value(s)</th>\n"+
                        "</tr>\n"
        );

        Enumeration paramNames = request.getParameterNames();

        while(paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();
            out.print("<tr><td>" + paramName + "</td>\n<td>");
            String[] paramValues = request.getParameterValues(paramName);

            // Read single valued data
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() == 0)
                    out.println("<i>No Value</i>");
                else
                    out.println(paramValue);
            } else {
                // Read multiple valued data
                out.println("<ul>");

                for(int i = 0; i < paramValues.length; i++) {
                    out.println("<li>" + paramValues[i]);
                }
                out.println("</ul>");
            }
        }
        //"/read ? first_name (user value) : last_name (uservalue)"
        out.println("</tr>\n</table>\n" +
                "<form action = \"read\" method = \"POST\">\n" +
                "    Username: <input type = \"text\" name = \"first_name\">\n" +
                "    <br />\n" +
                "    Password: <input type = \"text\" name = \"last_name\" />\n" +
                "    <input type = \"submit\" value = \"Submit\" />\n" +
                "</form></body></html>");
    }

    // Method to handle POST method request. - more secure way of sending data from client to server
    // -Carries request paramaters in message body
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}