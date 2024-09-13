package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Account_type;
import entity.Status;
import entity.UserTable;
import entity.Verified_status;
import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Email;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "UserRegister", urlPatterns = {"/UserRegister"})
public class UserRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
//        User_DTO fromJson = gson.fromJson(request.getReader(), User_DTO.class);

        String first_name = (String) request.getAttribute("first_name");
        String last_name = (String) request.getAttribute("last_name");
        final String email = (String) request.getAttribute("email");
        String password = (String) request.getAttribute("password");

        String encryptPassword = password;
//        String encryptPassword;
//        try {
//            encryptPassword = Cypher.encrypt(password);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            encryptPassword = password;
//        }
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        Account_type account_type=(Account_type) hibernateSession.get(Account_type.class, 2);//buyer
        Verified_status verified_status=(Verified_status) hibernateSession.get(Verified_status.class, 2);//notverified
        Status status=(Status) hibernateSession.get(Status.class, 1);//active

//        Account_type accountType = new Account_type();
//        accountType.setId(2); // buyer

//        Verified_status verified_status = new Verified_status();
//        verified_status.setId(2);//not verified

//        Status status = new Status();
//        status.setId(1);//active

        UserTable user = new UserTable();
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setEmail(email);
        user.setPassword(encryptPassword);
        user.setAccount_type(account_type);
        user.setVerified_status(verified_status);
        user.setStatus(status);

        final String token = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        user.setToken(token);

        final int addedUserId = (int) hibernateSession.save(user);
        hibernateSession.beginTransaction().commit();

        //send email verification code
        Thread emailThred = new Thread(new Runnable() {
            @Override
            public void run() {

                final String to = email;
                String subject = "Verification Code";
                String body = token;

                try {
                    Email.sendEmail(to, subject, body);

                    //delete verify token after 5 min
//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//
//                            Session openSession = HibernateUtil.getSessionFactory().openSession();
//                            Criteria createCriteria = openSession.createCriteria(UserTable.class);
//                            createCriteria.add(Restrictions.eq("id", addedUserId));
//
//                            UserTable searchedUser = (UserTable) createCriteria.uniqueResult();
//                            searchedUser.setToken("");
//
//                            openSession.update(searchedUser);
//                            openSession.beginTransaction().commit();
//                            openSession.close();
//
//                        }
//
//                    }, 300000);//5 min
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        emailThred.start();

        request.getSession().setAttribute("userEmail", email);

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(true, "Please Verify Your Account");

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
