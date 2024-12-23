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
import model.Validation;

@WebFilter(urlPatterns = {"/UpdateProductStatus"})
public class UpdateProductStatusFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(request.getReader(), JsonObject.class);

        HttpServletRequest hsr = (HttpServletRequest) request;
        if (hsr.getSession().getAttribute("user") != null) {

            boolean isInvalid = false;
            String message = "";

            if (!fromJson.has("id")) {

                isInvalid = true;
                message = "Id Cannot Be Found";

            } else {
                String id = fromJson.get("id").getAsString();
                if (id != null) {
                    int oid = Integer.parseInt(id);
//                    if (oid <= 0) {
//                        isInvalid = true;
//                        message = "Invalid Id";
//
//                    } else 
//                        if (!Validation.isInteger(id)) {
//                        isInvalid = true;
//                        message = "Not A Number";
//
//                    } else {
                        request.setAttribute("id", oid);
                        chain.doFilter(request, response);
//                    }
                } else {
                    isInvalid = true;
                    message = "Missing Id";
                }
            }
            if (isInvalid) {
                Response_DTO response_DTO = new Response_DTO(false, message);

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(response_DTO));
            }

        } else {
            Response_DTO response_DTO = new Response_DTO(false, "Please logIn First");

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
