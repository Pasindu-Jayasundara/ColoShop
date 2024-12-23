package controller.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.OrderDataTable;
import entity.Order_item;
import entity.Order_status;
import entity.Product;
import entity.UserTable;
import java.io.IOException;
import java.text.DecimalFormat;
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

@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/VerifyCheckoutProducts").include(request, response);

        if ((boolean) request.getAttribute("isValid")) {

            UserTable user = (UserTable) request.getSession().getAttribute("user");
            String address = (String) request.getAttribute("address");
            String text = (String) request.getAttribute("text");
            ArrayList<JsonObject> itemProductArr = (ArrayList<JsonObject>) request.getAttribute("itemProductArr");
            double totalPrice = (double) request.getAttribute("totalPrice");

            String itemsName = new String();
            for (JsonObject obj : itemProductArr) {

                String name = obj.get("product").getAsJsonObject().get("name").getAsString();
                itemsName += name + ", ";

            }

            String servletPath = "/ColoShop/VerifyPayment";
            String notifyUrl = "https://redbird-suitable-conversely.ngrok-free.app" + servletPath;
            String merchantId = "1228237";
            String orderId = String.valueOf((int) System.currentTimeMillis());
            String amount = new DecimalFormat("0.00").format(totalPrice);
            String currency = "LKR";
            String merchantSecretHash = Payhere.generateMD5("MTM1MDE0NTg5OTIxMTcxNDkxOTAxMTcxMTU2MTc3MTY0NzAxODc3Ng==");

            String md5hash = Payhere.generateMD5(merchantId + orderId + amount + currency + merchantSecretHash);

            JsonObject payhere = new JsonObject();
            payhere.addProperty("merchant_id", merchantId);

            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", notifyUrl);

            payhere.addProperty("first_name", user.getFirst_name());
            payhere.addProperty("last_name", user.getLast_name());
            payhere.addProperty("email", user.getEmail());

            payhere.addProperty("phone", "");
            payhere.addProperty("address", address);
            payhere.addProperty("city", "");
            payhere.addProperty("country", "");

            payhere.addProperty("order_id", String.valueOf(orderId));
            payhere.addProperty("items", itemsName);
            payhere.addProperty("currency", "LKR");
            payhere.addProperty("amount", amount);
            payhere.addProperty("sandbox", true);
            payhere.addProperty("hash", md5hash);

            Gson gson = new Gson();
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

            Criteria orderStatusCriteria = hibernateSession.createCriteria(Order_status.class);
            orderStatusCriteria.add(Restrictions.eq("status", "Pending"));
            Order_status status = (Order_status) orderStatusCriteria.uniqueResult();

            // order 
            OrderDataTable order = new OrderDataTable();
            order.setId(Integer.parseInt(orderId));
            order.setDatetime(new Date());
            order.setDelivery_date(new Date());
            order.setAddress(address);
            order.setText(text);
            order.setUser(user);
            order.setOrder_status(status);
            order.setTotal_amount(totalPrice);

            hibernateSession.save(order);

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

            Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(payhere));
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

}
