package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
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

@WebServlet(name = "DeleteBrand", urlPatterns = {"/DeleteBrand"})
public class DeleteBrand extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        int brandId = (int) request.getAttribute("brandId");
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isDone = true;
        String message = "";

        Brand brand = (Brand) hibernateSession.load(Brand.class, brandId);
        if (brand != null) {
            //brand avaliable
            if (brand.getStatus().getName().equals("Active")) {

                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("status", "De-Active"));
                Status deActiveStatus = (Status) statusCriteria.uniqueResult();

                brand.setStatus(deActiveStatus);
                hibernateSession.update(brand);
                hibernateSession.beginTransaction().commit();

                message = "Product Brand Removing Success";
            }
        } else {
            //no brand
            isDone = false;
            message = "Product Brand not Found";
        }

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(isDone, message);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }
}
