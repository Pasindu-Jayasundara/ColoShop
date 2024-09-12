package controller.seller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Account_type;
import entity.UserTable;
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

@WebServlet(name = "BecomeUser", urlPatterns = {"/BecomeUser"})
public class BecomeUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserTable user = (UserTable) request.getSession().getAttribute("user");
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Criteria accountTypeCriteria = hibernateSession.createCriteria(Account_type.class);
        accountTypeCriteria.add(Restrictions.eq("type", "Buyer"));

        Account_type accountType = (Account_type) accountTypeCriteria.uniqueResult();

        user.setAccount_type(accountType);
        hibernateSession.update(user);
        hibernateSession.beginTransaction().commit();

        hibernateSession.close();

        request.getSession().setAttribute("user", user);

        Response_DTO response_DTO = new Response_DTO(true, "Update Account To User Success");
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
