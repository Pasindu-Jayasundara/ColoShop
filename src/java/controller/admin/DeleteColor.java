package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product_color;
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

@WebServlet(name = "DeleteColor", urlPatterns = {"/DeleteColor"})
public class DeleteColor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String id = (String) request.getAttribute("id");
        int colorId = Integer.parseInt(id);
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isDone = true;
        String message = "";

        Product_color productColor = (Product_color) hibernateSession.load(Product_color.class, colorId);
        if (productColor != null) {
            //color avaliable
            if (productColor.getStatus().getName().equals("Active")) {

                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("name", "De-Active"));
                Status deActiveStatus = (Status) statusCriteria.uniqueResult();

                productColor.setStatus(deActiveStatus);
                hibernateSession.update(productColor);
                hibernateSession.beginTransaction().commit();

                message = "Product Color Removing Success";
            }
        } else {
            //no color
            isDone = false;
            message = "Product Color not Found";
        }

        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();
        
        //color
        Criteria colorCriteria = hibernateSession.createCriteria(Product_color.class);
        colorCriteria.add(Restrictions.eq("status", activeStatus));
        ArrayList<Product_color> colorList = (ArrayList<Product_color>) colorCriteria.list();
        
        hibernateSession.close();
        
        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(colorList));
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    } 
}
