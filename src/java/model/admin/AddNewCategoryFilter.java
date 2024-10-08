package model.admin;

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

@WebFilter(urlPatterns = {"/AddCategory"})
public class AddNewCategoryFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        boolean isInvalid = false;
        String message = "";

        if (httpServletRequest.getSession().getAttribute("admin") == null) {
            //need to log in first
            isInvalid = true;
            message = "Please LogIn First";

        } else {
            if (request.getParameter("category") == null || request.getParameter("category").isBlank()) {
                //no color
                isInvalid = true;
                message = "Missing Category";

            } else {
                String category = request.getParameter("category");
                if (category.length() > 25) {
                    //too long
                    isInvalid = true;
                    message = "Category Name Too Long";

                } else {
                    chain.doFilter(request, response);
                }

            }
        }

        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, message);
            Gson gson = new Gson();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }
        
    }

    @Override
    public void destroy() {
    }
    
}
