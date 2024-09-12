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
import model.Validation;

@WebFilter(urlPatterns = {"/DeleteCategory"})
public class DeleteCategoryFilter implements Filter {

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
            if (request.getParameter("categoryId") == null || request.getParameter("categoryId").isBlank()) {
                //no category
                isInvalid = true;
                message = "Missing Category Id";

            } else {
                String categoryId = request.getParameter("categoryId");
                if (Validation.isInteger(categoryId)) {
                    //not a number
                    isInvalid = true;
                    message = "Not a Number";

                } else {
                    int cId = Integer.parseInt(categoryId);
                    if (cId <= 0) {
                        // invalid range
                        isInvalid = true;
                        message = "Invalid Id Range";

                    } else {
                        chain.doFilter(request, response);

                    }
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
