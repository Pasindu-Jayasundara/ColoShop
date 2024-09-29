package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Message;
import entity.Message_status;
import entity.UserTable;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "UserContactMessage", urlPatterns = {"/UserContactMessage"})
public class MessageToAdmin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        UserTable user = (UserTable) request.getSession().getAttribute("user");
        String title = (String) request.getAttribute("title");
        String message = (String) request.getAttribute("msg");

        
        Criteria msgCriteria = hibernateSession.createCriteria(Message_status.class);
        msgCriteria.add(Restrictions.eq("status", "Received"));
        Message_status msgStatus= (Message_status) msgCriteria.uniqueResult();
        
        Message messageObj = new Message();
        messageObj.setMessage(message);
        messageObj.setTitle(title);
        messageObj.setDatetime(new Date());
        messageObj.setMessage_status(msgStatus);
        messageObj.setUser(user);

        hibernateSession.save(messageObj);
        hibernateSession.beginTransaction().commit();
        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(true, "Message Sent Successfuly");
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
