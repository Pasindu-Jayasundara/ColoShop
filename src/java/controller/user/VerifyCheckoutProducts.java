package controller.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "VerifyCheckoutProducts", urlPatterns = {"/VerifyCheckoutProducts"})
public class VerifyCheckoutProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        ArrayList<JsonObject> itemProductArr = new ArrayList<>();
        ArrayList<JsonElement> itemList = (ArrayList<JsonElement>) request.getAttribute("itemList");

        Gson gson = new Gson();

        boolean isValid = false;
        double totalPrice = 0.00;
        for (JsonElement obj : itemList) {

            JsonObject itemObj = obj.getAsJsonObject();
            int id = itemObj.get("id").getAsInt();
            int qty = itemObj.get("qty").getAsInt();

            Product product = (Product) hibernateSession.get(Product.class, id);
            if (product != null) {

                isValid = true;
                
                JsonObject jo = new JsonObject();
                jo.add("product", gson.toJsonTree(product));
                jo.addProperty("qty", qty);

                itemProductArr.add(jo);
                totalPrice += (product.getUnit_price() * qty) + product.getDelivery_fee();
            }
        }

        request.removeAttribute("itemList");
        request.setAttribute("totalPrice", totalPrice);
        request.setAttribute("itemProductArr", itemProductArr);
        request.setAttribute("isValid", isValid);

    }

}
