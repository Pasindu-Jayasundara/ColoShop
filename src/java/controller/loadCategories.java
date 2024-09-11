package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;



@WebServlet(name = "loadCategories", urlPatterns = {"/loadCategories"})
public class loadCategories extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Gson gson = new Gson();
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Criteria categoryCriteria = hibernateSession.createCriteria(Category.class);
        categoryCriteria.addOrder(Order.desc("category"));
        List<Category> list = categoryCriteria.list();
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(list));
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        
        hibernateSession.close();
        
    }
}
