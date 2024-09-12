package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.AdminDetailTable;
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

@WebServlet(name = "AdminLogin", urlPatterns = {"/AdminLogin"})
public class AdminLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Criteria adminCriteria = hibernateSession.createCriteria(AdminDetailTable.class);

        adminCriteria.add(Restrictions.and(
                Restrictions.eq("email", email),
                Restrictions.eq("password", password))
        );
        
        String message = "";
        boolean isSuccess = false;

        AdminDetailTable admin = (AdminDetailTable) adminCriteria.uniqueResult();
        if (admin != null) {
            if (admin.getVerified_status().getStatus().equals("Not-verified")) {
                //not verified
                message = "Not Verified";

            } else {
                //verified
                request.getSession().setAttribute("admin", admin);
                
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
