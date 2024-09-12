package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "AddCategory", urlPatterns = {"/AddCategory"})
public class AddNewCategory extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String newCategory = request.getParameter("category");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Category category = new Category();
        category.setCategory(newCategory);

        hibernateSession.save(category);
        hibernateSession.beginTransaction().commit();
        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(false, "Product Category Adding Successfull");
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        
    }

}
