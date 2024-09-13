package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Category;
import entity.Status;
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

@WebServlet(name = "DeleteCategory", urlPatterns = {"/DeleteCategory"})
public class DeleteCategory extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isDone = true;
        String message = "";

        Category category = (Category) hibernateSession.load(Category.class, categoryId);
        if (category != null) {
            //category avaliable
            if (category.getStatus().getName().equals("Active")) {

                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("status", "De-Active"));
                Status deActiveStatus = (Status) statusCriteria.uniqueResult();

                category.setStatus(deActiveStatus);
                hibernateSession.update(category);
                hibernateSession.beginTransaction().commit();

                message = "Category Removing Success";
            }
        } else {
            //no category
            isDone = false;
            message = "Category not Found";
        }

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(isDone, message);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }
}
