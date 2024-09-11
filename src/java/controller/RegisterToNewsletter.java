package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Wishlist;
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

        String email = request.getParameter("email");
        
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Wishlist wishlist = new Wishlist();
        wishlist.setEmail(email);
        
        hibernateSession.save(wishlist);
        hibernateSession.beginTransaction();
        
        Response_DTO response_DTO = new Response_DTO(true, "Subscribed Successfuly");
        Gson gson = new Gson();
        
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
