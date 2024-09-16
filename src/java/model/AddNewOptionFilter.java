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

@WebFilter(urlPatterns = {"/AddNewOption"})
public class AddNewOptionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest hsr = (HttpServletRequest) request;
        Gson gson = new Gson();

        if (hsr.getSession().getAttribute("admin") != null) {

            JsonObject fromJson = gson.fromJson(hsr.getReader(), JsonObject.class);
            String type = fromJson.get("type").getAsString();
            String option = fromJson.get("option").getAsString();

            boolean isError = false;
            String message = "";

            if (type.isBlank()) {
                message = "Missing Type";
                isError = true;

            } else if (option.isBlank()) {
                message = "Missing New Option";
                isError = true;

            } else {

                if (type.equals("color")) {
                    //color

                    request.setAttribute("option", option);
                    request.getRequestDispatcher("/AddNewColor").include(request, response);

                } else if (type.equals("size")) {
                    //size
                    
                    request.setAttribute("option", option);
                    request.getRequestDispatcher("/AddNewSize").include(request, response);

                } else if (type.equals("category")) {
                    //category
                    
                    request.setAttribute("option", option);
                    request.getRequestDispatcher("/AddCategory").include(request, response);

                } else if (type.equals("brand")) {
                    //brand
                    
                    request.setAttribute("option", option);
                    request.getRequestDispatcher("/AddBrand").include(request, response);

                } else {
                    Response_DTO response_DTO = new Response_DTO(false, "Missing Data");

                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(response_DTO));
                }

            }

            if (isError) {
                Response_DTO response_DTO = new Response_DTO(false, message);

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(response_DTO));
            }

        } else {
            Response_DTO response_DTO = new Response_DTO(false, "Please logIn First");

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
