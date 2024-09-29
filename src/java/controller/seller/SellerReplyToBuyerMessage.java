package controller.seller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Message_status;
import entity.Message_to_seller;
import entity.UserTable;
import java.io.IOException;
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

@WebServlet(name = "ReplyToBuyerMessage", urlPatterns = {"/ReplyToBuyerMessage"})
public class SellerReplyToBuyerMessage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int msgId = (int) request.getAttribute("id");
        final String reply = (String) request.getAttribute("reply");
        UserTable seller = (UserTable) request.getSession().getAttribute("user");

        if (seller.getAccount_type().getType().equals("Seller")) {

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

            Criteria msgStatusCriteria = hibernateSession.createCriteria(Message_status.class);
            msgStatusCriteria.add(Restrictions.eq("status", "Replied"));
            Message_status msgStatus = (Message_status) msgStatusCriteria.uniqueResult();

            String msg = "";
            boolean isSuccess = false;

            final Message_to_seller sellerMsg = (Message_to_seller) hibernateSession.get(Message_to_seller.class, msgId);
            if (sellerMsg != null) {
                sellerMsg.setReply(reply);
                sellerMsg.setMessage_status(msgStatus);

                Thread emailThred = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String to = sellerMsg.getUser().getEmail();
                        String subject = "Reply to your message";
                        String body = reply;

                        try {
                            Email.sendEmail(to, subject, body);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                emailThred.start();

                isSuccess = true;
                msg = "Reply Send Success";
            } else {
                msg = "No Message Found";
            }

            hibernateSession.update(sellerMsg);
            hibernateSession.beginTransaction().commit();
            hibernateSession.close();

            Gson gson = new Gson();
            Response_DTO response_DTO = new Response_DTO(isSuccess, msg);

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));

        }

    }

}
