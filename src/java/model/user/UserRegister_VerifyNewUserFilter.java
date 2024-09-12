package model.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Status;
import entity.UserTable;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UserRegister_VerifyNewUserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String email = request.getParameter("email");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        //status
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();

        //find user
        Criteria userCriteria = hibernateSession.createCriteria(UserTable.class);

        userCriteria.add(Restrictions.and(
                Restrictions.eq("email", email), 
                Restrictions.eq("status", activeStatus))
        );

        if (!userCriteria.list().isEmpty()) {
            // already emailregistered
            Response_DTO response_DTO = new Response_DTO(false, "User Already Registered");
            Gson gson = new Gson();

            hibernateSession.close();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
    }

}
