package model;

import com.Check;
import com.google.gson.Gson;
import dto.Response_DTO;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"/RegisterToNewsletter"})
public class RegisterToNewsletterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String message = "";

        if (request.getParameter("email") != null && !request.getParameter("email").isBlank()) {
            String email = request.getParameter("email");
            if (Check.isValidEmail(email)) {

                if (email.length() <= 60) {
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

        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, message);
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(response_DTO));
        }
    }

    @Override
    public void destroy() {
    }

}
