package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Account_type;
import entity.Seller;
import entity.Status;
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

@WebServlet(name = "BecomeSeller", urlPatterns = {"/BecomeSeller"})
public class BecomeSeller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserTable user = (UserTable) request.getSession().getAttribute("user");
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        // change user account type
        Criteria accountTypeCriteria = hibernateSession.createCriteria(Account_type.class);
        accountTypeCriteria.add(Restrictions.eq("type", "Seller"));

        Account_type accountType = (Account_type) accountTypeCriteria.uniqueResult();

        user.setAccount_type(accountType);
        hibernateSession.update(user);

        //add to seller table
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status status = (Status) statusCriteria.uniqueResult();

        Criteria sellerCriteria = hibernateSession.createCriteria(Seller.class);
        sellerCriteria.add(Restrictions.and(
                Restrictions.eq("user", user),
                Restrictions.eq("status", status)
        ));

        if (sellerCriteria.list().isEmpty()) {
            //not a seller
            // need to add to seller table

            Seller seller = new Seller();
            seller.setStatus(status);
            seller.setUser(user);

            hibernateSession.save(seller);
        }

        hibernateSession.beginTransaction().commit();
        hibernateSession.close();

        request.getSession().setAttribute("user", user);

        Response_DTO response_DTO = new Response_DTO(true, "Update Account To Seller Success");
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
