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

@WebFilter(urlPatterns = {"/UserContactMessage"})
public class MessageToAdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(request.getReader(), JsonObject.class);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        boolean isInvalid = false;
        String errorMessage = "";

        if (!fromJson.has("title")) {

            isInvalid = true;
            errorMessage = "Title Cannot Be FOund";

        } else if (!fromJson.has("msg")) {

            isInvalid = true;
            errorMessage = "Message Cannot Be FOund";

        } else {

            String title = fromJson.get("title").getAsString();
            String msg = fromJson.get("msg").getAsString();

            if (httpServletRequest.getSession().getAttribute("user") != null) {

                if (!title.isEmpty()) {
                    if (!msg.isEmpty()) {
                        if (title.length() <= 45) {

                            request.setAttribute("title", title);
                            request.setAttribute("msg", msg);
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
                } else {
                    //invalid title
                    isInvalid = true;
                    errorMessage = "Missing Message Title";
                }

            } else {
                isInvalid = true;
                errorMessage = "Please LogIn First";
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
