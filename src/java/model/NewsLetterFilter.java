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

@WebFilter(urlPatterns = {"/NewsLetter"})
public class NewsLetterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(httpServletRequest.getReader(), JsonObject.class);

        boolean isInvalid = false;
        String errorMessage = "";

        if (!fromJson.has("text")) {

            isInvalid = true;
            errorMessage = "Reply Text Cannot Be Found";

        } else {
            String text = fromJson.get("text").getAsString();

            if (httpServletRequest.getSession(false).getAttribute("admin") != null) {
                //already loged in
                if (text == null || text.isEmpty()) {
                    //missing reply
                    isInvalid = true;
                    errorMessage = "Missing Reply Text";

                } else {

                    request.setAttribute("text", text);

                    chain.doFilter(request, response);
                }
            } else {
                //need to login first
                isInvalid = true;
                errorMessage = "Missing Email Address";
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
