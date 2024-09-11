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

@WebFilter
public class LoadSingleProductFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String message = "";
        boolean isInvalid = false;

        if (request.getParameter("id") != null && !request.getParameter("id").isBlank()) {
            // have id
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                if (id >= 0) {
                    chain.doFilter(request, response);
                } else {
                    // id cannot be negative
                    isInvalid = true;
                    message = "Id Cannot be Negative";
                }
            } catch (Exception e) {
                e.printStackTrace();

                isInvalid = true;
                message = "Tnvalid Id Type";
            }

        } else {
            //no id
            isInvalid = true;
            message = "Cannot Find Product Id";
        }
        
        if(isInvalid){
            
            Response_DTO response_DTO = new Response_DTO(false, message);
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
