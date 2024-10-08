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

@WebServlet(name = "AddCategory", urlPatterns = {"/AddCategory"})
public class AddNewCategory extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String newCategory = (String) request.getAttribute("option");
        
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();
        
        Category category = new Category();
        category.setCategory(newCategory);
        category.setStatus(activeStatus);
        
        hibernateSession.save(category);
        hibernateSession.beginTransaction().commit();

        //category
        Criteria categoryCriteria = hibernateSession.createCriteria(Category.class);
        categoryCriteria.add(Restrictions.eq("status", activeStatus));
        ArrayList<Category> categoryList = (ArrayList<Category>) categoryCriteria.list();
        
        hibernateSession.close();
        
        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(categoryList));
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        
    }
    
}
