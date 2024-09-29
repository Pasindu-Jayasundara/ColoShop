package controller.seller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.Product;
import entity.Product_color;
import entity.Seller;
import entity.Size;
import entity.UserTable;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "UpdateProduct", urlPatterns = {"/UpdateProduct"})
public class UpdateProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        
        boolean isProductIdOk = false;
        boolean isValid = false;
        Product product = null;
        String message = "";

        Gson gson = new Gson();

        String name = (String) request.getAttribute("name");
        String description = (String)request.getAttribute("description");
        double unit_price = Double.parseDouble((String)request.getAttribute("unit_price"));
        double delivery_fee = Double.parseDouble((String) request.getAttribute("delivery_fee"));
        int product_color_id = Integer.parseInt((String) request.getAttribute("color"));
        int size_id = Integer.parseInt((String) request.getAttribute("size"));
        int category_id = Integer.parseInt((String) request.getAttribute("category"));
        int brand_id = Integer.parseInt((String) request.getAttribute("brand"));
        String id = (String) request.getAttribute("id");

        if (id.isEmpty()) {
            //no id
            message = "Missing Product Id";

        } else {
            int productId = Integer.parseInt(id);

            product = (Product) hibernateSession.load(Product.class, productId);
            if (product != null) {
                isProductIdOk = true;
            }
        }

        if (isProductIdOk) {

            Product_color product_color = (Product_color) hibernateSession.load(Product_color.class, product_color_id);
            Size size = (Size) hibernateSession.load(Size.class, size_id);
            Brand brand = (Brand) hibernateSession.load(Brand.class, brand_id);
            Category category = (Category) hibernateSession.load(Category.class, category_id);

            UserTable user = (UserTable) request.getSession().getAttribute("user");

            Criteria sellerCriteria = hibernateSession.createCriteria(Seller.class);
            sellerCriteria.add(Restrictions.eq("user", user));

            Seller seller = (Seller) sellerCriteria.uniqueResult();

            product.setName(name);
            product.setDescription(description);
            product.setUnit_price(unit_price);
            product.setAdded(new Date());
            product.setDelivery_fee(delivery_fee);
            product.setSold_count(0);
            product.setProduct_color(product_color);
            product.setSize(size);
            product.setBrand(brand);
            product.setSeller(seller);
            product.setCategory(category);

            hibernateSession.update(product);
            hibernateSession.beginTransaction().commit();

            isValid = true;
            message = "Product Updating Success";

        }
        Response_DTO response_DTO = new Response_DTO(isValid, message);

        hibernateSession.close();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
