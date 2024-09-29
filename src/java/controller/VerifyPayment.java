package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.Payhere_DTO;
import entity.OrderDataTable;
import entity.Order_item;
import entity.Order_status;
import entity.Product;
import entity.UserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Payhere;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "VerifyPayment", urlPatterns = {"/VerifyPayment"})
public class VerifyPayment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String merchant_id = request.getParameter("merchant_id");
        String order_id = request.getParameter("order_id");
        String payhere_amount = request.getParameter("payhere_amount");
        String payhere_currency = request.getParameter("payhere_currency");
        String status_code = request.getParameter("status_code");
        String md5sig = request.getParameter("md5sig");

        String merchant_secret = "MTM1MDE0NTg5OTIxMTcxNDkxOTAxMTcxMTU2MTc3MTY0NzAxODc3Ng==";
        String merchant_secret_md5hash = Payhere.generateMD5(merchant_secret);

        String generated_md5hash = Payhere.generateMD5(
                merchant_id
                + order_id
                + payhere_amount
                + payhere_currency
                + status_code
                + merchant_secret_md5hash
        );

        if (generated_md5hash.equals(md5sig) && status_code.equals("2")) {
            //update your order status paid

            System.out.println("payment completed ");

            JsonObject jsonObject = (JsonObject) request.getSession().getAttribute("payment");

            String address = jsonObject.get("address").getAsString();
            String text = jsonObject.get("text").getAsString();
            JsonArray itemProductArr = jsonObject.get("itemProductArr").getAsJsonArray();

            UserTable user = (UserTable) request.getSession().getAttribute("user");

            if (address != null && text != null && itemProductArr != null && user != null) {

                Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

                Criteria orderStatusCriteria = hibernateSession.createCriteria(Order_status.class);
                orderStatusCriteria.add(Restrictions.eq("status", "Paid"));
                Order_status status = (Order_status) orderStatusCriteria.uniqueResult();

                // order 
                OrderDataTable order = new OrderDataTable();
                order.setId(Integer.parseInt(order_id));
                order.setDatetime(new Date());
                order.setDelivery_date(new Date());
                order.setAddress(address);
                order.setText(text);
                order.setUser(user);
                order.setOrder_status(status);

                hibernateSession.save(order);

                Gson gson = new Gson();
                
                for (JsonElement obj : itemProductArr) {
                    
                    JsonObject productJson = obj.getAsJsonObject().get("product").getAsJsonObject();
                    Product product = gson.fromJson(productJson, Product.class);  // Convert JsonObject to Product object
                    
                    Order_item item = new Order_item();
                    item.setOrder(order);
                    item.setProduct(product);
                    item.setQty(obj.getAsJsonObject().get("qty").getAsInt()); 
                    hibernateSession.save(item);  
                }
                
                hibernateSession.beginTransaction().commit();
                hibernateSession.close();

            }

        }

    }

}
