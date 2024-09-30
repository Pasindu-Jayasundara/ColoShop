package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebFilter(urlPatterns = {"/LoadSingleProduct", "/loadSimilarProduct", "/LoadReview", "/DeleteReview"})
public class LoadSingleProductFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        JsonObject fromJson = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);

        String message = "";
        boolean isInvalid = false;

        if (!fromJson.has("id")) {

            isInvalid = true;
            message = "Id Cannot be Found";
        } else {
            String productId = fromJson.get("id").getAsString();

            if (productId != null) {
                // have id
                try {
                    int id = Integer.parseInt(productId);
                    if (id >= 0) {
                        request.setAttribute("id", id);
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
                message = "Cannot Find Id";
            }
        }
        if (isInvalid) {

            Response_DTO response_DTO = new Response_DTO(false, message);

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
