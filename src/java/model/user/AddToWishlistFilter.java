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

@WebFilter(urlPatterns = {"/AddToWishlist"})
public class AddToWishlistFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);
        String pId = fromJson.get("pId").getAsString();

        String message = "";
        boolean isSuccess = false;
        if (httpServletRequest.getSession().getAttribute("user") != null) {

            if (pId != null) {

                int productId = Integer.parseInt(pId);
                if (productId <= 0) {
                    //invalid product id
                    isSuccess = false;
                    message = "Invalid Product Id";
                } else {

                    isSuccess = true;

                    request.setAttribute("pId", pId);
                    chain.doFilter(request, response);

                }

            }

        } else {
            message = "Please LogIn First";
            isSuccess = false;
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
