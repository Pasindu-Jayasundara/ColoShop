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
import model.Validation;

@WebFilter(urlPatterns = {"/RegisterToNewsletter"})
public class RegisterToNewsletterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(request.getReader(), JsonObject.class);

        boolean isInvalid = false;
        String message = "";

        if (!fromJson.has("email")) {

            isInvalid = true;
            message = "Email Address Cannot Be FOund";

        } else {
            String email = fromJson.get("email").getAsString();

            if (!email.isEmpty()) {
                if (Validation.isValidEmail(email)) {

                    if (email.length() <= 60) {
                        request.setAttribute("email", email);
                        chain.doFilter(request, response);

                    } else {
                        isInvalid = true;
                        message = "Email Address Too Long";
                    }
                } else {
                    isInvalid = true;
                    message = "Invalid Email Address";
                }
            } else {
                isInvalid = true;
                message = "Cannot Find Email Address";
            }
        }
        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, message);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }
    }

    @Override
    public void destroy() {
    }

}
