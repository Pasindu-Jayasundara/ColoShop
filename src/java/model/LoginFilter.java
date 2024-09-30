package model;

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

@WebFilter(urlPatterns = {"/UserLogin", "/AdminLogin"})
public class LogInFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        JsonObject fromJson = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);

        boolean isInvalid = false;
        String errorMessage = "";

        if (!fromJson.has("email")) {

            isInvalid = true;
            errorMessage = "Email Address Cannot Be Found";

        } else if (!fromJson.has("password")) {

            isInvalid = true;
            errorMessage = "Password Cannot Be Found";

        } else {

            String email = fromJson.get("email").getAsString();
            String password = fromJson.get("password").getAsString();

            if (email == null || email.isEmpty()) {
                //no email
                isInvalid = true;
                errorMessage = "Missing Email Address";

            } else if (password == null || password.isEmpty()) {
                //no password
                isInvalid = true;
                errorMessage = "Missing Password";

            } else {

                if (email.length() > 60) {
                    //email too long
                    isInvalid = true;
                    errorMessage = "Email Too Long";

                } else if (password.length() > 45) {
                    //password too long
                    isInvalid = true;
                    errorMessage = "Password Too Long";

                } else if (!Validation.isValidEmail(email)) {
                    //invalid email format 
                    isInvalid = true;
                    errorMessage = "Invalid Email Format";

                } else if (!Validation.isValidPassword(password)) {
                    //invalid password
                    isInvalid = true;
                    errorMessage = "Invalid Password Format";

                } else {

                    request.setAttribute("email", email);
                    request.setAttribute("password", password);

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
