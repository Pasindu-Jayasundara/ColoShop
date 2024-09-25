package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
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

@WebServlet(name = "DeleteBrand", urlPatterns = {"/DeleteBrand"})
public class DeleteBrand extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String id = (String) request.getAttribute("id");
        int brandId = Integer.parseInt(id);
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        boolean isDone = true;
        String message = "";
        
        Brand brand = (Brand) hibernateSession.load(Brand.class, brandId);
        if (brand != null) {
            //brand avaliable
            if (brand.getStatus().getName().equals("Active")) {
                
                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("name", "De-Active"));
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
        
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();

        //brand
        Criteria brandCriteria = hibernateSession.createCriteria(Brand.class);
        brandCriteria.add(Restrictions.eq("status", activeStatus));
        ArrayList<Brand> brandList = (ArrayList<Brand>) brandCriteria.list();
        
        hibernateSession.close();
        
        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(isDone, gson.toJsonTree(brandList));
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }
}
