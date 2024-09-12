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

//not completed
@WebFilter(urlPatterns = {"/MessageToSeller"})
public class BuyerMessageToSellerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String errorMessage = "";

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (httpServletRequest.getSession().getAttribute("user") == null) {
            //login first
            isInvalid = true;
            errorMessage = "Please LogIn First";

        } else if (request.getParameter("message") != null && !request.getParameter("message").isBlank()) {

            String title = request.getParameter("title");

            if (title.length() <= 45) {

                chain.doFilter(request, response);

            } else {
                //title too long
                isInvalid = true;
                errorMessage = "Title Too Long";
            }

        } else {
            //invalid message
            isInvalid = true;
            errorMessage = "Missing Message Content";
        }
    }

    if (isInvalid

    
        ) {
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
