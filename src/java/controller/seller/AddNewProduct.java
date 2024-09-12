package controller.seller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.Product;
import entity.Product_color;
import entity.Seller;
import entity.Size;
import entity.Status;
import entity.UserTable;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "AddNewProduct", urlPatterns = {"/AddNewProduct"})
public class AddNewProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/AddNewProductImageUpload").include(request, response);

        boolean isValid = true;
        String message = "";

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        if ((boolean) request.getAttribute("isImageUploadSuccess")) {
            //image uploading success

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double unit_price = Double.parseDouble(request.getParameter("unit_price"));
            double delivery_fee = Double.parseDouble(request.getParameter("delivery_fee"));
            int status_id = Integer.parseInt(request.getParameter("status_id"));
            int product_color_id = Integer.parseInt(request.getParameter("product_color_id"));
            int size_id = Integer.parseInt(request.getParameter("size_id"));
            int brand_id = Integer.parseInt(request.getParameter("brand_id"));
            int category_id = Integer.parseInt(request.getParameter("category_id"));
            
            String img1Path = (String) request.getAttribute("img1Path");
            String img2Path = (String) request.getAttribute("img2Path");
            String img3Path = (String) request.getAttribute("img3Path");
            
            Status status = (Status) hibernateSession.load(Status.class, status_id);
            Product_color product_color = (Product_color) hibernateSession.load(Product_color.class, product_color_id);
            Size size = (Size) hibernateSession.load(Size.class, size_id);
            Brand brand = (Brand) hibernateSession.load(Brand.class, brand_id);
            Category category = (Category) hibernateSession.load(Category.class, category_id);
            
            UserTable user = (UserTable) request.getSession().getAttribute("user");
            
            Criteria sellerCriteria = hibernateSession.createCriteria(Seller.class);
            sellerCriteria.add(Restrictions.eq("user", user));
            
            Seller seller = (Seller) sellerCriteria.uniqueResult();

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setUnit_price(unit_price);
            product.setAdded(new Date());
            product.setImg1(img1Path);
            product.setImg2(img2Path);
            product.setImg3(img3Path);
            product.setDelivery_fee(delivery_fee);
            product.setSold_count(0);
            product.setStatus(status);
            product.setProduct_color(product_color);
            product.setSize(size);
            product.setBrand(brand);
            product.setSeller(seller);
            product.setCategory(category);
            
            message = "Product Adding Success";

        } else {
            // image uploading faild
            isValid = false;
            message = "Image Uploading Failed";
        }

        Response_DTO response_DTO = new Response_DTO(isValid, message);
        Gson gson = new Gson();

        hibernateSession.close();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }
}
