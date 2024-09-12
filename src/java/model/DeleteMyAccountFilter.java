package model;

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

@WebFilter(urlPatterns = {"/DeleteMyAccount"})
public class DeleteMyAccountFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isUser = false;
        boolean isAdmin = false;

        if (httpServletRequest.getSession().getAttribute("user") != null) {
            isUser = true;
        } else if (httpServletRequest.getSession().getAttribute("admin") != null) {
            isAdmin = true;
        } else {
            Response_DTO response_DTO = new Response_DTO(false, "Please Login First");
            Gson gson = new Gson();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

        if (isAdmin || isUser) {
            request.setAttribute("isUser", isUser);
            request.setAttribute("isAdmin", isAdmin);
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
    }

}
