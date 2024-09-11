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

@WebFilter(urlPatterns = {"/UserContactMessage"})
public class UserContactMessageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String errorMessage = "";
        if (request.getParameter("title") != null && !request.getParameter("title").isBlank()) {
            if (request.getParameter("message") != null && !request.getParameter("message").isBlank()) {

                String title = request.getParameter("title");

                if (title.length() <= 45) {

                    chain.doFilter(request, response);

                } else {
                    //title too long
                    isInvalid = true;
                    errorMessage = "Title Too Long";
                }

            } else {
                //invalid message
                isInvalid = true;
                errorMessage = "Missing Message Content";
            }
        } else {
            //invalid title
            isInvalid = true;
            errorMessage = "Missing Message Title";
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
