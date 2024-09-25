package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
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

@WebServlet(name = "AddBrand", urlPatterns = {"/AddBrand"})
public class AddNewBrand extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String newBrand = (String) request.getAttribute("option");
        
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();
        
        Brand brand = new Brand();
        brand.setBrand(newBrand);
        brand.setStatus(activeStatus);
        
        hibernateSession.save(brand);
        hibernateSession.beginTransaction().commit();

        //brand
        Criteria brandCriteria = hibernateSession.createCriteria(Brand.class);
        brandCriteria.add(Restrictions.eq("status", activeStatus));
        ArrayList<Brand> brandList = (ArrayList<Brand>) brandCriteria.list();
        
        hibernateSession.close();
        
        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(brandList));
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        
    }
    
}
