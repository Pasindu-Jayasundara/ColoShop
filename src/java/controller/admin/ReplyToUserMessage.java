package controller.admin;

import com.Email;
import com.google.gson.Gson;
import dto.Response_DTO;
import entity.AdminDetailTable;
import entity.Message;
import entity.Message_status;
import entity.Reply;
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

@WebServlet(name = "ReplyToUserMessage", urlPatterns = {"/ReplyToUserMessage"})
public class ReplyToUserMessage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String email = request.getParameter("email");
        String reply = request.getParameter("reply");
        int messageId = Integer.parseInt(request.getParameter("messageId"));
        
        boolean isInvalid = false;
        String errorMessage = "";
        
        AdminDetailTable admin = (AdminDetailTable) request.getSession().getAttribute("admin");
        
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Message message = (Message) hibernateSession.load(Message.class, messageId);
        if(message!=null){
            //found message
                       
            //message table
            Criteria messageStatusCriteria = hibernateSession.createCriteria(Message_status.class);
            messageStatusCriteria.add(Restrictions.eq("status", "3"));//replied
            
            Message_status messageStatus = (Message_status) messageStatusCriteria.uniqueResult();
            
            message.setAdmin(admin);
            message.setMessage_status(messageStatus);
            hibernateSession.update(message);
            
            
            //reply table
            Reply replyObj = new Reply();
            replyObj.setMessage(message);
            replyObj.setReply(reply);
            replyObj.setAdmin(admin);
            replyObj.setDatetime(new Date("yyyy-MM-dd HH:mm:ss"));
            
            hibernateSession.save(replyObj);
            
            hibernateSession.beginTransaction().commit();
            
            // send Email
            //send email verification code
            Thread emailThred = new Thread(new Runnable() {
                @Override
                public void run() {

                    String fromEmailAddress="pasindubathiya28@gmail.com";
                    String appPassword = "";
                    final String to = email;
                    String subject="";
                    String body="";

                    try {
                        Email.send(fromEmailAddress, appPassword, to, subject, body);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            emailThred.start();
            
            errorMessage = "Reply Send Success";
        }else{
            //cannot find message
            errorMessage ="Cannot Find Message";
            isInvalid=true;
        }
        
        hibernateSession.close();
        
        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, errorMessage);
            Gson gson = new Gson();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

}
