package model.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import model.Validation;

@WebFilter(urlPatterns = {"/Checkout"})
public class CheckoutFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(request.getReader(), JsonObject.class);

        String message = "";
        boolean isSuccess = false;

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            message = "Please LogIn First";
        } else {

            if (!fromJson.has("address") || !fromJson.has("array")) {
                message = "Missing Data";
            } else {
                String address = fromJson.get("address").getAsString();
                JsonArray list = fromJson.get("array").getAsJsonArray();
                String text;

                if (fromJson.has("text")) {
                    text = fromJson.get("text").getAsString();
                } else {
                    text = "";
                }

                if (address.isEmpty() || address.trim().equals("")) {
                    message = "Missing Addrress";
                } else if (list.isEmpty()) {
                    message = "Missing Addrress";
                } else {
                    isSuccess = true;

                    request.setAttribute("address", address);
                    request.setAttribute("text", text);

                    ArrayList<JsonElement> itemList = new ArrayList<>();

                    for (JsonElement obj : list) {
                        JsonObject productObj = obj.getAsJsonObject();
                        
                        String id = productObj.get("id").getAsString();
                        if (Validation.isInteger(id)) {

                            int pId = Integer.parseInt(id);
                            if (pId > 0) {
                                itemList.add(obj);
                            }
                        }
                    }

                    request.setAttribute("itemList", itemList);

                    chain.doFilter(request, response);

                }

            }

        }

        if (!isSuccess) {
            Response_DTO response_DTO = new Response_DTO(false, message);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
