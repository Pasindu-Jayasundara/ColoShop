package controller.admin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.AdminDetailTable;
import entity.Message;
import entity.Message_status;
import entity.Product;
import entity.Seller;
import entity.Status;
import entity.UserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadAdminData", urlPatterns = {"/LoadAdminData"})
public class LoadAdminData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status status = (Status) statusCriteria.uniqueResult();

        //  total sellers
        Criteria sellerCriteria = hibernateSession.createCriteria(Seller.class);
        sellerCriteria.add(Restrictions.eq("status", status));
        sellerCriteria.setProjection(
                Projections.count("id")
        );
        String sellerCount = (String) sellerCriteria.uniqueResult();

        // total users
        Criteria userCriteria = hibernateSession.createCriteria(UserTable.class);
        userCriteria.add(Restrictions.eq("status", status));
        userCriteria.setProjection(
                Projections.count("id")
        );
        String userCount = (String) userCriteria.uniqueResult();

        // total buyers
        int buyerCount = Integer.parseInt(userCount) - Integer.parseInt(sellerCount);

        // total products
        Criteria productCriteria = hibernateSession.createCriteria(Product.class);
        productCriteria.add(Restrictions.eq("status", status));
        productCriteria.setProjection(
                Projections.count("id")
        );
        String productCount = (String) productCriteria.uniqueResult();

        //username
        AdminDetailTable admin = (AdminDetailTable) request.getSession().getAttribute("admin");
        String name = admin.getFirst_name() + " " + admin.getLast_name();

        //not replyed message status
        Criteria messageStatusCriteria = hibernateSession.createCriteria(Message_status.class);
        messageStatusCriteria.add(Restrictions.ne("id", 3));
        messageStatusCriteria.setProjection(
                Projections.count("id")
        );
        Message_status messageStatus = (Message_status) messageStatusCriteria.uniqueResult();

        // message
        Criteria messageCriteria = hibernateSession.createCriteria(Message.class);
        messageCriteria.add(Restrictions.eq("message_status", messageStatus));
        ArrayList<Message> messageList = (ArrayList<Message>) messageCriteria.list();

        JsonArray jsonMessages = new JsonArray();
        for (Message message : messageList) {
            JsonObject messageJson = new JsonObject();
            messageJson.addProperty("id", message.getId()); 
            messageJson.addProperty("message", message.getMessage()); 
            messageJson.addProperty("title", message.getTitle());
            messageJson.addProperty("datetime", String.valueOf(message.getDatetime()));
            messageJson.addProperty("message_status", message.getMessage_status().getStatus()); 
            
            jsonMessages.add(messageJson);
        }

        JsonObject jo = new JsonObject();
        jo.addProperty("sellerCount", sellerCount);
        jo.addProperty("buyerCount", buyerCount);
        jo.addProperty("productCount", productCount);
        jo.addProperty("userName", name);
        jo.add("messageArr", jsonMessages);

        Response_DTO response_DTO = new Response_DTO(true, jo);

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
