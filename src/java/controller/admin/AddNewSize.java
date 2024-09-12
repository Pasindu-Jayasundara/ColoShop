package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product_color;
import entity.Size;
import entity.Status;
import java.io.IOException;
import java.io.PrintWriter;
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
        
        String productSize = request.getParameter("size");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();

        Size size = new Size();
        size.setSize(productSize);
        size.setStatus(activeStatus);

        hibernateSession.save(size);
        hibernateSession.beginTransaction().commit();
        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(false, "Product Size Adding Successfull");
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
