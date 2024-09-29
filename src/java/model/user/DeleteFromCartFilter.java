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

@WebFilter(urlPatterns = {"/DeleteFromCart"})
public class DeleteFromCartFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);
        String pId = fromJson.get("id").getAsString();

        boolean isLoggedIn = true;
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            //already logedin user
            isLoggedIn = false;
        }

        if (pId != null) {

            boolean isInvalid = false;
            String errorMessage = "";

            int productId = Integer.parseInt(pId);
            if (productId <= 0) {
                //invalid product id
                isInvalid = true;
                errorMessage = "Invalid Product Id";
            }

            if (isInvalid) {
                Response_DTO response_DTO = new Response_DTO(false, errorMessage);

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(response_DTO));
            } else {
                request.setAttribute("isLoggedIn", isLoggedIn);
                request.setAttribute("id", Integer.valueOf(pId));
                chain.doFilter(request, response);
            }
        } else {
            Response_DTO response_DTO = new Response_DTO(false, "No Id");

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
