package controller.seller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.OrderDataTable;
import entity.Order_status;
import entity.UserTable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "UpdateProductStatus", urlPatterns = {"/UpdateProductStatus"})
public class UpdateProductStatus extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = (int) request.getAttribute("id");
        UserTable seller = (UserTable) request.getSession().getAttribute("user");

        if (seller.getAccount_type().getType().equals("Seller")) {

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

            OrderDataTable order = (OrderDataTable) hibernateSession.get(OrderDataTable.class, id);
            String status = order.getOrder_status().getStatus();

            String newStatus = "";
            if (status.equals("Pending")) {

                Criteria orderStatusCriteria = hibernateSession.createCriteria(Order_status.class);
                orderStatusCriteria.add(Restrictions.eq("status", "Shipped"));
                Order_status orderStatus = (Order_status) orderStatusCriteria.uniqueResult();
                
                order.setOrder_status(orderStatus);
                
                newStatus="Shipped";
            } else if (status.equals("Shipped")) {

                Criteria orderStatusCriteria = hibernateSession.createCriteria(Order_status.class);
                orderStatusCriteria.add(Restrictions.eq("status", "Delivered"));
                Order_status orderStatus = (Order_status) orderStatusCriteria.uniqueResult();
                
                order.setOrder_status(orderStatus);
                
                newStatus="Delivered";
            }

            
            hibernateSession.update(order);
            hibernateSession.beginTransaction().commit();
            hibernateSession.close();
            
            JsonObject jo = new JsonObject();
            jo.addProperty("status", newStatus);
            jo.addProperty("message", "Update Success");
            
            Gson gson = new Gson();
            Response_DTO response_DTO = new Response_DTO(true, gson.toJson(jo));

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));

        }

    }

}
