package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product_color;
import entity.Size;
import entity.Status;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "AddNewSize", urlPatterns = {"/AddNewSize"})
public class AddNewSize extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String productSize = (String) request.getAttribute("option");
        
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();
        
        Size size = new Size();
        size.setSize(productSize);
        size.setStatus(activeStatus);
        
        hibernateSession.save(size);
        hibernateSession.beginTransaction().commit();

        //size
        Criteria sizeCriteria = hibernateSession.createCriteria(Size.class);
        sizeCriteria.add(Restrictions.eq("status", activeStatus));
        ArrayList<Size> sizeList = (ArrayList<Size>) sizeCriteria.list();
        
        hibernateSession.close();
        
        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(sizeList));
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }
    
}
