package model.admin;

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
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = {"/ReplyToUserMessage"})
public class ReplyToUserMessageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        boolean isInvalid = false;
        String errorMessage = "";

        if (httpServletRequest.getSession(false).getAttribute("admin") != null) {
            //already loged in

            if (request.getParameter("email") == null || request.getParameter("email").isBlank()) {
                // missing email
                isInvalid = true;
                errorMessage = "Missing Email Address";

            } else if (request.getParameter("messageId") == null || request.getParameter("messageId").isBlank()) {
                // missing message id
                isInvalid = true;
                errorMessage = "Missing Message Id";

            } else if (request.getParameter("reply") == null || request.getParameter("reply").isBlank()) {
                //missing reply
                isInvalid = true;
                errorMessage = "Missing Reply Text";

            } else {

                String email = request.getParameter("email");
                int messageId = Integer.parseInt(request.getParameter("messageId"));

                if (!Check.isValidEmail(email)) {
                    // invalid email format
                    isInvalid = true;
                    errorMessage = "Invalid Email Format";

                } else if (messageId > 0) {
                    //invalid message id
                    isInvalid = true;
                    errorMessage = "Invalid Message Id";

                } else if (email.length() > 60) {
                    //email too long
                    isInvalid = true;
                    errorMessage = "Email Too Long";

                } else {
                    chain.doFilter(request, response);
                }
            }
        } else {
            //need to login first
            isInvalid = true;
            errorMessage = "Missing Email Address";
        }

        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, errorMessage);
            Gson gson = new Gson();
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
