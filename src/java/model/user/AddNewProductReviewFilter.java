package model.user;

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
import model.Validation;

@WebFilter(urlPatterns = {"/AddNewReview"})
public class AddNewProductReviewFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        JsonObject fromJson = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);

        boolean isInvalid = false;
        String errorMessage = "";

        if (!fromJson.has("txt")) {

            isInvalid = true;
            errorMessage = "Review Cannot Be Found";

        } else if (!fromJson.has("id")) {

            isInvalid = true;
            errorMessage = "Id Cannot Be Found";

        } else {
            String review = fromJson.get("txt").getAsString();
            String productId = fromJson.get("id").getAsString();

            if (httpServletRequest.getSession().getAttribute("user") == null) {
                //not logedin
                isInvalid = true;
                errorMessage = "Please LogIn First";

            } else {

                if (review.isEmpty()) {
                    //no review text
                    isInvalid = true;
                    errorMessage = "Missing Review Text";

                } else {

                    if (!Validation.isInteger(productId)) {
                        //not a integer
                        isInvalid = true;
                        errorMessage = "Invalid Product Id";

                    } else {
                        int pId = Integer.parseInt(productId);
                        if (pId <= 0) {
                            //invalid id
                            isInvalid = true;
                            errorMessage = "Incorrect Product Id";

                        } else {

                            request.setAttribute("id", pId);
                            request.setAttribute("review", review);
                            chain.doFilter(request, response);
                        }
                    }

                }

            }
        }
        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, errorMessage);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
