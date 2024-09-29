package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.UserTable;
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

        String token = (String) request.getAttribute("token");
        String email = (String) request.getSession().getAttribute("userEmail");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Criteria userCriteria = hibernateSession.createCriteria(UserTable.class);

        userCriteria.add(Restrictions.eq("email", email));
        UserTable user = (UserTable) userCriteria.uniqueResult();

        Response_DTO response_DTO;

        if (user == null) {
            //missing user
            response_DTO = new Response_DTO(false, "Missing User");

        } else {
            if (!user.getToken().equals(token)) {
                // token not match
                response_DTO = new Response_DTO(false, "Incorrect Validation Token");

            } else {
                Criteria verifiedCriteria = hibernateSession.createCriteria(Verified_status.class);
                verifiedCriteria.add(Restrictions.eq("status", "Verified"));

                Verified_status verified_status = (Verified_status) verifiedCriteria.uniqueResult();
                user.setVerified_status(verified_status);

                hibernateSession.update(user);
                hibernateSession.beginTransaction().commit();

                response_DTO = new Response_DTO(true, "Account Verification Successfull");
                request.getSession().invalidate();

            }
        }

        hibernateSession.close();

        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
