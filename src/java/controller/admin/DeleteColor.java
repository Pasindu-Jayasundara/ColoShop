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

@WebServlet(name = "DeleteColor", urlPatterns = {"/DeleteColor"})
public class DeleteColor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        int colorId = Integer.parseInt(request.getParameter("colorId"));
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isDone = true;
        String message = "";

        Product_color productColor = (Product_color) hibernateSession.load(Product_color.class, colorId);
        if (productColor != null) {
            //color avaliable
            if (productColor.getStatus().getStatus().equals("Active")) {

                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("status", "De-Active"));
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

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(isDone, message);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    } 
}
