package controller;

import com.Cypher;
import com.Email;
import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Account_type;
import entity.Status;
import entity.User;
import entity.Verified_status;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "UserRegister", urlPatterns = {"/UserRegister"})
public class UserRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        final String email = request.getParameter("email");
        String password = request.getParameter("password");

        String encryptPassword;
        try {
            encryptPassword = Cypher.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
            
            encryptPassword = password;
        }
        
        
        
        User user = new User();
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setEmail(email);
        user.setPassword(encryptPassword);

        Account_type accountType = new Account_type();
        accountType.setId(2); // buyer
        user.setAccount_type(accountType);

        Verified_status verified_status = new Verified_status();
        verified_status.setId(2);//not verified
        user.setVerified_status(verified_status);

        Status status = new Status();
        status.setId(1);//active
        user.setStatus(status);

        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        user.setToken(token);

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        hibernateSession.save(user);

        Thread emailThred = new Thread(new Runnable() {
            @Override
            public void run() {

                String fromEmailAddress="pasindubathiya28@gmail.com";
                String appPassword = "";
                String to = email;
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

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(false, "Please Verify Your Account");
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
