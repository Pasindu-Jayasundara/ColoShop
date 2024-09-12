package model.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.Product_color;
import entity.Size;
import entity.Status;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

public class AddNewProductDBDataFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String message = "";

        int product_color_id = Integer.parseInt(request.getParameter("product_color_id"));
        int size_id = Integer.parseInt(request.getParameter("size_id"));
        int brand_id = Integer.parseInt(request.getParameter("brand_id"));
        int category_id = Integer.parseInt(request.getParameter("category_id"));

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Product_color product_color = (Product_color) hibernateSession.get(Product_color.class, product_color_id);
        Size size = (Size) hibernateSession.get(Size.class, size_id);
        Brand brand = (Brand) hibernateSession.get(Brand.class, brand_id);
        Category category = (Category) hibernateSession.get(Category.class, category_id);

        hibernateSession.close();

        if (product_color == null) {
            isInvalid = true;
            message = "Product Color Not Avaliable";

        } else if (size == null) {
            isInvalid = true;
            message = "Product Size Not Avaliable";

        } else if (brand == null) {
            isInvalid = true;
            message = "Product Brand Not Avaliable";

        } else if (category == null) {
            isInvalid = true;
            message = "Product Category Not Avaliable";

        } else {
            chain.doFilter(request, response);
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
