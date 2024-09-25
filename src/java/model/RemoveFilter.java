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

@WebFilter(urlPatterns = {"/Remove"})
public class RemoveFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest hsr = (HttpServletRequest) request;
        Gson gson = new Gson();

        if (hsr.getSession().getAttribute("admin") != null) {

            JsonObject fromJson = gson.fromJson(hsr.getReader(), JsonObject.class);
            String id = fromJson.get("id").getAsString();
            String of = fromJson.get("of").getAsString();

            boolean isError = false;
            String message = "";

            if (id.isEmpty()) {
                message = "Missing Id";
                isError = true;

            } else if (!Validation.isInteger(id)) {
                //not a number
                isError = true;
                message = "Not a Number";

            } else {

                int cid = Integer.parseInt(id);

                if (cid <= 0) {
                    message = "Invalid Id";
                    isError = false;

                } else {

                    if (of.equals("color")) {
                        //color

                        request.setAttribute("id", id);
                        request.setAttribute("of", of);

                        request.getRequestDispatcher("/DeleteColor").include(request, response);

                    } else if (of.equals("size")) {
                        //size

                        request.setAttribute("id", id);
                        request.setAttribute("of", of);

                        request.getRequestDispatcher("/DeleteSize").include(request, response);

                    } else if (of.equals("category")) {
                        //category

                        request.setAttribute("id", id);
                        request.setAttribute("of", of);

                        request.getRequestDispatcher("/DeleteCategory").include(request, response);

                    } else if (of.equals("brand")) {
                        //brand

                        request.setAttribute("id", id);
                        request.setAttribute("of", of);

                        request.getRequestDispatcher("/DeleteBrand").include(request, response);

                    } else {
                        Response_DTO response_DTO = new Response_DTO(false, "Missing Data");

                        response.setContentType("application/json");
                        response.getWriter().write(gson.toJson(response_DTO));
                    }

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
