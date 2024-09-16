package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.AdminDetailTable;
import entity.Message;
import entity.Message_status;
import entity.Newsletter;
import entity.Reply;
import entity.UserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "NewsLetter", urlPatterns = {"/NewsLetter"})
public class NewsLetter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String text = (String) request.getAttribute("text");

        boolean isInvalid = false;
        String errorMessage = "";

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Criteria newsLetterCriteria = hibernateSession.createCriteria(Newsletter.class);
        final ArrayList<Newsletter> list = (ArrayList<Newsletter>) newsLetterCriteria.list();

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

            errorMessage = "NewsLeteer Send";
        } else {
            //cannot find message
            errorMessage = "Cannot Find Message";
            isInvalid = true;
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