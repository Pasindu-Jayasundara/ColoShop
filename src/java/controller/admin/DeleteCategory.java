package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Category;
import entity.Status;
import java.io.IOException;
import java.util.ArrayList;
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

        String id = (String) request.getAttribute("id");
        int categoryId = Integer.parseInt(id);
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isDone = true;
        String message = "";

        Category category = (Category) hibernateSession.load(Category.class, categoryId);
        if (category != null) {
            //category avaliable
            if (category.getStatus().getName().equals("Active")) {

                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("name", "De-Active"));
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

        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();

        //category
        Criteria categoryCriteria = hibernateSession.createCriteria(Category.class);
        categoryCriteria.add(Restrictions.eq("status", activeStatus));
        ArrayList<Category> categoryList = (ArrayList<Category>) categoryCriteria.list();
        
        hibernateSession.close();
        
        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(isDone, gson.toJsonTree(categoryList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }
}
