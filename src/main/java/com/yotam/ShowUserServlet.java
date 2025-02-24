package com.yotam;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yotamm on 28/04/16.
 */

//This servlet must be called: /showuser?id=<username>

@WebServlet("/showuser")
public class ShowUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("id");
        if (userId == null){
            throw new ServletException("Missing parameter id");
        }

        UsersDB usersDB = UsersDB.getInstance();
        User user = usersDB.getUser(userId); // fetch the user from DB

        if (user == null){ //user not found
            resp.setStatus(404);
            req.getRequestDispatcher("notfound.jsp").forward(req, resp);
            return;
        }

        //Forward the request to showuser.jsp to render the user page
        req.setAttribute("model", user);
        req.getRequestDispatcher("showuser.jsp").forward(req, resp);
    }
}
