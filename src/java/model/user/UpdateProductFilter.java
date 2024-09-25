package model.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import model.Validation;

public class UpdateProductFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String message = "";

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(request.getReader(), JsonObject.class);

        String name = fromJson.get("name").getAsString();
        String description = fromJson.get("description").getAsString();
        String unit_price =fromJson.get("unit_price").getAsString();
        String delivery_fee = fromJson.get("delivery_fee").getAsString();
        String color = fromJson.get("color").getAsString();
        String size = fromJson.get("size").getAsString();
        String category =fromJson.get("category").getAsString();
        String brand = fromJson.get("brand").getAsString();
        String id = fromJson.get("id").getAsString();

        if (httpServletRequest.getSession().getAttribute("user") == null) {
            isInvalid = true;
            message = "Please Logedin First";

        } else if (name.trim().equals("")) {
            isInvalid = true;
            message = "Missing Name";

        }else if (id.trim().equals("")) {
            isInvalid = true;
            message = "Missing Id";

        } else if (description.trim().equals("")) {
            isInvalid = true;
            message = "Missing Description";

        } else if (unit_price.trim().equals("")) {
            isInvalid = true;
            message = "Missing Unit Price";

        }else if (delivery_fee.trim().equals("")) {
            isInvalid = true;
            message = "Missing Delivery Fee";

        } else if (color.trim().equals("")) {
            isInvalid = true;
            message = "Missing Product Colour";

        } else if (size.trim().equals("")) {
            isInvalid = true;
            message = "Missing Size";

        } else if (brand.trim().equals("")) {
            isInvalid = true;
            message = "Missing Brand";

        } else if (category.trim().equals("")) {
            isInvalid = true;
            message = "Missing Category";

        } else {
            if (!Validation.isDouble(unit_price) && !Validation.isInteger(unit_price)) {
                isInvalid = true;
                message = "Unit Price Not a Double";

            } else if (!Validation.isDouble(delivery_fee)&&!Validation.isInteger(unit_price)) {
                isInvalid = true;
                message = "Delivery Fee Not a Double";

            } else if (!Validation.isInteger(color)) {
                isInvalid = true;
                message = "Product Color Not a Number";

            } else if (!Validation.isInteger(size)) {
                isInvalid = true;
                message = "Size Not a Number";

            } else if (!Validation.isInteger(brand)) {
                isInvalid = true;
                message = "Brand Not a Number";

            } else if (!Validation.isInteger(category)) {
                isInvalid = true;
                message = "Category Not a Number";

            } else if (!Validation.isInteger(id)) {
                isInvalid = true;
                message = "Id Not a Number";

            }  else {

                double unit_price1 = Double.parseDouble(unit_price);
                double delivery_fee1 = Double.parseDouble(delivery_fee);
                int product_color_id1 = Integer.parseInt(color);
                int size_id1 = Integer.parseInt(size);
                int brand_id1 = Integer.parseInt(brand);
                int category_id1 = Integer.parseInt(category);
                int pId = Integer.parseInt(id);
                

                if (unit_price1 <= 0.0) {
                    isInvalid = true;
                    message = "Invalid Unit Price";

                } else if (delivery_fee1 < 0.0) {
                    isInvalid = true;
                    message = "Invalid Delivery Fee";

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

                }else if (pId <= 0) {
                    isInvalid = true;
                    message = "Invalid Id";

                }  else {

                    if (name.length() > 75) {
                        isInvalid = true;
                        message = "Name Length Too Long";

                    } else {

                        request.setAttribute("name", name);
                        request.setAttribute("description", description);
                        request.setAttribute("unit_price", unit_price);
                        request.setAttribute("delivery_fee", delivery_fee);
                        request.setAttribute("size", size);
                        request.setAttribute("brand", brand);
                        request.setAttribute("category", category);
                        request.setAttribute("color", color);
                        request.setAttribute("id", id);

                        chain.doFilter(request, response);
                    }
                }
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
