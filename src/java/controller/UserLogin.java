package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "UserLogin", urlPatterns = {"/UserLogin"})
public class UserLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Criteria userCriteria = hibernateSession.createCriteria(User.class);

        userCriteria.add(Restrictions.and(
                Restrictions.eq("email", email),
                Restrictions.eq("password", password))
        );

        String message = "";
        boolean isSuccess = false;

        User user = (User) userCriteria.uniqueResult();
        if (user != null) {
            if (user.getVerified_status().getStatus().equals("Not-verified")) {
                //not verified
                message = "Not Verified";

            } else {
                //verified
                request.getSession().setAttribute("user", user);
                
                //sync session cart and user cart
                request.getRequestDispatcher("/SyncSessionAndUserCart").include(request, response);
                
                message = "Login Success";
                isSuccess = true;
            }
        } else {
            message = "Invalid Details";
        }

        hibernateSession.close();

        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(isSuccess, message);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
