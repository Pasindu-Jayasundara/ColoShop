package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Size;
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

@WebServlet(name = "DeleteSize", urlPatterns = {"/DeleteSize"})
public class DeleteSize extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        int sizeId = (int) request.getAttribute("sizeId");
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isDone = true;
        String message = "";

        Size size = (Size) hibernateSession.load(Size.class, sizeId);
        if (size != null) {
            //size avaliable
            if (size.getStatus().getName().equals("Active")) {

                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("status", "De-Active"));
                Status deActiveStatus = (Status) statusCriteria.uniqueResult();

                size.setStatus(deActiveStatus);
                hibernateSession.update(size);
                hibernateSession.beginTransaction().commit();

                message = "Product Size Removing Success";
            }
        } else {
            //no size
            isDone = false;
            message = "Product Size not Found";
        }

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(isDone, message);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
