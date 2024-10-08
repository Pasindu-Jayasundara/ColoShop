package model.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = {"/AddToCart"})
public class AddToCartFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);

        boolean isInvalid = false;
        String errorMessage = "";
        boolean proceedToCart = true;

        if (!fromJson.has("productId")) {
            
            isInvalid = true;
            errorMessage = "Product Id Cannot Be Found";
            proceedToCart = false;
            
        } else {
            String pId = fromJson.get("productId").getAsString();

            //new cart product is a arraylist of product comes when syncing session cart and db cart
            if (httpServletRequest.getAttribute("newCartProduct") != null || pId != null) {

                boolean isOnlyOneProduct = false;

                boolean isLoggedIn = true;
                if (httpServletRequest.getSession().getAttribute("user") == null) {
                    //already logedin user
                    isLoggedIn = false;
                }

                if (pId != null) {

                    isOnlyOneProduct = true;

                    int productId = Integer.parseInt(pId);
                    if (productId <= 0) {
                        //invalid product id
                        isInvalid = true;
                        errorMessage = "Invalid Product Id";
                        proceedToCart = false;
                    }

                    if (isInvalid) {
                        proceedToCart = false;
                        Response_DTO response_DTO = new Response_DTO(false, errorMessage);

                        response.setContentType("application/json");
                        response.getWriter().write(gson.toJson(response_DTO));
                    }

                }

                if (proceedToCart) {
                    request.setAttribute("isOnlyOneProduct", isOnlyOneProduct);
                    request.setAttribute("isLoggedIn", isLoggedIn);
                    request.setAttribute("productId", Integer.valueOf(pId));
                    chain.doFilter(request, response);
                }
            }
        }

        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, errorMessage);

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }
    }

    @Override
    public void destroy() {
    }

}
