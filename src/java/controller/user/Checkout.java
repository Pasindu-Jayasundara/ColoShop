package controller.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.Payhere_DTO;
import dto.Response_DTO;
import entity.Product;
import entity.UserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Payhere;
import model.Validation;

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

            StringBuilder itemsName = new StringBuilder();
            for (JsonObject obj : itemProductArr) {
                
                String name = obj.get("product").getAsJsonObject().get("name").getAsString();
                itemsName.append(name).append(", ");

            }

            if (itemsName.length() > 0) {
                itemsName.setLength(itemsName.length() - 2); // Remove last ", "
            }

            String servletPath = "/ColoShop/VerifyPayment";
            String notifyUrl = "https://d8e0-2402-4000-b187-2113-38c7-bff7-1900-d68f.ngrok-free.app" + servletPath;
            String merchantId = "1228237";
            String orderId = String.valueOf((int) System.currentTimeMillis());
            String amount = new DecimalFormat("0.00").format(totalPrice);
            String currency = "LKR";
            String merchantSecretHash = Payhere.generateMD5("MTM1MDE0NTg5OTIxMTcxNDkxOTAxMTcxMTU2MTc3MTY0NzAxODc3Ng==");

            String md5hash = Payhere.generateMD5(merchantId + orderId + amount + currency + merchantSecretHash);

            Payhere_DTO payhere_DTO = new Payhere_DTO();
            payhere_DTO.setSandbox(true);
            payhere_DTO.setMerchant_id(merchantId);
            payhere_DTO.setNotify_url(notifyUrl);
            payhere_DTO.setOrder_id(orderId);
            payhere_DTO.setItems(String.valueOf(itemsName));
            payhere_DTO.setAmount(amount);
            payhere_DTO.setCurrency(currency);
            payhere_DTO.setHash(md5hash);
            payhere_DTO.setFirst_name(user.getFirst_name());
            payhere_DTO.setLast_name(user.getLast_name());
            payhere_DTO.setEmail(user.getEmail());
            payhere_DTO.setAddress(address);

            Gson gson = new Gson();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("address", address);
            jsonObject.addProperty("text", text);
            jsonObject.add("itemProductArr", gson.toJsonTree(itemProductArr));
            jsonObject.addProperty("itemsName", String.valueOf(itemsName));

            request.getSession().setAttribute("payment", gson.toJsonTree(jsonObject));

            Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(payhere_DTO));
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

}
