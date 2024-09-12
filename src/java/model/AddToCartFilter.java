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

@WebFilter(urlPatterns = {"/AddToCart"})
public class AddToCartFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (httpServletRequest.getAttribute("newCartProduct") != null || request.getParameter("productId") != null) {

            boolean isOnlyOneProduct = false;
            boolean proceedToCart = true;

            boolean isLoggedIn = true;
            if (httpServletRequest.getSession(false).getAttribute("user") == null) {
                //already logedin user
                isLoggedIn = false;
            }

            if (request.getParameter("productId") != null) {

                isOnlyOneProduct = true;

                boolean isInvalid = false;
                String errorMessage = "";

                int productId = Integer.parseInt(request.getParameter("productId"));
                if (productId <= 0) {
                    //invalid product id
                    isInvalid = true;
                    errorMessage = "Invalid Product Id";
                    proceedToCart = false;
                }

                if (isInvalid) {
                    proceedToCart = false;
                    Response_DTO response_DTO = new Response_DTO(false, errorMessage);
                    Gson gson = new Gson();

                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(response_DTO));
                }

            }

            if (proceedToCart) {
                request.setAttribute("isOnlyOneProduct", isOnlyOneProduct);
                request.setAttribute("isLoggedIn", isLoggedIn);
                chain.doFilter(request, response);
            }
        }

    }

    @Override
    public void destroy() {
    }

}
