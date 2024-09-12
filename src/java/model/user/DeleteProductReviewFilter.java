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

@WebFilter(urlPatterns = {"/DeleteReview"})
public class DeleteProductReviewFilter implements Filter{

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
            
        } else if (request.getParameter("reviewId") != null || request.getParameter("reviewId").isBlank()) {
            //no review id
            isInvalid = true;
            message = "Missing Review Id";
            
        } else {
            String reviewId = request.getParameter("reviewId");
            
            if (!Validation.isInteger(reviewId)) {
                //not a integer
                isInvalid = true;
                message = "Review Id is Not a Number";
            } else {
                int rId = Integer.parseInt(reviewId);
                if (rId <= 0) {
                    //invalid id
                    isInvalid = true;
                    message = "Invalid Review Id";
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
