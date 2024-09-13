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

@WebFilter(urlPatterns = {"/VerifyNewUser"})
public class VerifyNewUserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);
        String token = dto.get("token").getAsString();

        boolean isInvalid = false;
        String errorMessage = "";

        if (token == null) {
            //no token
            isInvalid = true;
            errorMessage = "Missing Verification Token";

        } else if (httpServletRequest.getSession().getAttribute("userEmail") == null) {
            //no email
            isInvalid = true;
            errorMessage = "Missing Email Address";

        } else {

            if (token.length() != 8) {
                //invalid token length
                isInvalid = true;
                errorMessage = "Invalid Token Length";

            } else {
                request.setAttribute("token", token);
                chain.doFilter(request, response);
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
