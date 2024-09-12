package model.user;

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

@WebFilter(urlPatterns = {"/DeleteProduct"})
public class DeleteProductFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        
        boolean isInvalid = false;
        String message = "";
        
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            //login first
            isInvalid = true;
            message = "Please Login First";
            
        } else if (request.getParameter("productId") != null || request.getParameter("productId").isBlank()) {
            //no product id
            isInvalid = true;
            message = "Missing Product Id";
            
        } else {
            String productId = request.getParameter("productId");
            
            if (!Validation.isInteger(productId)) {
                //not a integer
                isInvalid = true;
                message = "Product Id is Not a Number";
            } else {
                int pId = Integer.parseInt(productId);
                if (pId <= 0) {
                    //invalid id
                    isInvalid = true;
                    message = "Invalid Product Id";
                } else {
                    chain.doFilter(request, response);
                }
            }
            
        }
        
        if(isInvalid){
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
