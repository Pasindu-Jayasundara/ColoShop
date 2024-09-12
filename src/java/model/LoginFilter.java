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

@WebFilter(urlPatterns = {"/UserLogin","/AdminLogin"})
public class LogInFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        boolean isInvalid = false;
        String errorMessage = "";
        
        if (request.getParameter("email") == null && request.getParameter("email").isBlank()) {
            //no email
            isInvalid = true;
            errorMessage = "Missing Email Address";
            
        } else if (request.getParameter("password") == null && request.getParameter("password").isBlank()) {
            //no password
            isInvalid = true;
            errorMessage = "Missing Password";
            
        } else if (email.length() > 60) {
            //email too long
            isInvalid = true;
            errorMessage = "Email Too Long";
            
        } else if (password.length() > 45) {
            //password too long
            isInvalid = true;
            errorMessage = "Password Too Long";
            
        } else if (!Check.isValidEmail(email)) {
            //invalid email format 
            isInvalid = true;
            errorMessage = "Invalid Email Format";
            
        } else if (!Check.isValidPassword(password)) {
            //invalid password
            isInvalid = true;
            errorMessage = "Invalid Password Format";
            
        } else {
            chain.doFilter(request, response);
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
