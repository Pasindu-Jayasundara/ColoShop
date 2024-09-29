package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Newsletter;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Email;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;

@WebServlet(name = "NewsLetter", urlPatterns = {"/NewsLetter"})
public class NewsLetter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String text = (String) request.getAttribute("text");

        boolean isOk= true;
        String errorMessage = "";

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Criteria newsLetterCriteria = hibernateSession.createCriteria(Newsletter.class);
        final List<Newsletter> list = (List<Newsletter>) newsLetterCriteria.list();

        if (!list.isEmpty()) {
            //found emails

            // send Email
            Thread emailThred = new Thread(new Runnable() {
                @Override
                public void run() {

                    String subject = "ColoShop NewsLetter";
                    String body = text;

                    for (Newsletter newsletter : list) {

                        try {
                            Email.sendEmail(newsletter.getEmail(), subject, body);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
            emailThred.start();

            errorMessage = "NewsLetter Send";
        } else {
            //cannot find message
            errorMessage = "No Subscribers";
            isOk = false;
        }

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(isOk, errorMessage);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
