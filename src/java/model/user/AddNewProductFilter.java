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
import javax.servlet.http.HttpServletRequest;
import model.Validation;

public class AddNewProductFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String message = "";

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (httpServletRequest.getSession().getAttribute("user") == null) {
            isInvalid = true;
            message = "Please Logedin First";

        } else if (request.getParameter("name") == null && request.getParameter("name").isBlank()) {
            isInvalid = true;
            message = "Missing Name";

        } else if (request.getParameter("description") == null && request.getParameter("description").isBlank()) {
            isInvalid = true;
            message = "Missing Description";

        } else if (request.getParameter("unit_price") == null && request.getParameter("unit_price").isBlank()) {
            isInvalid = true;
            message = "Missing Unit Price";

        } else if (request.getParameter("img1") == null && request.getParameter("img1").isBlank()) {
            isInvalid = true;
            message = "Missing Image 1";

        } else if (request.getParameter("img2") == null && request.getParameter("img2").isBlank()) {
            isInvalid = true;
            message = "Missing Image 2";

        } else if (request.getParameter("img3") == null && request.getParameter("img3").isBlank()) {
            isInvalid = true;
            message = "Missing Image 3";

        } else if (request.getParameter("delivery_fee") == null && request.getParameter("delivery_fee").isBlank()) {
            isInvalid = true;
            message = "Missing Delivery Fee";

        } else if (request.getParameter("status_id") == null && request.getParameter("status_id").isBlank()) {
            isInvalid = true;
            message = "Missing Status";

        } else if (request.getParameter("product_color_id") == null && request.getParameter("product_color_id").isBlank()) {
            isInvalid = true;
            message = "Missing Product Colour";

        } else if (request.getParameter("size_id") == null && request.getParameter("size_id").isBlank()) {
            isInvalid = true;
            message = "Missing Size";

        } else if (request.getParameter("brand_id") == null && request.getParameter("brand_id").isBlank()) {
            isInvalid = true;
            message = "Missing Brand";

        } else if (request.getParameter("category_id") == null && request.getParameter("category_id").isBlank()) {
            isInvalid = true;
            message = "Missing Category";

        } else {

            String name = request.getParameter("name");
            String unit_price = request.getParameter("unit_price");
            String delivery_fee = request.getParameter("delivery_fee");
            String status_id = request.getParameter("status_id");
            String product_color_id = request.getParameter("product_color_id");
            String size_id = request.getParameter("size_id");
            String brand_id = request.getParameter("brand_id");
            String category_id = request.getParameter("category_id");

            if (Validation.isDouble(unit_price)) {
                isInvalid = true;
                message = "Unit Price Not a Double";

            } else if (Validation.isDouble(delivery_fee)) {
                isInvalid = true;
                message = "Delivery Fee Not a Double";

            } else if (Validation.isInteger(status_id)) {
                isInvalid = true;
                message = "Status Not a Number";

            } else if (Validation.isInteger(product_color_id)) {
                isInvalid = true;
                message = "Product Color Not a Number";

            } else if (Validation.isInteger(size_id)) {
                isInvalid = true;
                message = "Size Not a Number";

            } else if (Validation.isInteger(brand_id)) {
                isInvalid = true;
                message = "Brand Not a Number";

            } else if (Validation.isInteger(category_id)) {
                isInvalid = true;
                message = "Category Not a Number";

            } else {

                double unit_price1 = Double.parseDouble(request.getParameter("unit_price"));
                double delivery_fee1 = Double.parseDouble(request.getParameter("delivery_fee"));
                int status_id1 = Integer.parseInt(request.getParameter("status_id"));
                int product_color_id1 = Integer.parseInt(request.getParameter("product_color_id"));
                int size_id1 = Integer.parseInt(request.getParameter("size_id"));
                int brand_id1 = Integer.parseInt(request.getParameter("brand_id"));
                int category_id1 = Integer.parseInt(request.getParameter("category_id"));

                if (unit_price1 <= 0.0) {
                    isInvalid = true;
                    message = "Invalid Unit Price";

                } else if (delivery_fee1 < 0.0) {
                    isInvalid = true;
                    message = "Invalid Delivery Fee";

                } else if (status_id1 <= 0.0) {
                    isInvalid = true;
                    message = "Invalid Status";

                } else if (product_color_id1 <= 0.0) {
                    isInvalid = true;
                    message = "Invalid Product Color";

                } else if (size_id1 <= 0.0) {
                    isInvalid = true;
                    message = "Invalid Size";

                } else if (brand_id1 <= 0.0) {
                    isInvalid = true;
                    message = "Invalid Brand";

                } else if (category_id1 <= 0.0) {
                    isInvalid = true;
                    message = "Invalid Category";

                } else {

                    if (name.length() > 75) {
                        isInvalid = true;
                        message = "Name Length Too Long";

                    } else {
                        chain.doFilter(request, response);
                    }
                }
            }

        }

        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, message);
            Gson gson = new Gson();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
