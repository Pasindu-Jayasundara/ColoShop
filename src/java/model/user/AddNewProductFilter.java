package model.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import model.Validation;

@MultipartConfig
public class AddNewProductFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String message = "";

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        
        String name = httpServletRequest.getParameter("name");
        String description = httpServletRequest.getParameter("description");
        String unit_price = httpServletRequest.getParameter("unit_price");
        String delivery_fee = httpServletRequest.getParameter("delivery_fee");
        String color = httpServletRequest.getParameter("color");
        String size = httpServletRequest.getParameter("size");
        String category = httpServletRequest.getParameter("category");
        String brand = httpServletRequest.getParameter("brand");

        // Extract files using request.getPart()
        Part img1 = httpServletRequest.getPart("img1");
        String fileName1 = Paths.get(img1.getSubmittedFileName()).getFileName().toString();

        Part img2 = httpServletRequest.getPart("img2");
        String fileName2 = Paths.get(img2.getSubmittedFileName()).getFileName().toString();

        Part img3 = httpServletRequest.getPart("img3");
        String fileName3 = Paths.get(img3.getSubmittedFileName()).getFileName().toString();

        if (httpServletRequest.getSession().getAttribute("user") == null) {
            isInvalid = true;
            message = "Please Logedin First";

        } else 
            if (name.trim().equals("")) {
            isInvalid = true;
            message = "Missing Name";

        } else if (description.trim().equals("")) {
            isInvalid = true;
            message = "Missing Description";

        } else if (unit_price.trim().equals("")) {
            isInvalid = true;
            message = "Missing Unit Price";

        } else if (fileName1.trim().equals("")) {
            isInvalid = true;
            message = "Missing Image 1";

        } else if (fileName2.trim().equals("")) {
            isInvalid = true;
            message = "Missing Image 2";

        } else if (fileName3.trim().equals("")) {
            isInvalid = true;
            message = "Missing Image 3";

        } else if (delivery_fee.trim().equals("")) {
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

            } 
            else if (!Validation.isInteger(color)) {
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

            } else {

                double unit_price1 = Double.parseDouble(unit_price);
                double delivery_fee1 = Double.parseDouble(delivery_fee);
                int product_color_id1 = Integer.parseInt(color);
                int size_id1 = Integer.parseInt(size);
                int brand_id1 = Integer.parseInt(brand);
                int category_id1 = Integer.parseInt(category);
                

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

                } else {

                    if (name.length() > 75) {
                        isInvalid = true;
                        message = "Name Length Too Long";

                    } else {

                        request.setAttribute("name", name);
                        request.setAttribute("description", description);
                        request.setAttribute("unit_price", unit_price);
                        request.setAttribute("img1", img1);
                        request.setAttribute("img2", img2);
                        request.setAttribute("img3", img3);
                        request.setAttribute("delivery_fee", delivery_fee);
                        request.setAttribute("size", size);
                        request.setAttribute("brand", brand);
                        request.setAttribute("category", category);
                        request.setAttribute("color", color);

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
