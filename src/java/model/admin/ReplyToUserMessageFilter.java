package model.admin;

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

@WebFilter(urlPatterns = {"/ReplyToUserMessage"})
public class ReplyToUserMessageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Gson gson = new Gson();
        JsonObject fj = gson.fromJson(request.getReader(), JsonObject.class);

        String id = fj.get("id").getAsString();
        String text = fj.get("text").getAsString();

        boolean isInvalid = false;
        String errorMessage = "";

        if (httpServletRequest.getSession(false).getAttribute("admin") != null) {
            //already loged in
            if (id == null || id.isEmpty()) {
                // missing message id
                isInvalid = true;
                errorMessage = "Missing Message Id";

            } else if (text == null || text.isEmpty()) {
                //missing reply
                isInvalid = true;
                errorMessage = "Missing Reply Text";

            } else {

                int messageId = Integer.parseInt(id);

                if (messageId <= 0) {
                    //invalid message id
                    isInvalid = true;
                    errorMessage = "Invalid Message Id";

                } else {
                    
                    request.setAttribute("messageId", id);
                    request.setAttribute("reply", text);
                    
                    chain.doFilter(request, response);
                }
            }
        } else {
            //need to login first
            isInvalid = true;
            errorMessage = "Missing Email Address";
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
