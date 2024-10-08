package model.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.UserTable;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = {"/BecomeUser","/BecomeSeller"})
public class BecomeUserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getSession().getAttribute("user") != null) {
            
            UserTable user = (UserTable) httpServletRequest.getSession().getAttribute("user");
            if(user.getAccount_type().getType().equals("Buyer")){
                request.getRequestDispatcher("/BecomeSeller").include(request, response);
                
            }else if(user.getAccount_type().getType().equals("Seller")){
                request.getRequestDispatcher("/BecomeUser").include(request, response);
            }

        } else {
            //not loged in
            Response_DTO response_DTO = new Response_DTO(false, "Please Login First");
            Gson gson = new Gson();
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));

        }

    }

    @Override
    public void destroy() {
    }

}
