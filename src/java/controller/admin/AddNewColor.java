package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product_color;
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

@WebServlet(name = "AddNewColor", urlPatterns = {"/AddNewColor"})
public class AddNewColor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productColor = (String) request.getAttribute("option");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();

        Product_color product_color = new Product_color();
        product_color.setColor(productColor);
        product_color.setStatus(activeStatus);

        hibernateSession.save(product_color);
        hibernateSession.beginTransaction().commit();
        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(false, "Product Color Adding Successfull");
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
