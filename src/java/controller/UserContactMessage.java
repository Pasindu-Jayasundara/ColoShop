package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Message;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "UserContactMessage", urlPatterns = {"/UserContactMessage"})
public class UserContactMessage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            int userId = request.getSession().getAttribute("user").getId();
            String title = request.getParameter("title");
            String message = request.getParameter("message");
            
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatedDate = sdf.format(date);
            
            Message messageObj = new Message();
            messageObj.setMessage(message);
            messageObj.setTitle(title);
            messageObj.setDatetime(sdf.parse(formatedDate));//parse error
            messageObj.setId(userId);
            
            
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            hibernateSession.save(messageObj);
            
            hibernateSession.beginTransaction().commit();
            hibernateSession.close();
            
            Response_DTO response_DTO = new Response_DTO(true, "Message Sent Successfuly");
            Gson gson = new Gson();
            
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        } catch (ParseException ex) {
            ex.printStackTrace();
            
            Response_DTO response_DTO = new Response_DTO(false, ex);
            Gson gson = new Gson();
            
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
            
        }

    }

}
