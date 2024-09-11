package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.User;
import entity.Verified_status;
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

@WebServlet(name = "VerifyNewUser", urlPatterns = {"/VerifyNewUser"})
public class VerifyNewUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String token = request.getParameter("token");
        String email = (String) request.getSession().getAttribute("userEmail");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Criteria userCriteria = hibernateSession.createCriteria(User.class);

        userCriteria.add(Restrictions.eq("email", email));
        User user = (User) userCriteria.uniqueResult();

        Response_DTO response_DTO;

        if (!user.getToken().equals(token)) {
            // token not match
            response_DTO = new Response_DTO(false, "Incorrect Validation Token");

        } else {
            Criteria verifiedCriteria = hibernateSession.createCriteria(Verified_status.class);
            verifiedCriteria.add(Restrictions.eq("status", "Verified"));

            Verified_status verified_status = (Verified_status) verifiedCriteria.uniqueResult();
            user.setVerified_status(verified_status);

            response_DTO = new Response_DTO(true, "Account Verification Successfull");

        }

        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
