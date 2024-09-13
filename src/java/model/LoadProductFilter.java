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

@WebFilter(urlPatterns = {"/LoadProduct"})
public class LoadProductFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        int resultCount = 0;
        if (request.getParameter("productCount") != null && Validation.isInteger(request.getParameter("productCount"))) {
            
            try {
                resultCount = Integer.parseInt(request.getParameter("productCount"));
                request.setAttribute("productCount", resultCount);
                chain.doFilter(request, response);
                
            } catch (Exception e) {
                e.printStackTrace();

                Gson gson = new Gson();
                Response_DTO dTO = new Response_DTO(false, "Invalid Parameter");

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(dTO));
            }
        } else {
            resultCount = 10;
            request.setAttribute("productCount", resultCount);
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
    }

}
