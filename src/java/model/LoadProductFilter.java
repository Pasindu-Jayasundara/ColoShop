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

        //HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //result count
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
        }

        //brand
        boolean isBrandFound = false;
        if (request.getParameter("brand") != null && Validation.isInteger(request.getParameter("brand"))) {
            if (Validation.isInteger(request.getParameter("brand"))) {
                isBrandFound = true;
                request.setAttribute("brandId", request.getParameter("brand"));
            }
        }
        request.setAttribute("isBrandFound", isBrandFound);

        //category
        boolean isCategoryFound = false;
        if (request.getParameter("category") != null && Validation.isInteger(request.getParameter("category"))) {
            if (Validation.isInteger(request.getParameter("category"))) {
                isCategoryFound = true;
                request.setAttribute("categoryId", request.getParameter("category"));
            }
        }
        request.setAttribute("isCategoryFound", isCategoryFound);

        //color
        boolean isColorFound = false;
        if (request.getParameter("color") != null && Validation.isInteger(request.getParameter("color"))) {
            if (Validation.isInteger(request.getParameter("color"))) {
                isColorFound = true;
                request.setAttribute("colorId", request.getParameter("color"));
            }
        }
        request.setAttribute("isColorFound", isColorFound);

        //size
        boolean isSizeFound = false;
        if (request.getParameter("size") != null && Validation.isInteger(request.getParameter("size"))) {
            if (Validation.isInteger(request.getParameter("size"))) {
                isSizeFound = true;
                request.setAttribute("sizeId", request.getParameter("size"));
            }
        }
        request.setAttribute("isSizeFound", isSizeFound);

        //sort
        boolean isSortFound = false;
        if (request.getParameter("sortBy") != null && !request.getParameter("sortBy").isBlank()) {

            String sortBy = request.getParameter("sortBy").toLowerCase();
            if (sortBy.equals("default")) {
                isSortFound = true;
                request.setAttribute("sortBy", request.getParameter(sortBy));

            } else if (sortBy.equals("new")) {
                isSortFound = true;
                request.setAttribute("sortBy", request.getParameter(sortBy));

            } else if (sortBy.equals("priceasc")) {
                isSortFound = true;
                request.setAttribute("sortBy", request.getParameter(sortBy));

            } else if (sortBy.equals("pricedesc")) {
                isSortFound = true;
                request.setAttribute("sortBy", request.getParameter(sortBy));

            }

        }
        request.setAttribute("isSortFound", isSortFound);

        //search
        boolean isSearchFound = false;
        if (request.getParameter("search") != null && !request.getParameter("search").isBlank()) {
            if (!request.getParameter("search").isBlank()) {
                isSearchFound = true;
                request.setAttribute("search", request.getParameter("search"));
            }
        }
        request.setAttribute("isSearchFound", isSearchFound);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
