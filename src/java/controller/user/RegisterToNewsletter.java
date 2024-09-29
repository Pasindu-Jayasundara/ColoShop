package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Newsletter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "RegisterToNewsletter", urlPatterns = {"/RegisterToNewsletter"})
public class RegisterToNewsletter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = (String) request.getAttribute("email");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Newsletter newsletter = new Newsletter();
        newsletter.setEmail(email);

        hibernateSession.save(newsletter);
        hibernateSession.beginTransaction().commit();

        Response_DTO response_DTO = new Response_DTO(true, "Subscribed Successfuly");
        Gson gson = new Gson();

        hibernateSession.close();
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
